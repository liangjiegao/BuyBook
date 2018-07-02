package com.example.gdei.buybook;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by gdei on 2018/7/2.
 */

public class Store {

    public static ArrayList<Book> getBooks() {
        return books;
    }

    public static void setBooks(ArrayList<Book> books) {
        Store.books = books;
    }

    private static ArrayList<Book> books;


}
