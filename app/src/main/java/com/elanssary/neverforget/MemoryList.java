package com.elanssary.neverforget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemoryList extends AppCompatActivity {
    private RecyclerView mRecMem;
    private AdapterMem adapterMem;
    private DatabaseReference mReferenceAll;
    private MediaPlayer mediaPlayer;
    private ImageView mBack;
    private TextView mTxt;
    private ApplicationData mData;
    private AdView mAdView;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);
        mData = new ApplicationData(this);
        mActivity =this;
        mReferenceAll = FirebaseDatabase.getInstance().getReference().child(mData.getName());
        mReferenceAll.keepSynced(true);
        mediaPlayer = new MediaPlayer();
        mRecMem = findViewById(R.id.mem_recycler);
        mBack = findViewById(R.id.back_memories);
        mTxt =findViewById(R.id.mem_txt_list);
        if(mData.getLang()){
            mTxt.setText("الملاحظات");
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(mActivity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView_list);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecMem.setLayoutManager(linearLayoutManager);

        final FirebaseRecyclerOptions<Memory> options =
                new FirebaseRecyclerOptions.Builder<Memory>()
                        .setQuery(mReferenceAll,Memory.class)
                        .build();

        adapterMem = new AdapterMem(options,this);

        mRecMem.setAdapter(adapterMem);

    }



    //This function to stop get data.
    @Override
    public void onStop() {
        super.onStop();
        adapterMem.stopListening();
    }

    //ENd of onStop
    // This function to start get data.
    @Override
    public void onStart() {
        super.onStart();
        adapterMem.startListening();

    }
}
