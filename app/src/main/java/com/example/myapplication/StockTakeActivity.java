package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class StockTakeActivity extends AppCompatActivity {

    String name,zone,ip,item_val,i_code,i_name,i_barcode,shop_code,
            qtys,store_code,price,dept;
    EditText item,qty;
    Button scan,clear,save,view,back,exit;
    TextView t_name,t_zone,t_ip,item_code,item_name,item_num,t_store_code,
    t_dept,t_price;
    LinearLayout inp,item_lay;
    RequestQueue requestQueue,req;
    Toolbar toolbar;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take);

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        ip = getIntent().getStringExtra("ip");
        requestQueue = Volley.newRequestQueue(StockTakeActivity.this);
        req = Volley.newRequestQueue(this);

        Initialize();

        t_name.setText(name);
        t_zone.setText(zone);
        t_ip.setText(ip);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StockTakeActivity.this, SaveActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("zone",zone);
                startActivity(intent);

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.setText("");
                qty.setText("");
                item_code.setText("");
                item_name.setText("");
                item_num.setText("");
                t_price.setText("");
                t_dept.setText("");
                t_store_code.setText("");
                item_lay.setVisibility(View.GONE);

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
                item_lay.setVisibility(View.GONE);

                if (!item_val.isEmpty()){

                    getData(item_val);

                }else{
                    Toast.makeText(StockTakeActivity.this, "Item code is required",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qtys = qty.getText().toString();
                if (!TextUtils.isEmpty(qtys))
                {
                    if(!(Double.parseDouble(qtys) > 6)){
                        saveData();
                    }else{
                        Toast.makeText(StockTakeActivity.this, "Quantity should not exceed 6",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(StockTakeActivity.this, "please add quantity",
                            Toast.LENGTH_SHORT).show();
                }

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
        loadingBar = findViewById(R.id.loading_bar);
        item_lay = findViewById(R.id.item_lay);
        item_code = findViewById(R.id.item_code);
        item_name = findViewById(R.id.item_name);
        item_num =findViewById(R.id.item_num);
        t_store_code = findViewById(R.id.store_code);
        t_dept = findViewById(R.id.cat);
        t_price = findViewById(R.id.item_price);

        t_name = findViewById(R.id.name);
        t_zone = findViewById(R.id.zone);
        t_ip = findViewById(R.id.ip);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Stock Take Screen");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getData(String item_val){

        String url = getServerIp()+"/shop_audit.php?item_code="+item_val;
        loadingBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObject = response.getJSONObject(i);
                        i_code = jsonObject.getString("ITEM_CODE");
                        i_barcode = jsonObject.getString("BARCODE");
                        i_name = jsonObject.getString("ITEM_NAME");
                        shop_code = jsonObject.getString("SHOP_CODE");
                        price = jsonObject.getString("PRICE");
                        dept = jsonObject.getString("DEPT");
                        shop_code = jsonObject.getString("SHOP_CODE");

                        //String item_code = jsonObject.getString("ITEM_CODE");
                        item_lay.setVisibility(View.VISIBLE);
                        item_code.setText(i_code);
                        item_name.setText(i_name);
                        item_num.setText(i_barcode);
                        t_price.setText(price);
                        t_dept.setText(dept);
                        t_store_code.setText(shop_code);

                        loadingBar.setVisibility(View.GONE);
                        item_lay.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    loadingBar.setVisibility(View.GONE);
                    Toast.makeText(StockTakeActivity.this, e.toString(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.setVisibility(View.GONE);
                Toast.makeText(StockTakeActivity.this,error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Volley error:", String.valueOf(error));

            }
        });

        jsonArrayRequest.setTag("reqTag");
        requestQueue.add(jsonArrayRequest);

    }

    private void saveData(){

        String url = getServerIp()+"upload_audit_shop.php?shop_code="+shop_code+
                "&item_code="+i_code+"&qty="+qtys+"&user="+name+"&ip="+ip+"&rack="+zone;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("volleyError", response);
                Toast.makeText(StockTakeActivity.this, "Stock saved successfully",
                        Toast.LENGTH_SHORT).show();

                item.setText("");
                qty.setText("");
                item_code.setText("");
                item_name.setText("");
                item_num.setText("");
                t_price.setText("");
                t_dept.setText("");
                t_store_code.setText("");
                item_lay.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(StockTakeActivity.this,error.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        stringRequest.setTag("saveTag");
        req.add(stringRequest);

    }

    private String getServerIp(){

        SharedPreferences prefs = getSharedPreferences("IP", MODE_PRIVATE);
        String ip = prefs.getString("ip","");
        String s_ip = "http://"+ip+":8080/audit/";
        return s_ip;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
        {
            requestQueue.cancelAll("reqTag");
        }

        if(req != null)
        {
            req.cancelAll("saveTag");
        }
    }

}