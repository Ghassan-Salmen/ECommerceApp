package com.example.ecommerceapp.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.Buyers.MainActivity;
import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterationActivity extends AppCompatActivity {
    private Button sellerLoginBegin;
    private EditText nameInput,phoneInput,EmailInput,passwordInput,addressInput;
    private Button RegisterButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registeration);

        mAuth = FirebaseAuth.getInstance();

        sellerLoginBegin = findViewById(R.id.seller_already_have_account_btn);
        RegisterButton = findViewById(R.id.seller_register_btn);
        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        EmailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);
        loadingBar = new ProgressDialog(this);

        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerRegisterationActivity.this, SellerLoginActivity.class);
                startActivity(intent);

            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerSeller();
            }
        });
    }

    private void registerSeller() {
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = EmailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if(!name.equals("")  && !phone.equals("") && !email.equals("")   && !password.equals("")  && !address.equals(""))
        {
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try{
                            if(task.isSuccessful())
                            {

                                final DatabaseReference rootRef;
                                rootRef = FirebaseDatabase.getInstance().getReference();

                                String sid = mAuth.getCurrentUser().getUid();


                                HashMap<String,Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);
                                sellerMap.put("name",name);
                                sellerMap.put("password",password);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegisterationActivity.this, "You are Registered Successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(SellerRegisterationActivity.this, SellerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                            else{
                                Log.e("TAG", "Registration error: " + task.getException().getMessage());
                                Toast.makeText(SellerRegisterationActivity.this, "Registration error. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


        }else
        {
            Toast.makeText(this, "Please Complete the Registration Form", Toast.LENGTH_SHORT).show();
        }
    }

}
