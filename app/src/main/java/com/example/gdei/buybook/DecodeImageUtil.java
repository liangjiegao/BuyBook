package com.example.gdei.buybook;

import android.util.Base64;

/**
 * Created by gdei on 2018/7/2.
 */

public class DecodeImageUtil {

    public static byte[] decodeStringToBitmap(String imgStr){
        byte[] imgbyte = null;

        if (imgStr != null){
            imgbyte = Base64.decode(imgStr,Base64.DEFAULT);
        }
        return imgbyte;
    }

}
