package com.cintech.urbaincity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //_________________________________
    ArrayList<DataCollected> datas;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildtListener;

    private static final String FIREBASE_URL = "https://fliendlycht.firebaseio.com/";
    private static final String FIREBASE_ROOT_NODE = "fliendlycht";
    //_________________________________
    private GoogleMap mMap;
    BottomNavigationView bottomNavigationView;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = MapsActivity.class.getName();
    private boolean setClick = false;

    MaterialButton routeBtn;
    MaterialButton ordureBtn;
    MaterialButton epicerieBtn;
    MaterialButton lieuCulteBtn;
    private Bitmap mBmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottonNavigation_id);

        //FirebaseUtil.openFbReference("collected_data",this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpToolbar();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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

    private void showData(DataSnapshot dataSnapshot,GoogleMap mMap) {
        do{

            DataCollected data = dataSnapshot.getValue(DataCollected.class);

            if (data.getType().equals("potholes")){
                LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(home)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

                float zoom=12;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
            }else if (data.getType().equals("Rubbish")){
                LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(home)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

                float zoom=12;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
            }else if (data.getType().equals("Grocery")){
                LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(home)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

                float zoom=12;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
            }else if (data.getType().equals("Cult")){
                LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(home)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                        .title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

                float zoom=12;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
            }
            /*LatLng home = new LatLng(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(home).
                    title("Marker sur "+data.getDescription()+" - "+data.getQuartier()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

            float zoom=15;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));*/


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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

        // Add a marker in Sydney and move the camera


        /*GroundOverlayOptions homeOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.logocity))
                .position(home, 100);
        mMap.addGroundOverlay(homeOverlay);*/

        setMapLongClick(mMap);
        setPoiClick(mMap);
        setInfoWindowClickToPanorama(mMap);

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

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (setClick){
                //bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottonNavigation_id);
                    bottomNavigationView.setVisibility(View.GONE);
                }else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }

                setClick = false;
            }
        });
        enableMyLocation();

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
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListner();
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

    public void showMenu(){
        invalidateOptionsMenu();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        switch (id){

            case R.id.ubrcity_proximite:
                Intent intent = new Intent(this,ProximityActivity.class);
                startActivity(intent);
                return true;

            case R.id.ubrcity_explorer_menu:
                //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;// also use break

            case R.id.ubrcity_insert_menu:
                Intent intentToStartDataActivity = new Intent(this,DataActivity.class);
                startActivity(intentToStartDataActivity);
                return  true;


        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }


    private void setMapLongClick(final GoogleMap map) {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottonNavigation_id);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                setClick = true;
                bottomNavigationView.setVisibility(View.GONE);
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.dropped_pin))
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            }
        });

    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.setTag("poi");
                poiMarker.showInfoWindow();
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void setInfoWindowClickToPanorama(GoogleMap map) {
        map.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (marker.getTag() == "poi") {
                            StreetViewPanoramaOptions options =
                                    new StreetViewPanoramaOptions().position(
                                            marker.getPosition());

                            SupportStreetViewPanoramaFragment streetViewFragment
                                    = SupportStreetViewPanoramaFragment
                                    .newInstance(options);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.map,
                                            streetViewFragment)
                                    .addToBackStack(null).commit();
                        }
                    }
                });
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

    private class LoadImage extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlString = strings[0];
            mBmp = null;
            try {
                mBmp = Picasso.with(MapsActivity.this).load(urlString).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mBmp;
        }
    }

}
