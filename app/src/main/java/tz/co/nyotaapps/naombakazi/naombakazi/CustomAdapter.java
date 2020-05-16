package tz.co.nyotaapps.naombakazi.naombakazi;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.ArrayList;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import static tz.co.nyotaapps.naombakazi.naombakazi.MainActivity.context;


public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;
    final public String MESEJI = "";
    public String SENDEESIMCARD;
    private ProgressBar spinner;
    private Activity activity; //activity is defined as a global variable in your AsyncTask
    private View mesejiview;
    public String SENDEENAME;
    private InterstitialAd mInterstitialAd;
    public ProgressBar waitmsg;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView umri;
        TextView area;
        TextView maelezo;
        TextView pichasource;
        TextView aspectratio;
        TextView love;
        ImageView fire;
        ImageView send;
        SimpleDraweeView image;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(final View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        // Get the data item for this position
        final  DataModel dataModel = getItem(position);


        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-4482019772887748/9765994096");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            TextView name = convertView.findViewById(R.id.name);
            TextView umri = convertView.findViewById(R.id.umri);
            TextView area = convertView.findViewById(R.id.area);
            TextView maelezo = convertView.findViewById(R.id.maelezo);
            TextView love = convertView.findViewById(R.id.love);
            ImageView fire = convertView.findViewById(R.id.fire);
            ImageView send = convertView.findViewById(R.id.send);
            TextView numx = convertView.findViewById(R.id.thenumbercc);
            TextView contx = convertView.findViewById(R.id.thecountry);
            SimpleDraweeView image = convertView.findViewById(R.id.my_image_view);


            name.setText(dataModel.getname());
            umri.setText(dataModel.getumri());
            area.setText(dataModel.getarea());
            maelezo.setText(dataModel.getmaelezo());
            love.setText(dataModel.getlove());
            numx.setText(dataModel.thenumber());
            contx.setText(dataModel.country());


            Uri uri = Uri.parse("http://naombakazi.nyotaapps.co.tz/images/"+dataModel.getpichasource());
            //SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
            image.setAspectRatio(1);
            image.setImageURI(uri);



            fire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.requestLayout();


                    view.getLayoutParams().height = 70;
                    view.getLayoutParams().width = 70;
                        OkHttpClient client = new OkHttpClient();


                    //  permissions  granted.
                    //Add before making a phone call
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + dataModel.thenumber()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);



                        Request request = new Request.Builder()
                                .url("http://naombakazi.nyotaapps.co.tz/fire.php?fireto="+dataModel.simcard +"&firefrom="+splash.CURRENTSIMCARD)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override

                            public void onFailure(Call call, IOException e) {
                                call.cancel();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {


                                String myResponse = response.body().string();
                                ((Activity)context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                view.requestLayout();
                                ImageViewCompat.setImageTintList(fire, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
                                view.getLayoutParams().height = 60;
                                view.getLayoutParams().width = 60;

                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        } else {
                                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                                        }


                                    }
                                });

                                Log.e("RESULT FROM SERVER FIRE", "Response from url: " + myResponse);
                                Log.e("RESULT FROM SERVER FIRE", "Response from url: " + myResponse);
                                Log.e("RESULT FROM SERVER FIRE", "Response from url: " + myResponse);




                            }
                        });

                }
            });


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View viewX) {
                    Log.e("CALL RESULTS", "CALEEEEED");
                    Log.e("CALL RESULTS", "CALEEEEED");



                    viewX.requestLayout();
                     viewX.getLayoutParams().height = 70;
                    viewX.getLayoutParams().width = 70;
                    if(splash.GENERICUSER == 1){
                        dialog1 ();
                    }else {

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }


                            UserDetails.usernamex = dataModel.getname();
                            UserDetails.username = splash.CURRENTSIMCARD;
                            UserDetails.chatWith = dataModel.getsimcard();
                            UserDetails.joint = joint (splash.CURRENTSIMCARD,dataModel.getsimcard());
                            MainActivity.MSGON = 1;

                        Intent myIntent = new Intent(MainActivity.context, Chat.class);
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.context.startActivity(myIntent);


                        //dialog2 ();

                    }



                }
            });


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //convertView.startAnimation(animation);
        lastPosition = position;

        // Return the completed view to render on screen
        return convertView;
    }



public void dialog1 (){
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.theactivity, R.style.MyAlertDialogStyle);
    builder.setTitle("Welcome");
    builder.setMessage("Please, register first");
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }


            Intent myIntent = new Intent(MainActivity.theactivity, SignupActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.theactivity.startActivity(myIntent);
        }
    });
    builder.setNegativeButton("NO", null);
    builder.show();

}




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
