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
                filterText = editText.getText().toString() ;
                if (filterText.length() > 0) {
                    list_excel.setFilterText(filterText) ;
                }
                else {
                    list_excel.clearTextFilter() ;
                }
                n=1;
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        excel = Excel();
        list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView)view;
                        line = tv.getText().toString();
                        lines = excel.split(System.getProperty("line.separator"));
                        lines2 = buf.split(System.getProperty("line.separator"));
                        if(n!=0){
                            for(int i=0; i<lines2.length; i++){
                                if(lines2[i].equals(line))
                                    m=i;
                            }
                            lineid = lines[m];
                        }
                        else{
                            lineid = lines[position];
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NewActivity.this, BuslineInfo.class);
                                intent.putExtra("linenum", line);
                                intent.putExtra("key", lineid);
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
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("test.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int MaxColumn = 2, RowStart = 0, RowEnd = sheet.getColumn(MaxColumn - 1).length -1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
            for(int row = RowStart;row <= RowEnd;row++) {
                String excelload = sheet.getCell(ColumnStart, row).getContents();
                String excelload2 = sheet.getCell(ColumnStart+1, row).getContents();
                arrayAdapter.add(excelload);
                buffer.append(excelload2);
                buffer.append("\n");
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
        buf = buffer2.toString();
        return buffer.toString();
    }
}
