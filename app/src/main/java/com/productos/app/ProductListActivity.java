package com.productos.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ProductListActivity extends FragmentActivity
        implements ProductListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (findViewById(R.id.product_detail_container) != null) {
            mTwoPane = true;


        }

    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(ProductDetailFragment.ARG_ITEM_ID, id);
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()

                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ProductDetailActivity.class);
            detailIntent.putExtra(ProductDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
