package com.example.kk.mybmi;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

public class RateListActivity2 extends AppCompatActivity implements Runnable {
    Thread t;
    List<Rate> list1 = new ArrayList<>();
    ListAdapter adapter;
    Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);
        ListView rateList = findViewById(R.id.RateList);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==7){
                    String str = (String) msg.obj;
                    Log.i("DollarRate","handleMessage msg = "+str);
                    ((ArrayAdapter)adapter).notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };
//        String[] list_data = {"one","two","three","four","five"};
        t = new Thread(this);
        t.start();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list1);
        rateList.setAdapter(adapter);

    }

    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL("https://huobiduihuan.51240.com/");
            HttpURLConnection http  = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
//            Log.i("tag","run:html"+html);
            Document doc = Jsoup.parse(html);
            Element elementDollar = doc.getElementsByAttributeValueContaining("title","United States Dollars - 美元").first();
            Element elementEuro = doc.getElementsByAttributeValueContaining("title","Euro - 欧元").first();
            Element elementWon = doc.getElementsByAttributeValueContaining("title","South Korea Won - 韩元").first();

            list1.add(new Rate("美元",Float.parseFloat(elementDollar.text())));
            list1.add(new Rate("欧元",Float.parseFloat(elementEuro.text())));
            list1.add(new Rate("韩元",Float.parseFloat(elementWon.text())));
            Log.i("$:",elementDollar.text());
            Message msg = handler.obtainMessage(7);
            msg.obj=elementDollar.text();
            handler.sendMessage(msg);


            Log.i("spyder","爬虫完成");
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

    class Rate{
        String type;
        Float rate;
        public Rate(){
        }
        public Rate(String type,Float rate){
            this.type=type;
            this.rate=rate;
        }

        @Override
        public String toString() {
            return "Rate{" +
                    "type='" + type + '\'' +
                    ", rate=" + rate +
                    '}';
        }
    }
}

