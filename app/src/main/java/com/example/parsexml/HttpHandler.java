package com.example.parsexml;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
   public InputStream CallServer(String remoteURL)
   {
       InputStream inStr=null;
       try{
           URL url = new URL(remoteURL);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setReadTimeout(10000 /* milliseconds */);
           connection.setConnectTimeout(15000 /* milliseconds */);
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           connection.connect();

           int response=connection.getResponseCode();
           if(response==HttpURLConnection.HTTP_OK)
                 inStr = connection.getInputStream();
       }catch(Exception e){}
       Log.d("temp", inStr.toString());
       return inStr;
   }

   public String StreamToString(InputStream stream){
       InputStreamReader isr=new InputStreamReader(stream);
       BufferedReader bufReader=new BufferedReader(isr);
       StringBuilder response=new StringBuilder();
       String line="";
       try {
           while((line=bufReader.readLine()) != null)
               response.append(line);
       }catch(IOException e) {
           Log.e("HTTPHandler","Error in stream reading",e);
       }
//       Log.d("HTTPHandler: ",response.toString());
       return response.toString();
   }

}
