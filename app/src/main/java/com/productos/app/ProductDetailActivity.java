package com.productos.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ProductDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ProductDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ProductDetailFragment.ARG_ITEM_ID));
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(arguments);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ProductListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
