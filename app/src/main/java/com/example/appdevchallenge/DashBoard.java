package com.example.appdevchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appdevchallenge.Model.ProfileData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BottomSheetInfo.BottomSheetListener,BottomSheetMail.BottomSheetListener {


private FirebaseAuth firebaseAuth;
private FirebaseDatabase firebaseDatabase;
private FirebaseStorage firebaseStorage;
private StorageReference storageReference;
private DrawerLayout mDrawer;
private Toolbar mToolbar;
NavigationView navigationView;
private CircularImageView circularImageView;
private TextView useName,useEmail;
    GoogleSignInClient mGoogleSignInClient;

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
        setContentView(R.layout.activity_dash_board);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        storageReference=firebaseStorage.getReference();


        mDrawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.drawerView);
        mToolbar = findViewById(R.id.toolbar);
        View header = navigationView.getHeaderView(0);
        useName=header.findViewById(R.id.nameTxt);
        useEmail =header.findViewById(R.id.emailTxt);
        circularImageView = header.findViewById(R.id.profileImageView);
        navigationView.setNavigationItemSelectedListener(this);
        mToolbar.setTitle("Advaita");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Weather()).commit();
        }

        setupDrawer();
        setProfile();


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.profile:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"subhamhazra25@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Issue or Compliment");
                i.putExtra(Intent.EXTRA_TEXT   , "Hey Sandy, ");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DashBoard.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.about:
                BottomSheetInfo bottomSheet = new BottomSheetInfo();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                break;

            case R.id.logout:
                if(firebaseAuth.getCurrentUser()!= null){
                    new AlertDialog.Builder(this)
                            .setTitle("LOGOUT")
                            .setMessage("Are you sure you want to logout ?")

                            .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth.signOut();
                                    mGoogleSignInClient.signOut();
                                    finish();
                                    startActivity(new Intent(DashBoard.this,Login.class));
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), null)
                            .setIcon(android.R.drawable.ic_lock_power_off)
                            .show();}
                else{
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(DashBoard.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DashBoard.this, Login.class));
                                    finish();
                                }
                            });

                }




                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setupDrawer() {



        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    public void setProfile(){
        DatabaseReference databaseReference=firebaseDatabase.getReference();
        databaseReference.child("PROFILES").child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileData userProfile=dataSnapshot.getValue(ProfileData.class);
                useName.setText(userProfile.getName());
                useEmail.setText(userProfile.getEmail());

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DashBoard.this);

                if(acct!=null){
                    Uri personPhoto = acct.getPhotoUrl();
                    Glide.with(DashBoard.this).load(personPhoto).into(circularImageView);}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //  Toast.makeText(start.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
        circularImageView.setBorderWidth(10f);
        circularImageView.setBorderColor(Color.BLACK);
        circularImageView.setShadowEnable(true);
        circularImageView.setShadowRadius(20f);
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getCurrentUser().getUid()).child("ProfileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().into(circularImageView);
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.weather:
                            selectedFragment = new Weather();
                            break;
                        case R.id.email:
                            selectedFragment = new Email();
                            break;
                        case R.id.music:
                            selectedFragment = new MusicPlayer();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }

            };

    @Override
    public void onButtonClicked(String text) {
        //Toast.makeText(this,"Sandy",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onButtonClicked1(String from,String msg) {
        //Toast.makeText(getContext(),from+"  "+msg,Toast.LENGTH_SHORT).show();
    }
}
