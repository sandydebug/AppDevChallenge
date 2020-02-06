package com.example.appdevchallenge;

import android.app.ProgressDialog;
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
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MusicPlayer extends Fragment {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel,songname;
    static MediaPlayer mp;
    int totalTime = 0;
    ListView listView;
    ArrayList<String> songs = new ArrayList<String>();
    ArrayAdapter adapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_music_player,container,false);
        playBtn = v.findViewById(R.id.playBtn);
        elapsedTimeLabel = v.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = v.findViewById(R.id.remainingTimeLabel);
        listView = v.findViewById(R.id.songsList);
        songname = v.findViewById(R.id.songname);
        progressDialog = new ProgressDialog(getContext()) {
            @Override
            public void onBackPressed() {
                progressDialog.dismiss();
            }};
        final ImageView imageView = v.findViewById(R.id.songimg);

        songs.add("Humraah by Sachet Tandon");
        songs.add("Malang Title Track by Arijit Singh");
        songs.add("Yeh Dooriyan by Mohit Chahaun");
        songs.add("Scary sound by unknown");
        songs.add("Faded by Alan Walker");
        songs.add("Alone by Alan Walker");
        songs.add("Let me down slowly by alec benjamin");
        songs.add("Why do you love by Chainsmokers");
        positionBar = v.findViewById(R.id.positionBar);

        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,songs);
        listView.setAdapter(adapter);
        mp = new MediaPlayer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressDialog.setMessage("Hang on while we load your song");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mp.pause();
                playBtn.setBackgroundResource(R.drawable.stop);
                if(i==0){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/humraah.mp3?alt=media&token=eb8d28eb-24ac-45c0-8dac-e34d45b61987");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageResource(R.drawable.humraah);
                }
                else if(i==1){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/malangtitle.mp3?alt=media&token=34e949db-5258-4b93-9e28-e03dfc238a7b");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //mp = MediaPlayer.create(getContext(), R.raw.malangtitle);
                    imageView.setImageResource(R.drawable.malang);
                }
                else if(i==2){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/yedooriyan.mp3?alt=media&token=21599d05-c807-4e40-b498-c0e7a3366263");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageResource(R.drawable.yedooriyan);
                }
                else if(i==3){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/scary.mp3?alt=media&token=1b8b6477-a262-4fcc-a7fe-a821bdb4a62c");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageResource(R.drawable.image);
                }
                else if(i==4){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/Faded%20-%20Alan%20Walker.mp3?alt=media&token=4c2d562e-c5c8-44cf-8d74-6772596f9042");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Picasso.get().load("https://img.favpng.com/25/6/3/faded-song-music-download-remix-png-favpng-0Vqb7t57aSeD2UQU6RFfAic7W.jpg").into(imageView);
                }
                else if(i==5){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/Alan%20Walker%20-%20Alone.mp3?alt=media&token=fc23dfab-71e6-4d12-a0ac-ed2221d705a7");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //imageView.setImageResource(R.drawable.image);
                    Picasso.get().load("https://vignette.wikia.nocookie.net/alan-walker/images/1/1d/9fa13acf7fd692b9310adda64cd0be3f.jpg/revision/latest/scale-to-width-down/340?cb=20191029171926").into(imageView);

                }
                else if(i==6){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/Alec%20Benjamin%20-%20Let%20Me%20Down%20Slowly.mp3?alt=media&token=99fbe369-b5ff-4362-987a-f7cbc4f5354c");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Picasso.get().load("https://i.ytimg.com/vi/jLNrvmXboj8/maxresdefault.jpg").into(imageView);
                }
                else if(i==7){
                    try {
                        mp.reset();
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/app-dev-44ceb.appspot.com/o/The%20Chainsmokers%20%26%205%20Seconds%20of%20Summer%20-%20Who%20Do%20You%20Love.mp3?alt=media&token=f34b6254-2369-4c49-8ad8-df45ce05cbe2");
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                                progressDialog.dismiss();
                                totalTime = mp.getDuration();
                                positionBar.setMax(totalTime);
                            }
                        });
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Picasso.get().load("https://upload.wikimedia.org/wikipedia/en/6/69/The_Chainsmokers_-_Who_Do_You_Love.png").into(imageView);
                     }

                songname.setText(songs.get(i));
                songname.setSelected(true);
                mp.setLooping(false);
                mp.seekTo(0);
                mp.setVolume(0.5f, 0.5f);
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
        if(mp!=null){
            if(mp.isPlaying()){
           mp.pause();}}
    }

}
