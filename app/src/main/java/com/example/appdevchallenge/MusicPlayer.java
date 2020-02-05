package com.example.appdevchallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MusicPlayer extends Fragment {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel,songname;
    MediaPlayer mp;
    int totalTime;
    ListView listView;
    ArrayList<String> songs = new ArrayList<String>();
    ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_music_player,container,false);
        playBtn = v.findViewById(R.id.playBtn);
        elapsedTimeLabel = v.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = v.findViewById(R.id.remainingTimeLabel);
        listView = v.findViewById(R.id.songsList);
        songname = v.findViewById(R.id.songname);
        final ImageView imageView = v.findViewById(R.id.songimg);

        songs.add("Humraah by Sachet Tandon");
        songs.add("Malang Title Track by Arijit Singh");
        songs.add("Yeh Dooriyan by Mohit Chahaun");
        songs.add("Scary sound by unknown");
        positionBar = v.findViewById(R.id.positionBar);

        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,songs);
        listView.setAdapter(adapter);

        mp = MediaPlayer.create(getContext(), R.raw.humraah);
        mp.setLooping(false);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();
        songname.setText("Humraah by Sachet Tandon");
        imageView.setImageResource(R.drawable.humraah);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mp.pause();
                playBtn.setBackgroundResource(R.drawable.play);
                if(i==0){
                    mp = MediaPlayer.create(getContext(), R.raw.humraah);
                    imageView.setImageResource(R.drawable.humraah);
                }
                else if(i==1){
                    mp = MediaPlayer.create(getContext(), R.raw.malangtitle);
                    imageView.setImageResource(R.drawable.malang);
                }
                else if(i==2){
                    mp = MediaPlayer.create(getContext(), R.raw.yedooriyan);
                    imageView.setImageResource(R.drawable.yedooriyan);
                }
                else if(i==3){
                    mp = MediaPlayer.create(getContext(), R.raw.scary);
                    imageView.setImageResource(R.drawable.image);
                }
                songname.setText(songs.get(i));
                mp.setLooping(false);
                mp.seekTo(0);
                mp.setVolume(0.5f, 0.5f);
                totalTime = mp.getDuration();
                positionBar.setMax(totalTime);
            }
        });



        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        volumeBar = v.findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );



        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying()) {
                    mp.start();
                    playBtn.setBackgroundResource(R.drawable.stop);

                } else {
                    mp.pause();
                    playBtn.setBackgroundResource(R.drawable.play);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();


        return v;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);

            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);
            if(elapsedTime.equals(totalTime)){
                positionBar.setProgress(0);
                elapsedTimeLabel.setText("0:00");
            }

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }


    @Override
    public void onStop() {
        super.onStop();
        mp.pause();
    }




}
