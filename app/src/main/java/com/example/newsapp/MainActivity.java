package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titlesList;
    ArrayList<String> linkList;
    DBHelper mDBHelper;

    public void OnGetLatestNewsClick(View view) {
        DownloadNewsTask downloadNewsTask = new DownloadNewsTask();


        downloadNewsTask.execute("https://newsdata.io/api/1/news?apikey=pub_147277cc393e219ef18a46943dba3f2c55229&language=fr,en");
        callListActivity();
        updateListWithData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titlesList = new ArrayList<>();
        linkList = new ArrayList<>();
        mDBHelper = new DBHelper(this);
    }

    public class DownloadNewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while(data != -1) {
                    char ch = (char) data;
                    result += ch;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return  null;
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // JSON parsing code
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String totalResults = jsonObject.getString("totalResults");
                String results = jsonObject.getString("results");

                Log.i("result status", status);
                Log.i("result total", totalResults);
               // Log.i("result results", results);

                JSONArray resultArray = new JSONArray(results);
                for(int i = 0; i<resultArray.length(); i++) {
                    JSONObject jsonPart = resultArray.getJSONObject(i);

                    String title = jsonPart.getString("title");
                    String link = jsonPart.getString("link");
                    long id = mDBHelper.insertData(title,link);
                    if( id <= 0) {
                        Log.i("db","Insertion unsuccessful");
                    } else {
                        Log.i("db","Insertion successful");
                    }
                    //titlesList.add(jsonPart.getString("title"));
                    //Insert title and link in database


                    
                    Log.i("results title",jsonPart.getString("title"));
                    Log.i("result image", jsonPart.getString("image_url"));
                }

                callListActivity();
                updateListWithData();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateListWithData() {
        Cursor cursor = mDBHelper.getCursorForData();
        while(cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(mDBHelper.ID));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(mDBHelper.TITLE));
            titlesList.add(title);
            @SuppressLint("Range") String link = cursor.getString(cursor.getColumnIndex(mDBHelper.LINK));
            linkList.add(link);
        }
    }


    private void callListActivity() {
        Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
        intent.putExtra("title",titlesList);
        intent.putExtra("link",linkList);
        startActivity(intent);
    }
}