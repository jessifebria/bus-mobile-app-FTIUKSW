package com.ftiuksw.busmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity<adapter> extends AppCompatActivity {

    private ListView lv;
    private Button btntofti, btntouksw, btnbook;
    private ImageView btnhome, btnupcoming, btndone;
    public String route, nim;
    ArrayList<HashMap<String, String>> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btntofti = findViewById(R.id.btntofti);
        btntouksw = findViewById(R.id.btntouksw);
        btnhome = findViewById(R.id.btnhome);
        btnupcoming = findViewById(R.id.btnupcoming);
        btndone = findViewById(R.id.btndone);

        btndone.setBackgroundResource(R.drawable.bgbtnroute);
        btnupcoming.setBackgroundResource(R.drawable.bgbtnroute);
        btnhome.setBackgroundResource(R.drawable.bggray);
        btntofti.setBackgroundResource(R.drawable.bggray);
        busList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        nim = "672018079";
        route = "tofti";

        btnupcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UpcomingActivity.class);
                startActivity(i);
            }
        });

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DoneActivity.class);
                startActivity(i);
            }
        });


        btntofti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btntofti.setBackgroundResource(R.drawable.bggray);
                btntouksw.setBackgroundResource(R.drawable.bgbtnroute);
                route = "tofti";
                busList.clear();
                new GetBus().execute();
            }
        });

        btntouksw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btntouksw.setBackgroundResource(R.drawable.bggray);
                btntofti.setBackgroundResource(R.drawable.bgbtnroute);
                route = "touksw";
                busList.clear();
                new GetBus().execute();
            }
        });

        new GetBus().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                System.out.println(busList.get(myItemInt).get("id"));
                Intent i = new Intent(MainActivity.this, SeatActivity.class);
                i.putExtra("scheduleid",busList.get(myItemInt).get("id") );
                startActivity(i);
            }
        });

    }

    private class GetBus extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://10.0.2.2:8089/schedule/"+route+"/"+nim;
            String jsonStr = sh.makeServiceCall(url);
            System.out.println("CALLES!!!!!!!!!!!!!!!!!!");
            System.out.println("http://10.0.2.2:8089/schedule/"+route+"/"+nim);
            System.out.println(jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray buses = jsonObj.getJSONArray("BusList");

                    // looping through All Contacts
                    for (int i = 0; i < buses.length(); i++) {
                        JSONObject c = buses.getJSONObject(i);
                        String id = c.getString("id");
                        String from = c.getString("from");
                        String destination = c.getString("destination");
                        String arrivaltime = c.getString("arrivaltime").substring(0,5);
                        String departuretime = c.getString("departuretime").substring(0,5);

                        String urlgetcapacity = "http://10.0.2.2:8089//schedule/avail/count/"+id;
                        String jsoncapacity = sh.makeServiceCall(urlgetcapacity);
                        System.out.println("JSONCAPACITY"+jsoncapacity);
                        String capacity = jsoncapacity;

                        HashMap<String, String> bus = new HashMap<>();

                        // adding each child node to HashMap key => value
                        bus.put("id", id);
                        bus.put("from", from);
                        bus.put("destination", destination);
                        bus.put("arrivaltime", arrivaltime);
                        bus.put("departuretime", departuretime);
                        bus.put("capacity", capacity);
                        busList.add(bus);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "ERROR",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "ERROR",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println("LISTADAPTER CHANGED!!!!!");
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, busList,
                    R.layout.list_bus_home, new String[]{ "id","from", "destination", "arrivaltime", "departuretime", "capacity"},
                    new int[]{R.id.id, R.id.from, R.id.destination, R.id.arrivaltime, R.id.departuretime, R.id.capacity});
            lv.setAdapter(adapter);


        }
    }



    }

