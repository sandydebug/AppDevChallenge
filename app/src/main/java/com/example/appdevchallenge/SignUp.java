package com.example.appdevchallenge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdevchallenge.Model.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUp extends AppCompatActivity {

    EditText editText1, editText2, editText3,editText4,editText5;
    String name,email,password,location,phone;
    TextView textView;
    Button button;
    int a=1;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static int PICK_IMAGE=10000;
    Uri uri;
    View decorView;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());

        }
    }

    private int hideSystemBars(){

        return  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode== RESULT_OK && data.getData()!=null){
            uri=data.getData();
            //Toast.makeText(this, String.valueOf(uri),Toast.LENGTH_LONG).show();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
                //Glide.with(this).load(bitmap).into(imageView);
                imageView.setTag("Set");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        intialize();

        firebaseAuth=FirebaseAuth.getInstance();
        decorView = getWindow().getDecorView();
        databaseReference = FirebaseDatabase.getInstance().getReference("PROFILES");

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String username=editText2.getText().toString().trim();
                    String password =editText3.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendVerification();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this,"Registration Failed!!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });
    }

    private void intialize() {
        editText1 = (EditText) findViewById(R.id.etUserName);
        editText2 = (EditText) findViewById(R.id.etUserEmail);
        editText3 = (EditText) findViewById(R.id.etPassword);
        editText5 = findViewById(R.id.phone);
        textView = (TextView) findViewById(R.id.tvUserLogin);
        button = (Button) findViewById(R.id.btnRegister);
        imageView=(ImageView)findViewById(R.id.imageView2);
        progressDialog=new ProgressDialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);


    }

    private boolean validate() {
        Boolean res=false;
        name = editText1.getText().toString();
        email = editText2.getText().toString();
        password = editText3.getText().toString();
        location = editText4.getText().toString();
        phone = editText5.getText().toString();

        progressDialog.setMessage("Hang on while we register your profile ");
        progressDialog.show();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            Toast.makeText(SignUp.this, "Enter all the details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(phone.length()!=10){
            editText5.setError("Invalid Phone Number");
            editText5.requestFocus();
            progressDialog.dismiss();
        }
        else if(!("Set".equals(imageView.getTag()))){
            Toast.makeText(SignUp.this, "Add a image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(password.length()<5 && a!=0){
            Toast.makeText(SignUp.this, "Password too weak.....||REGISTER AGAIN||", Toast.LENGTH_LONG).show();
            a=0;
            progressDialog.dismiss();

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText2.setError("Invalid EmailID");
            progressDialog.dismiss();
        }
        else{
            res=true;}

        return res;

    }

    private void sendVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        sendData();
                        Toast.makeText(SignUp.this,"Verification Mail Sent !",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(SignUp.this,Login.class));

                    }
                    else {
                        Toast.makeText(SignUp.this,"Mail not sent !",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendData(){
        databaseReference.child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new ProfileData(email,name,phone));
        //Toast.makeText(SignUp.this,"Database Updated!!",Toast.LENGTH_SHORT).show();
        StorageReference imageref=storageReference.child(firebaseAuth.getUid()).child("ProfileImage");
        UploadTask uploadTask=imageref.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this,"Upload Failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SignUp.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

