//package com.example.camera_1;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
//import static androidx.constraintlayout.widget.Constraints.TAG;
//
//public class CameraActivity extends Activity {
//
//    private Camera mCamera;
//    private CameraPreview mPreview;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cameraactivity);
//
//        // Create an instance of Camera
//        mCamera = getCameraInstance();
//
//        // Create our Preview view and set it as the content of our activity.
//        mPreview = new CameraPreview(this, mCamera);
//        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//        preview.addView(mPreview);
//
//        Button captureButton=(Button)findViewById(R.id.button_capture);
//        captureButton.setOnClickListener(
//                new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v){
//                        mCamera.takePicture(null,null,picture);
//                    }
//
//                }
//
//        );
//    }
//
//
//
//
//    /**
//     * Check if this device has a camera
//     */
//
//
//
//
//    private boolean checkCameraHardware(Context context) {
//        // no camera on this device
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            // this device has a camera
//            return true;
//        } else return false;
//    }
////打开摄像头
//    public static Camera getCameraInstance(){
//        Camera c=null;
//        try{
//            c=Camera.open();
//
//        }
//        catch(Exception e){
//
//        }
//        return c;
//    }
//
//    /**
//     *
//     * 用于记录拍摄类接口
//     */
//    private Camera.PictureCallback mPicture=new Camera.PictureCallback() {
//        @Override
//        public void onPictureTaken(byte[] data, Camera camera) {
//            File pictureFile=getOutputMediaFile(MEDIA_TYPE_IMAGE);
//            if(pictureFile==null){
//                Log.d(TAG, "Error creating media file, check storage permissions");
//                return;
//            }
//            try{
//                FileOutputStream fos=new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.close();
//
//            }
//            catch (FileNotFoundException e) {
//                Log.d(TAG, "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d(TAG, "Error accessing file: " + e.getMessage());
//            }
//        }
//    };
//
//
//
//
//}
