package com.example.gdei.buybook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by gdei on 2018/7/2.
 */

public class GetBookListThread implements Callable<ArrayList<Book>> {

    @Override
    public ArrayList<Book> call() throws Exception {

        Socket socket = new Socket("",1000);

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String result = brToString(br);

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;
        Book book;
        ArrayList<Book> bookList = new ArrayList<>();
        String bookName;
        double price;
        String img;
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = new JSONObject(jsonArray.getString(i));
            bookName = jsonObject.optString("bookName");
            price = jsonObject.optDouble("price");
            img = jsonObject.optString("img");
            book = new Book(bookName,price,img);
            bookList.add(book);
        }

        return bookList;
    }
    private String brToString(BufferedReader br){
        String str = "";
        StringBuffer result = new StringBuffer();
        try {
            if (br != null){
                while ((str = br.readLine()) != null) {
                    result.append(str);
                }
            }
            return result.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
