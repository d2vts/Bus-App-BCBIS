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
import android.widget.ListView;

import java.util.ArrayList;

public class BookMark extends Activity implements AdapterView.OnItemClickListener {
    private String name="";
    private String buskey = "";
    private String linenum = "";
    private String key = "";
    private String x = "";
    private String y = "";
    private String gettime = "";
    private ListView list_bookmark;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> items;
    public SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        list_bookmark = (ListView) findViewById(R.id.list_bookmark);
        items = new ArrayList<String>();
        pref = getSharedPreferences("bookname", 0);
        name = pref.getString("name", " ");
        buskey = pref.getString("buskey", " ");
        key = pref.getString("key4", " ");
        x = pref.getString("x", " ");
        y = pref.getString("y", " ");
        linenum = pref.getString("linenum", " ");
        gettime = pref.getString("gettime", " ");
        items.add(name);
        list_bookmark.setOnItemClickListener(this);
        arrayAdapter = new ArrayAdapter<String>(BookMark.this, android.R.layout.simple_list_item_1, items);
        list_bookmark.setAdapter(arrayAdapter);
    }
    public void home(View v){
        Intent intent = new Intent(BookMark.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        Intent intent = new Intent(this, BusstopInfo.class);
        intent.putExtra("name", name);
        intent.putExtra("buskey", buskey);
        intent.putExtra("arsno", key);
        intent.putExtra("xx", x);
        intent.putExtra("yy", y);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        if(linenum.equals("")){
            builder.setContentTitle("");
            builder.setContentText("");
        }
        else {
            builder.setContentTitle(name + "(" + linenum + ")");
            builder.setContentText(gettime);
        }
        builder.setColor(Color.RED);
        builder.setAutoCancel(false);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        notificationManager.notify(1, builder.build());
    }
}