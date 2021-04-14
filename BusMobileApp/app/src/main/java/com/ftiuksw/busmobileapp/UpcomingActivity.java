package com.ftiuksw.busmobileapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpcomingActivity<adapter> extends AppCompatActivity {

    private ListView lv;
    private ImageView btnhome, btnupcoming, btndone;
    public String route, nim;
    ArrayList<HashMap<String, String>> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        btnhome = findViewById(R.id.btnhome);
        btnupcoming = findViewById(R.id.btnupcoming);
        btndone = findViewById(R.id.btndone);

        btndone.setBackgroundResource(R.drawable.bgbtnroute);
        btnhome.setBackgroundResource(R.drawable.bgbtnroute);
        btnupcoming.setBackgroundResource(R.drawable.bggray);
        bookList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        nim = "672018079";

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpcomingActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpcomingActivity.this, DoneActivity.class);
                startActivity(i);
            }
        });
        new GetBook().execute();


    }

    private class GetBook extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://10.0.2.2:8089/booking/upcoming/"+nim;
            String jsonStr = sh.makeServiceCall(url);
            System.out.println("CALLES!!!!!!!!!!!!!!!!!!");
            System.out.println(jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray buses = jsonObj.getJSONArray("BookList");

                    // looping through All Contacts
                    for (int i = 0; i < buses.length(); i++) {
                        JSONObject c = buses.getJSONObject(i);
                        String datetime = c.getString("datetime");
                        String seat = c.getString("seat");
                        String scheduleid = c.getString("schedule_id");

                        String[] datetimeparse = datetime.split("T");
                        datetime = "Booked in : " + datetimeparse[0].substring(8,10) + "-" +datetimeparse[0].substring(5,7) + "-" + datetimeparse[0].substring(0,4) + "  " + datetimeparse[1].substring(0,8);

                        String urlgetschedule = "http://10.0.2.2:8089/schedule/"+scheduleid;
                        String jsonschedule = sh.makeServiceCall(urlgetschedule);
                        System.out.println(jsonschedule);
                        JSONObject obj = new JSONObject(jsonschedule);
                        JSONArray schedules = obj.getJSONArray("BusList");
                        JSONObject schedule = schedules.getJSONObject(0);
                        String from = schedule.getString("from");
                        String destination = schedule.getString("destination");
                        String arrivaltime = schedule.getString("arrivaltime").substring(0,5);
                        String departuretime = schedule.getString("departuretime").substring(0,5);

                        HashMap<String, String> book = new HashMap<>();

                        // adding each child node to HashMap key => value
                        book.put("id", scheduleid);
                        book.put("from", from);
                        book.put("destination", destination);
                        book.put("arrivaltime", arrivaltime);
                        book.put("departuretime", departuretime);
                        book.put("seat", seat);
                        book.put("datetime", datetime);
                        System.out.println("BOOK "+ book);
                        bookList.add(book);
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
            ListAdapter adapter = new SimpleAdapter(UpcomingActivity.this, bookList,
                    R.layout.list_bus_history, new String[]{ "id","from", "destination", "arrivaltime", "departuretime", "seat", "datetime"},
                    new int[]{R.id.id, R.id.from, R.id.destination, R.id.arrivaltime, R.id.departuretime, R.id.seat, R.id.datebook});
            lv.setAdapter(adapter);


        }
    }



    }

