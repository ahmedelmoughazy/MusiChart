package com.example.ahmed.musichart;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrackActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Track>> {

    private ProgressBar mProgressBar;
    private final String TAG = "MyActivity";
    private final int Track_LOADER_ID = 0;
    private final String REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=9b8aa23ab5066c338aeb65d98700c786&format=json";
    private ListView trackList;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView emptyElement = findViewById(R.id.emptyElement);
        mProgressBar = findViewById(R.id.progress_bar);

        trackList = findViewById(R.id.tracks_list);

        adapter = new TrackAdapter(getApplicationContext(), new ArrayList<Track>());

        trackList.setAdapter(adapter);

        // check if there is internet connection to initiate request
        ConnectivityManager cM = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cM.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            getLoaderManager().initLoader(Track_LOADER_ID, null, this).forceLoad(); // لما بشيل forceload ال loadInBackGround مش بتشتغل
        }else {
            mProgressBar.setVisibility(View.GONE);
            emptyElement.setText("Check Your Internet Connection");
            trackList.setEmptyView(emptyElement);
        }

        //Loader initiator


        /*TrackAsyncTask task = new TrackAsyncTask();
        task.execute(REQUEST_URL);*/

    }

    @Override
    public Loader<ArrayList<Track>> onCreateLoader(int i, Bundle bundle) {
        // when loader is init this the first method invoked it returns the result from asyncTaskLoader ex.(new TrackLoader())
        Log.v(TAG, "onCreateLoader");
        return new TrackLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Track>> loader, ArrayList<Track> tracks) {

        adapter.clear();
        //tracks.clear();
        if (tracks != null && !tracks.isEmpty()) {
            adapter.addAll(tracks);
        } else {
            trackList.setEmptyView(findViewById(R.id.emptyElement));
        }

        mProgressBar.setVisibility(View.GONE);

        Log.v(TAG, "onLoadFinished");
        // executed after onCreateLoader and takes the return of it as a parameter
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Track>> loader) {
        adapter.clear();
    }

    private static class TrackLoader extends AsyncTaskLoader<ArrayList<Track>> {

        private String mUrl;

        public TrackLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        public ArrayList<Track> loadInBackground() {


            Log.v("MyActivity", "loadInBackground()");
            ArrayList<Track> tracks = QueryUtils.extractTracks(mUrl);
            return tracks;
        }

        @Override
        protected void onStartLoading() {
            Log.v("MyActivity", "onStartLoading");
            // similar to onPreExecute from asyncTask  شبهها
            super.onStartLoading();
        }
    }

    /*private class TrackAsyncTask extends AsyncTask<String,Void,ArrayList<Track>>{
        @Override
        protected ArrayList<Track> doInBackground(String... strings) {

            ArrayList<Track> tracks = QueryUtils.extractTracks(REQUEST_URL);
            return tracks;
        }

        @Override
        protected void onPostExecute(ArrayList<Track> tracks) {

            updateUI(tracks);
        }
    }*/

}
