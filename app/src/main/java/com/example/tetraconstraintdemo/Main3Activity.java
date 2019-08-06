package com.example.tetraconstraintdemo;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main3Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView txtView ;
    TextView locationstart;
    TextView locationend1;
    List<LatLng> path = new ArrayList();
    Button routr;
    List<Place> placess = new ArrayList<>();
    RequestQueue queue;

    private BottomSheetBehavior bottomSheet;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        txtView = findViewById(R.id.test);
        locationstart = findViewById(R.id.test1);
        locationend1 = findViewById(R.id.test2);
        routr = findViewById(R.id.router);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Places.initialize(getApplicationContext(), apiKey);



        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_content);
// The View with the BottomSheetBehavior
       // View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

        View nestedScrollView = (View) findViewById(R.id.bottom_sheet);
        bottomSheet = BottomSheetBehavior.from(nestedScrollView);
        final BottomSheetBehavior behavior = bottomSheet;
        bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                Log.e("onStateChanged", "onStateChanged:" + newState);
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        break;
                    }
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events

            }
        });

        behavior.setPeekHeight(500);








        Places.initialize(getApplicationContext(), "AIzaSyDYWB9hpF_-53-IYlfSVHsM1rXAkVa35aY");

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        routr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+placess.get(0).getAddress()+"&destinations="+placess.get(1).getAddress()+"&key=AIzaSyDYWB9hpF_-53-IYlfSVHsM1rXAkVa35aY";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array1 = jsonObject.getJSONArray("rows");
                                    JSONObject jsonObject1 = new JSONObject(array1.get(0).toString());
                                    JSONArray array2 = jsonObject1.getJSONArray("elements");
                                    JSONObject jsonObject2 = new JSONObject(array2.get(0).toString());
                                    JSONObject obj1 = jsonObject2.getJSONObject("distance");
                                    JSONObject obj2 = jsonObject2.getJSONObject("duration");
                                    txtView.setText("Response is: "+ obj1.toString() + "\n"+obj2.toString());
                                    placess.clear();
                                }catch (JSONException err){
                                    Log.d("Error", err.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtView.setText("That didn't work!");
                    }
                });
  queue.add(stringRequest);
            }
        });
        locationstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS);
// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(Main3Activity.this);
                startActivityForResult(intent, 2);






            }





        });

    locationend1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS);
// Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(Main3Activity.this);
            startActivityForResult(intent, 3);

        }
    });






    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //Execute Directions API request


                CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                mMap.moveCamera(center);





            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (requestCode==2){
                    locationstart.setText(place.getAddress());
                    LatLng latlng = place.getLatLng();
                    txtView.setText(latlng.toString());
                    latlng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    path.add(latlng);
                    placess.add(place);
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15), 2000, null);
                }
                if (requestCode==3){
                    locationend1.setText(place.getAddress());
                    LatLng latlng = place.getLatLng();
                    txtView.setText(latlng.toString());
                    latlng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    path.add(latlng);
                    placess.add(place);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15), 2000, null);

                }

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

    }


/*Also add googleMap != null contition in your click listener.

if(googleMap!=null && location!=null && !location.equals("")){
    new GeocoderTask().execute(location);
}*/


















}
