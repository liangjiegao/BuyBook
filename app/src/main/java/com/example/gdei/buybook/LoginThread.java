package com.example.gdei.buybook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import java.util.ArrayList;

import java.util.concurrent.Callable;

/**
 * Created by gdei on 2018/7/2.
 */

public class LoginThread implements Callable<ArrayList<Book>> {

    private String userName;
    private String password;
    public LoginThread(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    @Override
    public ArrayList<Book> call() throws Exception {

        JSONArray jsonArray = SocketUtil.sendLoginRequest(userName, password);
        if (jsonArray == null){
            return null;
        }
        JSONObject jsonObject;
        Book book;
        ArrayList<Book> bookList = new ArrayList<>();
        String bookName;
        double price;
        String img;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = new JSONObject(jsonArray.getString(i));
            bookName = jsonObject.optString("name");
            price = jsonObject.optDouble("price");
            img = jsonObject.optString("image");
            book = new Book(bookName,price,img);
            bookList.add(book);
        }

        return bookList;
    }

}
