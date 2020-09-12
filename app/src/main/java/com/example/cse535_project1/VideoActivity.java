package com.example.cse535_project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    MediaRecorder mediaRecorder;
    Camera mCamera;
    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    TextView tv_time;
    CountDownTimer time,timer;
    String returnfile;
    String word;
    String user_lastname;
    Intent returnIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        word= getIntent().getStringExtra("001");
        user_lastname= getIntent().getStringExtra("002");
//        Log.e(TAG, "onCreate: "+word );
        returnIntent=getIntent();

        mSurfaceView=findViewById(R.id.camera);
        tv_time=findViewById(R.id.text_time);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);

        time = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText("seconds remaining: " + (millisUntilFinished / 1000+1));
            }

            @Override
            public void onFinish() {
                onPause();
                time.cancel();

//                Log.e(TAG, "onFinish: "+returnfile );
                returnIntent.putExtra("003",returnfile);
                setResult(8888,returnIntent);
                finish();
            }
        };
        timer=new CountDownTimer(0001,0001) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                mediaRecorder.start();
                time.start();
            }
        };

        timer.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }
    @Override
    protected void onDestroy() { super.onDestroy(); }

    private void initRecorder(Surface surface) throws IOException {
        if(mCamera==null){
            //Open Camera
            mCamera = getCameraInstance();
        }
        mCamera.setDisplayOrientation(90);
        //Connect Preview
        mCamera.setPreviewDisplay(mHolder);
        //Start Preview
        mCamera.startPreview();
        //Start Recording Video
        //Unlock the Camera
        mCamera.unlock();


        //Configure MediaRecorder
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.setPreviewDisplay(surface);
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);


        File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/cse535/");
        while (!folder.exists()) {
            folder.mkdirs();
        }

        int i=0;
        File file = new File(
                Environment.getExternalStorageDirectory().getPath()+"/cse535/"
                        +word.toUpperCase()+"_PRACTICE_"+"_0_"+user_lastname.toUpperCase()+".mp4");
        while(file.exists()) {
            i++;
            file = new File(
                    Environment.getExternalStorageDirectory().getPath() +"/cse535/"
                            +word.toUpperCase()+"_PRACTICE_"+i+"_"+ user_lastname.toUpperCase()+".mp4");
        }
        returnfile = file.getPath();
        mediaRecorder.setOutputFile(file.getPath());

        mediaRecorder.setMaxDuration(5000);
        mediaRecorder.setVideoSize(320,240);
        mediaRecorder.setOrientationHint(270);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        //Prepare MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
        } catch (IOException e) {
            Log.e(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
        }
    }

    public static Camera getCameraInstance(){

        Camera c = null;
        try {
            c = Camera.open(1); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "getCameraInstance: "+"Camera is not available (in use or does not exist)");
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}
