package com.example.bj.test;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;


public class BusstopDetail extends Activity implements OnMapReadyCallback {
    private String x="";
    private String y="";
    private String bsid="";
    private String name="";
    private String key="";
    private String gtime="";
    private String[] lines;
    private double b;
    private double b2;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> items;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busstop_detail);
        Intent intent = getIntent();
        x = intent.getStringExtra("xx");
        y = intent.getStringExtra("yy");
        bsid = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        b = Double.parseDouble(x);
        b2 = Double.parseDouble(y);
        key=bsid;
        listView = (ListView) findViewById(R.id.listview);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        items = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                gtime = getTime();
                lines = gtime.split(System.getProperty("line.separator"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<lines.length; i++){
                            items.add(lines[i]+"\n"+lines[i+1]);
                            i++;
                        }
                        arrayAdapter = new ArrayAdapter<String>(BusstopDetail.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        LatLng SEOUL = new LatLng(b2, b);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title(name);
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(19));
    }
    String getTime(){ // editText에 line아이디 입력시 해당 라인아이디에 해당하는 값 파싱해오는 것인듯????
        StringBuffer buffer = new StringBuffer();
        String str = key;
        String lineID = URLEncoder.encode(str);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/stopArr?bstopid="+lineID+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D";
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
                        else if(tag.equals("lineNo")){
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("번 노선");
                            buffer.append("\n");
                        }
                        else if(tag.equals("min1")){
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("분후 도착예정");
                            buffer.append("\n");
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