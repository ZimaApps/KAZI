package tz.co.nyotaapps.naombakazi.naombakazi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;

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


public class CustomAdapter1 extends ArrayAdapter<DataModel1> implements View.OnClickListener{

    private ArrayList<DataModel1> dataSet;
    Context mContext;
    final public String MESEJI = "";
    public String SENDEESIMCARD;
    private ProgressBar spinner;
    private Activity activity; //activity is defined as a global variable in your AsyncTask
    private View mesejiview;
    public String SENDEENAME;
    private InterstitialAd mInterstitialAd;

    // View lookup cache
    private static class ViewHolder {
        String timestamp;
        String name;
        String love;
        String gender;
        String picha;
        String umri;
        String area;
        String maelezo;
        String simcard;
        String reg_date;
        String meseji;
        String state;
        String joint;
        SimpleDraweeView image;
    }

    public CustomAdapter1(ArrayList<DataModel1> data, Context context) {
        super(context, R.layout.msg, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(final View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel1 dataModel1=(DataModel1)object;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        // Get the data item for this position

        //final  DataModel1 dataModel1 = getItem(position);
        Object object= getItem(position);
        final  DataModel1 dataModel1=(DataModel1)object;
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-4482019772887748/9765994096");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.msg, parent, false);
            TextView namev = convertView.findViewById(R.id.namecc);
            TextView mesejiv = convertView.findViewById(R.id.meseji);

            namev.setText(dataModel1.getname());
            mesejiv.setText(dataModel1.getmeseji());

            Uri uri = Uri.parse("http://naombakazi.nyotaapps.co.tz/images/"+dataModel1.getpicha());
            SimpleDraweeView draweeView = convertView.findViewById(R.id.gooovo);
            draweeView.setAspectRatio(1);
            draweeView.setImageURI(uri);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        Log.e("SIMCARD", dataModel1.getsimcard().toString());
        Log.e("SIMCARD", dataModel1.getsimcard().toString());
        Log.e("SIMCARD", dataModel1.getsimcard().toString());
        Log.e("SIMCARD", dataModel1.getsimcard().toString());
        Log.e("SIMCARD", dataModel1.getsimcard().toString());
        Log.e("SIMCARD", dataModel1.getsimcard().toString());

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(context,Chat.class);
                myIntent.putExtra("guestsimcard", dataModel1.getsimcard()); //Optional parameters
                context.startActivity(myIntent);

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });



        return convertView;
    }






}
