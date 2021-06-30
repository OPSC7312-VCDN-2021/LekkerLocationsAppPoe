package com.myapp.lekkerlocationsapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myapp.lekkerlocationsapp.nearby.GetNearbyPlaces;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    RelativeLayout searchRl;
    CameraUpdate update = null;
    TextView atmTv, bankTv, hospitalTv, movieTv, hotelTv;
    LatLng currentLatLng = null;
    private int PROXIMITY_RADIUS = 10000;
    private Context context;
    Object[] transferData;
    FloatingActionButton goBtn;
    private List<Polyline> polylines=null;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.context = this;

        searchRl = findViewById(R.id.searchRl);
        atmTv = findViewById(R.id.atmTv);
        bankTv = findViewById(R.id.bankTv);
        hospitalTv = findViewById(R.id.hospitalTv);
        movieTv = findViewById(R.id.movieTv);
        hotelTv = findViewById(R.id.hotelTv);
        goBtn=findViewById(R.id.goBtn);
        toolbar=findViewById(R.id.toolbar);

        configureToolBar();


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapFragment);
        supportMapFragment.getMapAsync(this);
        requestPermissionLocation();

        Places.initialize(getApplicationContext(), getString(R.string.places_key));
        searchRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(HomeActivity.this);
                startActivityForResult(intent, 100);
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(HomeActivity.this);
                startActivityForResult(intent,101);
            }
        });

        atmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
                mMap.clear();
                transferData = new Object[2];
                String url = getUrl(currentLatLng.latitude, currentLatLng.longitude, "atm");
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(context, "Searching Nearby Hospital...", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Showing Nearby Hospital...", Toast.LENGTH_SHORT).show();
            }
        });

        bankTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
                mMap.clear();
                transferData = new Object[2];
                String url = getUrl(currentLatLng.latitude, currentLatLng.longitude, "bank");
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(context, "Searching Nearby Hospital...", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Showing Nearby Hospital...", Toast.LENGTH_SHORT).show();
            }
        });

        hospitalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
                mMap.clear();
                transferData = new Object[2];
                String url = getUrl(currentLatLng.latitude, currentLatLng.longitude, "hospital");
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(context, "Searching Nearby Hospital...", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Showing Nearby Hospital...", Toast.LENGTH_SHORT).show();
            }
        });

        movieTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
                mMap.clear();
                transferData = new Object[2];
                String url = getUrl(currentLatLng.latitude, currentLatLng.longitude, "movie_theater");
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(context, "Searching Nearby Hospital...", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Showing Nearby Hospital...", Toast.LENGTH_SHORT).show();
            }
        });

        hotelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
                mMap.clear();
                transferData = new Object[2];
                String url = getUrl(currentLatLng.latitude, currentLatLng.longitude, "restaurant");
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(context, "Searching Nearby Hospital...", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Showing Nearby Hospital...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void configureToolBar() {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.optmenu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }

        return true;
    }

    private void requestPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1122);
        }
    }

    private void getCurrentLocation() {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Toast.makeText(HomeActivity.this, "" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                        //TODO: UI updates.
                    }
                }
            }
        };
        //  LocationServices.getFusedLocationProviderClient(HomeActivity.this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        LocationServices.getFusedLocationProviderClient(HomeActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 & resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            update = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10f);
            // mMap.animateCamera(update);
            //  mMap.addMarker(new MarkerOptions(). position(place.getLatLng()).title(place.getName()));

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap=googleMap;
                    mMap.clear();
                    googleMap.animateCamera(update);
                    LatLng latLng = place.getLatLng();
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    googleMap.addMarker(markerOptions);
                }
            });

        }

        else if (requestCode==101&resultCode==RESULT_OK){

            Place place=Autocomplete.getPlaceFromIntent(data);

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap=googleMap;
                    mMap.clear();


                    LatLng  latLng =place.getLatLng();
                    Findroutes(currentLatLng,latLng);

                    double distance;
                    Location locationA = new Location("");
                    locationA.setLatitude(currentLatLng.latitude);
                    locationA.setLongitude(currentLatLng.longitude);
                    Location locationB = new Location("");
                    locationB.setLatitude(latLng.latitude);
                    locationB.setLongitude(latLng.longitude);
                    distance = locationA.distanceTo(locationB)/1000;
                 //   kmeter.setText(String.valueOf(distance));
                    Toast.makeText(getApplicationContext(), "Distance : "+distance+" KM", Toast.LENGTH_LONG).show();


                  double d2=  distance(currentLatLng.latitude,currentLatLng.longitude,latLng.latitude,latLng.latitude);



                }
            });

        }


        else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1122) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=").append(latitude).append(",").append(longitude);
        googleURL.append("&radius=").append(PROXIMITY_RADIUS);
        googleURL.append("&type=").append(nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + getResources().getString(R.string.places_key));
        Log.d("GoogleMapsActivity", "url =" + googleURL.toString());

        return googleURL.toString();
    }



    public void Findroutes(LatLng Start, LatLng End )
    {
        if(Start==null || End==null) {
            Toast.makeText(HomeActivity.this,"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyCNe9fFGxRRWhHDCjMh6n0faXprtziilYs")  //also define your api key here.
                    .build();
            routing.execute();

        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(HomeActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        // CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        //  CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);

                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);




    }

    @Override
    public void onRoutingCancelled() {

    }



//    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
//        int Radius = 6371;// radius of earth in Km
//        double lat1 = StartP.latitude;
//        double lat2 = EndP.latitude;
//        double lon1 = StartP.longitude;
//        double lon2 = EndP.longitude;
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
//                * Math.sin(dLon / 2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        double valueResult = Radius * c;
//        double km = valueResult / 1;
//        DecimalFormat newFormat = new DecimalFormat("####");
//        int kmInDec = Integer.valueOf(newFormat.format(km));
//        double meter = valueResult % 1000;
//        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);
//
//        return Radius * c;
//    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



}