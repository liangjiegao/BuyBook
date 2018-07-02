package com.example.gdei.buybook;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.HashSet;

/**
 * Created by gdei on 2018/7/2.
 */

public class SocketUtil {

    public static void setHost(String host) {
        SocketUtil.host = host;
    }

    public static void setPost(int post) {
        SocketUtil.post = post;
    }

    private static String host = "10.2.229.233";
    private static int post = 3333;

    private static String code = "1";    //访问结果码，是否访问成功,1表示失败，0表示成功

    public static JSONArray sendLoginRequest(String user, String pass){

        try {
            Socket socket = new Socket(host,post);
            JSONArray jsonArray;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("1/"+user+"/"+pass);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            code = reader.readLine();
            if (code.equals("1")){
                socket.close();
                return null;
            }else {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String result = brToString(br);
                jsonArray = new JSONArray(result);
                return jsonArray;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean sendSubmitRequest(String userName, HashSet<Book> bookSelect){

        try {
            Socket socket = new Socket(host,post);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            StringBuffer bookMsg = new StringBuffer();
            for (Book book: bookSelect) {
                bookMsg.append(book.getBookName()+"/"+book.getNum()+"/");
            }
            bw.write("2/"+userName+bookMsg);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if (br.readLine().equals("0")){
                return true;
            }else {
                return false;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }

    private static String brToString(BufferedReader br){
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
