package com.productos.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Solari on 23/05/14.
 */
public class ProductDBManagerAndroid {
    private static final String NOMBRE_BD = "db_product";
    private static final int VERSION_BD = 1;
    private SQLiteDatabase dbProduct;
    private Context mContext;

    public ProductDBManagerAndroid(Context context) {
        mContext = context;
        ProductsDBOpenHelper productsDBOpenHelper = new ProductsDBOpenHelper(context, NOMBRE_BD, null, VERSION_BD);
        dbProduct = productsDBOpenHelper.getWritableDatabase();
    }

    public void insertProduct(Product product) {


        String sql = "INSERT INTO product "
                + "(id, name, category, price, comments, photo_file_name) "
                + "VALUES ("
                + "'" + product.getId()+ "', "
                + "'" +  product.getName()+ "', "
                + "'" + product.getCategory()+ "', "
                + "'" + product.getPrice()+ "', "
                + "'" + product.getComments()+ "', "
                + "'" + product.getPhotoFileName()+ "')";
        Log.d(ProductDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbProduct.execSQL(sql);
    }

    public ArrayList<Product> getProductsList() {
        ArrayList<Product> productsList = new ArrayList();
        String sql = "SELECT * FROM product";
        Log.d(ProductDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbProduct.rawQuery(sql, null);
        while(rs.moveToNext()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String category = rs.getString(2);
            String price = rs.getString(3);
            String comments = rs.getString(4);
            String photoFileName = rs.getString(5);
            Product product = new Product(id, name, category, price, comments, photoFileName);
            productsList.add(product);
        }
        return productsList;
    }

    public Product getProductByID(int productId) {
        Product product = null;

        String sql = "SELECT * FROM product WHERE id="+productId;
        Log.d(ProductDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbProduct.rawQuery(sql, null);

        if(rs.moveToNext()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String category = rs.getString(2);
            String price = rs.getString(3);
            String comments = rs.getString(4);
            String photoFileName = rs.getString(5);
            product = new Product(id, name, category, price, comments, photoFileName);
        }
        return product;
    }

    public void updateProduct(Product product) {


        String sql = "UPDATE product SET "
                + "name='" + product.getName()+ "', "
                + "category='" + product.getCategory()+ "', "
                + "price='" + product.getPrice()+ "', "
                + "comments='" + product.getComments()+ "', "
                + "photo_file_name='" + product.getPhotoFileName()+ "' "
                + "WHERE id="+product.getId();
        Log.d(ProductDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbProduct.execSQL(sql);
    }

    public void deleteProductById(int id) {
        String sql = "DELETE FROM product WHERE id="+id;
        Log.d(ProductDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbProduct.execSQL(sql);
    }

    public class ProductsDBOpenHelper extends SQLiteOpenHelper {

        public ProductsDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS product ("
                    + "id INT PRIMARY KEY, "
                    + "name VARCHAR(50), "
                    + "category VARCHAR(100), "
                    + "price INT, "
                    + "comments TEXT, "
                    + "photo_file_name VARCHAR(50))";
            Log.d(ProductDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }
}
