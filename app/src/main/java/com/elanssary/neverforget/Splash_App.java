package com.elanssary.neverforget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Splash_App extends AppCompatActivity {
    private CircleImageView mSplImg;
    private TextView mTxt1,mTxt2;
    private Animation mTopAnim,mBottomAnim;
    private boolean connected = false;
    private ApplicationData mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__app);
        mSplImg = findViewById(R.id.splash_img);
        mData = new ApplicationData(this);


        mTxt1 =findViewById(R.id.splash_txt1);
        mTxt2=findViewById(R.id.splash_txt2);
        if(mData.getLang()){
            mTxt1.setText("لا تنسى ابداً");
            mTxt2.setText("احفظ جميع الأشياء المهمة في مكان واحد.");
        }
        mTopAnim = AnimationUtils.loadAnimation(Splash_App.this,R.anim.splash_top_anim);
        mBottomAnim = AnimationUtils.loadAnimation(Splash_App.this,R.anim.splash_bottom_anim);
        mSplImg.setAnimation(mTopAnim);
        mTxt1.setAnimation(mBottomAnim);
        mTxt2.setAnimation(mBottomAnim);


        if(isConnected()){

            if(mData.getNameState()){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent toHome = new Intent(Splash_App.this, MainActivity.class);
                        //Log.d("TAG", "signed");
                        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(toHome);

                        finish();

                    }
                }, 3000);
            }else{
                Intent toName = new Intent(Splash_App.this, User.class);
                startActivity(toName);
                finish();
            }


        }else{
            Intent gotonowifi = new Intent(Splash_App.this, InternetConnection.class);
            startActivity(gotonowifi);
            finish();
        }


    }

    boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }
}
