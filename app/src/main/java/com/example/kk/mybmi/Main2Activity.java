package com.example.kk.mybmi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,Runnable{

    private float dollar_rate;
    private float euro_rate;
    private float won_rate;
    Handler handler;
    Thread t;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button dollar = findViewById(R.id.dollar);
        Button euro = findViewById(R.id.euro);
        Button won = findViewById(R.id.won);
        Button config = findViewById(R.id.config);
        dollar.setOnClickListener(this);
        euro.setOnClickListener(this);
        won.setOnClickListener(this);
        config.setOnClickListener(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){
                    String str = (String) msg.obj;
                    Log.i("DollarRate","handleMessage msg = "+str);
                }
                super.handleMessage(msg);
            }
        };

        t = new Thread(this);
        Calendar calendar = Calendar.getInstance();
        Log.i("日期",calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+"月"+calendar.get(Calendar.DATE)+"日");


        SharedPreferences sharedPreferencesDate = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editorDate = sharedPreferencesDate.edit();
        dollar_rate=sharedPreferencesDate.getFloat("dollar_rate",0.144420f);
        euro_rate = sharedPreferencesDate.getFloat("euro_rate",0.125815f);
        won_rate = sharedPreferencesDate.getFloat("won_rate",164.103849f);

        if (sharedPreferencesDate.getInt("year",1970)==calendar.get(Calendar.YEAR)&&
                sharedPreferencesDate.getInt("month",0)==calendar.get(Calendar.MONTH)&&
                sharedPreferencesDate.getInt("day",1)==calendar.get(Calendar.DATE)){
        }else {
            t.start();
            editorDate.putInt("year",calendar.get(Calendar.YEAR));
            editorDate.putInt("month",calendar.get(Calendar.MONTH));
            editorDate.putInt("day",calendar.get(Calendar.DATE));
            editorDate.apply();
        }
        Thread t1 = new Thread(this){
            @Override
            public void run() {
                Log.i("tag2","this is thread2!");
            }
        };
        t1.start();



//        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
//        dollar_rate = sharedPreferences.getFloat("dollar_rate", (float) 0.14442050379784);
//        euro_rate = sharedPreferences.getFloat("euro_rate", (float) 0.1258155457983);
//        won_rate = sharedPreferences.getFloat("won_rate", (float) 164.10384926188);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bundle bundle = data.getExtras();
        if(requestCode==1&&resultCode==2){
            dollar_rate=bundle.getFloat("dollar_rate",0.144420f);
            euro_rate=bundle.getFloat("euro_rate",0.125815f);
            won_rate=bundle.getFloat("won_rate",164.103849f);
            Log.i("save","手动更新完成！");
        }
    }

    public float exchange(Float rate, double count){
        return (float) (rate*count);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dollar){
            TextView rmb = findViewById(R.id.rmb);
            EditText count = findViewById(R.id.count);
            rmb.setText(String.valueOf(exchange(dollar_rate,Double.parseDouble(count.getText().toString()))));
        }
        if(v.getId()==R.id.euro){
            TextView rmb = findViewById(R.id.rmb);
            EditText count = findViewById(R.id.count);
            rmb.setText(String.valueOf(exchange(euro_rate,Double.parseDouble(count.getText().toString()))));
        }
        if(v.getId()==R.id.won){
            TextView rmb = findViewById(R.id.rmb);
            EditText count = findViewById(R.id.count);
            rmb.setText(String.valueOf(exchange(won_rate,Double.parseDouble(count.getText().toString()))));
        }
        if (v.getId()==R.id.config){
            Bundle bundle = new Bundle();
            bundle.putFloat("dollar_rate", dollar_rate);
            bundle.putFloat("euro_rate", euro_rate);
            bundle.putFloat("won_rate", won_rate);
            Intent intent = new Intent(this,config.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,1);
        }
    }

    @Override
    public void run() {
        Log.i("spyder","爬虫启动");
        SharedPreferences sharedPreferencesRate = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editorRate = sharedPreferencesRate.edit();
        URL url = null;
        try {
            url = new URL("https://huobiduihuan.51240.com/");
            HttpURLConnection http  = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
//            Log.i("tag","run:html"+html);
            Message msg = handler.obtainMessage(5);
            Document doc = Jsoup.parse(html);
            Element elementDollar = doc.getElementsByAttributeValueContaining("title","United States Dollars - 美元").first();
            Element elementEuro = doc.getElementsByAttributeValueContaining("title","Euro - 欧元").first();
            Element elementWon = doc.getElementsByAttributeValueContaining("title","South Korea Won - 韩元").first();

            dollar_rate=Float.parseFloat(elementDollar.text());
            euro_rate = Float.parseFloat(elementEuro.text());
            won_rate = Float.parseFloat(elementWon.text());

            editorRate.putFloat("dollar_rate",dollar_rate);
            editorRate.putFloat("euro_rate",euro_rate);
            editorRate.putFloat("won_rate",won_rate);

            Log.i("spyder","爬虫完成");




            msg.obj=elementDollar.text();
            handler.sendMessage(msg);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"utf-8");
        while (true){
            int rsz = in.read(buffer,0,buffer.length);
            if (rsz<0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
