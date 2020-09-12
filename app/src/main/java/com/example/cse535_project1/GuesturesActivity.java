package com.example.cse535_project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class GuesturesActivity extends AppCompatActivity {

    VideoView videoVieW;
    EditText usr_name;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestures);

        text=getIntent().getStringExtra("001");

        TextView textView=findViewById(R.id.textView);
        textView.setText(text);

        videoVieW=findViewById(R.id.videoView);
        play_video(text);
        videoVieW.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();;
                mp.setLooping(true);
            }
        });

        usr_name=findViewById(R.id.usr_name);

        Button button=findViewById(R.id.button_practice);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String user_name=usr_name.getText().toString();
                Intent intent=new Intent(GuesturesActivity.this, PracticeActivity.class );
                intent.putExtra("001",text);
                intent.putExtra("002",user_name);
                startActivity(intent);
            }
        });

    }
    public void play_video(String text) {
        String path="";
        if(text.equals("arrive")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._arrive;
        } else if(text.equals("buy")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._buy;
        } else if (text.equals("communicate")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._communicate;
        }else if (text.equals("create")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._create;
        }else if (text.equals("drive")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._drive;
        }else if (text.equals("fun")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._fun;
        }else if (text.equals("hope")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hope;
        }else if (text.equals("house")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._house;
        }else if (text.equals("lip")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._lip;
        }else if (text.equals("man")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._man;
        }else if (text.equals("mother")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._mother;
        }else if (text.equals("mouth")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._mouth;
        }else if (text.equals("one")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._one;
        }else if (text.equals("perfect")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._perfect;
        } else if (text.equals("pretend")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._pretend;
        } else if (text.equals("read")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._read;
        }else if (text.equals("really")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._really;
        }else if (text.equals("sister")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._sister;
        }else if (text.equals("some")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._some;
        }else if (text.equals("write")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._write;
        }
        if(!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            videoVieW.setVideoURI(uri);
            videoVieW.requestFocus();
            videoVieW.start();
        }
    }
}
