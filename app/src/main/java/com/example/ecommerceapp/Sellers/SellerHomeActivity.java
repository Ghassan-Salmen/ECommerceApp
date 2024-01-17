package com.example.ecommerceapp.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceapp.Admin.AdminCheckNewProductsActivity;
import com.example.ecommerceapp.Buyers.MainActivity;
import com.example.ecommerceapp.Model.Products;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.ViewHolder.ItemViewHolder;
import com.example.ecommerceapp.ViewHolder.ProductViewHolder;
import com.example.ecommerceapp.databinding.ActivitySellerHomeBinding;
import com.example.ecommerceapp.ui.home.HomeFragment;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SellerHomeActivity extends AppCompatActivity {

    private ActivitySellerHomeBinding binding;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySellerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = binding.sellerHomeRecyclerview;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        binding.navView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.navigation_home:

                    Intent intentHome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intentHome);
                    break;

                case R.id.navigation_add:
                    Intent intentCat = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intentCat);
                    break;

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intentMain = new Intent(SellerHomeActivity.this, MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();
                    break;



            }

            return true;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products,ItemViewHolder > adapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull  ItemViewHolder holder, int position, @NonNull Products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductStatus.setText("State :  "+model.getProductState());
                        holder.txtProductPrice.setText("Price = "+model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String productID = model.getPid();
                                CharSequence options []= new CharSequence[] {

                                        "Yes",
                                        "No"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to Delete this Product. Are you Sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0)
                                        {
                                            deleteProduct(productID);
                                        }
                                        if(i == 1)
                                        {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productID) {

        unverifiedProductsRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(SellerHomeActivity.this, "Item has been deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_home,fragment);
        fragmentTransaction.commit();
    }

}