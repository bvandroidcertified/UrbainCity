package com.cintech.urbaincity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = DataActivity.class.getName();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;

    public boolean setClick=false;
    LinearLayout mConstraintLayout;
    TextInputEditText tvLatitude;
    TextInputEditText tvLongitude;
    TextInputEditText tvArrondissement;
    TextInputEditText tvQuartier;
    TextInputEditText tvDescription;
    ImageView imageView;
    MaterialButton btnImage;
    MaterialButton btnSave;
    DataCollected data;
    private String mTypeChoose;
    RadioButton rbSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase ;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mConstraintLayout = (LinearLayout) findViewById(R.id.form);
        tvLatitude = (TextInputEditText) findViewById(R.id.latitude);
        tvLongitude = (TextInputEditText) findViewById(R.id.longitude);
        tvArrondissement = (TextInputEditText) findViewById(R.id.arrondissement);
        tvQuartier = (TextInputEditText) findViewById(R.id.quartier);
        tvDescription = (TextInputEditText) findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.image);
        btnImage = (MaterialButton) findViewById(R.id.btnImage);
        btnSave = (MaterialButton) findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent,"Insert picture"),PICTURE_RESULT);
            }
        });
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
                    mConstraintLayout.setVisibility(View.VISIBLE);
                }else {
                    mConstraintLayout.setVisibility(View.GONE);
                }

                setClick = false;
            }
        });
        enableMyLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent dataparam) {
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri imageUri = dataparam.getData();
            StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    /*String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    deal.setImageUrl(url);
                    showImage(url);*/
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    String url = downloadUrl.toString();
                    String pictureName = taskSnapshot.getStorage().getPath();

                    data.setImageUrl(url);
                    data.setImageName(pictureName);
                    /*
                    data.setImageUrl(url);
                    data.setImageName(pictureName);*/
                    Log.d("Url",url);
                    Log.d("Name",pictureName);
                    showImage(url);
                    //saveData();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, dataparam);
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
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListner();
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


    private void setMapLongClick(final GoogleMap map) {
        //bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottonNavigation_id);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                setClick = true;
                mConstraintLayout.setVisibility(View.GONE);
                tvLatitude.setText(String.valueOf(latLng.latitude));
                tvLongitude.setText(String.valueOf(latLng.longitude));
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

    private void saveData() {

        data.setLatitude(tvLatitude.getText().toString());
        data.setLongitude(tvLongitude.getText().toString());
        data.setDescription(tvDescription.getText().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        data.setDate(formatter.format(date));
        data.setArrondissement(tvArrondissement.getText().toString());
        data.setQuartier(tvQuartier.getText().toString());

        RadioGroup rdGroup = (RadioGroup) findViewById(R.id.radiogroup);
        int selectedId = rdGroup.getCheckedRadioButtonId();
        rbSelected = (RadioButton)findViewById(selectedId);
        mTypeChoose = (String) rbSelected.getText();
        data.setType(mTypeChoose);
        mDatabaseReference.push().setValue(data);
        //Toast.makeText(this, "Data insert", Toast.LENGTH_SHORT).show();
        clean();
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
       /* if (data.getId() == null){
            mDatabaseReference.push().setValue(data);
        }else {
            mDatabaseReference.child(data.getId()).setValue(data);
        }*/
    }

    private void clean() {
        tvArrondissement.setText("");
        tvQuartier.setText("");
        tvDescription.setText("");
        tvLatitude.setText("");
        tvLongitude.setText("");
        setClick = true;
    }

    private void showImage(String url){
        if (url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width,width*2/3)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
