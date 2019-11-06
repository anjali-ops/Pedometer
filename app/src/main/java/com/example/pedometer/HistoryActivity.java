package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    public ArrayList<HistoryItems> mHistoryList;
    private RecyclerView mRecylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        loadHistoryData();
        buildRecylerview();

    }

    public void loadHistoryData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("HistoryItemsList",null);
        Type type = new TypeToken<ArrayList<HistoryItems>>() {}.getType();
        mHistoryList = gson.fromJson(json, type);
        if(mHistoryList == null){
            mHistoryList = new ArrayList<>();
        }
    }

    public void buildRecylerview(){
        mRecylerView = findViewById(R.id.recyclerView);
        //mRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HistoryAdapter(mHistoryList);

        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(mAdapter);
    }
}
