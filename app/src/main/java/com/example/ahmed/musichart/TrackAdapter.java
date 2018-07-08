package com.example.ahmed.musichart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ahmed on 5/13/2018.
 */

public class TrackAdapter extends ArrayAdapter <Track>{


    public TrackAdapter(Context context, ArrayList<Track> tracks) {
        super(context, 0,tracks);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Track currentTrack = getItem(position);

        TextView trackName = listItemView.findViewById(R.id.track_name);
        trackName.setText(currentTrack.getTrackName());

        TextView artistName = listItemView.findViewById(R.id.artist_name);
        artistName.setText(currentTrack.getArtistName());

        ImageView thumbnail = listItemView.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(currentTrack.getbitmap());

        return listItemView;
    }
}
