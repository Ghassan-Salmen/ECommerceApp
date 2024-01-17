package com.example.ecommerceapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText InputName,InputphoneNumber, InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputphoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        loadingBar = new ProgressDialog(this);


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                creatAcount();
            }
        });
    }

    private void creatAcount() {

        String name = InputName.getText().toString();
        String phone = InputphoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please write your name...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validatephoneNumber(name,phone,password);

        }



    }

    private void validatephoneNumber(String name, String phone, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //here root node->users->phone numbers -> name,phone,password
                //to check if the phone number already exists
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    // HashMap<key of type string,value of type object> key must be unique,element in hashmap are not stored in order
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    //this will update the root->Users->phone->  the data stored in hashmap will be here as a children of phone node
                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Congratulations, your account has been created successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network error, Please try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else
                {
                    Toast.makeText(RegisterActivity.this,"This " + phone+"already exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again using another phone number",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}