package com.example.bj.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class NewActivity extends Activity {
    private String line="";
    private String lineid="";
    private String excel="";
    private String filterText="";
    private String[] lines;
    private String[] lines2;
    private String buf="";
    private int n=0;
    private int m=0;
    private EditText editText;
    private ListView list_excel;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        editText = (EditText)findViewById(R.id.editText);
        list_excel = (ListView)findViewById(R.id.list_excel);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //editText에 글자를 입력하면 발생하는 이벤트인데 입력한글자랑 listview내용이랑 비교해서 맞는 항목을 보여주는 코드
                filterText = editText.getText().toString() ;
                if (filterText.length() > 0) {
                    list_excel.setFilterText(filterText) ;
                }
                else {
                    list_excel.clearTextFilter() ;
                }
                n=1; //editText가 발생했는지 안했는지 알아보기위한 변수 값 설정
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        excel = Excel(); //excel 문자열에 lineid값이 저장된 스트링버퍼를 문자열로변환해서 저장
        list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                new Thread(new Runnable() { //여러개의 작업을 동시에 처리해야하기때문에 스레드를 생성
                    @Override
                    public void run() {
                        TextView tv = (TextView)view;
                        line = tv.getText().toString();
                        lines = excel.split(System.getProperty("line.separator")); //스트링버퍼에서 저장된 값을 라인별로 나누어서 스트링배열에 순서대로 저장
                        lines2 = buf.split(System.getProperty("line.separator")); //스트링버퍼에서 저장된 값을 라인별로 나누어서 스트링배열에 순서대로 저장
                        if(n!=0){ //n은 editText에 값을 입력을 했는지 안했는지 알아보기위해서 만든 변수이고 입력했다면 n값이 1증가되기때문에 position값을 조정해야한다.
                            for(int i=0; i<lines2.length; i++){
                                if(lines2[i].equals(line)) //입력한값과 lines2배열에 값들중 일치하는 것의 배열순서i를 알아내서 m에 저장
                                    m=i;
                            }
                            lineid = lines[m];
                        }
                        else{ //입력을 안했다면 position값은 변화없기때문에 순서대로 저장
                            lineid = lines[position];
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NewActivity.this, BuslineInfo.class);
                                intent.putExtra("linenum", line); //다음 액티비티에 필요한 값(노선 번호)
                                intent.putExtra("key", lineid); //다음 액티비티에 필요한 값(lineid)
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
        StringBuffer buffer = new StringBuffer(); //스트링버퍼에 저장하는 이유는 스트링에 연결할때 빠르고 새로운 값을 계속 넣기때문에 저장하는데도 편리하기때문
        StringBuffer buffer2 = new StringBuffer();
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("test.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int MaxColumn = 2, RowStart = 0, RowEnd = sheet.getColumn(MaxColumn - 1).length -1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
            for(int row = RowStart;row <= RowEnd;row++) {
                String excelload = sheet.getCell(ColumnStart, row).getContents(); //엑셀의 첫번째 열에서 행의값만 증가시켜서 계속 저장
                String excelload2 = sheet.getCell(ColumnStart+1, row).getContents(); //엑셀의 두번째 열에서 행의값만 증가시켜서 계속 저장
                arrayAdapter.add(excelload);
                buffer.append(excelload2); //스트링버퍼에 값을 저장
                buffer.append("\n"); //라인별로 나누는 기준을위해서 \n을 저장
                buffer2.append(excelload);
                buffer2.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            list_excel.setAdapter(arrayAdapter);
            workbook.close();
        }
        buf = buffer2.toString(); //저장한 스트링버퍼를 문자열로 변환하고 문자열에 저장
        return buffer.toString(); //문자열로 변환한 스트링버퍼를 return
    }
}