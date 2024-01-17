package com.example.ecommerceapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerceapp.Buyers.HomeActivity;
import com.example.ecommerceapp.Buyers.MainActivity;
import com.example.ecommerceapp.R;

public class AdminHomeActivity extends AppCompatActivity {
    private Button logoutBtn,checkOrdersBtn,maintainProductBtn,checkApprovedProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn = (Button)findViewById(R.id.admin_logout_btn);
        checkOrdersBtn = (Button)findViewById(R.id.check_orders_btn);
        maintainProductBtn = (Button)findViewById(R.id.maintain_btn);
        checkApprovedProductsBtn = (Button)findViewById(R.id.check_approve_products_btn);


        maintainProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        checkApprovedProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
                startActivity(intent);
            }
        });
    }
}