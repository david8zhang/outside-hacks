package com.example.dzhang.outsidehacks;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {

    /** Google Permissions constant. */
    private int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 200;

    /** Google API Variables */
    private GoogleApiClient mGoogleApiClient;
    private Marker marker;
    private GoogleMap mMap;
    private LocationManager mLocationManager;

    /** User to Marker & Marker to User Mappings */
    private HashMap<String, Marker> users = new HashMap<>();

    private User dummyUser;
    private String latitude;
    private String longitude;
    private long lastTime;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username;
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                username = null;
            } else {
                username = extras.getString("Username");
            }
        } else {
            username = (String)savedInstanceState.getSerializable("user_id");
        }

        // ===== Create Dummy User =====
        List<String> friendList = new ArrayList<>();
        friendList.add("Bill Gates");
        friendList.add("Test user #e");
        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist("a", "Vulfpeck"));
        artists.add(new Artist("b", "Red Taxi"));
        dummyUser = new User(Build.ID, username, "I am at OutsideLands with the VIP tickets I won from OutsideHacks", friendList, null);
        // ===== End Create Dummy User =====

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        handleLocationIncoming();
        handleLocationBroadcast();
        handlePendingRequests();

        Button friends_button = (Button)findViewById(R.id.friends_button);
        friends_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFriendsList = new Intent(MapsActivity.this, FriendList.class);
                MapsActivity.this.startActivity(toFriendsList);
            }
        });

        Button pending_button = (Button)findViewById(R.id.request_button);
        pending_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPending = new Intent(MapsActivity.this, PendingRequestActivity.class);
                MapsActivity.this.startActivity(toPending);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * Called when Google APIS are connected
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null) {
                LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title("Me")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(18)
                        .bearing(0)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Render all nearby strangers
     * @param jsonArray
     */
    public void renderNearby(JSONArray jsonArray) {
        ArrayList<JSONObject> arr = checkInRadius(jsonArray);
        ArrayList<User> dummyUsers = new ArrayList<>();
        dummyUsers.add(new User("a", "Edward Zhang", "A cool person", null, null));
        dummyUsers.add(new User("b", "Bill Gates", "Not as rich as Edward", null, null));
        dummyUsers.add(new User("c", "Steve Jobs", "Not as successful as Edward", null, null));
        dummyUsers.add(new User("d", "Anonymous", "I am anonymous", null, null));
        dummyUsers.add(new User("e", "Test person #e", "id = e", null, null));
        dummyUsers.add(new User("f", "Albert", "e = mc squared", null, null));
        if (latitude != null && longitude != null)
        {
            double myLat = Double.parseDouble(latitude);
            double myLon = Double.parseDouble(longitude);
            for (User u : dummyUsers){
                double randLat = Math.random() * 100 - 50 + myLat;
                double randLon = Math.random() * 100 - 50 + myLon;
                addMarker(u.userId, randLat, randLon);
            }
        }

        for(int i = 0; i < arr.size(); i++) {
            try {
                JSONObject obj = arr.get(i);
                if(!obj.getString("user_id").equals(dummyUser.userId) &&
                        !DataManager.friends.contains(obj.getString("user_id"))) {
                    JSONArray location = new JSONArray(obj.getString("location"));
                    addMarker(obj.getString("user_id"), location.getDouble(0), location.getDouble(1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Render all the nearby friends
     * @param jsonArray
     */
    public void renderFriends(JSONArray jsonArray) {
        try {
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if(DataManager.friends.contains(obj.getString("user_id"))) {
                    JSONArray location = new JSONArray(obj.getString("location"));
                    addFriend(obj.getString("user_id"), location.getDouble(0), location.getDouble(1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array of all users within 100 meters
     * @param jsonArray An array of all users currently broadcasting to the socket.
     * @return Array list with json objects corresponding to the users.
     */
    public ArrayList<JSONObject> checkInRadius(JSONArray jsonArray) {
        ArrayList<JSONObject> arr = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray location = new JSONArray(obj.getString("location"));
                if(latitude != null && longitude != null) {
                    double myLat = Double.parseDouble(latitude);
                    double myLng = Double.parseDouble(longitude);
                    float[] results = new float[1];
                    Location.distanceBetween(myLat, myLng, location.getDouble(0), location.getDouble(1), results);
                    if(results[0] < 100) {
                        arr.add(obj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * Add a marker for a new user retrieved from the websocket stream
     * @param user_id
     * @param lat
     * @param lng
     */
    public void addMarker(String user_id, double lat, double lng) {
        if(!users.containsKey(user_id)) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(user_id)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            users.put(user_id, marker);
        } else {
            Marker marker = users.get(user_id);
            marker.setPosition(new LatLng(lat, lng));
        }
    }

    public void addFriend(String user_id, double lat, double lng) {
        if(!users.containsKey(user_id)) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(user_id)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            users.put(user_id, marker);
        } else {
            Marker marker = users.get(user_id);
            marker.setPosition(new LatLng(lat, lng));
        }
    }

    /**
     * Clear all the markers on the map besides self marker
     */
    public void clearMarkers() {
        mMap.clear();
        users.clear();
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                .title("Me")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    /**
     * Override the default action performed on google maps marker click.
     * @param marker
     * @return A new marker
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        String user_id = null;
        for (Map.Entry<String, Marker> entry: users.entrySet()) {
            if(marker.equals(entry.getValue())) {
                user_id = entry.getKey();
            }
        }
        if(user_id == null) {
            return false;
        }
        if(DataManager.friends.contains(user_id)) {
            Intent toChat= new Intent(MapsActivity.this, ChatActivity.class);
            toChat.putExtra("user_id", user_id);
            MapsActivity.this.startActivity(toChat);
            return true;
        }
        Intent chatRequest = new Intent(MapsActivity.this, ChatRequestActivity.class);
        chatRequest.putExtra("user_id", user_id);
        MapsActivity.this.startActivity(chatRequest);
        return true;
    }

    /**
     * Handle incoming location data from websocket stream
     */
    public void handleLocationIncoming() {
        // Location broadcaster
        Emitter.Listener onNearbyListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Log.d("DATA", data);
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            long time = System.currentTimeMillis();
                            if(time - lastTime > 10000) {
                                Log.d("TIME", Long.toString(time));
                                Log.d("LAST TIME", Long.toString(lastTime));
                                if(lastTime != 0) {
                                    clearMarkers();
                                }
                                renderNearby(jsonArray);
                                renderFriends(jsonArray);
                                lastTime = time;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        mSocket.on("nearby-users", onNearbyListener);
        mSocket.connect();
    }

    /**
     * Handle the broadcast of location data to the websocket stream
     */
    public void handleLocationBroadcast() {
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString(location.getLongitude());
                String latlng = "[" + latitude + "," + longitude + "]";
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("user_id", dummyUser.userId);
                    obj.put("location", latlng);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(18)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    if(marker != null) {
                        marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mSocket.emit("emit-location", obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_MEDIUM);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(3000L, 0.5f, crit, listener, Looper.myLooper());
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
    }

    public void handlePendingRequests() {
        Emitter.Listener messageListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject)args[0];
                        Log.d("REQUESTS", data.toString());
                        try {
                            Request req = new Request(data.getString("from"), data.getString("target"), data.getString("status"));
                            for(int i = 0; i < DataManager.requests.size(); i++) {
                                Request pending = DataManager.requests.get(i);
                                if(pending.target.equals(data.getString("from"))) {
                                    DataManager.requests.remove(pending);
                                }
                            }
                            if(!data.getString("from").equals(Build.ID)) {
                                DataManager.requests.add(req);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        mSocket.on("requests", messageListener);
    }

    /**
     * Handle the result of requesting location permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleLocationBroadcast();
            } else {
                Toast.makeText(MapsActivity.this, "Location denied, app cannot function", Toast.LENGTH_SHORT).show();
            }
        }
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
        mMap.setOnMarkerClickListener(MapsActivity.this);
    }

    /**
     * Define logic that gets executed when the activity stops
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Disconnect from the websocket stream when android client disconnects
     */
    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        mMap.clear();
        mSocket.disconnect();
        super.onDestroy();
    }
}
