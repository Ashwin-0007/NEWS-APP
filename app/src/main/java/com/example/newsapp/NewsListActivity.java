package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public class NewsListActivity extends AppCompatActivity implements  NewsRecyclerViewAdapter.ItemClickListener {

    NewsRecyclerViewAdapter mNewsRecyclerViewAdapter;
    ArrayList<String> titlesList;
    ArrayList<String> linkList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        titlesList = (ArrayList<String>) getIntent().getSerializableExtra("title");
        linkList = (ArrayList<String>) getIntent().getSerializableExtra("link");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, titlesList,this);

        recyclerView.setAdapter(mNewsRecyclerViewAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        //call webview activity
        Intent intent = new Intent(NewsListActivity.this,NewsWebViewActivity.class);
        intent.putExtra("url", linkList.get(position));
        startActivity(intent);
    }
}