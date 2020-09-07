package com.elanssary.neverforget;

public class Memory {
    String mKey,mTitle,mPhoto,mRecord;
    Memory(){}

    public Memory(String mKey, String mTitle, String mPhoto, String mRecord) {
        this.mKey = mKey;
        this.mTitle = mTitle;
        this.mPhoto = mPhoto;
        this.mRecord = mRecord;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmRecord() {
        return mRecord;
    }

    public void setmRecord(String mRecord) {
        this.mRecord = mRecord;
    }
}
