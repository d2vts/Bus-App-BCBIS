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
    private int count_name = 0;
    private int count_arsno = 0;
    private int name_arsno= 0;
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
        s2 = intent.getStringExtra("linenum"); //이전 액티비티에서 받은 String값을 문자열에 저장
        s4 = intent.getStringExtra("key");
        key=s4;
        tv = (TextView)findViewById(R.id.textView);
        tv.setText(s2);
        list_excel = (ListView)findViewById(R.id.list_excel);
        items = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                data= getData(); //xml을 파싱한 후 받은 문자열을 문자열 data에 저장
                lines = data.split(System.getProperty("line.separator")); //문자열을 라인별로 나눠서 스트링배열에 저장
                lines2 = ndata.split(System.getProperty("line.separator"));
                if(lines.length==lines2.length){ //버스정류장번호가 없는 경우가 있기때문에 버스정류장이름과 정류장번호의 개수가 같다면 같은 index에 해당하는 값을 그대로 새로운 배열에 저장
                    Newlines = new String[lines2.length];
                    for(int i=0; i<lines2.length; i++){
                        Newlines[i]=lines2[i];
                    }
                }
                else{
                    Newlines = new String[lines2.length+name_arsno]; //만약 다르다면 차이나는 개수만큼 새로운배열 앞의 값을 비우고 정류장이름과 번호가 매칭이되도록 새로운 배열에 저장
                    for(int i=0; i<lines2.length; i++){
                        Newlines[i+name_arsno]=lines2[i];
                    }
                    for(int j=0; j<name_arsno; j++){
                        Newlines[j] = "";
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<lines.length; i++){ //정류장이름을 ArrayList에 순서대로 저장
                            items.add(lines[i]);
                        }
                        arrayAdapter = new ArrayAdapter<String>(BuslineInfo.this, android.R.layout.simple_list_item_1, items);
                        list_excel.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
        list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() { //listview를 클릭할 때 일어나는 이벤트
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView)view;
                        name = tv.getText().toString();
                        a = getX(lines[position],Newlines[position]); //해당 정류소의 지도의 X값을 xml에서 파싱해와서 문자열 a에 저장
                        b = getY(lines[position],Newlines[position]); //해당 정류소의 지도의 Y값을 xml에서 파싱해와서 문자열 b에 저장
                        parsno=Newlines[position]; //다음 엑티비티에 필요한 클릭한 position에 해당하는 값을 문자열 parsno에 저장
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(parsno.length()<3)
                                    Toast.makeText(BuslineInfo.this, "지원하지 않는 정류소입니다", Toast.LENGTH_SHORT).show(); //만약 arsno값이 없다면 출력되는 토스트 창 설정
                                else {
                                    Intent intent = new Intent(BuslineInfo.this, BusstopInfo.class);
                                    intent.putExtra("xx", a);
                                    intent.putExtra("yy", b);
                                    intent.putExtra("buskey", key);
                                    intent.putExtra("arsno", parsno);
                                    intent.putExtra("name", name);
                                    intent.putExtra("linenum", s2);
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
        StringBuffer buffer = new StringBuffer(); //xml값을 파싱해서 문자열로 넘겨주기위한 스트링버퍼 선언
        StringBuffer arsnobuffer = new StringBuffer();
        String str = key; //파싱에 필요한 key값
        String lineID = URLEncoder.encode(str);
        String queryUrl="http://61.43.246.153/openapi-data/service/busanBIMS2/busInfoRoute?" +"lineid="+ lineID +"&serviceKey=SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D";
        //서비스키 인증 오류를 대비해서---- 서비스 인증키 : SdrLAKjBW4RbSm5NzeLOwWjE%2BAZhRUmxz%2BxBoeIwLz8w7SDQ7VZR%2BgXx7lY8rMJTpM%2ByVy9068jhDaRu9eFTUw%3D%3D
        try {

            URL url= new URL(queryUrl);
            InputStream is= url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance(); //XmlPullParser 선언
            XmlPullParser xpp= factory.newPullParser();

            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag; //xml태그비교를 위해 선언
            xpp.next(); //값을 읽은 후 다음으로 이동

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();
                        if(tag.equals("item")) ; //먼저 item태그를 들어온다
                        else if(tag.equals("arsNo")){ //arsNo태그가 있다면 들어온다
                            count_arsno++; //arsno가 있는지 없는지 정류장이름의 개수와 비교를 위해 값을 증가
                            xpp.next();
                            arsnobuffer.append(xpp.getText());
                            arsnobuffer.append("\n");
                        }
                        else if(tag.equals("bstopnm")){
                            count_name++; //arsno가 있는지 없는지 정류장번호의 개수와 비교를 위해 값을 증가
                            if(count_name==1){ //처음bstopnm태그를 만나서 들어온다
                                if(count_name != count_arsno) //처음들어왔는데 arsNo에 들어간횟수랑 다르다면(처음인 이유는 처음 차이나는 개수를 알아야 스트링배열에서 앞에 몇개를 비울지 결정가능)
                                    name_arsno = count_name - count_arsno; //차이나는 개수 = 정류장이름 개수 - 정류장번호 개수
                            }
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
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
    String getX(String a, String b){ //xml파싱에 필요한 값을 인자로 전달받는 부분
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
                        else if(tag.equals("gpsX")){ //gpsX를 통해 지도파싱에 필요한 X값을 얻는다
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
    String getY(String a, String b){
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
                        else if(tag.equals("gpsY")){ //gpsY를 통해 지도파싱에 필요한 Y값을 얻는다
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