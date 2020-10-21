package com.example.bj.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    int sp1;
    int sp2;
    int rsp = -1;
    private Spinner spinner1;
    private Spinner spinner2;
    private Button btn;
    private ImageView iv;
    private TextView gotv;
    //private int [] Imageid = {R.drawable.digitalkj};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner1.setSelection(1);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        btn = (Button)findViewById(R.id.btngo);
        iv = (ImageView)findViewById(R.id.howtogo);
        gotv = (TextView)findViewById(R.id.gotv);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,int position, long id) {
                sp1=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                sp2=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
    public void go4(View v) {
        Intent intent = new Intent(this, BookMark.class);
        startActivity(intent);
    }
    public void go5(View v) {
        Toast.makeText(this, "경성대학교 | 컴퓨터공학과\n이태승 2013851118\n배재현 2013951068", Toast.LENGTH_LONG).show();
    }
    public void goUniv(View v) {
        if(sp2==0){iv.setImageResource(R.drawable.digitalkj); rsp=0; gotv.setText("경성대학교 ▶ 경남정보대학교");}
        else if(sp2==1){Toast.makeText(this, "출발지와 목적지가 동일합니다.", Toast.LENGTH_SHORT).show();gotv.setText("경성대학교");}
        else if(sp2==2){iv.setImageResource(R.drawable.gosin); rsp=2; gotv.setText("경성대학교 ▶ 고신대학교");}        else if(sp2==3){iv.setImageResource(R.drawable.daedong); rsp=3;gotv.setText("경성대학교 ▶ 대동대학교");}
        else if(sp2==4){iv.setImageResource(R.drawable.dongmyeong); rsp=4;gotv.setText("경성대학교 ▶ 동명대학교");}        else if(sp2==5){iv.setImageResource(R.drawable.dongbusan); rsp=5;gotv.setText("경성대학교 ▶ 동부산대학교");}
        else if(sp2==6){iv.setImageResource(R.drawable.dongseo); rsp=6;gotv.setText("경성대학교 ▶ 동서대학교");}        else if(sp2==7){iv.setImageResource(R.drawable.donga); rsp=7; gotv.setText("경성대학교 ▶ 동아대학교");}
        else if(sp2==8){iv.setImageResource(R.drawable.dit); rsp=8; gotv.setText("경성대학교 ▶ 동의과학대학교");}        else if(sp2==9){iv.setImageResource(R.drawable.busanyeo); rsp=9; gotv.setText("경성대학교 ▶ 동의대학교");}
        else if(sp2==10){iv.setImageResource(R.drawable.dongju); rsp=10;gotv.setText("경성대학교 ▶ 동주대학교");}        else if(sp2==11){Toast.makeText(this, "도보로 갈 수 있습니다.", Toast.LENGTH_SHORT).show();gotv.setText("경성대학교 ▶ 부경대학교");}
        else if(sp2==12){iv.setImageResource(R.drawable.buga); rsp=12;gotv.setText("경성대학교 ▶ 부산가톨릭대학교");}        else if(sp2==13){iv.setImageResource(R.drawable.kyungsang); rsp=13;gotv.setText("경성대학교 ▶ 부산경상대학교");}
        else if(sp2==14){iv.setImageResource(R.drawable.bugwagi); rsp=14;gotv.setText("경성대학교 ▶ 부산과학기술대학교");}        else if(sp2==15){iv.setImageResource(R.drawable.gyo); rsp=15;gotv.setText("경성대학교 ▶ 부산교육대학교");}
        else if(sp2==16){iv.setImageResource(R.drawable.busan); rsp=16;gotv.setText("경성대학교 ▶ 부산대학교");}        else if(sp2==17){iv.setImageResource(R.drawable.digitalkj); rsp=17;gotv.setText("경성대학교 ▶ 부산디지털대학교");}
        else if(sp2==18){iv.setImageResource(R.drawable.busanyeo); rsp=18; gotv.setText("경성대학교 ▶ 부산여자대학교");}        else if(sp2==19){iv.setImageResource(R.drawable.busanye); rsp=19; gotv.setText("경성대학교 ▶ 부산예술대학교");}
        else if(sp2==20){iv.setImageResource(R.drawable.busanforeign); rsp=20; gotv.setText("경성대학교 ▶ 부산외국어대학교");}        else if(sp2==21){iv.setImageResource(R.drawable.sinla); rsp=21;gotv.setText("경성대학교 ▶ 신라대학교");}
        else if(sp2==22){iv.setImageResource(R.drawable.yeongsan); rsp=22;gotv.setText("경성대학교 ▶ 영산대학교");}        else if(sp2==23){iv.setImageResource(R.drawable.inje); rsp=23;gotv.setText("경성대학교 ▶ 인제대학교(부산)");}
        else if(sp2==24){iv.setImageResource(R.drawable.bangtong); rsp=24;gotv.setText("경성대학교 ▶ 방송통신대학교(부산)");}        else if(sp2==25){iv.setImageResource(R.drawable.haeyang); rsp=25;gotv.setText("경성대학교 ▶ 한국해양대학교");}
        else{
            Toast.makeText(this,"다시 설정해주세요",Toast.LENGTH_SHORT).show();
        }
    }
    public void gomap(View v){
        if(rsp!=-1) {
        Intent intent = new Intent(this, univMap.class);
        intent.putExtra("map",rsp);
        startActivity(intent);}
        else{}
    }
}
