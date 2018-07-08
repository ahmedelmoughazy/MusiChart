package com.example.ahmed.musichart;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Ahmed on 5/13/2018.
 */

public class Track {

    private String mTrackName;
    private String mArtistName;
    private Bitmap mBitmap;

    public Track(String trackName,String artistName,Bitmap bitmap){
        mTrackName  = trackName;
        mArtistName = artistName;
        mBitmap        = bitmap;
    }


    public String getTrackName(){
        return mTrackName;
    }

    public String getArtistName(){
        return mArtistName;
    }

    public Bitmap getbitmap(){
        return mBitmap;
    }
}
