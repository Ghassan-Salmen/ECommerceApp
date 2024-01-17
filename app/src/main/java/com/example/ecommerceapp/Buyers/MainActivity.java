package com.example.ecommerceapp.Buyers;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.Model.Users;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.Sellers.SellerHomeActivity;
import com.example.ecommerceapp.Sellers.SellerRegisterationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        sellerBegin = (TextView) findViewById(R.id.seller_begin);
        loadingBar = new ProgressDialog(this);
        //to create the file which will store username and password permanently
        Paper.init(this);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });


        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SellerRegisterationActivity.class);
                startActivity(intent);

            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

                startActivity(intent);

            }
        });
        //if already there exist some data in the paper we read it
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        //empty string is not null
        if(UserPhoneKey != "" && UserPasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                //we pass the username and password to be checked whether they are in the database or not
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the seller is already logged in
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    //final indicates that these variables values will not be changed
    private void AllowAccess(final String phone, final String password) {

        //getting the reference or the root node in the database
        //RootRef.child("users") this will add child named users
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //to check for the phone number if it is a child of users
                if(snapshot.child("Users").child(phone).exists())
                {
                    //here all the information phone,password and name will be mapped in users class(open users class for more info)
                    //data is retrieve from database to users class
                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone))
                    {

                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this,"Please wait, You Are Already Logged In.",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            // to set the current user
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Account with this " +phone+ "number do not exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}