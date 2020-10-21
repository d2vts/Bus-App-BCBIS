package com.example.bj.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class New2Activity extends Activity {
    private ListView list_excel;
    private ArrayAdapter<String> arrayAdapter;
    private EditText editText;
    private int m=0;
    private String x="";
    private String y="";
    private String bsid="";
    private String name="";
    private String arsno="";
    private String bstopid="";
    private String compare="";
    private String[] lines;
    private String[] lines2;
    private String[] lines3;
    private int n=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
        editText = (EditText)findViewById(R.id.editText);
        list_excel = (ListView)findViewById(R.id.list_excel);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filterText = editText.getText().toString() ; //editText에 입력한 값을 문자열에 저장
                if (filterText.length() > 0) { //입력한다면 실행
                    list_excel.setFilterText(filterText) ;
                }
                else {
                    list_excel.clearTextFilter() ;
                }
                n=1; //입력이 일어났는지 안일어났는지 확인을 위한 변수 설정
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        arsno = Excel();
        list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView)view;
                        name = tv.getText().toString().trim();
                        lines = arsno.split(System.getProperty("line.separator")); //arsno값을 라인단위로 구분해서 스트링 배열에 저장
                        lines2 = bstopid.split(System.getProperty("line.separator")); //bstopid값을 라인단위로 구분해서 스트링 배열에 저장
                        lines3 = compare.split(System.getProperty("line.separator")); //버스정류장 이름을 라인단위로 구분해서 스트링 배열에 저장
                        if(n!=0){ //editText에 값을 입력하면 실행되는 부분
                            for(int i=0; i<lines3.length; i++){
                                if(lines3[i].equals(name)) {
                                    m = i;
                                    bsid = lines2[m];
                                    x = getX(name,lines[m]);
                                    y = getY(name,lines[m]);
                                    break;
                                }
                            }
                        }
                        else{ //입력값이 없다면 실행되는 부분
                            bsid = lines2[position];
                            x = getX(name,lines[position]);
                            y = getY(name,lines[position]);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(New2Activity.this, BusstopDetail.class);
                                intent.putExtra("xx",x);
                                intent.putExtra("yy",y);
                                intent.putExtra("id",bsid);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void back(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public String Excel() {
        StringBuffer buffer = new StringBuffer(); //arsno를 저장하는 스트링배열 선언
        StringBuffer buffer2 = new StringBuffer(); //bstopid를 저장하는 스트링배열 선언
        StringBuffer buffer3 = new StringBuffer(); //정류장이름을 저장하는 스트링배열 선언
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("bstopid.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int MaxColumn = 2, RowStart = 0, RowEnd = sheet.getColumn(MaxColumn - 1).length -1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
            for(int row = RowStart;row <= RowEnd;row++) {
                String excelload = sheet.getCell(ColumnStart, row).getContents(); //첫번째 열의 행값들을 읽는다
                String excelload2 = sheet.getCell(ColumnStart+1, row).getContents(); //두번째 열의 행값들을 읽는다
                String excelload3 = sheet.getCell(ColumnStart+2, row).getContents(); //세번째 열의 행값들을 읽는다
                arrayAdapter.add(excelload);
                buffer.append(excelload2);
                buffer.append("\n");
                buffer2.append(excelload3);
                buffer2.append("\n");
                buffer3.append(excelload);
                buffer3.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            list_excel.setAdapter(arrayAdapter);
            workbook.close();
        }
        bstopid = buffer2.toString();
        compare = buffer3.toString();
        return buffer.toString();
    }
    String getX(String a, String b){ //xml에서 지도좌표 X를 파싱해오는 함수
        StringBuffer buffer = new StringBuffer();
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
                        buffer.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item")) ;
                        else if(tag.equals("gpsX")){
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
    String getY(String a, String b){ //xml에서 지도좌표 Y를 파싱해오는 함수
        StringBuffer buffer = new StringBuffer();
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
                        buffer.append("파싱 시작 단계 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item")) ;
                        else if(tag.equals("gpsY")){
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