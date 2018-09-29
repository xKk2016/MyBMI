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

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();



        dollar_edit.setText(String.valueOf(bundle.getDouble("dollar_rate",6.8833)));
        euro_edit.setText(String.valueOf(bundle.getDouble("euro_rate",8.016)));
        won_edit.setText(String.valueOf(bundle.getDouble("won_rate",0.006205)));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putDouble("dollar_rate", Double.parseDouble(String.format("%.6f",Double.parseDouble(dollar_edit.getText().toString()))));
                bundle2.putDouble("euro_rate", Double.parseDouble(String.format("%.6f",Double.parseDouble(euro_edit.getText().toString()))));
                bundle2.putDouble("won_rate",Double.parseDouble(String.format("%.6f",Double.parseDouble(won_edit.getText().toString()))));

                editor.putFloat("dollar_rate", (float) Double.parseDouble(String.format("%.6f",Double.parseDouble(dollar_edit.getText().toString()))));
                editor.putFloat("euro_rate", (float) Double.parseDouble(String.format("%.6f",Double.parseDouble(euro_edit.getText().toString()))));
                editor.putFloat("won_rate", (float) Double.parseDouble(String.format("%.6f",Double.parseDouble(won_edit.getText().toString()))));
                editor.apply();
                intent.putExtras(bundle2);
                setResult(2,intent);
                finish();
            }
        });

    }
}