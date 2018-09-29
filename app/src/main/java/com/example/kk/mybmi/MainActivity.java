package com.example.kk.mybmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText height = findViewById(R.id.height);
        final EditText weight = findViewById(R.id.weight);
        Button caculate = findViewById(R.id.caculate);
        Button next = findViewById(R.id.next);
        final TextView result = findViewById(R.id.result);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });


        caculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (height.getText().toString().equals("")||height.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "输入不能为空或者0！", Toast.LENGTH_LONG).show();
                }
                else{
                    Double option2 = Double.parseDouble(height.getText().toString());
                    Double option1 = Double.parseDouble(weight.getText().toString());
                    if (option1!=0&&option2!=0){
                        result.setText(String.format("%.2f", option1 / (option2 * option2)));
                        Toast.makeText(MainActivity.this, "计算完成！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "输入不能为空或者0！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
