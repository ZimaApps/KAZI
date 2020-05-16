package tz.co.nyotaapps.naombakazi.naombakazi;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class image extends AppCompatActivity {
    public static String filePath = null;
    public AdView mAdView;
    public static Uri IMAGEURI;
    public static boolean FROMIMAGE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        MobileAds.initialize(this, "ca-app-pub-4482019772887748~5289646748");
        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        onSelectImageClick();
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////GET IMAGES/////////////////////////////////////////////////
    public void reselect(View v) {
        onSelectImageClick();
    }

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
                ((ImageView) findViewById(R.id.imgPreviewzzzz)).setImageURI(result.getUri());

                IMAGEURI  = result.getUri();

                filePath = result.getUri().getPath().toString();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Problem, please try again or choose another photo..", Toast.LENGTH_LONG).show();

                FROMIMAGE = false;

            }
        }
    }


    public void goback (View v){
        FROMIMAGE = true;
        Intent myIntent = new Intent(image.this, MainActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        image.this.startActivity(myIntent);

    }




}
