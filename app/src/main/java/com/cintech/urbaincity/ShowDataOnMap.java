package com.cintech.urbaincity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class ShowDataOnMap extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = DataActivity.class.getName();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;
    DataCollected data;

    MaterialButton routeBtn;
    MaterialButton ordureBtn;
    MaterialButton epicerieBtn;
    MaterialButton lieuCulteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_on_map);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase ;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpToolbar();

        Intent intent = getIntent();
        DataCollected deal = (DataCollected) intent.getSerializableExtra("Data");
        if (deal == null){
            deal = new DataCollected();
        }
        this.data = deal;

        //Get references to items
        routeBtn = (MaterialButton) findViewById(R.id.route_btn);
        ordureBtn = (MaterialButton) findViewById(R.id.ordure_btn);
        epicerieBtn = (MaterialButton)findViewById(R.id.epicerie_btn);
        lieuCulteBtn = (MaterialButton)findViewById(R.id.lieu_cult_btn);

        routeBtn.setOnClickListener(this);
        ordureBtn.setOnClickListener(this);
        epicerieBtn.setOnClickListener(this);
        lieuCulteBtn.setOnClickListener(this);

    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                this,
                findViewById(R.id.map),
                new AccelerateDecelerateInterpolator(),
                this.getResources().getDrawable(R.drawable.ubrcity_branded_menu), // Menu open icon
                this.getResources().getDrawable(R.drawable.ubrcity_close_menu)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        /*LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(home)
                .title("Marker sur "+data.getDescription())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        float zoom=15;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));*/

        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("collected_data",this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showData(dataSnapshot,mMap);
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
        });
        /*FirebaseUtil.openFbReference("traveldeals",this);
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rvDeals);
        final DealAdapter adapter = new DealAdapter();
        rvDeals.setAdapter(adapter);
        LinearLayoutManager dealsLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,false);
        rvDeals.setLayoutManager(dealsLayoutManager);*/

        FirebaseUtil.attachListner();
    }

    private void showData(DataSnapshot dataSnapshot, GoogleMap map) {

        do{

            DataCollected data = dataSnapshot.getValue(DataCollected.class);
            String latitude = this.data.getLatitude().toString();
            String longitude = this.data.getLongitude().toString();
            String lat = data.getLatitude().toString();
            String lon = data.getLongitude().toString();
            if (latitude.equals(lat) && longitude.equals(lon) ){
                LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));

                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.10f, Long: %2$.10f",
                        Double.parseDouble(data.getLatitude()),
                        Double.parseDouble(data.getLongitude()));

                mMap.addMarker(new MarkerOptions().position(home)
                        .title("Marker sur "+data.getDescription()+" - "+data.getQuartier())
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
                float zoom=15;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));


            }else{

                if (data.getType().equals("potholes")){

                    String snippet = String.format(Locale.getDefault(),
                            "Lat: %1$.10f, Long: %2$.10f",
                            Double.parseDouble(data.getLatitude()),
                            Double.parseDouble(data.getLongitude()));
                    LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(home).snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));

                }else if (data.getType().equals("Rubbish")){

                    String snippet = String.format(Locale.getDefault(),
                            "Lat: %1$.10f, Long: %2$.10f",
                            Double.parseDouble(data.getLatitude()),
                            Double.parseDouble(data.getLongitude()));
                    LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(home).snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));

                }else if (data.getType().equals("Grocery")){

                    String snippet = String.format(Locale.getDefault(),
                            "Lat: %1$.10f, Long: %2$.10f",
                            Double.parseDouble(data.getLatitude()),
                            Double.parseDouble(data.getLongitude()));
                    LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(home).snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));

                }else if (data.getType().equals("Cult")){

                    String snippet = String.format(Locale.getDefault(),
                            "Lat: %1$.10f, Long: %2$.10f",
                            Double.parseDouble(data.getLatitude()),
                            Double.parseDouble(data.getLongitude()));
                    LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(home).snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                            .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));
                }

                /*mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
                float zoom=15;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));*/
            }



            /*GroundOverlayOptions homeOverlay = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.logocity))
                    .position(home, 100);
            mMap.addGroundOverlay(homeOverlay);*/

            /*LatLng home = new LatLng(0.369854,9.480963);
            mMap.addMarker(new MarkerOptions().position(home).title("Marker sur IAI"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

            float zoom=15;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));*/

           /* DataCollected dataInfo = new DataCollected();
            String arrondissement = ds.getValue(DataCollected.class).getArrondissement();
            String quartier = ds.getValue(DataCollected.class).getQuartier();
            String description = ds.getValue(DataCollected.class).getDescription();*/
            /*dataInfo.setDescription(ds.getValue(DataCollected.class).getDescription());
            dataInfo.setLatitude(ds.getValue(DataCollected.class).getLatitude());
            dataInfo.setLongitude(ds.getValue(DataCollected.class).getLongitude());*/
            Log.d(TAG,"Latitude: "+data.getLatitude());
        }while (!dataSnapshot.hasChildren());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem hybridMenu = menu.findItem(R.id.hybrid_menu);
        MenuItem terrainMenu = menu.findItem(R.id.terrain_menu);
        if (FirebaseUtil.isAdmin == true){
            hybridMenu.setVisible(true);
            terrainMenu.setVisible(true);
        }else {
            hybridMenu.setVisible(false);
            terrainMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.normal_menu:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_menu:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_menu:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_menu:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.route_btn:
                Intent intent = new Intent(this,RouteActivity.class);
                startActivity(intent);
                return;
            case R.id.ordure_btn:
                Intent intentOrdure = new Intent(this,OrdureActivity.class);
                startActivity(intentOrdure);
                return;
            case R.id.epicerie_btn:
                Intent intentGrocery = new Intent(this,EpicerieActivity.class);
                startActivity(intentGrocery);
                return;
            case R.id.lieu_cult_btn:
                Intent intentCult = new Intent(this,CulteActivity.class);
                startActivity(intentCult);
                return;
            default:
                return;
        }
    }
}
