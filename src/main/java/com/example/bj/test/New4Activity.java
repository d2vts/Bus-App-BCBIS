package com.example.bj.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class New4Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String buf="";

    String[] s = {"북구","부산진구","동구", "동래구", "강서구", "금정구", "기장군", "해운대구",
            "중구", "남구", "사하구","사상구","서구", "수영구", "영도구", "연제구"};
    TextView tv;
    GridView gv;
    ArrayAdapter adapter;
    private ListView list_excel;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new4);
        tv = (TextView) findViewById(R.id.textview);
        gv = (GridView) findViewById(R.id.gridview);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, s);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);

        list_excel = (ListView) findViewById(R.id.list_excel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    }

    public void Excel(int i) {

        //StringBuffer buffer = new StringBuffer();
        //StringBuffer buffer2 = new StringBuffer();

        Workbook workbook = null;
        Sheet sheet = null;
        try {
            arrayAdapter.clear();

            InputStream inputStream = getBaseContext().getResources().getAssets().open("dongs.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
           // String excelload = sheet.getCell(ColumnStart+1, row).getContents();
            //String excelload2 = sheet.getCell(ColumnStart+2, row).getContents();
            int MaxColumn = i-1, RowStart = 1, RowEnd = sheet.getColumn(MaxColumn).length - 1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
            for (int row = RowStart; row <= RowEnd; row++) {
                String excelload = sheet.getCell(ColumnStart+i-1, row).getContents();
                arrayAdapter.add(excelload);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            list_excel.setAdapter(arrayAdapter);
            workbook.close();
        }
        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView)view;
        TextView gutv = (TextView)findViewById(R.id.textview);
        if(tv.getText().equals("북구")) {
            Excel(1);
            gutv.setText("북구");
        }
        else if(tv.getText().equals("부산진구")) {
            Excel(2);
            gutv.setText("부산진구");
        }
        else if(tv.getText().equals("동구")) {
            Excel(3);
            gutv.setText("동구");
        }
        else if(tv.getText().equals("동래구")) {
            Excel(4);
            gutv.setText("동래구");
        }
        else if(tv.getText().equals("강서구")) {
            Excel(5);
            gutv.setText("강서구");
        }
        else if(tv.getText().equals("금정구")) {
            Excel(6);
            gutv.setText("금정구");
        }
        else if(tv.getText().equals("기장군")) {
            Excel(7);
            gutv.setText("기장군");
        }
        else if(tv.getText().equals("해운대구")) {
            Excel(8);
            gutv.setText("해운대구");
        }
        else if(tv.getText().equals("중구")) {
            Excel(9);
            gutv.setText("중구");
        }
        else if(tv.getText().equals("남구")) {
            Excel(10);
            gutv.setText("남구");
        }
        else if(tv.getText().equals("사하구")) {
            Excel(11);
            gutv.setText("사하구");
        }
        else if(tv.getText().equals("사상구")) {
            Excel(12);
            gutv.setText("사상구");
        }
        else if(tv.getText().equals("서구")) {
            Excel(13);
            gutv.setText("서구");
        }
        else if(tv.getText().equals("수영구")) {
            Excel(14);
            gutv.setText("수영구");
        }
        else if(tv.getText().equals("영도구")) {
            Excel(15);
            gutv.setText("영도구");
        }
        else if(tv.getText().equals("연제구")) {
            Excel(16);
            gutv.setText("연제구");
        }

    }
}
