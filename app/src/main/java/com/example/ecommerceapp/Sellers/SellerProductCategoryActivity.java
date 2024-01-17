package com.example.ecommerceapp.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ecommerceapp.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView tShirt,sportsTSirts, femaleDresses, sweathers;
    private ImageView glasses, hatsCaps,walletsBagsPurses,shoes;
    private ImageView headPhonesHandFree,Laptops,watches,mobilePhones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);




        tShirt = (ImageView) findViewById(R.id.t_shirts);
        sportsTSirts = (ImageView) findViewById(R.id.spott_t_shirts);
        femaleDresses = (ImageView) findViewById(R.id.female_dresses);
        sweathers = (ImageView) findViewById(R.id.sweathers);

        glasses = (ImageView) findViewById(R.id.glasses);
        hatsCaps = (ImageView) findViewById(R.id.hats_caps);
        walletsBagsPurses = (ImageView) findViewById(R.id.Purses_bags_wallets);
        shoes = (ImageView) findViewById(R.id.shoes);

        headPhonesHandFree = (ImageView) findViewById(R.id.headphones_handfree);
        Laptops = (ImageView) findViewById(R.id.laptop_pc);
        watches = (ImageView) findViewById(R.id.watches);
        mobilePhones = (ImageView) findViewById(R.id.mobilephones);

        tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                // putExtra(key,value) It allows you to pass data from one activity to another
                intent.putExtra("category","tShirt");
                startActivity(intent);
            }
        });

        sportsTSirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","sports tSirts");
                startActivity(intent);
            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Sweathers");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Hats caps");
                startActivity(intent);
            }
        });

        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Wallets Bags Purses");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","HeadPhones HandFree");
                startActivity(intent);
            }
        });

        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category","Mobile Phones");
                startActivity(intent);
            }
        });

    }
}