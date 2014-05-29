package com.productos.app;

/**
 * Created by Solari on 23/05/14.
 */
public class Product {
    private int id;
    private String name = "";
    private String category = "";
    private String price = "";
    private String comments = "";
    private String photoFileName = "";

    public Product() {
    }

    public Product(int id, String name, String category, String price, String comments, String photoFileName) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.comments = comments;
        this.photoFileName = photoFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    @Override
    public String toString() {
        return name + " " + price;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Product)o).getId();
    }
}
