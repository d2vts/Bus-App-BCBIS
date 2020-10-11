package com.example.bj.test;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class GuMap extends Activity implements OnMapReadyCallback {
    private int count=0;
    private String dongname = "";
    private String x="";
    private String y="";
    private String name="";
    private String count_s="";
    private String[] namelines;
    private String[] xlines;
    private String[] ylines;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gu_map);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        count_s = intent.getStringExtra("count");
        tv = (TextView)findViewById(R.id.text);
        tv.setText(name);
        int a = Integer.parseInt(count_s);
        x = Excel(a);
        namelines = dongname.split(System.getProperty("line.separator"));
        xlines = x.split(System.getProperty("line.separator"));
        ylines = y.split(System.getProperty("line.separator"));
        for(int i=0; i<namelines.length; i++){
            if(namelines[i].equals(name)){
                count++;
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        double[] dx = new double[count];
        double[] dy = new double[count];
        for(int i=0; i<count; i++){
            dx[i] = Double.parseDouble(xlines[i]);
            dy[i] = Double.parseDouble(ylines[i]);
        }
        for(int i=0; i<count; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(dy[i], dx[i]));
            markerOptions.title("busan");
            map.addMarker(markerOptions);
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(dy[1], dx[1])));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
    public String Excel(int n) {
        StringBuffer Namebuffer = new StringBuffer();
        StringBuffer Xbuffer = new StringBuffer();
        StringBuffer Ybuffer = new StringBuffer();
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("Alldong.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int MaxColumn = 2, RowStart = 0, RowEnd = sheet.getColumn(MaxColumn - 1).length -1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
            for(int row = RowStart;row <= RowEnd;row++) {
                String excel_name = sheet.getCell(ColumnStart+(n*3), row).getContents();
                String excel_x = sheet.getCell(ColumnStart+(1+n*3), row).getContents();
                String excel_y = sheet.getCell(ColumnStart+(2+n*3), row).getContents();
                Namebuffer.append(excel_name);
                Namebuffer.append("\n");
                Xbuffer.append(excel_x);
                Xbuffer.append("\n");
                Ybuffer.append(excel_y);
                Ybuffer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
        dongname = Namebuffer.toString();
        y = Ybuffer.toString();
        return Xbuffer.toString();
    }
}
