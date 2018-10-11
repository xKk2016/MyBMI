package com.example.kk.mybmi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class config extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        final EditText dollar_edit = findViewById(R.id.dollar_config);
        final EditText euro_edit = findViewById(R.id.euro_config);
        final EditText won_edit = findViewById(R.id.won_config);
        Button save = findViewById(R.id.save);
        Button seeRate = findViewById(R.id.seeRate);


        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();



        dollar_edit.setText(String.valueOf(bundle.getFloat("dollar_rate",0.144420f)));
        euro_edit.setText(String.valueOf(bundle.getFloat("euro_rate",0.125815f)));
        won_edit.setText(String.valueOf(bundle.getFloat("won_rate",164.103849f)));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putFloat("dollar_rate", Float.parseFloat(dollar_edit.getText().toString()));
                bundle2.putFloat("euro_rate", Float.parseFloat(euro_edit.getText().toString()));
                bundle2.putFloat("won_rate",Float.parseFloat(won_edit.getText().toString()));

                editor.putFloat("dollar_rate", Float.parseFloat(dollar_edit.getText().toString()));
                editor.putFloat("euro_rate", Float.parseFloat(euro_edit.getText().toString()));
                editor.putFloat("won_rate", Float.parseFloat(won_edit.getText().toString()));
                editor.apply();
                intent.putExtras(bundle2);
                setResult(2,intent);
                finish();
            }
        });
        seeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(config.this,RateListActivity2.class);
                startActivity(intent1);
            }
        });

    }
}
