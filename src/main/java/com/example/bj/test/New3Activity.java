package com.example.bj.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class New3Activity extends Activity implements AdapterView.OnItemClickListener{
    private int count=0;
    private String[] s = {"강서구", "금정구", "남구", "북구", "서구",
            "중구", "동구", "동래구", "부산진구", "사하구", "사상구", "연제구", "영도구", "수영구", "해운대구", "기장군"};
    private TextView tv;
    private GridView gv;
    private ArrayAdapter adapter;
    private ListView list_excel;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new3);
        tv = (TextView) findViewById(R.id.textview);
        gv = (GridView) findViewById(R.id.gridview);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, s);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);

        list_excel = (ListView) findViewById(R.id.list_excel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                String name = "";
                String count_s = String.valueOf(count);
                TextView tv = (TextView)view;
                name = tv.getText().toString();
                Intent intent = new Intent(New3Activity.this, GuMap.class);
                intent.putExtra("name", name);
                intent.putExtra("count", count_s);
                startActivity(intent);
            }
        });
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
            count = position;
            gutv.setText("북구");
        }
        else if(tv.getText().equals("부산진구")) {
            Excel(2);
            count = position;
            gutv.setText("부산진구");
        }
        else if(tv.getText().equals("동구")) {
            Excel(3);
            count = position;
            gutv.setText("동구");
        }
        else if(tv.getText().equals("동래구")) {
            Excel(4);
            count = position;
            gutv.setText("동래구");
        }
        else if(tv.getText().equals("강서구")) {
            Excel(5);
            count = position;
            gutv.setText("강서구");
        }
        else if(tv.getText().equals("금정구")) {
            Excel(6);
            count = position;
            gutv.setText("금정구");
        }
        else if(tv.getText().equals("기장군")) {
            Excel(7);
            count = position;
            gutv.setText("기장군");
        }
        else if(tv.getText().equals("해운대구")) {
            Excel(8);
            count = position;
            gutv.setText("해운대구");
        }
        else if(tv.getText().equals("중구")) {
            Excel(9);
            count = position;
            gutv.setText("중구");
        }
        else if(tv.getText().equals("남구")) {
            Excel(10);
            count = position;
            gutv.setText("남구");
        }
        else if(tv.getText().equals("사하구")) {
            Excel(11);
            count = position;
            gutv.setText("사하구");
        }
        else if(tv.getText().equals("사상구")) {
            Excel(12);
            count = position;
            gutv.setText("사상구");
        }
        else if(tv.getText().equals("서구")) {
            Excel(13);
            count = position;
            gutv.setText("서구");
        }
        else if(tv.getText().equals("수영구")) {
            Excel(14);
            count = position;
            gutv.setText("수영구");
        }
        else if(tv.getText().equals("영도구")) {
            Excel(15);
            count = position;
            gutv.setText("영도구");
        }
        else if(tv.getText().equals("연제구")) {
            Excel(16);
            count = position;
            gutv.setText("연제구");
        }

    }
}