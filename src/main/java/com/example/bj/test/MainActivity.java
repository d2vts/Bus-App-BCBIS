package com.example.bj.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void go(View v) {
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }
    public void go2(View v) {
        Intent intent = new Intent(this, New2Activity.class);
        startActivity(intent);
    }
    public void go3(View v) {
        Intent intent = new Intent(this, New3Activity.class);
        startActivity(intent);
    }
    public void go4(View v){
        Intent intent = new Intent(this, BookMark.class);
        startActivity(intent);
    }
    public void go5(View v){
        Toast.makeText(this,"경성대학교 | 컴퓨터공학과\n이태승 2013851118\n배재현 2013951068", Toast.LENGTH_LONG).show();
    }
}
