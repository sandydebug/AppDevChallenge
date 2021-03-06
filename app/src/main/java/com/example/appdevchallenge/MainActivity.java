package com.example.appdevchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        ShimmerTextView textview  = findViewById(R.id.textView5);
        Shimmer shimmer = new Shimmer();
        shimmer.start(textview);
        if(firebaseAuth.getCurrentUser()==null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, Login.class);

                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                }
            }, SPLASH_TIME_OUT);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, DashBoard.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                }
            }, SPLASH_TIME_OUT);
        }

    }
}
