package com.example.camera_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import uploadPhoto.UploadMain;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void int_1(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,startcamera.class);
        startActivity(intent);
    }

    public void int_2(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,tw_camera.class);
        startActivity(intent);
    }

    public void int_tohttp(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,HttpDemo.class);
        startActivity(intent);


    }

    public void intent_ToOkHttpUpload(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,OkHttpUpload.class);
        startActivity(intent);

    }

    public void intent_toUploadMain(View view){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, UploadMain.class);
        startActivity(intent);
    }



//    public void but_3(View view) {
//         Intent intent=new Intent(MainActivity.this,CameraActivity.class);
//         startActivity(intent);
//    }
//



}
