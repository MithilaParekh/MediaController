package com.example.mediacontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    Button btnRecord, btnStopRecord, btnPlay, btnStop;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    final int REQUEST_PERMISSION_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPermissionFromDevice())
            requestPermissions();
        btnPlay=(Button)findViewById(R.id.btnPlay);
        btnStop=(Button)findViewById(R.id.btnStop);
        btnRecord=(Button)findViewById(R.id.btnRecord);
        btnStopRecord=(Button)findViewById(R.id.btnStopRecord);


btnRecord.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (checkPermissionFromDevice()) {
            pathSave = Environment.getExternalStorageDirectory().
                    getAbsolutePath() + "/" +
                    UUID.randomUUID().toString() +
                    "_audio_record.3gp";
            setupMediaRecorder();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btnPlay.setEnabled(false);
            btnStop.setEnabled(false);
            Toast.makeText(MainActivity.this, "Recording", Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestPermissions();
        }
    }
});

btnStopRecord.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mediaRecorder.stop();
        btnStopRecord.setEnabled(false);
        btnPlay.setEnabled(true);
        btnRecord.setEnabled(true);
        btnStop.setEnabled(false);
    }
});
btnPlay.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        btnStop.setEnabled(true);
        btnStopRecord.setEnabled(false);
        btnRecord.setEnabled(false);
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        Toast.makeText(MainActivity.this, "Playing.....", Toast.LENGTH_SHORT).show();
    }
});
btnStop.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        btnStopRecord.setEnabled(false);
        btnRecord.setEnabled(true);
        btnStop.setEnabled(false);
        btnPlay.setEnabled(true);
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            setupMediaRecorder();
            Toast.makeText(getApplicationContext(),"Stop Playing",Toast.LENGTH_SHORT).show();
        }

    }
});

    }

    private void setupMediaRecorder() {
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }

    }

    private boolean checkPermissionFromDevice() {

        int write_external_storage_result= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recor_audio_result=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result== PackageManager.PERMISSION_GRANTED &&
                recor_audio_result==PackageManager.PERMISSION_GRANTED;

    }
}

