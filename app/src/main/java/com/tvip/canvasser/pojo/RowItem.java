package com.tvip.canvasser.pojo;

public class RowItem {
    private int imageId;
    private String title;
    private int quantity = 0;

    public RowItem(int imageId, String title, int quantity) {
        this.imageId = imageId;
        this.title = title;
        this.quantity = quantity;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDesc() {
        return "Select " + quantity + " Item";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return title + "\n" + quantity;
    }
}