package com.example.ecommerceapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Interface.ItemClickListner;
import com.example.ecommerceapp.R;

//The ProductViewHolder class holds references to the views within each item in the RecyclerView
//ProductViewHolder class extends RecyclerView.ViewHolder, indicating that it is a custom ViewHolder class for the RecyclerView.
public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        //The absolute adapter position refers to the position of the item in the dataset, regardless of any sorting or filtering applied to the RecyclerView.
        listner.onClick(view,getAbsoluteAdapterPosition(),false);

    }
}
