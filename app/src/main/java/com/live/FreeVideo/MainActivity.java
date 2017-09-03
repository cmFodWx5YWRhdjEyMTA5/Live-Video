package com.live.FreeVideo;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import lazylist.LazyAdapter;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextView mTextMessage;
    private AsyncRequest request;
    private ListView list;
    private Session session;
    private LazyAdapter adapter;
    private ProgressBar progressBar;
    public static int APP_WRITE_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        verifyStoragePermissions(MainActivity.this);

        session = new Session();
        session.init(this.getBaseContext());

        request = new AsyncRequest();
        request.delegate = this;
        request.execute(session.SERVICE_URL);
    }

    public void  verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        APP_WRITE_EXTERNAL_STORAGE = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (APP_WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission

                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);

                } else
                {
                    ShowAlertBox();
                }
            }
        }

    }


    private void ShowAlertBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission denied!");
        String str = "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission";

        builder.setMessage(str)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setAdapter() {

        list = (ListView) findViewById(R.id.list_youtube);
        adapter = new LazyAdapter((Activity) this, session.movies);
        list.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Session.MyMovie movie = session.movies.get(position);

                Toast.makeText(session.context, "Please wait...", Toast.LENGTH_LONG)
                        .show();

                Intent i = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://www.youtube.com/watch?v=" + movie.getYoutubeId()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        });

    }

    @Override
    public void processFinish(String output) {
        session.movies = session.parseJson(output);
        setAdapter();
    }

    @Override
    public void processBitmapFinish(Bitmap output) {

    }

    @Override
    public void processMessageFinish(String output) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {

            case R.id.share: {
                String text = "Live Video TOP 100";
                String subject = "https://play.google.com/store/apps/details?id=com.live.FreeVideo";
                String shareText = "Share this app with friends";

                Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, text);
                sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, subject);
                startActivity(Intent.createChooser(sendIntent, shareText));

                return true;

            }

            case R.id.thumb_up: {
                String message = "If you enjoy using Live Video, would you mind taking a moment to rate it? It won’t take more than a minute. Thanks for your support!";
                AlertBox(message);
                return true;

            }


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    private void AlertBox(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setTitle("Confirmation")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                ApkGooglePlayUrl();
                                return;
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void ApkGooglePlayUrl() {

        Uri uri = Uri.parse("market://details?id="
                + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

}
