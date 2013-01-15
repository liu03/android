package com.example.zoo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;  
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;  
import org.json.JSONObject;  

import android.util.Log;
  
public class JSONUtil {  
      
    public static JSONObject getJSON(String url) throws JSONException, Exception {  
          
        return new JSONObject(getRequest(url));  
    }  
 
    protected static String getRequest(String url) throws Exception {  
        return getJsonString(url);  
    }    
    
    protected static String getJsonString(String urlPath) throws Exception {  
        InputStream is = null;  
        
        String result = "";  
       try {  
//    	   String proxy = "cache.univ-lille1.fr";
//    	   int port = 3128;
//    	   HttpHost httpHost = new HttpHost(proxy, port);
//           HttpParams httpParams = new BasicHttpParams();
//           httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
//           HttpClient httpClient = new DefaultHttpClient(httpParams);  
//           HttpGet httpGet = new HttpGet(urlPath);
////           HttpPost httpPost = new HttpPost(URLEncoder.encode(urlPath)); 
//           HttpResponse response = httpClient.execute(httpGet);  
//           HttpEntity entity = response.getEntity();  
//           is = entity.getContent();  

           HttpParams httpParams = new BasicHttpParams();
           HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
           HttpClient httpClient = new DefaultHttpClient(httpParams);  
           HttpPost httpPost = new HttpPost(urlPath);
           HttpResponse response = httpClient.execute(httpPost);  
           HttpEntity entity = response.getEntity();  
           is = entity.getContent();  
       } catch (Exception e) {  
           // TODO Auto-generated catch block  
           Log.e("log_tag", "Error in http connection "+e.toString());  
       }   
         
       try {  
           BufferedReader br = new BufferedReader(new InputStreamReader(is,  
                   "utf-8"), 8);  
           StringBuilder sb = new StringBuilder();  
           String line = null;  
           while ((line = br.readLine()) != null) {  
               sb.append(line + "\n");  
           }  
           is.close(); 
           result = sb.toString();  
       } catch (Exception e) {  
           // TODO: handle exception  
           Log.e("log_tag", "Error converting result "+e.toString());  
       }  

       return result;  
    } 
} 