package com.bignerdranch.android.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public int dataLen;
    public double[] longitude;
    public double[] latitude;
    public HashMap<Marker, Integer> markerHashMap = new HashMap<>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified, when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject resp = response.getJSONObject();
                            JSONArray data = resp.getJSONArray("data");
                            JSONArray dataList = new JSONArray(data.toString());
                            dataLen = dataList.length();
                            longitude = new double[dataLen];
                            latitude = new double[dataLen];
                            for (int i = 0; i < dataLen; i++) {
                                if (data.getJSONObject(i).has("place")) {
                                    JSONObject temp = data.getJSONObject(i);
                                    JSONObject place = temp.getJSONObject("place");
                                    JSONObject location = place.getJSONObject("location");
                                    latitude[i] = location.optDouble("latitude");
                                    longitude[i] = location.optDouble("longitude");
                                    LatLng markerPosition = new LatLng(latitude[i], longitude[i]);
                                    //for markerHashMap
                                    Marker currentMarker = mMap.addMarker(new MarkerOptions().position(markerPosition).draggable(false));
                                    markerHashMap.put(currentMarker, i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "place");
        request.setParameters(parameters);
        request.executeAsync();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = markerHashMap.get(marker);
                Intent intent = new Intent(MapsActivity.this, MarkerActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                return true;
            }
        });

    }

}