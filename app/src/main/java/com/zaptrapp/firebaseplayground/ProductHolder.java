package com.zaptrapp.firebaseplayground;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Nishanth on 21-Oct-17.
 */

public class ProductHolder extends RecyclerView.ViewHolder {
    public final TextView mProductName;
    public ProductHolder(View itemView) {
        super(itemView);
        mProductName = itemView.findViewById(android.R.id.text1);
    }

    public void setName(String name){
        mProductName.setText(name);
    }
}
