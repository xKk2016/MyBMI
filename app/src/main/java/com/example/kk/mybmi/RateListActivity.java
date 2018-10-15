package com.example.kk.mybmi;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    private static final String TAG = "RateList";
    Handler handler;
    private ArrayList<HashMap<String,String>> listItems;
    private SimpleAdapter listItemAdapter;
    private int msgWhat = 7;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rate_list);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == msgWhat){
                    List<HashMap<String, String>> retList = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(RateListActivity.this,
                            retList,
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[] { "ItemTitle", "ItemDetail" },
                            new int[] { R.id.itemTitle, R.id.itemDetail }
                            );
                    setListAdapter(adapter);
                    Log.i("handler","reset list...");
                }
                super.handleMessage(msg);
            }
        };




        initListView();
        setListAdapter(listItemAdapter);
        getListView().setOnItemClickListener(this);

        Thread t = new Thread(this);
        t.start();
    }

    private void initListView(){
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i=0;i<10;i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("ItemTile","Rate"+i);
            map.put("ItemDetail","detail"+i);
            listItems.add(map);
        }

        listItemAdapter = new SimpleAdapter(this,
                listItems,
                R.layout.list_item,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
                );
    }

    @Override
    public void run() {
        URL url = null;
        boolean marker = false;
        List<HashMap<String,String>> rateList = new ArrayList<>();
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

            HashMap<String,String> map = new HashMap<>();
            map.put("ItemTitle","美元");
            map.put("ItemDetail",elementDollar.text());
            rateList.add(map);
            marker = true;
            Log.i("$:",elementDollar.text());



            Log.i("spyder","爬虫完成");
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        msg.what=msgWhat;
        if (marker){
            msg.arg1=1;
        }else {
            msg.arg1=0;
        }
        msg.obj=rateList;
        handler.sendMessage(msg);


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

        Bundle bundle = new Bundle();
//        Intent intent = new Intent();


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }
}
