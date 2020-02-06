package com.example.appdevchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdevchallenge.Adapters.PostAdapter;
import com.example.appdevchallenge.Model.EmailModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Email extends Fragment implements BottomSheetMail.BottomSheetListener {

    ArrayList<EmailModel> emails = new ArrayList<EmailModel>();
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference mails;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_email,container,false);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        editText = v.findViewById(R.id.searchSender);
        firebaseDatabase=FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(getContext()) {
            @Override
            public void onBackPressed() {
                progressDialog.dismiss();
            }};
        progressDialog.setMessage("Hang on while we load your emails ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        mails = FirebaseDatabase.getInstance().getReference("Mails");
        mails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(int i =0;i<postSnapshot.getChildrenCount();i++) {

                            emails.add(new EmailModel(postSnapshot.child(String.valueOf(i)).child("desc").getValue().toString(),postSnapshot.child(String.valueOf(i)).child("from").getValue().toString(),postSnapshot.child(String.valueOf(i)).child("date").getValue().toString(),postSnapshot.child(String.valueOf(i)).child("msg").getValue().toString()));}


                }
                if(emails.isEmpty()){
                    emails.add(new EmailModel("...","NO EMAILS YET ","NULL","..."));
                }
                buildRecyclerView();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*emails.add(new EmailModel("Hello Boss","Ajitesh Panda","05-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","hello World","05-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","Ritesh Agarwal","05-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","Ajitesh Panda","05-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","Subham Hazra","05-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","Saidatta Sahu","04-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","Sidhartha Mallick","04-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        emails.add(new EmailModel("Hello Boss","Bapun","04-02-2020","asjdasdjakjdn sadkkjas asjdkjasdkj ajasdnjjn"));
        buildRecyclerView();*/
        FloatingActionButton myFab = v.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BottomSheetMail bottomSheet = new BottomSheetMail();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "exampleBottomSheet1");
                //doMyThing();
            }
        });
        return v;
    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PostAdapter(emails,getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void filter(String text) {
        ArrayList<EmailModel> filteredList = new ArrayList<>();

        for (EmailModel item : emails) {
            if (item.getFrom().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    @Override
    public void onButtonClicked1(String from,String msg) {
        emails.add(new EmailModel("Check Mail",from,"05-02-2020",msg));
        Toast.makeText(getContext(),from+"  "+msg,Toast.LENGTH_SHORT).show();
        buildRecyclerView();
    }

}
