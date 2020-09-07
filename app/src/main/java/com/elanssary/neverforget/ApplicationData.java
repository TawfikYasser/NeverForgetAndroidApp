package com.elanssary.neverforget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationData {
    private static final String FILE_NAME = "NEVERFORGETAppFile";
    private static final String LANG = "lang";
    private static final String NAME = "name";
    private static final String NAME_STATE = "name_state";

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;

    @SuppressLint("CommitPrefEdits")
    public ApplicationData(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    public void saveLang(boolean isAr){
        mEditor.putBoolean(LANG,isAr);
        mEditor.apply();
    }
    public boolean getLang(){
        return mSharedPreferences.getBoolean(LANG,false);
    }


    public void saveName(String name){
        mEditor.putString(NAME,name);
        mEditor.apply();
    }
    public String getName(){
        return mSharedPreferences.getString(NAME,"");
    }
    public void saveNameState(boolean nameState){
        mEditor.putBoolean(NAME_STATE,nameState);
        mEditor.apply();
    }
    public boolean getNameState(){
        return mSharedPreferences.getBoolean(NAME_STATE,false);
    }
}
