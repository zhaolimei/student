package com.example.a337910542.dormitory.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by a337910542 on 2017/11/23.
 */

public class HttpUtil {
    //https与http的通信，在我看来主要的区别在于https多了一个安全验证机制，而Android采用的是X509验证，
    // 首先我们需要这重写X509类，建立我们的验证规则、不过对于特定的项目，我们一般都是无条件信任服务端的，
    // 因此我们可以对任何证书都无条件信任

    // 忽略证书信任 信任所有主机-对于任何证书都不做检查
    private static void initTrustSSL() {
        try {
            SSLContext sslCtx = SSLContext.getInstance("TLS");
            sslCtx.init(null, new TrustManager[]{new X509TrustManager() {
                // do nothing, let the check pass.
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }


                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }


                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());


            HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //向服务器发送请求
    public static void queryFrom(final String address, final HttpCallbackListener listener){

        Log.d("login", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try{
                    URL url = new URL(address);
                    initTrustSSL();
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("login", str);
                    }

                    if(listener != null){
                        //回调onFinish（）方法
                        listener.onFinish(response.toString());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    //向服务器发送请求
    /*if(v.getId() == R.id.fifth_floor){
            final String url = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
            HttpUtil.postSelection(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                        Log.d("sele", response);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }*/
    public static void postSelection(final String address, final String postdata, final HttpCallbackListener listener){

        Log.d("login", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try{
                    URL url = new URL(address);
                    initTrustSSL();
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    con.setDoOutput(true);

                    //post请求的参数
                    String data=new String(postdata);
                    OutputStream out=con.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    out.close();

                    con.connect();

                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("selection", str);
                    }

                    if(listener != null){
                        //回调onFinish（）方法
                        listener.onFinish(response.toString());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
}
