package com.example.bj.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BuslineInfo extends Activity {
    private String s2="";
    private String s4="";
    private String key="";
    private String data="";
    private String ndata="";
    private String a="";
    private String b="";
    private String name="";
    private String parsno="";
    private TextView tv;
    private ListView list_excel;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> items;
    private String[] lines;
    private String[] lines2;
    private String[] Newlines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busline_info);
        Intent intent = getIntent();
        s2 = intent.getStringExtra("linenum");
        s4 = intent.getStringExtra("key");
        key=s4;
        tv = (TextView)findViewById(R.id.textView);
        tv.setText(s2);
        list_excel = (ListView)findViewById(R.id.list_excel);
        items = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                data= getData();
                lines = data.split(System.getProperty("line.separator"));
                lines2 = ndata.split(System.getProperty("line.separator"));
                if(lines.length==lines2.length){
                    Newlines = new String[lines2.length];
                    for(int i=0; i<lines2.length; i++){
                        Newlines[i]=lines2[i];
                    }
                }
                else{
                    int count = lines.length-lines2.length;
                    Newlines = new String[lines2.length+count];
                    for(int i=0; i<lines2.length; i++){
                        Newlines[i+1]=lines2[i];
                    }
                    Newlines[0]="";
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<lines.length; i++){
                            items.add(lines[i]);
                        }
                        arrayAdapter = new ArrayAdapter<String>(BuslineInfo.this, android.R.layout.simple_list_item_1, items);
                        list_excel.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
        list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView)view;
                        name = tv.getText().toString();
                        a = getX(lines[position],Newlines[position]);
                        b = getY(lines[position],Newlines[position]);
                        parsno=Newlines[position];
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(parsno.length()<3)
                                    Toast.makeText(BuslineInfo.this, "지원하지 않는 정류소입니다", Toast.LENGTH_SHORT).show();
                                else {
                                    Intent intent = new Intent(BuslineInfo.this, BusstopInfo.class);
                                    intent.putExtra("xx", a);
                                    intent.putExtra("yy", b);
                                    intent.putExtra("buskey", key);
                                    intent.putExtra("arsno", parsno);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void back(View v){
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }
    String getData(){
        StringBuffer buffer = new StringBuffer();
        StringBuffer arsnobuffer = new StringBuffer();
        String str = key;
        String lineID = URLEncoder.encode(str);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busInfoRoute?" +"lineid="+ lineID +"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D";
        //서비스키 인증 오류를 대비해서---- 서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {

            URL url= new URL(queryUrl);
            InputStream is= url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();

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
                        tag= xpp.getName();
                        if(tag.equals("item")) ;
                        else if(tag.equals("bstopnm")){
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("arsNo")){
                            xpp.next();
                            arsnobuffer.append(xpp.getText());
                            arsnobuffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();
                        if(tag.equals("item"))
                            break;
                }
                eventType= xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ndata = arsnobuffer.toString();
        return buffer.toString();
    }
    String getX(String a, String b){
        StringBuffer xbuffer = new StringBuffer();
        String str = a;
        String str2 = b;
        String lineID = URLEncoder.encode(str);
        String lineID2 = URLEncoder.encode(str2);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busStop?bstopnm="+lineID+"&arsno="+lineID2+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D&numOfRows=3&pageNo=1";
        //서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {

            URL url= new URL(queryUrl);
            InputStream is= url.openStream();
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();

            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        xbuffer.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();
                        if(tag.equals("item")) ;
                        else if(tag.equals("gpsX")){
                            xpp.next();
                            xbuffer.append(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();
                        if(tag.equals("item"))
                            break;
                }
                eventType= xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xbuffer.toString();
    }
    String getY(String a, String b){ // editText에 line아이디 입력시 해당 라인아이디에 해당하는 값 파싱해오는 것인듯????
        StringBuffer ybuffer = new StringBuffer();
        String str = a;
        String str2 = b;
        String lineID = URLEncoder.encode(str);
        String lineID2 = URLEncoder.encode(str2);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busStop?bstopnm="+lineID+"&arsno="+lineID2+"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D&numOfRows=3&pageNo=1";
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
                        ybuffer.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item")) ;
                        else if(tag.equals("gpsY")){
                            xpp.next();
                            ybuffer.append(xpp.getText());
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
        return ybuffer.toString();
    }
}