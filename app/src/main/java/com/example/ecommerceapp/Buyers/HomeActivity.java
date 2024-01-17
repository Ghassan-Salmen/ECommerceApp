package com.example.ecommerceapp.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Admin.AdminMaintainProductActivity;
import com.example.ecommerceapp.Model.Products;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    //The LayoutManager is responsible for positioning and measuring the individual items within the RecyclerView
    RecyclerView.LayoutManager layoutManager;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle= intent.getExtras();
        if(bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);
        //Set up the toolbar and the title for the activity
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        //Set up a FloatingActionButton with a click listener.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //only for users
                if(!type.equals("Admin"))
                {
                    Intent intent  = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);

                }

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle provides the functionality to toggle the navigation drawer open and closed by tapping on the navigation icon in the action bar.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //enable ActionBarDrawerToggle to listen to various events related to the navigation drawer, such as drawer slide events, drawer open events, and drawer close events.
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //to access the header view which is in the navigation view
        //The 0 represents the index of the HeaderView (in case there are multiple headers).
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView =  headerView.findViewById(R.id.user_profile_image);

        //to set userName and user photo
        if(!type.equals("Admin"))
        {
            if (Prevalent.currentOnlineUser != null) {
                userNameTextView.setText(Prevalent.currentOnlineUser.getName());

                // Load user image using Picasso if image URL is available
                if (Prevalent.currentOnlineUser.getImage() != null && !Prevalent.currentOnlineUser.getImage().isEmpty()) {
                    Picasso.get().load(Prevalent.currentOnlineUser.getImage())
                            .placeholder(R.drawable.profile)
                            .into(profileImageView);
                } else {
                    // Load a placeholder image if no image URL is available
                    profileImageView.setImageResource(R.drawable.profile);
                }
            } else {
                // Handle the case where currentOnlineUser is null
            }


        }

        recyclerView = findViewById(R.id.recycler_menu);
        //you are telling the RecyclerView that the size of each item within the RecyclerView will remain constant
        recyclerView.setHasFixedSize(true);
        //The LinearLayoutManager is a built-in layout manager provided by the Android framework for arranging items in a RecyclerView in a linear fashion, either vertically or horizontally.
        //By default, the LinearLayoutManager arranges the items vertically
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // to bind data from a Firebase query to a RecyclerView.
        // FirebaseRecyclerOptions is used to define the query and data mapping configuration for the FirebaseRecyclerAdapter,
        // specifying the location of the data in the database and the class type (Products)to map the retrieved data.
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"),Products.class)
                        .build();
        //FirebaseRecyclerAdapter  It specifies the types of data to be displayed (Products)
        // and the type of the ViewHolder (ProductViewHolder) that will be used to display the data.
        // the anonymous class serves as a subclass of FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    //onBindViewHolder() method sets the data from the model object to the corresponding views within the ProductViewHolder
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = "+model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);





                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(type.equals("Admin"))
                                {
                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProductActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }else
                                {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }


                            }
                        });
                    }
                    //onCreateViewHolder() is called first to create the view holder, and then
                    // onBindViewHolder() is called to bind the data to the views within that view holder for the specific item.

                    @NonNull
                    @Override
                    //method is called when the RecyclerView needs to create a new ProductViewHolder object for an item
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                       // "inflating the layout file," it means that we are converting an XML layout file
                       // into corresponding View objects that can be used in the app's user interface.
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        //The Adapter is responsible for binding the data to the individual item views within the RecyclerView
        //sets the adapter for the RecyclerView, and starts listening for changes in the database to keep the displayed product items up to date.
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

    // method checks if the drawer is open, and if so, it closes the drawer.
    // Otherwise, it performs the default behavior of the back button press
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //onNavigationItemSelected is called when a menu item in the navigation drawer is selected.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

            if(id == R.id.nav_cart) {

                //only for users
                if(!type.equals("Admin"))
                {
                    Intent intent  = new Intent(HomeActivity.this,CartActivity.class);
                    startActivity(intent);

                }

            }
            else if(id == R.id.nav_settings){

                //only for users
                if(!type.equals("Admin"))
                {
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);

                }

                }

            else if(id == R.id.nav_logout) {
                //only for users
                if(!type.equals("Admin"))
                {
                    Paper.book().destroy();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }

            }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //After handling the selected menu item, this line is used to close the drawer by calling the closeDrawer()
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
