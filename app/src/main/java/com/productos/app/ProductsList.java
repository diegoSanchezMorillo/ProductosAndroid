package com.productos.app;

import java.util.ArrayList;

/**
 * Created by Solari on 23/05/14.
 */
public class ProductsList {
    private static ArrayList<Product> productsList;

    public static ArrayList<Product> getProductsList() {
        return productsList;
    }

    public static void setProductsList(ArrayList<Product> productsList) {
        ProductsList.productsList = productsList;
    }
}
