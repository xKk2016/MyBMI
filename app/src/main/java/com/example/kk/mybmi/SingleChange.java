package com.example.kk.mybmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class SingleChange extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "SingleChange";
    Float rate = 0.0f;
    TextView type;
    EditText count;
    TextView ans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_change);
        Bundle bundle = getIntent().getExtras();

        type = findViewById(R.id.moneytype);

        count = findViewById(R.id.count);
        ans = findViewById(R.id.asw);
        type.setText(bundle.getString("type"));
        rate = bundle.getFloat("rate");
        Log.i(TAG, "onCreate: rate="+rate);
        count.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i(TAG, "beforeTextChanged: s="+s);
        Log.i(TAG, "beforeTextChanged: start="+start);
        Log.i(TAG, "beforeTextChanged: count="+count);
        Log.i(TAG, "beforeTextChanged: after="+after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i(TAG, "onTextChanged: s="+s);
        Log.i(TAG, "onTextChanged: start="+start);
        Log.i(TAG, "onTextChanged: before="+before);
        Log.i(TAG, "onTextChanged: count="+count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i(TAG, "afterTextChanged: s="+s);

        if ("".equals(s.toString())){
            ans.setText("0");
        }else {
            ans.setText(String.valueOf(Float.parseFloat(s.toString())*rate));
        }
    }
}
