package com.example.camera_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDemo extends Activity implements View.OnClickListener{


    public  void Jp_tohttp(View view){
        Intent intent=new Intent();
        intent.setClass(HttpDemo.this,OKHttp_test.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_demo);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //在Android中不能在主线程中访问网络，所以我们需要开启一个线程
        new TestGetOrPostThread(view).start();
    }

    public class TestGetOrPostThread extends Thread{
        private View view;
        public TestGetOrPostThread(View view){            this.view=view;        }

        @Override
        public void run() {
            switch (view.getId()){
                case R.id.btn_get://get请求
                    String getResult=getUserInfo("123");
                    Log.i("MainActivity","Get 获取用户信息:"+getResult);
                    break;
                case R.id.btn_post://post请求
                    String postResult=login("ansen","123");
                    Log.i("MainActivity","Post 登录结果:"+postResult);
                    break;
            }
        }
    }

    private String getUserInfo(String userid){
        //get的方式提交就是url拼接的方式
        String path = "http://116.255.232.223:8080/picup/";
        int Request_Code=0;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);//设置连接超时时间
            connection.setRequestMethod("GET");//设置以Get方式提交数据
            Request_Code=connection.getResponseCode();//获取连接的响应码
            if(connection.getResponseCode() ==200){//请求成功
                InputStream is = connection.getInputStream();
                System.out.println();
                System.out.print(is);
                Log.i("MainActivity","以发送get请求");
                return dealResponseResult(is);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Request_Code);
        return null;
    }

    private String login(String username,String password){
        String path = "http://116.255.232.223:";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);//设置连接超时时间
            connection.setRequestMethod("POST");//设置以Post方式提交数据
            String data = "username="+username+"&password="+password;//请求数据

            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",data.length()+"");

            //post方式实际上是把请求参数以流的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            if(connection.getResponseCode() ==200){//状态码==200请求成功
                InputStream is = connection.getInputStream();
                return dealResponseResult(is);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理服务器的响应结果（将输入流转化成字符串）
     * @param inputStream 服务器的响应输入流
     * @return
     */
    private String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }


    /**
     * 下为OkHttp部分
     */



}
