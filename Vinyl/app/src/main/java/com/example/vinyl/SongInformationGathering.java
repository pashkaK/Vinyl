package com.example.vinyl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;


import com.example.vinyl.Model.SongModel;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class SongInformationGathering {

    private static Context context;

    public static ArrayList<SongModel> getInfo(Context c) {

        context = c;
        ArrayList<SongModel> arrayOfSongs = new ArrayList<>();

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION };

        final Cursor cursor = context.getContentResolver().query(uri,
                    cursor_cols, null, null, null);

        while (cursor.moveToNext()) {

            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            arrayOfSongs.add(new SongModel(title, artist, album, path, duration, getAlbumArt(albumId)));
        }
        cursor.close();
        return arrayOfSongs;
    }

    private static Bitmap getAlbumArt(long albumId){
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri urii = ContentUris.withAppendedId(sArtworkUri, albumId);
        ContentResolver res = context.getContentResolver();
        InputStream in = null;
        try {
            in = res.openInputStream(urii);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in);
    }
}
