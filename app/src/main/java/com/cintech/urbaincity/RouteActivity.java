package com.cintech.urbaincity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    ArrayList<DataCollected> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildtListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                Intent intent = new Intent(RouteActivity.this,ProximityActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListner();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUtil.openFbReference("collected_data",RouteActivity.this);
        RecyclerView rvDatas = (RecyclerView) findViewById(R.id.rvData);
        final DataAdapter adapter = new DataAdapter("potholes");

        rvDatas.setAdapter(adapter);
        LinearLayoutManager dealsLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,false);
        rvDatas.setLayoutManager(dealsLayoutManager);

        FirebaseUtil.attachListner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem insertMenu = menu.findItem(R.id.hybrid_menu);
        if (FirebaseUtil.isAdmin == true){
            insertMenu.setVisible(true);
        }else {
            insertMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.hybrid_menu:
                //startActivity(new Intent(this,DealActivity.class));
                return true;
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Log.d("Logout", "User Logged out");
                                FirebaseUtil.attachListner();
                            }
                        });
                FirebaseUtil.detachListner();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showMenu(){
        invalidateOptionsMenu();
    }
}
