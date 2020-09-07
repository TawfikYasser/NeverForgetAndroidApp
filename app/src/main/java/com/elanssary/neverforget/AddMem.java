package com.elanssary.neverforget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddMem extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mTitle;
    private Button mImageBtn, mSaveBtn;
    private ImageView mRecordBtn, mBack;
    private TextView mRecStatus,mHead;
    private Uri mainImageUri = null;
    private Uri voice = null;
    private String recordFile;

    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private MediaRecorder recorder;
    private String fileName = null;
    private ProgressDialog mProgressDialog;
    private boolean isRec = false;
    private String recordPer = Manifest.permission.RECORD_AUDIO;
    private int PERCODE = 21;
    private Chronometer mTimer;
    String filename;
    private int num = 1;
    private ApplicationData mData;
    private String nameU;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mem);
        mData = new ApplicationData(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mData.getName());
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        mImageBtn = findViewById(R.id.choose_img_mem);
        mImageBtn.setOnClickListener(this);
        mTitle = findViewById(R.id.title_mem);
        mHead = findViewById(R.id.mem_txt);
        mSaveBtn = findViewById(R.id.save_btn);
        mTimer = findViewById(R.id.record_timer);
        mBack = findViewById(R.id.back_addmem);
        mRecordBtn = findViewById(R.id.record_mem);
        mRecStatus = findViewById(R.id.rec_status_txt);
        if(mData.getLang()){
            mTitle.setHint("العنوان");
            mHead.setText("إضافة ملاحظة جديدة");
            mImageBtn.setText("اختيار صورة الملاحظة");
            mRecStatus.setText("حالة المُسجل");
            mSaveBtn.setText("حفظ");

        }else{
            mTitle.setHint("Title");
        }
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToCloud();
            }
        });


        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRec) {
                    stopRecording();
                    mRecordBtn.setImageDrawable(getResources().getDrawable(R.drawable.rec_base, null));
                    mRecStatus.setText("Recording Stopped...");
                    isRec = false;
                } else {
                    if(checkPermissions()){
                        startRecording();
                        mRecStatus.setText("Recording Started...");
                        mRecordBtn.setImageDrawable(getResources().getDrawable(R.drawable.rec_pause, null));
                        isRec = true;
                    }


                }
            }

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_img_mem:
                //Choose the image
                choosePhoto();
                break;
        }
    }

    private void uploadVoice() {
        final Uri uri1 = Uri.fromFile(new File(filename));
        final StorageReference audioFilePath = mStorageReference.child("MemAudio").child(uri1.getLastPathSegment());
        audioFilePath.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                audioFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mRecStatus.setText("Recording Status");
                        voice = uri;
                        if(mData.getLang()){
                            Toast.makeText(AddMem.this, "تم حفظ الصوت!", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(AddMem.this, "Voice Saved To Cloud!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    private void saveDataToCloud() {

        final String title = mTitle.getText().toString();
        if (!TextUtils.isEmpty(title) && mainImageUri != null && voice != null) {
            // Upload title and image and voice to firebase
            if (mData.getLang()){
                mProgressDialog.setMessage("جارى رفع الملاحظة لقاعدة البيانات، انتظر ثواني...");
                mProgressDialog.show();

            }else{
                mProgressDialog.setMessage("Uploading Memory To Cloud, Please wait a moments...");
                mProgressDialog.show();

            }
            final StorageReference filepath = mStorageReference.child("MemImages").child(mainImageUri.getLastPathSegment());
            filepath.putFile(mainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String memKey = mDatabaseReference.push().getKey();

                            DatabaseReference databaseReference = mDatabaseReference.child(memKey);
                            Log.d("VVV", "onSuccess: " + voice.toString());
                            Memory memory = new Memory(memKey, title, uri.toString(), voice.toString());
                            databaseReference.setValue(memory);
                            mProgressDialog.dismiss();
                        }
                    });
                }
            });

        } else {
            if (mData.getLang()){
                Toast.makeText(this, "يجب كتابة العنوان واختيار صورة وتسجيل صوت قبل الحفظ!", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Type a title and choose an image and record a voice!", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void choosePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check the permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(AddMem.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                //Toast.makeText(this, "Permission ok!", Toast.LENGTH_SHORT).show();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(AddMem.this);
            }
        }
    }


    // This function to choose and crop the image.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //image choosed ok and cropped then get it as uri
                mainImageUri = result.getUri();
                Toast.makeText(this, "Image Loaded Successfully!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    //END of onActivityResult


    private void startRecording() {
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.start();

        String recordPath = AddMem.this.getExternalFilesDir("/").getPath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        recordFile = "Recording " + formatter.format(now) + ".3gp";
        filename = recordPath + "/" + recordFile;
        Log.d("TAGG", "startRecording: "+filename);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.stop();
        recorder.stop();
        recorder.release();
        recorder = null;
        if(mData.getLang()){
            Toast.makeText(this, "جارى تحميل الصوت، انتظر ثواني!، بعدها يمكنك الضغط على حفظ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Loading the voice!, after that you can save!", Toast.LENGTH_SHORT).show();
        }
        uploadVoice();
    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(AddMem.this, recordPer) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(AddMem.this, new String[]{recordPer}, PERCODE);
            return false;
        }
    }
}
