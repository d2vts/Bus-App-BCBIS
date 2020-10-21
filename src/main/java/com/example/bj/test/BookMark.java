package com.example.bj.test;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookMark extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener { //listview,button 이벤트를 자기수신자를 통해 처리
    private String name="";
    private String buskey = "";
    private String linenum = "";
    private String key = "";
    private String x = "";
    private String y = "";
    private String gettime = "";
    private ImageButton alarm;
    private ImageButton delete;
    private ListView list_bookmark;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> items;
    public SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        alarm = (ImageButton)findViewById(R.id.alarm);
        delete = (ImageButton)findViewById(R.id.delete);
        alarm.setOnClickListener(this);
        list_bookmark = (ListView) findViewById(R.id.list_bookmark);
        items = new ArrayList<String>();
        pref = getSharedPreferences("bookname", 0); //Busstopinfo에서 선언한 SharedPreferences에 저장된 값을 읽기위해 선언
        name = pref.getString("name", " "); //저장된 내용을 읽기위해 저장시 설정한 key과 동일함 key값을 통해 값을 읽는다
        buskey = pref.getString("buskey", " ");
        key = pref.getString("key4", " ");
        x = pref.getString("x", " ");
        y = pref.getString("y", " ");
        linenum = pref.getString("linenum", " ");
        editor = pref.edit(); //SharedPreferences 내용 수정하기위해 선언
        delete.setOnClickListener(new View.OnClickListener() { //즐겨찾기 삭제 버튼 이벤트
            @Override
            public void onClick(View v) {
                editor.clear(); //저장된 내용을 비운다
                editor.commit(); //저장
                arrayAdapter.clear(); //arrayAdapter에 들어있는 값을 비운다
                linenum = ""; //즐겨찾기 삭제를 하면 linenum값을 없앤다
            }
        });
        items.add(name); //ArrayList에 해당 정류소이름 저장
        list_bookmark.setOnItemClickListener(this);
        arrayAdapter = new ArrayAdapter<String>(BookMark.this, android.R.layout.simple_list_item_1, items);
        list_bookmark.setAdapter(arrayAdapter);
    }
    public void home(View v){ //뒤로가기버튼 클릭시 호출되는 함수
        Intent intent = new Intent(BookMark.this, MainActivity.class);
        startActivity(intent);
    }
    public void alarm(){ //상단알림 설정을 위한 함수
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default"); //NotificationCompat.Builder선언
        Intent intent = new Intent(this, BusstopInfo.class); //알림 클릭시 이동하는 액티비티 설정
        intent.putExtra("name", name); //값을 넘겨주는 이유는 이동하는 액티비티에 필요한 값을 줘야하기때문에(없다면 오류발생(지도,실시간정보))
        intent.putExtra("buskey", buskey);
        intent.putExtra("arsno", key);
        intent.putExtra("xx", x);
        intent.putExtra("yy", y);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(name); //상단알림내용 title내용설절
        builder.setContentText(linenum); //상단알림내용 세부내용설정
        builder.setColor(Color.RED);
        builder.setAutoCancel(false); //상단알림을 클릭해도 제거가 안되도록 설정(이유는 자주사용하는 정류장의 해당노선을 클릭한번으로 계속 사용할 수 있도록)
        builder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        notificationManager.notify(1, builder.build());
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BookMark.this, BusstopInfo.class);
        intent.putExtra("name", name);
        intent.putExtra("buskey", buskey);
        intent.putExtra("arsno", key);
        intent.putExtra("xx", x);
        intent.putExtra("yy", y);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(linenum.equals("")){ //즐겨찾기 삭제할때 linenum값을 설정했기때문에 설정값과 일치하면 이부분을 실행
            Toast.makeText(BookMark.this, "즐겨찾기가 없습니다", Toast.LENGTH_SHORT).show();
        }
        else{ //그렇지않으면 상단알림을 실행
            alarm();
        }
    }
}