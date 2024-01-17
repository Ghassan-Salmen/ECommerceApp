package com.example.ecommerceapp.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.ValueEventListener;

import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;



public class SellerAddNewProductActivity extends AppCompatActivity {

    private String categoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductsRef,sellersRef;
    private ProgressDialog loadingBar;
    private String sName,sAddress,sPhone,sEmail,sID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        //here we retrieve the category name sent by putExtra() function (in AdminCategoryActivity)
        //getIntent(): This method returns the intent that started the current activity
        categoryName = getIntent().getExtras().get("category").toString();
        //code is retrieving the reference to the "Product Images" folder or directory within the Firebase Storage bucket
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VallidateProductData();
            }
        });

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    sName = dataSnapshot.child("name").getValue().toString();
                    sAddress = dataSnapshot.child("address").getValue().toString();
                    sPhone = dataSnapshot.child("phone").getValue().toString();
                    sID = dataSnapshot.child("sid").getValue().toString();
                    sEmail = dataSnapshot.child("email").getValue().toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //this method will open the gallery to the user to select an image
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        // This sets the action of the intent to ACTION_GET_CONTENT, which is used to retrieve data of a specific type
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        //This sets the MIME type of the data to be retrieved as "image/*"->(image/jpeg) which means any image file type
        galleryIntent.setType("image/*");
        //used to start an activity and receive a result from it.The GalleryPick is an integer constant that serves as a unique identifier for this specific request
        startActivityForResult(galleryIntent,GalleryPick);
    }

    //Once the user selects an image or cancels the selection, the onActivityResult() method of the calling activity will be invoked, passing the result and the request code.
    //requestCode: It represents the request code that was passed when starting the activity. It helps identify which activity's result is being returned.
    //resultCode: It indicates the result of the activity, whether it was successful or canceled.
    //data: It contains the data returned from the activity, such as the selected image URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!=null)
        {
            //image uri will be stored
            ImageUri = data.getData();
            //image will be displayed in the image view
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void VallidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        //If the progress dialog is not added, there won't be any visual indication or feedback to the user that the product is being added.
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("please wait, while we are Adding the new product...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        //The getLastPathSegment() method returns the last segment of the path in the URI, which typically corresponds to the filename of the image in the gallery.
        //By concatenating these values together, a unique filename is created for the image, which includes the original filename, the product random key, and the file extension.
        StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        //here an UploadTask named uploadTask is created to upload the image file to the specified filePath in Firebase Storage.
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(SellerAddNewProductActivity.this, "Product Image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        //To retrieve the download URL of the uploaded file in Firebase Storage
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });



    }

    private void SaveProductInfoToDatabase() {

        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("Pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);

        productMap.put("sellerName",sName);
        productMap.put("sellerAddress",sAddress);
        productMap.put("sellerPhone",sPhone);
        productMap.put("sellerEmail",sEmail);
        productMap.put("sid",sID);

        productMap.put("productState","Not Approved");

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product is added Successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}