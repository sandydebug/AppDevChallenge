package com.example.appdevchallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appdevchallenge.Model.EmailModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class BottomSheetMail extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private EditText editText1,editText2;
    String id,mes;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference mail;
    long count=0;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheetmail, container, false);
        Button button1 = v.findViewById(R.id.button1);
        editText1 = v.findViewById(R.id.mailid);
        editText2 = v.findViewById(R.id.msg);
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        final String date = simpleDateFormat.format(calendar.getTime());
        mail = FirebaseDatabase.getInstance().getReference("Mails");
        mail.child("EMAILS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editText1.getText().toString();
                mes = editText2.getText().toString();
                if ( id.equals("") || mes.equals("")) {
                    Toast.makeText(getActivity(), "Enter all the details", Toast.LENGTH_SHORT).show();
                    if(id.equals("")){
                    editText1.setError("Enter email");}
                    else{
                    editText2.setError("Type your message");}
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(id).matches()){
                    editText1.setError("Invalid EmailID");
                    editText1.requestFocus();
                }
                else {
                    mListener.onButtonClicked1(editText1.getText().toString(), editText2.getText().toString());
                    mail.child("EMAILS").child(String.valueOf(count)).setValue(new EmailModel(mes.substring(0,5)+" ...Continued",id,date,mes));
                    Toast.makeText(getContext(),"Mail added successfully, Press Email tab to refresh ",Toast.LENGTH_LONG).show();
                    dismiss();
                }

            }
        });
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked1(String from,String msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
