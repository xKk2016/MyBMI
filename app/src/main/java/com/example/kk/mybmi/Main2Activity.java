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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener,Runnable{

    private double dollar_rate =  0.14442050379784;
    private double euro_rate =  0.1258155457983;
    private double won_rate =  164.10384926188;
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
        t.start();
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
            dollar_rate=bundle.getDouble("dollar_rate",0.14442050379784);
            euro_rate=bundle.getDouble("euro_rate",0.1258155457983);
            won_rate=bundle.getDouble("won_rate",164.10384926188);
        }
    }

    public double exchange(double rate, double count){
        return rate*count;
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
            bundle.putDouble("dollar_rate", dollar_rate);
            bundle.putDouble("euro_rate", euro_rate);
            bundle.putDouble("won_rate", won_rate);
            Intent intent = new Intent(this,config.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,1);
        }
    }

    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL("https://huobiduihuan.51240.com/");
            HttpURLConnection http  = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i("tag","run:html"+html);
            Message msg = handler.obtainMessage(5);
            Document doc = Jsoup.parse(html);
            Element elementDollar = doc.getElementsByAttributeValueContaining("title","United States Dollars - 美元").first();
            Element elementEuro = doc.getElementsByAttributeValueContaining("title","Euro - 欧元").first();
            Element elementWon = doc.getElementsByAttributeValueContaining("title","South Korea Won - 韩元").first();

            dollar_rate=Double.parseDouble(elementDollar.text());
            euro_rate = Double.parseDouble(elementEuro.text());
            won_rate = Double.parseDouble(elementWon.text());




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
}
