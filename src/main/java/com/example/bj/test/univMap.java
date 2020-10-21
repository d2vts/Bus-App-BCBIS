package com.example.bj.test;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//a. rsp = 12, 15, 16,
//b. rsp = 0, 2, 6, 7, 8, 9, 10, 14, 17, 18, 19, 21, 23, 24, 25
//c. rsp = 4
//d. rsp = 3, 5, 13, 22
//e. rsp = 20

//예외 1, 11
//a좌표 : 35.136041, 129.097464
//b좌표 : 35.136609, 129.098005
//c좌표 : 35.136473, 129.100418
//d좌표 : 35.136496, 129.100589
//e좌표 : 35.137606, 129.100864



public class univMap extends Activity implements OnMapReadyCallback {
    double x,y;
    int r=4;
    int rsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_univ_map);
        rsp = getIntent().getIntExtra("map",1);
        if(rsp==12||rsp==15||rsp==16){
            y=35.136041;
            x=129.097464;
        }
        else if(rsp==0||rsp==2||rsp==6||rsp==7||rsp==8||rsp==9||rsp==10||
                rsp==14||rsp==17||rsp==18||rsp==19||rsp==21||rsp==23||rsp==24||rsp==25){
            y=35.136609;
            x=129.098005;
        }
        else if(rsp == 4){
            y=35.136473;
            x=129.100418;
        }
        else if(rsp==3||rsp==5||rsp==13||rsp==22){
            y=35.136496;
            x=129.100589;
        }
        else if(rsp == 20){
            y=35.137606;
            x=129.100864;
        }
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
       LatLng SEOUL = new LatLng(y,x);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("경성대학교 정류장");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    public void gomain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
