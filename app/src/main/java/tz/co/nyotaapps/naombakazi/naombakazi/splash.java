package tz.co.nyotaapps.naombakazi.naombakazi;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.view.ContextThemeWrapper;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class splash extends AppCompatActivity {

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    public static String SERVERVERSION = "1"; // code you want.
    public static String CURRENTSIMCARD = "1"; // code you want.
    public static String CURRENTSIMCARDCOUNTRYX;
    public static String CURRENTNAME;
    public static String CURRENTUMRI;
    public static String CURRENTAREA;
    public static String CURRENTMAELEZO;
    public static String CURRENTPICHASOURCE;
    public static String CURRENTASPECTRATIO;
    public static String CURRENTLOVE;
    public static String CURRENTNUMBER;
    public static String CURRENTCOUNTRY;
    public static int GENERICUSER = 1000;
    public static int MESSAGESPRESENT = 0;
    public static Context context;
    public JSONObject sendersx;
    public AndroidMultiPartEntity entity3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        context = splash.this;

        sendersx = new JSONObject();



        if (getIntent().getExtras() != null) {

            MESSAGESPRESENT = 1;

        }




        if (checkPermissions()) {
            //  permissions  granted.
            if (ContextCompat.checkSelfPermission(splash.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                Button vvb = findViewById(R.id.toaruhusa);
                vvb.setVisibility(View.VISIBLE);
                TextView vvbx = findViewById(R.id.checkpermit);
                vvbx.setVisibility(View.VISIBLE);

                ProgressBar vog = findViewById(R.id.progressBar1xxc);
                vog.setVisibility(View.GONE);

                TextView vogj = findViewById(R.id.checkinta);
                vogj.setVisibility(View.GONE);

                TextView bbn = findViewById(R.id.fullscreen_content);
                bbn.setVisibility(View.GONE);

            } else {

                    if(checkinternet())
                    {


                        entity3 = new AndroidMultiPartEntity(
                                new AndroidMultiPartEntity.ProgressListener() {

                                    @Override
                                    public void transferred(long num) {
                                        //publishProgress((int) ((num / (float) totalSize) * 100));
                                    }
                                });


                        checkIfsignedin();
                        //new forceUpdate().execute();
                        //new Getuser().execute();
                    }else {
                        ProgressBar vog = findViewById(R.id.progressBar1xxc);
                        vog.setVisibility(View.GONE);

                        TextView vogj = findViewById(R.id.checkinta);
                        vogj.setVisibility(View.VISIBLE);
                    }



            }


        }


    }





    public void checkIfsignedin(){
        try{
            SQLiteDatabase mydatabase = openOrCreateDatabase("fundi",MODE_PRIVATE,null);
            Cursor resultSet = mydatabase.rawQuery("Select * from user",null);
            //resultSet.moveToFirst();
            if( resultSet != null && resultSet.moveToFirst() ){
                String userNamex = resultSet.getString(0);
                storage.userName = userNamex;
                String userPhonex = resultSet.getString(1);
                storage.userNumber = userPhonex;
                String userIdx = resultSet.getString(2);
                storage.userId = userIdx;
                Log.e("SQL RESULT","THE ID " +userIdx);
                Log.e("SQL RESULT","THE NAME " +userNamex);
                Log.e("SQL RESULT","THE NAMBA " +userPhonex);
                resultSet.close();
                GENERICUSER = 1000;
                startActivity(new Intent(splash.this, MainActivity.class));

            }else{
                GENERICUSER = 10;
                startActivity(new Intent(splash.this, MainActivity.class));
            }



        }catch (SQLException v){
            Log.e("SQL ERROR","THE ERROR " +v.getMessage().toString());
            GENERICUSER = 10;
            startActivity(new Intent(splash.this, MainActivity.class));
        }



    }




    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(splash.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                            Button vvb = findViewById(R.id.toaruhusa);
                            vvb.setVisibility(View.VISIBLE);
                            TextView vvbx = findViewById(R.id.checkpermit);
                            vvbx.setVisibility(View.VISIBLE);

                            ProgressBar vog = findViewById(R.id.progressBar1xxc);
                            vog.setVisibility(View.GONE);

                            TextView vogj = findViewById(R.id.checkinta);
                            vogj.setVisibility(View.GONE);

                            TextView bbn = findViewById(R.id.fullscreen_content);
                            bbn.setVisibility(View.GONE);

                        }else {

                            if(checkinternet())
                            {
                                checkIfsignedin();
                            }else {
                                ProgressBar vog = findViewById(R.id.progressBar1xxc);
                                vog.setVisibility(View.GONE);

                                TextView vogj = findViewById(R.id.checkinta);
                                vogj.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                    // Show permissionsDenied
                    //updateViews();



                }
                return;
            }
        }
    }


    public boolean checkinternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;

        }


        return connected;


    }


    public void restartApp(View v) {
        Intent intent = new Intent(getApplicationContext(), splash.class);
        int mPendingIntentId = 201;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }




    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            finishAffinity();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}