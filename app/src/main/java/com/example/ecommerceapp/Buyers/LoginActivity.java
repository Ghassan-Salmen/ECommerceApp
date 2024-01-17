package com.example.ecommerceapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.Admin.AdminHomeActivity;
import com.example.ecommerceapp.Sellers.SellerProductCategoryActivity;
import com.example.ecommerceapp.Model.Users;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.example.ecommerceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;


import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNmuber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdminLink,ForgetPasswordLink;


    private String  ParentDbName = "Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNmuber = (EditText) findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        ForgetPasswordLink = findViewById(R.id.forget_password_link);

        chkBoxRememberMe  = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();
            }
        });


        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
        //when clicking on i am an admin it will change the parentDbName to Admin which will be used to identify the parent used
        // to retrieve data from database
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                ParentDbName = "Admin";
            }
        });

        //when clicking on I'm not an Admin? it will change the parentDbName to Users which will be used to identify the parent used
        // to retrieve data from database
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                ParentDbName = "Users";
            }
        });
    }

    private void loginUser() {

        String phone = InputPhoneNmuber.getText().toString();
        String password = InputPassword.getText().toString();

        //checking if the text fields are empty
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccesssToAccount(phone,password);
        }
    }

    private void AllowAccesssToAccount(String phone, String password) {

        //if remember me is checked then it will write the phone number and password along with passing the key element for both
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        //getting the reference for the root node in the database
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //to check for the phone number if it is a child of Users or Admin node
                if(snapshot.child(ParentDbName).child(phone).exists())
                {
                    //here all the information phone,password and name will be mapped in users class(open users class for more info)
                    Users usersData = snapshot.child(ParentDbName).child(phone).getValue(Users.class);
                    Log.d("userss", usersData.toString()+ParentDbName);

                    if(usersData.getPhone().equals(phone))
                    {

                        if(usersData.getPassword().equals(password))
                        {
                            if(ParentDbName.equals("Admin"))
                            {
                                Toast.makeText(LoginActivity.this,"Welcome Admin You Are Logged in successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if(ParentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Account with this " +phone+ "number do not exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}