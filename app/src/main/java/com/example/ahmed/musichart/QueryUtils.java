package com.example.ahmed.musichart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ahmed on 5/13/2018.
 */

public final class QueryUtils {

    private QueryUtils(){}


    public static ArrayList<Track> extractTracks(String requestUrl) {

        URL url;

        ArrayList<Track> tracks;
        StringBuffer response = new StringBuffer();

        try {
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            throw new IllegalArgumentException("invalid url");
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            int status = connection.getResponseCode();

            if (status!=200){
                throw new IOException("Post failed with error code " + status);
            }else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }
                in.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }

            String responseJSON = response.toString();
            tracks = getTracksFromJSON(responseJSON);
        }
        Log.v("MyActivity","extractTracks");

        return tracks;
    }

    private static ArrayList<Track> getTracksFromJSON(String jsonResponse){

        ArrayList<Track> tracks = new ArrayList<>();

        try {
            JSONObject baseJSONObject = new JSONObject(jsonResponse);
            JSONObject tracksObject = baseJSONObject.getJSONObject("tracks");
            JSONArray tracksArray = tracksObject.getJSONArray("track");

            for (int i = 0 ;i<tracksArray.length();i++){
                JSONObject currentTrack = tracksArray.getJSONObject(i);
                String trackName = currentTrack.getString("name");
                JSONObject trackArtist = currentTrack.getJSONObject("artist");
                String artistName = trackArtist.getString("name");
                JSONArray imageArray = currentTrack.getJSONArray("image");
                JSONObject thumbnail = imageArray.getJSONObject(1);
                String imageUrl = thumbnail.getString("#text");

                Bitmap bitmap = null;

                try {
                    URL url = new URL(imageUrl);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tracks.add(new Track(trackName,artistName,bitmap));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tracks;
    }
}
