package com.example.cse535_project1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PracticeActivity extends AppCompatActivity {

    VideoView videoVieW;
    String mUri;
    TextView video_name;
    String word;
    String user_lastname;
    EditText id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        word=getIntent().getStringExtra("001");
        user_lastname=getIntent().getStringExtra("002");

        id=findViewById(R.id.asu_id_input);

        video_name=findViewById(R.id.video_name);

        Intent intent=new Intent(PracticeActivity.this,VideoActivity.class);
        intent.putExtra("001",word);
        intent.putExtra("002",user_lastname);
        startActivityForResult(intent,9999);

        Button button=findViewById(R.id.button_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadTask().execute();
                Toast.makeText(getApplicationContext(),"Start Uploading",Toast.LENGTH_LONG).show();


                Intent intent=new Intent(PracticeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, "onActivityResult: "+resultCode);
        if(requestCode==9999&&resultCode==8888){
            mUri=data.getStringExtra("003");
            videoVieW=findViewById(R.id.user_video);
            play_video(mUri);
            videoVieW.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
        }
    }

    public void play_video(String mUri) {
        Uri uri = Uri.parse(mUri);
        String text=uri.toString().substring((Environment.getExternalStorageDirectory().getPath()+"/cse535/").length());
        video_name.setText(text);
        videoVieW.setVideoURI(uri);
        videoVieW.requestFocus();
        videoVieW.start();
    }

    public class UploadTask extends AsyncTask<String, String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String charset = "UTF-8";
            File file = new File(
                    Environment.getExternalStorageDirectory().getPath()+"/cse535/"
                            +word.toUpperCase()+"_PRACTICE"+"_0_"+user_lastname.toUpperCase()+".mp4");

//            File uploadFile2 = new File("E:/test/2.mp4");
            String requestURL = "http://10.218.107.121/cse535/upload_video.php";

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");

//                multipart.addFormField("description", "Cool Pictures");
//                multipart.addFormField("keywords", "Java,upload,Spring");
                multipart.addFormField("group_id","team09");
                multipart.addFormField("id",id.getText().toString());
                multipart.addFormField("accept","1");



//                multipart.addFilePart("uploaded_file", uploadFile2);
                multipart.addFilePart("uploaded_file", file);

                List<String> response = multipart.finish();

                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    publishProgress(String.valueOf(line));
                }
            } catch (IOException ex) {
                publishProgress(String.valueOf(ex));
            }


            return null;
        }
        @Override
        protected void onProgressUpdate(String... text) {
            Toast.makeText(getApplicationContext(), "In Background Task " + text[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
        }



    }







}


