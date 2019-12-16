package com.example.vinyl.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vinyl.Adapter.SongAdapter;
import com.example.vinyl.Model.SongModel;
import com.example.vinyl.R;
import com.example.vinyl.SongInformationGathering;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private ListView mListView;
    public static ArrayList<SongModel> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        permissionRequest();
    }

    private void initUI() {
        mListView = findViewById(R.id.listView);
    }

    public void permissionRequest(){

        if(ContextCompat.checkSelfPermission(Main.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Main.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }else{
            setAdapter();
        }
    }

    private void setAdapter() {

        songs = SongInformationGathering.getInfo(this);
        SongAdapter songAdapter = new SongAdapter(this, songs);
        mListView.setAdapter(songAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                               @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Map", "Permission granted");
                setAdapter();
            } else {
                Toast.makeText(this, "This permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(Main.this,
                CurrentSong.class).putExtra("position", position));
    }
}
