package com.it.androidexpandablelistview.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
    public static String doGet(String urlStr){
        HttpURLConnection connection=null;
        InputStream is=null;
        ByteArrayOutputStream bos=null;
        try {
            URL url=new URL(urlStr);
             connection=(HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode()==200){
                 is=connection.getInputStream();
                 bos=new ByteArrayOutputStream();
                int len=-1;
                byte[] buf=new byte[512];
                while ((len=is.read(buf)) != -1){
                    bos.write(buf,0,len);
                }
                bos.flush();
                return bos.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bos !=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is !=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bos !=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();

        }
        return null;
    }
}

