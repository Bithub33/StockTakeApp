package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveActivity extends AppCompatActivity {

    RecyclerView rec;
    List<Model> list = new ArrayList<>();
    String name,zone;
    SaveAdapter saveAdapter;
    RequestQueue requestQueue;
    EditText searchView;
    Toolbar toolbar;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        requestQueue = Volley.newRequestQueue(this);
        Initialize();
        getData();
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){

                    search();
                    return true;
                }
                return false;
            }
        });

        searchView.setOnTouchListener((v,event)->{
            if(event.getAction() == MotionEvent.ACTION_UP){
                if(event.getRawX() >= (searchView.getRight() -
                        searchView.getCompoundDrawables()[2].getBounds().width())){

                    search();
                    return true;

                }

            }
            return false;
        });

    }

    private void Initialize(){

        rec = findViewById(R.id.rec);
        searchView = findViewById(R.id.search);
        rec.setLayoutManager(new LinearLayoutManager(this));
        saveAdapter = new SaveAdapter(list,this);
        rec.setAdapter(saveAdapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Past Entries");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getData(){

        String url = getServerIp()+"/shop_REPORT.php?username="+name.
                toUpperCase()+"&rack_num="+zone.toUpperCase();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObject = response.getJSONObject(i);
                        Model model = new Model();

                        model.setName(jsonObject.getString("ITEM_NAME"));
                        model.setItem_code(jsonObject.getString("ITEM_CODE"));
                        model.setQty(jsonObject.getString("QTY"));

                        list.add(model);

                    }
                    saveAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    //throw new RuntimeException(e);
                    Toast.makeText(SaveActivity.this, e.toString(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SaveActivity.this,error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Volley error:", String.valueOf(error));

            }
        });

        jsonArrayRequest.setTag("saveTag");
        requestQueue.add(jsonArrayRequest);

    }

    private String getServerIp(){

        SharedPreferences prefs = getSharedPreferences("IP", MODE_PRIVATE);
        String ip = prefs.getString("ip","");
        String s_ip = "http://"+ip+":8080/audit/";
        return s_ip;
    }

    private void search(){
        String query = searchView.getText().toString();

        String url = getServerIp()+"/shop_REPORT.php?username="+name.
                toUpperCase()+"&rack_num="+zone.toUpperCase();

        if(!query.isEmpty()){
            JsonArrayRequest ja = new JsonArrayRequest(Request.Method.GET,url,
                    null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        list.clear();
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonObject = response.getJSONObject(i);
                            Model model = new Model();

                            if(jsonObject.getString("ITEM_CODE").equals(query) ||
                                    jsonObject.getString("QTY").equals(query)||
                                    jsonObject.getString("ITEM_NAME").contains(query)){

                                model.setName(jsonObject.getString("ITEM_NAME"));
                                model.setItem_code(jsonObject.getString("ITEM_CODE"));
                                model.setQty(jsonObject.getString("QTY"));

                                list.add(model);

                            }

                        }
                        saveAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(SaveActivity.this, e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SaveActivity.this, error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("Volley error:", String.valueOf(error));

                }
            });

            ja.setTag("searchTag");
            requestQueue.add(ja);
        }else{
            getData();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
        {
            requestQueue.cancelAll("saveTag");
            requestQueue.cancelAll("searchTag");
        }
    }
}
