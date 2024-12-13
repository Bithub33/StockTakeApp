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

    EditText name,zone;
    Button login,exit;
    LinearLayout settings;
    RelativeLayout rel;
    ImageView close;
    String s_name,s_zone,ip;
    Toolbar toolbar;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();
        ip = getIp();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ip.equals(""))
                {
                    Execute();
                }else {
                    Toast.makeText(MainActivity.this, "Ip address could not be fetched",
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


    }

    private void Initialize(){

        name = findViewById(R.id.u_name);
        zone = findViewById(R.id.u_zone);
        login = findViewById(R.id.login);
        exit = findViewById(R.id.exit);
        settings = findViewById(R.id.settings);
        rel = findViewById(R.id.pin_lay);
        close = findViewById(R.id.close);
        //toolbar = findViewById(R.id.appbar);
        prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        edit = prefs.edit();

    }

    private void Execute(){

        s_name = name.getText().toString();
        s_zone = zone.getText().toString();

        if(!TextUtils.isEmpty(s_name) && !TextUtils.isEmpty(s_zone)){

            edit.putString("name",s_name);
            edit.putString("zone",s_zone);
            edit.commit();

            Intent intent = new Intent(this, StockTakeActivity.class);
            intent.putExtra("name",s_name);
            intent.putExtra("zone",s_zone);
            intent.putExtra("ip",ip);
            startActivity(intent);
            //finish();

        }

    }

    private String getIp(){
        try{
            /*URL url = new URL("https://google.com");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return  reader.readLine();*/

            WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null){

                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                if(dhcpInfo != null){

                    //WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ip = dhcpInfo.serverAddress;
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
}