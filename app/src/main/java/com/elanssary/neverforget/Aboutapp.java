package com.elanssary.neverforget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Aboutapp extends AppCompatActivity {
    private ImageView mBack;
    private TextView mTxt,mAb1,mAb2,mAb3,mA1,mA2,mA3;
    private ApplicationData mData;
    private AdView mAdView;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);
        mData = new ApplicationData(this);
        mActivity =this;

        mBack = findViewById(R.id.back_about);
        mTxt = findViewById(R.id.mem_txt_about);
        mAb1 = findViewById(R.id.txt_about);
        mAb2 = findViewById(R.id.text_about2);
        mAb3 = findViewById(R.id.text_about3);
        mA1 = findViewById(R.id.txt_about1_1);
        mA2 = findViewById(R.id.text_about2_2);
        mA3 = findViewById(R.id.text_about3_3);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(mActivity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView_about);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (mData.getLang()){
            mTxt.setText("عن التطبيق");
            mAb1.setVisibility(View.GONE);
            mAb2.setVisibility(View.GONE);
            mAb3.setVisibility(View.GONE);
        }else{
            mA1.setVisibility(View.GONE);
            mA2.setVisibility(View.GONE);
            mA3.setVisibility(View.GONE);
        }
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
