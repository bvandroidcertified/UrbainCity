package com.cintech.urbaincity;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cintech.urbaincity.MapsActivity;
import com.cintech.urbaincity.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static FirebaseAuth.AuthStateListener mAuthListner;
    public static ArrayList<DataCollected> mDatas;
    private static final int RC_SIGN_IN = 123;
    private static MapsActivity caller;
    private static RouteActivity callerR;
    private static ShowDataOnMap callerS;
    private static OrdureActivity callerOrd;
    private static CulteActivity callerCult;
    private static EpicerieActivity callerEpi;
    private static ProximityActivity callerProxi;
    public static boolean isAdmin;

    private FirebaseUtil() {
    }

    public static void openFbReference(String ref, final MapsActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference(String ref, final OrdureActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            callerOrd = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference(String ref, final CulteActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            callerCult = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference(String ref, final ProximityActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            callerProxi = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference(String ref, final EpicerieActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            callerEpi = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference(String ref, final RouteActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            callerR = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void openFbReference(String ref, final ShowDataOnMap callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
            mFirebaseAuth = FirebaseAuth.getInstance();
            callerS = callerActivity;
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null){
                        Log.d("status_user","not yet connected");
                        FirebaseUtil.signIn();

                    }else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    //Toast.makeText(callerActivity, "Welcome back!", Toast.LENGTH_SHORT).show();

                }
            };
            connectStorage();

        }
        mDatas = new ArrayList<DataCollected>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    private static void checkAdmin(String uId){
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators")
                .child(uId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                Log.d("status_user","is admin");
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(listener);
    }

    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logocity)
                        .setTheme(R.style.FullscreenTheme)
                        .build(),
                RC_SIGN_IN);
    }

    public static void attachListner() {
        mFirebaseAuth.addAuthStateListener(mAuthListner);
    }

    public static void detachListner() {
        mFirebaseAuth.removeAuthStateListener(mAuthListner);
    }

    public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("all_pictures");
    }

}