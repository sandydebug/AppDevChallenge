package com.example.appdevchallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailDetail extends AppCompatActivity {

    private TextView message,desc,from;
    String msg,frm,des,pos;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        message = findViewById(R.id.message1);
        desc = findViewById(R.id.email);
        from = findViewById(R.id.name);
        if(getIntent()!=null){
            pos = String.valueOf(getIntent().getIntExtra("Position",10000));
            msg = getIntent().getStringExtra("Message");
            frm = getIntent().getStringExtra("From");
            des = getIntent().getStringExtra("Date");
        }
        message.setText(msg);
        from.setText(frm);
        desc.setText(des);


    }
}
