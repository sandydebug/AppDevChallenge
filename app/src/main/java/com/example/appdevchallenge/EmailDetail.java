package com.example.appdevchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EmailDetail extends AppCompatActivity {

    private TextView message,desc,from;
    String msg,frm,des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);

        message = findViewById(R.id.message1);
        desc = findViewById(R.id.email);
        from = findViewById(R.id.name);
        if(getIntent()!=null){
            msg = getIntent().getStringExtra("Message");
            frm = getIntent().getStringExtra("From");
            des = getIntent().getStringExtra("Date");
        }

        message.setText(msg);
        from.setText(frm);
        desc.setText(des);

    }
}
