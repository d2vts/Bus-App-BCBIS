package com.example.bj.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
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

public class BusstopInfo extends Activity implements OnMapReadyCallback { //구글맵 파싱을 위해 implements 사용
    private int clock = 0;
    private int clock_re = 0;
    private int clock_a = 0;
    private int clock_b = 0;
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
    private String time_c="";
    private double b;
    private double b2;
    private TextView tv;
    private TextView gettime1;
    private TextView gettime2;
    private Button bookmark;
    private Button re_bookmark;
    private Button alarm;
    private CountDownTimer ct;
    private Handler handler = new Handler();
    private AlertDialog.Builder builder;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private NotificationCompat.Builder builder_c;
    private NotificationManager notificationManager;
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
        alarm = (Button)findViewById(R.id.alarm);
        bookmark = (Button)findViewById(R.id.bookmark);
        re_bookmark = (Button)findViewById(R.id.re_bookmark);
        pref = getSharedPreferences("bookname", 0); //즐겨찾기를 위해 공유 레퍼런스(SharedPreferences)를 선언
        editor = pref.edit(); //값을 입력할 수 있도록 설정
        re_bookmark.setOnClickListener(new View.OnClickListener() { //re_bookmark 클릭시 발생하는 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusstopInfo.this, BookMark.class);
                startActivity(intent);
            }
        });
        b = Double.parseDouble(x); //받은 X값을 double형식으로 변환(지도좌표에는 실수값이 들어가기때문에)
        b2 = Double.parseDouble(y); //받은 Y값을 double형식으로 변환
        tv.setText(name+"("+key+")");
        FragmentManager fragmentManager = getFragmentManager(); //FragmentManager선언
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pbstopid = getbstopid(); //xml에서 bstopId값을 얻어와 문자열 pbstopid에 저장
                gettime = getTime(pbstopid); //xml에서 도착시간을 파싱해와서 문자열 gettime에 저장
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(gettime.length()==0){ //xml에서 받은 gettime(첫번째 차량 도착시간)의 값이 없다면 실핼
                            gettime2.setText("도착정보 없음");
                        }
                        else {
                            int index = gettime.indexOf("분"); //문자열에는 도착시간(숫자)+텍스트 가 저장되어있기때문에 구분을 위해 숫자다음부분의 index값을 얻는다
                            time_c = gettime.substring(0,index); //substring메소드를 사용해서 숫자부분만 따로 문자열에 저장
                            clock = Integer.parseInt(time_c)*60; //숫자부분을 정수형으로 바꾸고 알림설정을 위해 시간(초)으로 변환 후 저장
                            clock_re = clock; //다시알림을 설정할 때 초기화시켜주기 위해 시간을 변수에 저장
                            gettime1.setText(gettime);
                        }
                        if(gettime_2.length()==0){ //xml에서 받은 gettime_2(두번째 차량 도착시간)의 값이 없다면 실핼
                            gettime2.setText("도착정보 없음");
                        }
                        else {
                            gettime2.setText(gettime_2);
                        }
                    }
                });
            }
        }).start();
        alarm.setOnClickListener(new View.OnClickListener() { //알람 버튼을 누른다면 발생하는 이벤트
            @Override
            public void onClick(View v) {
                if(gettime.length()==0 || Integer.parseInt(time_c) < 2){ //첫번째 도착정보가 없거나 도착시간이 2분미만일 경우 알림을 설정안하는 부분
                    Toast.makeText(BusstopInfo.this, "도착알림 없음", Toast.LENGTH_SHORT).show();
                }
                else{ //첫번째 도착정보가 있고 2분이상일 경우
                    clock = clock_re; //도착시간을 초기화 한다.
                    ct = new CountDownTimer(clock * 60 * 1000, 1000) { //도착알림을 위해 시간은 초당 1초씩 줄어들도록 카운트 하는 부분
                        @Override
                        public void onTick(long millisUntilFinished) {
                            update(); //카운트가 시작되면 호출되는 함수
                        }
                        @Override
                        public void onFinish() {
                            ct.cancel(); //끝나면 카운트를 종료
                            notificationManager.cancel(1); //카운트가 종료되면 상단알림을 삭제
                        }
                    }.start();
                }
            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() { //즐겨찾기 이동 버튼을 클릭시 발생하는 이벤트
            @Override
            public void onClick(View v) {
                Toast.makeText(BusstopInfo.this, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                editor.putString("name",name); //SharedPreferences에 값을 입력
                editor.putString("buskey",key3);
                editor.putString("key4",key4);
                editor.putString("x",x);
                editor.putString("y",y);
                editor.putString("linenum",linenum);
                editor.putString("gettime",gettime);
                editor.commit(); //SharedPreferences에 입력한 값을 저장
            }
        });
    }
    public void update(){ //다른 스레드에서 수행할 작업(알림설정)을 위해 Handler실행
        Runnable updater = new Runnable(){
            public void run(){
                alarm_c();
            }
        };
        handler.post(updater);
    }
    public void alarm_c(){ //상단 알림 설정
        builder_c = new NotificationCompat.Builder(this, "default");
        Intent intent = new Intent(this, BusstopInfo.class); //알림을 클릭했을때 액티비티를 호출하기위한 부분
        intent.putExtra("name", name);
        intent.putExtra("buskey", key3);
        intent.putExtra("arsno", key);
        intent.putExtra("xx", x);
        intent.putExtra("yy", y);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder_c.setSmallIcon(R.mipmap.ic_launcher);
        clock_a = clock / 60; //도착시간을 분과 초로 나눌때 해당하는 분 부분
        clock_b = clock % 60; //초 부분
        builder_c.setContentTitle(name + "(" + linenum + ")"); //알림내역에 title내용 설정
        builder_c.setContentText(String.valueOf(clock_a) + "분" + String.valueOf(clock_b) + "초"); //알림영역에 세부내용 설정
        clock--; //1초씩 감소
        if (clock_a == 1 && clock_b == 0) { //카운트를 시작해서 남은 시간이 1분0초가 되면 들어오는 부분
            ct.cancel(); //상단알림 종료
            notificationManager.cancel(1); //상단알림 제거
            builder = new AlertDialog.Builder(this); //팝업창 선언
            builder.setTitle("도착 예정 알림"); //팝업창 title내용 설정
            builder.setMessage(linenum + "번 버스 곧 도착!"); //팝업창 세부내용 설정
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() { //팝업창 버튼 생성
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        } else { //남은 시간이 1분 0초가 아니라면 실행하는 부분
            builder_c.setColor(Color.RED);
            builder_c.setContentIntent(pendingIntent);
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE); //NotificationManager 선언
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            notificationManager.notify(1, builder_c.build()); //상단 알림을 보여주는 부분(없으면 알림이 안보인다)
        }
    }
    /*
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (clock_a == 1 && clock_b == 0) {
            builder.show();
        }
    }
    */
    @Override
    public void onMapReady(GoogleMap map) { //구글지도맵 세부설정
        MarkerOptions markerOptions = new MarkerOptions(); //마커 선언
        markerOptions.position(new LatLng(b2, b)); //마커의 위치 설정
        markerOptions.title(name); //마커 title이름 설정
        map.addMarker(markerOptions); //구글맵에 마커 추가
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(b2, b))); //카메라위치 설정
        map.animateCamera(CameraUpdateFactory.zoomTo(19)); //구글맵 줌인 설정
    }
    String getTime(String a){ //xml에서 도착시간을 얻어오는 함수
        StringBuffer buffer = new StringBuffer(); //첫번째 차량 도착정보를 저장하는 스트링버퍼
        StringBuffer buffer2 = new StringBuffer(); //두번째 차량 도착정보를 저장하는 스트링버퍼
        String str = a;
        String str2 = key3;
        String bstopid = URLEncoder.encode(str);
        String lineID = URLEncoder.encode(str2);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busStopArr?bstopid="+bstopid+"&lineid="+lineID+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D";
        //서비스키 인증 오류를 대비해서---- 서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {
            int count=0;
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
                            if(count==0) {
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("분후 도착예정");
                                count++;
                            }
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
    String getbstopid(){ //xml에서 버스정류장 번호를 얻어오는 함수
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