package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IPActivity extends AppCompatActivity {

    private EditText ips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipactivity);

        ips = findViewById(R.id.ip);
        Button set = findViewById(R.id.set);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences prefs = getSharedPreferences("IP", MODE_PRIVATE);
        String ip = prefs.getString("ip","");
        if (!ip.isEmpty())
        {
            ips.setText(ip);
        }
        SharedPreferences.Editor edit = prefs.edit();

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = ips.getText().toString();
                if (!TextUtils.isEmpty(ip))
                {
                    if (isValidIp(ip)){

                        edit.putString("ip",ip);
                        edit.commit();
                        onBackPressed();

                    }else{

                        Toast.makeText(IPActivity.this, "Please enter a valid ip",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(IPActivity.this, "Empty field is required",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean isValidIp(String ip){

        String ipv4 =
                "^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\."+
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\."+
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\."+
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";

        return ip.matches(ipv4);

    }
}