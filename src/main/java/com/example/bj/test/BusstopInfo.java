package com.example.bj.test;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class BusstopInfo extends Activity implements OnMapReadyCallback {
    private String x="";
    private String y="";
    private String name="";
    private String linenum="";
    private String key="";
    private String key2="";
    private String key3="";
    private String key4="";
    private String pbstopid="";
    private String gettime="";
    private String gettime_2="";
    private double b;
    private double b2;
    private TextView tv;
    private TextView gettime1;
    private TextView gettime2;
    private Button bookmark;
    private Button re_bookmark;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busstop_info);
        Intent intent = getIntent();
        x = intent.getStringExtra("xx");
        y = intent.getStringExtra("yy");
        key2 = intent.getStringExtra("buskey");
        key = intent.getStringExtra("arsno");
        name = intent.getStringExtra("name");
        linenum = intent.getStringExtra("linenum");
        key3 = key2;
        key4 = key;
        tv = (TextView) findViewById(R.id.nametext);
        gettime1 = (TextView) findViewById(R.id.textView3);
        gettime2 = (TextView) findViewById(R.id.textView5);
        bookmark = (Button)findViewById(R.id.bookmark);
        re_bookmark = (Button)findViewById(R.id.re_bookmark);
        pref = getSharedPreferences("bookname", 0);
        editor = pref.edit();
        re_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusstopInfo.this, BookMark.class);
                startActivity(intent);
            }
        });
        b = Double.parseDouble(x);
        b2 = Double.parseDouble(y);
        tv.setText(name+"("+key+")");
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pbstopid = getbstopid();
                gettime = getTime(pbstopid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(gettime.length()==0){
                            gettime2.setText("도착정보 없음");
                        }
                        else {
                            gettime1.setText(gettime);
                        }
                        if(gettime_2.length()==0){
                            gettime2.setText("도착정보 없음");
                        }
                        else {
                            gettime2.setText(gettime_2);
                        }
                    }
                });
            }
        }).start();
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BusstopInfo.this, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                editor.putString("name",name);
                editor.putString("buskey",key3);
                editor.putString("key4",key4);
                editor.putString("x",x);
                editor.putString("y",y);
                editor.putString("linenum",linenum);
                editor.putString("gettime",gettime);
                editor.commit();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap map) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(b2, b));
        markerOptions.title(name);
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(b2, b)));
        map.animateCamera(CameraUpdateFactory.zoomTo(19));
    }
    String getTime(String a){ //
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        String str = a;
        String str2 = key3;
        String bstopid = URLEncoder.encode(str);
        String lineID = URLEncoder.encode(str2);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busStopArr?bstopid="+bstopid+"&lineid="+lineID+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D";
        //서비스키 인증 오류를 대비해서---- 서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {

            URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); // url 위치로 인풋스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();

            // inputstream 으로부터 xml 입력받기
            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작 단계 \n\n");
                        buffer2.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item"));
                        else if(tag.equals("min1")){
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("분후 도착예정");
                        }
                        else if(tag.equals("min2")){
                            xpp.next();
                            buffer2.append(xpp.getText());
                            buffer2.append("분후 도착예정");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item"))// 첫번째 검색결과종료 후 줄바꿈
                            break;
                }
                eventType= xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        gettime_2 = buffer2.toString();
        return buffer.toString();
    }
    String getbstopid(){ // editText에 line아이디 입력시 해당 라인아이디에 해당하는 값 파싱해오는 것인듯????
        StringBuffer buffer = new StringBuffer();
        String str = name;
        String str2 = key4;
        String bstopnm = URLEncoder.encode(str);
        String arsno = URLEncoder.encode(str2);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busStop?bstopnm="+bstopnm+"&arsno="+arsno+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D&numOfRows=3&pageNo=1";
        //서비스키 인증 오류를 대비해서---- 서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {

            URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); // url 위치로 인풋스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();

            // inputstream 으로부터 xml 입력받기
            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item"));
                        else if(tag.equals("bstopId")){
                            xpp.next();
                            buffer.append(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item"))// 첫번째 검색결과종료 후 줄바꿈
                            break;
                }
                eventType= xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}