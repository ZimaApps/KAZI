package tz.co.nyotaapps.naombakazi.naombakazi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.annotations.NotNull;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;




public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    String limeseji;
    public String CURRENTSIMCARDCHAT;
    public AdView mAdView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        MobileAds.initialize(this, "ca-app-pub-4482019772887748~5289646748");
        mAdView = findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4482019772887748/9765994096");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if(MainActivity.MSGON == 1){
            ProgressBar bogo = findViewById(R.id.progressBar1xxczpo);
            bogo.setVisibility(View.GONE);
        }

        //  permissions  granted.
        if (ContextCompat.checkSelfPermission(Chat.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted


        } else {
            final TelephonyManager tm = (TelephonyManager) Chat.this.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                CURRENTSIMCARDCHAT = "" + tm.getSimSerialNumber();

            }


        }

        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://naombakazi-30dab.firebaseio.com/messages/" + joint (CURRENTSIMCARDCHAT,UserDetails.chatWith));
        //reference2 = new Firebase("https://androidchatapp-76776.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                limeseji = messageText;
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    //reference2.push().setValue(map);
                    messageArea.setText("");

                    new sendmesejitoserver().execute();
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                ProgressBar bogo = findViewById(R.id.progressBar1xxczpo);
                bogo.setVisibility(View.GONE);

                if(userName.equals(UserDetails.username)){

                    addMessageBox(message, 1);
                }
                else{
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        TextView bbn = findViewById(R.id.userx);
        bbn.setText(UserDetails.usernamex);
        MainActivity.MSGON = 0;


    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.requestFocus();
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.scrollTo(0, scrollView.getBottom());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }






    /**
     * Uploading the file to server
     * */
    private class sendmesejitoserver extends AsyncTask<Void, Integer, String> {

        private String THEMESEJI;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            Log.e("SEND MESEJI CLASS", "Yes");
            Log.e("SEND MESEJI CLASS", "Yes");
            Log.e("SEND MESEJI CLASS", "Yes");

            //spinner = mesejiview.findViewById(R.id.progressBar1);

            //spinner.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {

            String getResponseString = "";

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            // Adding file data to http body

            // Extra parameters if you want to pass to server
            try{




                String postURL = "http://naombakazi.nyotaapps.co.tz/savemsg.php";

                HttpPost post = new HttpPost(postURL);

                List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
                paramsx.add(new BasicNameValuePair("sendersimcard", CURRENTSIMCARDCHAT));
                paramsx.add(new BasicNameValuePair("receiversimcard", UserDetails.chatWith));
                paramsx.add(new BasicNameValuePair("meseji", limeseji));
                paramsx.add(new BasicNameValuePair("joint", joint (CURRENTSIMCARDCHAT,UserDetails.chatWith)));
                paramsx.add(new BasicNameValuePair("timestamp", Long.toString(timestamp.getTime())));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(paramsx, "UTF-8");
                post.setEntity(ent);

                HttpClient client = new DefaultHttpClient();
                HttpResponse responsePOST = client.execute(post);

                BufferedReader reader = new BufferedReader(new InputStreamReader(responsePOST.getEntity().getContent()), 2048);

                if (responsePOST != null) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(" line : " + line);
                        Log.e("RESPONSE", "Response from server: " + line);

                        sb.append(line);
                    }

                    getResponseString = sb.toString();
//use server output getResponseString as string value.
                }


            } catch (UnsupportedEncodingException un){

            }catch (IOException io){

            }





            return getResponseString;
        }




        @Override
        protected void onPostExecute(String result) {
            Log.e("RESPONSE", "Response from server: " + result);
            Log.e("SENDMESEJI CLASS ENDING", "Yes" + result);
            Log.e("SENDMESEJI CLASS ENDING", "Yes" + result);
            Log.e("SENDMESEJI CLASS ENDING", "Yes" + result);

            FirebasePush firebasePush = new FirebasePush("AIzaSyDNkSNSCRM8tXcgQHoxFIRGSQflCwHaLFs");
            firebasePush.setAsyncResponse(new PushNotificationTask.AsyncResponse() {
                @Override
                public void onFinishPush(@NotNull String ouput) {
                    Log.e("OUTPUT", ouput);
                    Log.e("OUTPUT", ouput);
                    Log.e("OUTPUT", ouput);
                    Log.e("OUTPUT", ouput);
                }
            });

            firebasePush.setNotification(new Notification(splash.CURRENTNAME,limeseji));
            firebasePush.sendToTopic("USER_"+UserDetails.chatWith);


            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */





    // function for merging two strings
    public static String joint(String s1, String s2)
    {
        String s = s1;

        int total = 0;
        for(int i=0; i<s.length(); i+=1) {
            total = total + Character.getNumericValue(s.charAt(i));
        }

        String sx = s2;

        int totalx = 0;
        for(int ix=0; ix<sx.length(); ix+=1) {
            totalx = totalx + Character.getNumericValue(sx.charAt(ix));
        }

        int totalp = total + totalx;


        return Integer.toString(totalp);
    }





}