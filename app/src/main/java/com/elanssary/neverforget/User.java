package com.elanssary.neverforget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends AppCompatActivity {
    private TextView mTxt;
    private TextInputEditText mName;
    private Button mNameSave;
    private ApplicationData mData;
    private Pattern pattern ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mData = new ApplicationData(this);
        pattern = Pattern.compile("[a-zA-Z0-9]*");
        mTxt = findViewById(R.id.name_txt);
        mName = findViewById(R.id.name_et);
        mNameSave =findViewById(R.id.name_btn);
        mNameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mName.getText().toString())){
                    Matcher matcher = pattern.matcher(mName.getText().toString());
                    if(!matcher.matches()){
                        //ok
                        mData.saveName(mName.getText().toString());
                        Log.d("TAG", "onClick: "+mName.getText().toString());
                        Log.d("TTTT", "name: "+ mData.getName());
                        mData.saveNameState(true);
                        goToMain();
                    }else{
                        //no
                        if(mData.getLang()){
                            Toast.makeText(User.this, "يجب ان يحتوى الاسم على احرف مثل @ أو %", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(User.this, "Name must contains chars like @ or # or % or &", Toast.LENGTH_SHORT).show();

                        }
                    }
                }else{
                    if(mData.getLang()){
                        Toast.makeText(User.this, "قم بكتابة اسمك!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(User.this, "Type your name!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void goToMain(){
        Intent toHome = new Intent(User.this, MainActivity.class);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toHome);
        finish();
    }
}
