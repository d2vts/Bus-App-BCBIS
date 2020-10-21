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

import org.w3c.dom.Text;
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
    private String gtime="";
    private String[] lines;
    private double b;
    private double b2;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> items;
    private TextView tv;
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
        listView = (ListView) findViewById(R.id.listview);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        items = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                gtime = getTime(); //xml에서 해당정류장을 정차하는 버스노선들의 도착정보를 가져와서 문자열에 저장하는 부분
                lines = gtime.split(System.getProperty("line.separator")); //라인별로 문자열을 나눠서 문자열 배열에 저장
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < lines.length; i++) { //문자열 배열을 ArrayList에 추가하기위한 부분
                            items.add(lines[i] + "\n" + lines[i + 1]); //문자열 배열의 노선정보와 도착정보를 2개단위로 저장했기때문에 ArrayList에 2개(노선번호+도착시간) 저장
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
    String getTime(){ //xml에서 정류장에 정차하는 버스들의 노선과 도착정보를 파싱하기위한 함수
        StringBuffer buffer = new StringBuffer();
        String str = bsid;
        String lineID = URLEncoder.encode(str);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/stopArr?bstopid="+lineID+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D";
        //서비스키 인증 오류를 대비해서---- 서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {
            int count_name=0;
            int count_min=0;

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
                            count_name++; //노선번호 읽은 횟수
                            xpp.next();
                            buffer.append(xpp.getText()+"번 노선"+"\n");
                        }
                        else if(tag.equals("min1")){
                            count_min++; //도착시간 읽은 횟수
                            xpp.next();
                            buffer.append(xpp.getText()+"분후 도착예정"+"\n");
                        }
                        else if(tag.equals("station2")) { //마지막 태그를 읽는다.
                            xpp.next();
                            if (count_name != count_min) { //만약 노선에 따른 도착정보가 없다면 들어오는 if문
                                int n = count_name - count_min; //노선을 읽은 횟수랑 도착정보를 읽은 횟수를 비교
                                buffer.append("도착정보 없음" + "\n");
                                count_min += n; //차이나는 만큼 값을 따라가준다.
                            }
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