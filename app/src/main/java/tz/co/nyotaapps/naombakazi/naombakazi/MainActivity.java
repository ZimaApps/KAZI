package tz.co.nyotaapps.naombakazi.naombakazi;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.mapdb.DB;
//import org.mapdb.DBMaker;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static tz.co.nyotaapps.naombakazi.naombakazi.splash.CURRENTSIMCARD;
import static tz.co.nyotaapps.naombakazi.naombakazi.splash.GENERICUSER;


public class MainActivity extends AppCompatActivity {

    ArrayList<DataModel> dataModels;
    ArrayList<DataModel> dataModels2;
    ArrayList<DataModel1> dataModels3;
    ListView listView;
    ListView listView2;
    ListView listView3;
    private static CustomAdapter adapter;
    private static CustomAdapter adapter2;
    private static CustomAdapter1 adapter3;
    public static Activity theactivity;
    //public DB db;
    SimpleDraweeView wenzuri;
    View convertViewxxx;
    public File[] filesx;
    public LinearLayout inmeseji;
    public AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private ProgressBar progressBar;
    private TextView txtPercentage;
    private ImageView imgPreview;
    long totalSize = 0;
    public static Context context;
    public static int ON = 0;
    public static int MSGON = 0;
    public static int FROMIMAGE = 0;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    RelativeLayout ccc1 = findViewById(R.id.wachumbacc);
                    ccc1.setVisibility(View.VISIBLE);
                    ScrollView cccdd1 = findViewById(R.id.mimi);
                    cccdd1.setVisibility(View.GONE);
                    ScrollView cccx1 = findViewById(R.id.mesejizz);
                    cccx1.setVisibility(View.GONE);

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    return true;
                case R.id.navigation_meseji:
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    //mTextMessage.setText(R.string.title_dashboard);

                    inmeseji = findViewById(R.id.mesejilistcpp);

                    RelativeLayout ccc = findViewById(R.id.wachumbacc);
                    ccc.setVisibility(View.GONE);
                    ScrollView cccdd = findViewById(R.id.mimi);
                    cccdd.setVisibility(View.GONE);
                    ScrollView cccx = findViewById(R.id.mesejizz);
                    cccx.setVisibility(View.VISIBLE);


                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    return true;
                case R.id.navigation_mimi:
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    RelativeLayout ccc13 = findViewById(R.id.wachumbacc);
                    ccc13.setVisibility(View.GONE);
                    ScrollView cccdd13 = findViewById(R.id.mimi);
                    cccdd13.setVisibility(View.VISIBLE);
                    ScrollView cccx13 = findViewById(R.id.mesejizz);
                    cccx13.setVisibility(View.GONE);

                    //mTextMessage.setText(R.string.title_notifications);
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    return true;

                case R.id.navigation_jiunge:

                    Intent myIntent = new Intent(MainActivity.this, SignupActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    MainActivity.this.startActivity(myIntent);



                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        //Fresco.getImagePipeline().clearCaches();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        context = MainActivity.this;

        ON = 1;

        if(splash.GENERICUSER==10){
            navigation.getMenu().removeItem(R.id.navigation_mimi);
            navigation.getMenu().removeItem(R.id.navigation_meseji);
            Log.e("GOING MTUUxxxxxx", "USER."+splash.GENERICUSER);
            Log.e("GOING MTUUxxxxxx", "USERRRRR."+splash.GENERICUSER);
            Log.e("GOING MTUUzzzzzz", "USERRRRRRRRRR."+splash.GENERICUSER);
        }
        if(splash.GENERICUSER==1000) {
            Log.e("GOING MTUUyyyyyyy", "USERRRRRRRRRR. "+splash.GENERICUSER);
            Log.e("GOING MTUU", "USERRRRRRRRRR."+splash.GENERICUSER);
            Log.e("GOING MTUU", "USERRRRRRRRRR."+splash.GENERICUSER);
            navigation.getMenu().removeItem(R.id.navigation_jiunge);

            if(image.FROMIMAGE){
                RelativeLayout ccc13 = findViewById(R.id.wachumbacc);
                ccc13.setVisibility(View.GONE);
                ScrollView cccdd13 = findViewById(R.id.mimi);
                cccdd13.setVisibility(View.VISIBLE);
                ScrollView cccx13 = findViewById(R.id.mesejizz);
                cccx13.setVisibility(View.GONE);

                SimpleDraweeView draweeViewxx = findViewById(R.id.avatarImageViewggx);
                draweeViewxx.setAspectRatio(1);
                draweeViewxx.setImageURI(image.IMAGEURI);


            }else {

                Uri uri = Uri.parse("http://naombakazi.nyotaapps.co.tz/images/"+splash.CURRENTPICHASOURCE);
                SimpleDraweeView draweeViewxx = findViewById(R.id.avatarImageViewggx);
                draweeViewxx.setAspectRatio(1);
                draweeViewxx.setImageURI(uri);

            }

            EditText vv1 = findViewById(R.id.namenzz);
            vv1.setText(splash.CURRENTNAME);
            EditText vv2 = findViewById(R.id.umrinzz);
            vv2.setText(splash.CURRENTUMRI);
            EditText vv3 = findViewById(R.id.areamzz);
            vv3.setText(splash.CURRENTAREA);
            EditText vv4 = findViewById(R.id.maelezobzz);
            vv4.setText(splash.CURRENTMAELEZO);
            TextView vgb = findViewById(R.id.lovekk);
            vgb.setText(splash.CURRENTLOVE);
            TextView vgbc = findViewById(R.id.thenumber);
            vgbc.setText(splash.CURRENTNUMBER);

        }


        progressBar = findViewById(R.id.progressBar);
        txtPercentage = findViewById(R.id.txtPercentage);

        progressBar = findViewById(R.id.progressBar);



        inmeseji = findViewById(R.id.mesejilistcpp);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = "2345";
            String channelName = "Housekeepers";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }





        Log.d("FIREBASEEEEEEEEEEEE", "Subscribing to news topic");
        // [START subscribe_topics]

        FirebaseMessaging.getInstance().subscribeToTopic("USER_"+splash.CURRENTSIMCARD)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "IMEUNGWANISHWAAAAA AAA";
                        if (!task.isSuccessful()) {
                            msg = "NOOOO KUUUNGANISHWAAAAAA";
                        }
                        Log.d("FIREBASEEEEEEEEEEEE", msg);
                        Log.d("FIREBASEEEEEEEEEEEE", msg);
                        Log.d("FIREBASEEEEEEEEEEEE", msg);
                        Log.d("FIREBASEEEEEEEEEEEE", msg);

                    }
                });
        // [END subscribe_topics]



        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-4482019772887748~5289646748");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4482019772887748/9765994096");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        theactivity = MainActivity.this;

        FirebaseMessaging.getInstance().subscribeToTopic("USER_"+splash.CURRENTSIMCARD);

        //filesx = getFileList(context.getFilesDir().getPath() + "/");

        Fresco.initialize(this);
        //Fresco.getImagePipeline().clearCaches();

        listView= findViewById(R.id.list);
        listView2= findViewById(R.id.list2);

        //mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        dataModels= new ArrayList<>();
        dataModels2= new ArrayList<>();
        dataModels3= new ArrayList<>();






        if (getIntent().getExtras() != null) {

            inmeseji = findViewById(R.id.mesejilistcpp);

            RelativeLayout ccc = findViewById(R.id.wachumbacc);
            ccc.setVisibility(View.GONE);
            ScrollView cccdd = findViewById(R.id.mimi);
            cccdd.setVisibility(View.GONE);
            ScrollView cccx = findViewById(R.id.mesejizz);
            cccx.setVisibility(View.VISIBLE);


            new Getmesejingpeople().execute();

        }


        if (splash.MESSAGESPRESENT == 1){
            inmeseji = findViewById(R.id.mesejilistcpp);

            RelativeLayout ccc = findViewById(R.id.wachumbacc);
            ccc.setVisibility(View.GONE);
            ScrollView cccdd = findViewById(R.id.mimi);
            cccdd.setVisibility(View.GONE);
            ScrollView cccx = findViewById(R.id.mesejizz);
            cccx.setVisibility(View.VISIBLE);


            new Getmesejingpeople().execute();
            new GetGirls().execute();



        }else {

            new GetGirls().execute();
            //new GetBoys().execute();

            new Getmesejingpeople().execute();
        }


        //new Getmesajes().execute();






    }



    private class GetGirls extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://naombakazi.nyotaapps.co.tz/getwatu.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e("RESULT FROM SERVER", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("wachumba");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        final String simcard = c.getString("uniqueID");
                        final String name = c.getString("Mtangazaji");
                        final String umri = c.getString("umri");
                        //final String gender = c.getString("gender");
                        final String area = c.getString("Mtaa");
                        final String maelezo = c.getString("Maelezo1");
                        final String pichasource = c.getString("Image");
                        final String aspectratio = "1";
                        final String love = c.getString("bad");
                        final String thenumber = c.getString("Namba");
                        final String country = c.getString("country");


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                dataModels.add(new DataModel(simcard, name, umri,area,maelezo,pichasource,aspectratio,love,thenumber,country));
                                //dataModels.add(new DataModel("Apple Pie", "Android 1.0", "1","September 23, 2008","hjh","jhjh","hhjh","100"));

                                adapter= new CustomAdapter(dataModels,getApplicationContext());


                                listView.setAdapter(adapter);
                                // listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                //  @Override
                                //  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                //  DataModel dataModel= dataModels.get(position);

                                //Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
                                //.setAction("No action", null).show();
                                // }
                                // });






                            }
                        });



                    }
                } catch (final JSONException e) {
                    Log.e("RESULT FROM SERVER", "Json parsing error: " + e.getMessage());


                }

            } else {
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ProgressBar vbg = findViewById(R.id.progressBar1xxcz);
            vbg.setVisibility(View.GONE);
        }
    }





    public void getimage(View v){

        Intent myIntent = new Intent(MainActivity.this, image.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }




    public void goupdate(View v){


        new UploadFileToServer().execute();


    }

    public void godelete(View v){


        new deleteuser().execute();


    }


    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPDATE_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                if(image.FROMIMAGE) {
                    File sourceFile = new File(image.filePath);
                    // Adding file data to http body
                    entity.addPart("image", new FileBody(sourceFile));
                }



                EditText namep = findViewById(R.id.namenzz);
                String namept = namep.getText().toString();
                EditText umrii = findViewById(R.id.umrinzz);
                String umriit = umrii.getText().toString();
                EditText areamzzp = findViewById(R.id.areamzz);
                String areamzzpt = areamzzp.getText().toString();
                EditText maelezobzzp = findViewById(R.id.maelezobzz);
                String maelezobzzpt = maelezobzzp.getText().toString();
                EditText thenumberp = findViewById(R.id.thenumber);
                String myInternationalNumber = thenumberp.getText().toString();



                // Extra parameters if you want to pass to server
                entity.addPart("uniqueID", new StringBody(splash.CURRENTSIMCARD));
                entity.addPart("Jina", new StringBody(namept.replaceAll("[^A-Za-z0-9 ]", "")));
                entity.addPart("umri", new StringBody(umriit));
                entity.addPart("area", new StringBody(areamzzpt));
                entity.addPart("maelezo", new StringBody(maelezobzzpt));
                entity.addPart("thenumber", new StringBody(myInternationalNumber));
                entity.addPart("country", new StringBody(splash.CURRENTSIMCARDCOUNTRYX));
                //CURRENTNUMBER = c.getString("thenumber");
                //CURRENTCOUNTRY = c.getString("country");




                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("WANGOMBE", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Results")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                        new Getuser().execute();
                        //restartApp();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

















    @SuppressWarnings("unchecked")

    private class Getmesejingpeople extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://naombakazi.nyotaapps.co.tz/getallmesages.php?simcard=" + CURRENTSIMCARD;
            String jsonStr = sh.makeServiceCall(url);

            Log.e("RESULT FROM SERVER", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("meseji");

                    if (contacts.length() > 0) {


                        // looping through All Contacts
                        for (int i = 0; i < contacts.length(); i++) {


                            //System.out.println(file.getName());
                            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                            convertViewxxx = inflater.inflate(R.layout.senders, inmeseji, false);


                            JSONObject c = contacts.getJSONObject(i);
                            //final String simcard = c.getString("simcard");

                            JSONObject object = new JSONObject();
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            try {
                                object.put("timestamp", Long.toString(timestamp.getTime()));
                                object.put("name", c.getString("Mtangazaji"));
                                object.put("simcard", c.getString("uniqueID"));
                                object.put("towho", c.getString("towho"));
                                object.put("picha", c.getString("Image"));
                                object.put("meseji", c.getString("meseji"));
                                object.put("joint", c.getString("joint"));

                                //sendersx.put(c.getString("simcard"), "");

                                runOnUiThread(new Runnable() {
                                    public void run() {


                                        try {



                                            Uri uri = Uri.parse("http://naombakazi.nyotaapps.co.tz/" + c.getString("Image").toString());
                                            //SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                                            wenzuri = convertViewxxx.findViewById(R.id.avatarImageViewpp);
                                            wenzuri.setAspectRatio(1);
                                            wenzuri.setImageURI(uri);
                                            TextView lijina = convertViewxxx.findViewById(R.id.sendername);
                                            lijina.setText(c.getString("Mtangazaji").toString());
                                            TextView limeseji = convertViewxxx.findViewById(R.id.mesejix);
                                            limeseji.setText(c.getString("meseji").toString());
                                            if (convertViewxxx.getParent() != null)
                                                ((ViewGroup) convertViewxxx.getParent()).removeView(convertViewxxx); // <- fix


                                            LinearLayout zzx = convertViewxxx.findViewById(R.id.readmsj);
                                            zzx.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(final View view) {

                                                    try {
                                                        UserDetails.usernamex = c.getString("Mtangazaji").toString();
                                                        UserDetails.username = splash.CURRENTSIMCARD;
                                                        UserDetails.chatWith = c.getString("uniqueID").toString();
                                                        UserDetails.joint = c.getString("joint").toString();
                                                    }catch (JSONException m){

                                                    }


                                                    Intent myIntent = new Intent(MainActivity.this, Chat.class);
                                                    //Optional parameters
                                                    MainActivity.this.startActivity(myIntent);

                                                }

                                            });


                                            inmeseji.addView(convertViewxxx, 0);




                                        }catch (JSONException v){

                                        }


                                    }
                                });






                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                            Log.e("SAVINGMESEJIII", object.toString());
                            Log.e("SAVINGMESEJIII", object.toString());
                            Log.e("SAVINGMESEJIII", object.toString());
                            Log.e("SAVINGMESEJIII", object.toString());


                        }



                    } else {

                        runOnUiThread(new Runnable() {
                            public void run() {

                                TextView iiio = findViewById(R.id.nomeseji);
                                iiio.setVisibility(View.VISIBLE);
                                ProgressBar nnmm = findViewById(R.id.progressmsj);
                                nnmm.setVisibility(View.GONE);

                            }
                        });

                    }


                } catch (final JSONException e) {
                    Log.e("RESULT FROM SERVER", "Json parsing error: " + e.getMessage());


                }

                markread();


            } else {
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Json parsing error: ");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ProgressBar nnmm = findViewById(R.id.progressmsj);
            nnmm.setVisibility(View.GONE);


        }
    }


    void markread() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://naombakazi.nyotaapps.co.tz/markread.php?simcard=" + CURRENTSIMCARD)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String myResponse = response.body().string();

                Log.e("RESULT FROM SERVER MAKK", myResponse);
                Log.e("RESULT FROM SERVER MAKK", myResponse);
                Log.e("RESULT FROM SERVER MAKK", myResponse);
                Log.e("RESULT FROM SERVER MAKK", myResponse);
                Log.e("RESULT FROM SERVER MAKK", myResponse);
                Log.e("RESULT FROM SERVER MAKK", myResponse);




            }
        });
    }














    private class deleteuser extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");


        }

        @Override
        protected String doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://naombakazi.nyotaapps.co.tz/deleteuser.php?simcard=" + splash.CURRENTSIMCARD;
            String jsonStr = sh.makeServiceCall(url);



            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // this code will be executed after 2 seconds

                    restartApp();
                }
            }, 2000);
            super.onPostExecute(result);
        }
    }









    private class Getuser extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.e("GOTEN SIMCARD", "GOTEN SIMCARD: " + CURRENTSIMCARD);
            Log.e("GOTEN SIMCARD", "GOTEN SIMCARD: " + CURRENTSIMCARD);
            Log.e("GOTEN SIMCARD", "GOTEN SIMCARD: " + CURRENTSIMCARD);
            Log.e("GOTEN SIMCARD", "GOTEN SIMCARD: " + CURRENTSIMCARD);


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://naombakazi.nyotaapps.co.tz/user.php?simcard=" + CURRENTSIMCARD;
            String jsonStr = sh.makeServiceCall(url);

            Log.e("RESULT FROM SERVER", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("mtu");

                    if(contacts.length()>0){

                        // looping through All Contacts
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            //final String simcard = c.getString("simcard");
                            splash.CURRENTNAME = c.getString("Mtangazaji");
                            splash.CURRENTUMRI = c.getString("umri");
                            splash.CURRENTAREA = c.getString("Mtaa");
                            splash.CURRENTMAELEZO = c.getString("Maelezo1");
                            splash.CURRENTPICHASOURCE = c.getString("Image");
                            splash.CURRENTASPECTRATIO = "1";
                            splash.CURRENTLOVE = c.getString("bad");
                            splash.CURRENTNUMBER = c.getString("Namba");
                            splash.CURRENTCOUNTRY = c.getString("country");

                            Log.e("GOING MTUU", "JINAAAAxXXXXXXXXX." + splash.CURRENTNAME);
                            Log.e("GOING MTUU", "JINAAAA." + splash.CURRENTNAME);
                            Log.e("GOING MTUU", "JINAAAA." + splash.CURRENTNAME);


                        }
                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                RelativeLayout ccc1 = findViewById(R.id.wachumbacc);
                                ccc1.setVisibility(View.VISIBLE);
                                ScrollView cccdd1 = findViewById(R.id.mimi);
                                cccdd1.setVisibility(View.GONE);
                                ScrollView cccx1 = findViewById(R.id.mesejizz);
                                cccx1.setVisibility(View.GONE);
                            }
                        });

                        splash.GENERICUSER = 1000;
                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        MainActivity.this.startActivity(myIntent);


                    }else{
                        Log.e("RESULT FROM SERVER", "NO USERS NO USERS NO USERS: ");
                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                RelativeLayout ccc1 = findViewById(R.id.wachumbacc);
                                ccc1.setVisibility(View.VISIBLE);
                                ScrollView cccdd1 = findViewById(R.id.mimi);
                                cccdd1.setVisibility(View.GONE);
                                ScrollView cccx1 = findViewById(R.id.mesejizz);
                                cccx1.setVisibility(View.GONE);
                            }
                        });
                        splash.GENERICUSER = 10;
                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        MainActivity.this.startActivity(myIntent);

                    }





                } catch (final JSONException e) {
                    Log.e("RESULT FROM SERVER", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            RelativeLayout ccc1 = findViewById(R.id.wachumbacc);
                            ccc1.setVisibility(View.VISIBLE);
                            ScrollView cccdd1 = findViewById(R.id.mimi);
                            cccdd1.setVisibility(View.GONE);
                            ScrollView cccx1 = findViewById(R.id.mesejizz);
                            cccx1.setVisibility(View.GONE);
                        }
                    });
                    splash.GENERICUSER = 10;
                    Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }

            } else {
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        RelativeLayout ccc1 = findViewById(R.id.wachumbacc);
                        ccc1.setVisibility(View.VISIBLE);
                        ScrollView cccdd1 = findViewById(R.id.mimi);
                        cccdd1.setVisibility(View.GONE);
                        ScrollView cccx1 = findViewById(R.id.mesejizz);
                        cccx1.setVisibility(View.GONE);
                    }
                });
                splash.GENERICUSER = 10;
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



            super.onPostExecute(result);


        }
    }



















    public void restartApp() {


        GENERICUSER = 10;
        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(myIntent);
        //new GetBoys().execute();
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