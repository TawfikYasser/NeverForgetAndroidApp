package com.elanssary.neverforget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;

public class AdapterMem extends FirebaseRecyclerAdapter<Memory, AdapterMem.MyViewHolder> {
    private boolean isPlaying = false;
    private Context mContext;
    private MediaPlayer mediaPlayer = null;
    DatabaseReference databaseReference ;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterMem(@NonNull FirebaseRecyclerOptions<Memory> options,Context mContext) {
        super(options);
        this.mContext =mContext;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Memories");

    }


    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final Memory model) {
        holder.mTitle.setText(model.getmTitle());
        final String key = getRef(position).getKey();
        final String uri = model.getmPhoto();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.mImage);

        holder.mVoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String voiceUrl = model.getmRecord();
               final MediaPlayer player = new MediaPlayer();

                if(isPlaying)
                {

                    isPlaying= false;
                    player.stop();
                    holder.mRec.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rec_play,null));
                    holder.animationView.cancelAnimation();


                    //stopAudio(holder);
                    //playAudio(voiceUrl,holder);
                }else
                {
                    //playAudio(voiceUrl,holder);
                    try {

                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(voiceUrl);
                        player.prepare();
                        player.start();
                        holder.animationView.playAnimation();
                        holder.mRec.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rec_pause,null));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    isPlaying = true;
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isPlaying= false;
                            player.stop();
                            holder.mRec.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rec_play,null));
                            holder.animationView.cancelAnimation();

                        }
                    });

                }
/*
                try {
                    MediaPlayer player = new MediaPlayer();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(voiceUrl);
                    player.prepare();
                    player.start();
                    holder.mRec.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rec_pause));
                } catch (Exception e) {
                    // TODO: handle exception
                }

*/
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mem, parent, false);
        MyViewHolder MVH = new MyViewHolder(view);
        return MVH;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        ImageView mImage, mRec;
        RelativeLayout mVoiceLayout;
        LottieAnimationView animationView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.mem_title);
            mImage = itemView.findViewById(R.id.mem_image);
            mRec = itemView.findViewById(R.id.mem_voice);
            mVoiceLayout = itemView.findViewById(R.id.voice_item);
            animationView = itemView.findViewById(R.id.voice_anim);
        }
    }

/*
    private void stopAudio(MyViewHolder viewHolder) {
        //stop audio
        isPlaying= false;
        mediaPlayer.stop();
        viewHolder.mRec.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rec_play));
        viewHolder.animationView.cancelAnimation();
    }

    private void playAudio(String fileToPlay, final MyViewHolder viewHolder) {
        //play audio
        mediaPlayer = new MediaPlayer();
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(fileToPlay);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
        viewHolder.mRec.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rec_pause));
        viewHolder.animationView.playAnimation();
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio(viewHolder);
            }
        });

    }

 */
}
