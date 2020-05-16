package tz.co.nyotaapps.naombakazi.naombakazi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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

import butterknife.ButterKnife;
import butterknife.InjectView;


import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.VideoView;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import static tz.co.nyotaapps.naombakazi.naombakazi.splash.CURRENTSIMCARD;


public class SignupActivity extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = SignupActivity.class.getSimpleName();


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private Button btnCapturePicture, btnRecordVideo;


    //private static final String TAG = "SignupActivity";

    @InjectView(R.id.namex) EditText _namex;
    @InjectView(R.id.umrix) EditText _umrix;
    @InjectView(R.id.areax) EditText _areax;
    @InjectView(R.id.maelezox) EditText _maelezox;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.my_phone_input) net.rimoto.intlphoneinput.IntlPhoneInput _my_phone_input;


    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////PICHAAA/////////////////////////////////////////////////



    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private TextView waitt;
    private ImageView imgPreview;
    private VideoView vidPreview;
    //private Button btnUpload;
    long totalSize = 0;
    public AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public String THEGENDER;
    String myInternationalNumber;
    IntlPhoneInput phoneInputView;
    public String CURRENTSIMCARDX;
    public String CURRENTSIMCARDCOUNTRYX;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name)+" : Registration");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ButterKnife.inject(this);
        THEGENDER = "100";

// Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-4482019772887748~5289646748");
        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4482019772887748/9765994096");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        //mTextMessage.setText(R.string.title_notifications);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });


        //  permissions  granted.
        if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted


        } else {
            final TelephonyManager tm = (TelephonyManager) SignupActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                CURRENTSIMCARDX =tm.getSimSerialNumber();
                CURRENTSIMCARDCOUNTRYX =tm.getNetworkCountryIso();

            }


        }

        // Changing action bar background color
        // These two lines are not needed
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));

        btnCapturePicture = findViewById(R.id.cappp);
        //btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                onSelectImageClick();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }


            }
        });

        Button btnUpload = findViewById(R.id.btnUploadx);
        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // uploading the file to server
                new UploadFileToServer().execute();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });

        phoneInputView = findViewById(R.id.my_phone_input);

        progressBar = findViewById(R.id.progressBar);
        txtPercentage = findViewById(R.id.txtPercentage);
        waitt = findViewById(R.id.wait);
        progressBar = findViewById(R.id.progressBar);
        imgPreview = findViewById(R.id.imgPreview);
        imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
    }






    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.mume:
                if (checked)
                    // Pirates are the best
                    THEGENDER = "100";
                    break;
            case R.id.mke:
                if (checked)
                    // Ninjas rule
                    THEGENDER = "200";
                    break;
        }
    }







    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        //final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.MyAlertDialogStyle);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Creating Account...");
        //progressDialog.show();

        RadioGroup radioSexGroup = findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = findViewById(selectedId);

        myInternationalNumber = _my_phone_input.getText().toString();
        String gender =  THEGENDER;
        String name = _namex.getText().toString();
        String email = _umrix.getText().toString();
        String password = _areax.getText().toString();
        String maelezox = _maelezox.getText().toString();

        LinearLayout voik = findViewById(R.id.pichajjjj);
        LinearLayout votu = findViewById(R.id.bbbbbv);
        votu.setVisibility(View.GONE);
        voik.setVisibility(View.VISIBLE);


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    public boolean validate() {
        boolean valid = true;
        myInternationalNumber = _my_phone_input.getText().toString();
        String namex = _namex.getText().toString();
        String umrix = _umrix.getText().toString();
        String areax = _areax.getText().toString();
        String maelezox = _maelezox.getText().toString();


        if (namex.isEmpty() || namex.length() < 4) {
            _namex.setError("A minimum of 4 characters is required");
            valid = false;
        } else {
            _namex.setError(null);
        }

        if (umrix.isEmpty() || umrix.length() < 2) {
            _umrix.setError("A minimum of 2 characters is required");
            valid = false;
        } else {
            _umrix.setError(null);
        }

        if (areax.isEmpty() || areax.length() < 4) {
            _areax.setError("A minimum of 4 characters is required");
            valid = false;
        } else {
            _areax.setError(null);
        }

        if (maelezox.isEmpty() || maelezox.length() < 20) {
            _maelezox.setError("A minimum of 20 characters is required");
            valid = false;
        } else {
            _maelezox.setError(null);
        }


        return valid;
    }



   ////////////////////////////////////////////////////////////////////////////////////////////////////////
   /////////////////////////////////////////////GET IMAGES/////////////////////////////////////////////////

    /** Start pick image activity with chooser. */
    public void onSelectImageClick() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Crop")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("GO")
                .setRequestedSize(612, 612)
                //.setCropMenuCropButtonIcon(R.drawable.ic_ok_round)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.imgPreview)).setImageURI(result.getUri());

                Button btnUpload = findViewById(R.id.btnUploadx);;
                btnUpload.setVisibility(View.VISIBLE);
                filePath = result.getUri().getPath().toString();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Problem, please try again or choose another photo.." + result.getError(), Toast.LENGTH_LONG).show();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }


            }
        }
    }





    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////PICHAA///////////////////////////////////////////////////////////////


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
            waitt.setVisibility(View.VISIBLE);
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
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                RadioGroup radioSexGroup = findViewById(R.id.radioSex);
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                RadioButton radioSexButton = findViewById(selectedId);

                String gender =  THEGENDER;
                String name = _namex.getText().toString();
                String umri = _umrix.getText().toString();
                String area = _areax.getText().toString();
                String maelezo = _maelezox.getText().toString();
                String JAMANII = CURRENTSIMCARD;

                if(phoneInputView.isValid()) {
                    myInternationalNumber = phoneInputView.getNumber();

                }else {


                }


                Log.d("KADIIIIIIIIII", "HIIII HAPAAAAAAAAAAAAAAA"+myInternationalNumber);
                Log.d("KADIIIIIIIIII", "HIIII HAPAAAAAAAAAAAAAAA"+myInternationalNumber);
                Log.d("KADIIIIIIIIII", "HIIII HAPAAAAAAAAAAAAAAA"+myInternationalNumber);
                Log.d("KADIIIIIIIIII", "HIIII HAPAAAAAAAAAAAAAAA"+CURRENTSIMCARDX);
                Log.d("KADIIIIIIIIII", "HIIII HAPAAAAAAAAAAAAAAA"+CURRENTSIMCARDX);
                Log.d("KADIIIIIIIIII", "HIIII HAPAAAAAAAAAAAAAAA"+CURRENTSIMCARDX);

                // Extra parameters if you want to pass to server
                entity.addPart("uniqueID", new StringBody(CURRENTSIMCARDX));
                entity.addPart("Jina", new StringBody(name.replaceAll("[^A-Za-z0-9 ]", "")));
                entity.addPart("umri", new StringBody(umri));
                entity.addPart("area", new StringBody(area));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("maelezo", new StringBody(maelezo));
                entity.addPart("thenumber", new StringBody(myInternationalNumber));
                entity.addPart("country", new StringBody(CURRENTSIMCARDCOUNTRYX));

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
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            new Getuser().execute();

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlertnnn(String message) {
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


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void goback(View v){

        LinearLayout voik = findViewById(R.id.pichajjjj);
        ScrollView votu = findViewById(R.id.infoo);
        votu.setVisibility(View.VISIBLE);
        voik.setVisibility(View.GONE);
        _signupButton.setEnabled(true);
    }

    public void restartApphh() {


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


                        splash.GENERICUSER = 1000;
                        Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        SignupActivity.this.startActivity(myIntent);


                    }else{
                        Log.e("RESULT FROM SERVER", "NO USERS NO USERS NO USERS: ");

                        splash.GENERICUSER = 10;
                        Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        SignupActivity.this.startActivity(myIntent);

                    }





                } catch (final JSONException e) {
                    Log.e("RESULT FROM SERVER", "Json parsing error: " + e.getMessage());

                    splash.GENERICUSER = 10;
                    Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    SignupActivity.this.startActivity(myIntent);
                }

            } else {
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                Log.e("RESULT FROM SERVER", "Couldn't get json from server.");
                splash.GENERICUSER = 10;
                Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                SignupActivity.this.startActivity(myIntent);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }
    }



}