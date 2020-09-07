package com.elanssary.neverforget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InternetConnection extends AppCompatActivity {
    Button mRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection);
        mRetry=findViewById(R.id.retry_btn);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent gotosplash = new Intent(InternetConnection.this,Splash_App.class);
                startActivity(gotosplash);
            }
        });
    }
}
