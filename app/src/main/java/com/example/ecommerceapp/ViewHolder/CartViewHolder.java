package com.example.ecommerceapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Interface.ItemClickListner;
import com.example.ecommerceapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);


    }

    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view,getAbsoluteAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
