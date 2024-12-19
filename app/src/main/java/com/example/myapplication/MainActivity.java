package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText name,zone,pin;
    Button login,exit,next;
    RelativeLayout rel,settings;
    ImageView close;
    String s_name,s_zone,ip;
    Toolbar toolbar;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)){
            finish();
            return;

        }
        setContentView(R.layout.activity_main);

        Initialize();
        ip = prefs.getString("ip","");
        Actions();

    }

    private void Initialize(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Melcom Shop Audit");

        name = findViewById(R.id.u_name);
        zone = findViewById(R.id.u_zone);
        name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        zone.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        login = findViewById(R.id.login);
        exit = findViewById(R.id.exit);
        settings = findViewById(R.id.settings);
        rel = findViewById(R.id.pin_lay);
        close = findViewById(R.id.close);
        pin = findViewById(R.id.u_pins);
        next = findViewById(R.id.confirm);
        prefs = getSharedPreferences("IP", MODE_PRIVATE);
        edit = prefs.edit();

    }

    private void Actions(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ip.equals(""))
                {
                    Execute();
                }else {
                    Toast.makeText(MainActivity.this, "Please set your server ip",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rel.setVisibility(View.VISIBLE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rel.setVisibility(View.GONE);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pins = pin.getText().toString();
                if (!TextUtils.isEmpty(pins))
                {
                    if (pins.equals("12345")){

                        Intent intent = new Intent(MainActivity.this, IPActivity.class);
                        startActivity(intent);
                        pin.setText("");
                        rel.setVisibility(View.GONE);

                    }else{

                        Toast.makeText(MainActivity.this, "Incorrect pin",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Empty field is required",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void Execute(){

        s_name = name.getText().toString();
        s_zone = zone.getText().toString();

        if(!TextUtils.isEmpty(s_name) && !TextUtils.isEmpty(s_zone)){

            Intent intent = new Intent(this, StockTakeActivity.class);
            intent.putExtra("name",s_name);
            intent.putExtra("zone",s_zone);
            intent.putExtra("ip",getIp());
            startActivity(intent);
            //finish();

        }

    }

    private String getIp(){
        try{

            WifiManager wifiManager = (WifiManager) this.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null){

                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                if(dhcpInfo != null){

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ip = wifiInfo.getIpAddress();
                    return String.format(Locale.US,"%d.%d.%d.%d",
                            (ip & 0xff),
                            (ip >> 8 & 0xff),
                            (ip >> 16 & 0xff),
                            (ip >> 24 & 0xff));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    @Override
    protected void onResume() {
        super.onResume();

        ip = prefs.getString("ip","");

    }
}