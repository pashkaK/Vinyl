package com.example.vinyl.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.vinyl.Model.SongModel;
import com.example.vinyl.R;

import java.util.ArrayList;


public class SongAdapter extends BaseAdapter {

    private ArrayList<SongModel> arrayOfSongs;
    private LayoutInflater songInf;
    private Bitmap bitmap;

    public SongAdapter(Context c, ArrayList<SongModel> songs){
        arrayOfSongs = songs;
        songInf = LayoutInflater.from(c);

//        Drawable myDrawable = c.getResources().getDrawable(R.drawable.ic_launcher_background);
//        bitmap = ((BitmapDrawable) myDrawable).getBitmap();
    }


    @Override
        public int getCount() {
            return arrayOfSongs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = songInf.inflate(R.layout.song_item_model, parent, false);

            TextView titleView = convertView.findViewById(R.id.songTitle);
            TextView artistView = convertView.findViewById(R.id.songArtist);
            ImageView artView = convertView.findViewById(R.id.songArt);

            SongModel currSong = arrayOfSongs.get(position);

            if (currSong.getArt() != null) {
                RoundedBitmapDrawable dr =
                        RoundedBitmapDrawableFactory.create(null, currSong.getArt());
                dr.setCornerRadius(15f);
                artView.setImageDrawable(dr);
            }

            titleView.setText(currSong.getTitle());
            artistView.setText(currSong.getArtist());

            return convertView;
        }
    }