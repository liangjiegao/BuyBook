package com.example.gdei.buybook;

/**
 * Created by gdei on 2018/7/2.
 */

public class Book {

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    private String bookName;
    private double price;
    private String img;

    private int num;

    public Book(String bookName, double price, String img){
        this.bookName = bookName;
        this.price = price;
        this.img = img;
    }


}
