package com.elanssary.neverforget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mAdd, mShow, mAbout, mLang,mSearch;
    private TextView mT1,mT1_1,mT2,mT2_2,mT3,mT3_3,mT4,mT4_4,mT5,mT5_5,mHead;
    private SwitchMaterial mLangSwitch;
    private boolean  isAr;
    private ApplicationData mData;
    private AdView mAdView;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = new ApplicationData(this);
        mActivity =this;
        initViews();
        if(mData.getLang()){
            //the lang is ar
            mLangSwitch.setChecked(true);
            isAr = true;
            mHead.setText("لا تنسى ابداً");
            mT1.setText("أضف ملاحظة جديدة");
            mT1_1.setText("اضغط وقم بحفظ ملاحظة جديدة");
            mT2.setText("عرض جميع الملاحظات");
            mT2_2.setText("اضغط لترى جميع الملاحظات");
            mT3.setText("عن التطبيق");
            mT3_3.setText("اضغط لتعرف اكثر عنا");
            mT4.setText("ابحث عن ملاحظة معينة");
            mT4_4.setText("اضغط واكتب اسم الملاحظة");
            mT5.setText("لغة عربية / انجليزية");
            mT5_5.setText("التحويل بين العربية والإنجليزية");
        }else{
            //the lang is en
            mLangSwitch.setChecked(false);
            isAr = false;



        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(mActivity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private void initViews() {

        mAdd = findViewById(R.id.add_memory_layout);
        mAdd.setOnClickListener(this);
        mShow = findViewById(R.id.show_memories_layout);
        mShow.setOnClickListener(this);
        mAbout = findViewById(R.id.about_layout);
        mAbout.setOnClickListener(this);
        mSearch = findViewById(R.id.search_layout);
        mSearch.setOnClickListener(this);
        mLang = findViewById(R.id.lang_layout);
        mLang.setOnClickListener(this);
        mLangSwitch = findViewById(R.id.switch_lang);
        mHead = findViewById(R.id.main_txt_app);
        mT1  = findViewById(R.id.t1);
        mT1_1  = findViewById(R.id.t1_1);
        mT2  = findViewById(R.id.t2);
        mT2_2  = findViewById(R.id.t2_2);
        mT3  = findViewById(R.id.t3);
        mT3_3  = findViewById(R.id.t3_3);
        mT4  = findViewById(R.id.t4);
        mT4_4  = findViewById(R.id.t4_4);
        mT5  = findViewById(R.id.t5);
        mT5_5  = findViewById(R.id.t5_5);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_memory_layout:
                startActivity(new Intent(MainActivity.this, AddMem.class));
                break;
            case R.id.show_memories_layout:
                startActivity(new Intent(MainActivity.this, MemoryList.class));
                break;
            case R.id.about_layout:
                startActivity(new Intent(MainActivity.this, Aboutapp.class));
                break;
            case R.id.search_layout:
                startActivity(new Intent(MainActivity.this,Search.class));
                break;
            case R.id.lang_layout:
                if(isAr){
                    //the lang is ar -> to En
                    mLangSwitch.setChecked(false);
                    mData.saveLang(false);
                    isAr = false;
                    Toast.makeText(this, "Restart the application!", Toast.LENGTH_SHORT).show();
                }else{
                    // the lang is en -> to Ar
                    mLangSwitch.setChecked(true);
                    mData.saveLang(true);
                    isAr = true;
                    Toast.makeText(this, "قم بإعادة تشغيل التطبيق مرة اخرى", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
