package com.live.FreeVideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Session implements AsyncResponse {

    private boolean Session;
    private SharedPreferences sharedPrefs;
    public Context context;

    public static String SERVICE_URL = "http://kazanlachani.com/live_video/video.php";
    public static String interstitial_key = "ca-app-pub-2108590561691007/4300312372";

    public static ArrayList<MyMovie> movies = new ArrayList<MyMovie>();

    public Session() {

        this.Session = false;
    }

    public void init(Context context) {

        this.context = context;
        this.InterstitialAd();
    }

    public void InterstitialAd() {

        final InterstitialAd interstitial  = new InterstitialAd(this.context);
        interstitial.setAdUnitId(this.interstitial_key);

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {

                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });

    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    public ArrayList<MyMovie> parseJson(String result) {

        if (!isNetworkAvailable(context)) {
            System.exit(0);
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<MyMovie> movies = new ArrayList<MyMovie>();
        for (int i = 0; i < jsonArray.length(); ++i) {

            JSONObject element = null;
            try {
                element = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            MyMovie user = null;
            try {
                MyMovie movie = new MyMovie(element);
                movies.add(movie);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return movies;

    }

    @Override
    public void processFinish(String output) {

    }

    @Override
    public void processBitmapFinish(Bitmap output) {

    }

    @Override
    public void processMessageFinish(String output) {

    }

    public class MyMovie {

        private String username;
        private String youtubeId;
        private String reg_date;

        public MyMovie(JSONObject element) throws JSONException {

            if (element.has("username")) {
                this.username = element.getString("username");
            } else
                this.username = "";

            if (element.has("youtubeId")) {
                this.youtubeId = element.getString("youtubeId");
            } else
                this.youtubeId = "";

            if (element.has("reg_date")) {
                this.reg_date = element.getString("reg_date");
            } else
                this.reg_date = "";

        }

        public String getYoutubeId() {
            return youtubeId;
        }

        public String getReg_date() {
            return reg_date;
        }

        public String getUsername() {
            return username;
        }

        public String getImageName() {
            return "https://i.ytimg.com/vi/" + this.getYoutubeId() + "/default.jpg";
        }
    }

}
