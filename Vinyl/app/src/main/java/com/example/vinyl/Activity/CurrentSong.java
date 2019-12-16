package com.example.vinyl.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.vinyl.Interface.MusicInterface;
import com.example.vinyl.Model.SongModel;
import com.example.vinyl.R;

import static com.example.vinyl.Activity.Main.songs;

public class CurrentSong extends AppCompatActivity
        implements View.OnClickListener, Runnable, MusicInterface {

    private ImageView art;
    private SeekBar seekBar;
    private Handler handler;
    private ImageButton play;
    private MediaPlayer mPlayer;
    private TextView title, artist;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        position = getIntent().getIntExtra("position", 0);

        initUI();
        setUI();
        new Thread(this).start();
    }

    private void initUI() {

        handler = new Handler();
        mPlayer = new MediaPlayer();
        seekBar = findViewById(R.id.seekBarCS);
        title = findViewById(R.id.titleCS);
        artist = findViewById(R.id.artistCS);
        art = findViewById(R.id.albumArtCS);
        play = findViewById(R.id.playCS);

        play.setOnClickListener(this);
        findViewById(R.id.nextCS).setOnClickListener(this);
        findViewById(R.id.previousCS).setOnClickListener(this);

        resetMP();
        playSong();

        seekBar.setMax(mPlayer.getDuration());
        seekBar.setProgress(mPlayer.getCurrentPosition());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUI() {

        SongModel model = Main.songs.get(position);

        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(getResources(), model.getArt());
        dr.setCornerRadius(6f);

        title.setText(model.getTitle());
        artist.setText(model.getArtist());
        art.setImageDrawable(dr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.playCS:
                playSong();
                break;

            case R.id.previousCS:
                previousSong();
                break;

            case R.id.nextCS:
                nextSong();
                break;
        }
    }

    @Override
    public void run() {

        if(mPlayer.isPlaying()){
            seekBar.setProgress(mPlayer.getCurrentPosition());
        }
        handler.postDelayed(this, 1000);
    }

    @Override
    public boolean playSong() {

        if (mPlayer.isPlaying()){
            mPlayer.pause();
            play.setImageResource(R.drawable.play);
            return true;
        } else {
            mPlayer.start();
            play.setImageResource(R.drawable.pause);
            return false;
        }
    }

    @Override
    public void nextSong() {
        if(position == songs.size()-1){position = 0;}else{++position;}
        resetMP();
        playSong();
        setUI();
    }

    @Override
    public void previousSong() {
        if(position == 0){position = songs.size()-1;}else{--position;}
        resetMP();
        playSong();
        setUI();
    }

    private void resetMP() {
        try{
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.setDataSource(songs.get(position).getPath());
            mPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(this);
        mPlayer.stop();
        mPlayer.reset();
        super.onDestroy();
    }
}