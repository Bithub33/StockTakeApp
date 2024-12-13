package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockTakeActivity extends AppCompatActivity {

    String name,zone,ip,item_val;
    EditText item,qty;
    Button scan,clear,save,view,back,exit;
    TextView t_name,t_zone,t_ip,item_code,item_name,item_num;
    LinearLayout inp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take);

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        ip = getIntent().getStringExtra("ip");

        Initialize();

        t_name.setText(name);
        t_zone.setText(zone);
        t_ip.setText(ip);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StockTakeActivity.this, SaveActivity.class);
                startActivity(intent);

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setText("");
                qty.setText("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item_val = item.getText().toString();
                getData(item_val);

            }
        });


    }

    private void Initialize(){

        item = findViewById(R.id.item);
        qty = findViewById(R.id.qty);

        scan = findViewById(R.id.scan);
        clear =findViewById(R.id.clear);
        save = findViewById(R.id.save);
        view = findViewById(R.id.view);
        back = findViewById(R.id.back);
        exit = findViewById(R.id.exit);
        inp = findViewById(R.id.inp);
        item_code = findViewById(R.id.item_code);
        item_name = findViewById(R.id.item_name);
        item_num =findViewById(R.id.item_num);

        t_name = findViewById(R.id.name);
        t_zone = findViewById(R.id.zone);
        t_ip = findViewById(R.id.ip);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getData(String item_val){

        String url = "http://10.10.0.180:8080/audit/shop_audit.php?item_code="+item_val;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String i_code = jsonObject.getString("ITEM_CODE");
                        String i_barcode = jsonObject.getString("BARCODE");
                        String i_name = jsonObject.getString("ITEM_NAME");
                        //String item_code = jsonObject.getString("ITEM_CODE");
                        inp.setVisibility(View.VISIBLE);
                        item_code.setText(i_code);
                        item_name.setText(i_name);
                        item_num.setText(i_barcode);

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(StockTakeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StockTakeActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }
}