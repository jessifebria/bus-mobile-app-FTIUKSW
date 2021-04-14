package com.ftiuksw.busmobileapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SeatActivity extends AppCompatActivity {

    public String scheduleid;
    private Button btn1,btn2,btn3,btn4, btn5, btn6,btn7,btn8, btn9, btn10, btn11, btn12,btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20, btn21, btn22, btn23, btn24, btn25, btn26, btn27, btn28, btn29, btn30;
    private Button btndone, btncancel;
    private List<Button> listbutton = new ArrayList<>();
    private int seatchoosen = -1;
    private TextView txtscheduleid;
    private String nim = "672018079";
    List<String> arraylistavailseat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_seat);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn10 = findViewById(R.id.btn10);
        btn11 = findViewById(R.id.btn11);
        btn12 = findViewById(R.id.btn12);
        btn13 = findViewById(R.id.btn13);
        btn14 = findViewById(R.id.btn14);
        btn15 = findViewById(R.id.btn15);
        btn16 = findViewById(R.id.btn16);
        btn17 = findViewById(R.id.btn17);
        btn18 = findViewById(R.id.btn18);
        btn19 = findViewById(R.id.btn19);
        btn20 = findViewById(R.id.btn20);
        btn21 = findViewById(R.id.btn21);
        btn22 = findViewById(R.id.btn22);
        btn23 = findViewById(R.id.btn23);
        btn24 = findViewById(R.id.btn24);
        btn25 = findViewById(R.id.btn25);
        btn26 = findViewById(R.id.btn26);
        btn27 = findViewById(R.id.btn27);
        btn28 = findViewById(R.id.btn28);
        btn29 = findViewById(R.id.btn29);
        btn30 = findViewById(R.id.btn30);

        listbutton.add(btn1);
        listbutton.add(btn2);
        listbutton.add(btn3);
        listbutton.add(btn4);
        listbutton.add(btn5);
        listbutton.add(btn6);
        listbutton.add(btn7);
        listbutton.add(btn8);
        listbutton.add(btn9);
        listbutton.add(btn10);
        listbutton.add(btn11);
        listbutton.add(btn12);
        listbutton.add(btn13);
        listbutton.add(btn14);
        listbutton.add(btn15);
        listbutton.add(btn16);
        listbutton.add(btn17);
        listbutton.add(btn18);
        listbutton.add(btn19);
        listbutton.add(btn20);
        listbutton.add(btn21);
        listbutton.add(btn22);
        listbutton.add(btn23);
        listbutton.add(btn24);
        listbutton.add(btn25);
        listbutton.add(btn26);
        listbutton.add(btn27);
        listbutton.add(btn28);
        listbutton.add(btn29);
        listbutton.add(btn30);

        btndone = findViewById(R.id.btndone);
        btncancel = findViewById(R.id.btncancel);
        scheduleid= getIntent().getStringExtra("scheduleid");

        txtscheduleid = findViewById(R.id.scheduleid);
        txtscheduleid.setText(scheduleid);

        for(int i=0; i<30;i++){
            int finalI = i;
            listbutton.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (seatchoosen!=-1){
                        listbutton.get(seatchoosen-1).setBackgroundResource(R.drawable.bg);
                    }
                    seatchoosen = finalI + 1;
                    listbutton.get(finalI).setBackgroundResource(R.drawable.bggray);
                    System.out.println("SETACHOOSEN"+seatchoosen);
                }
            });
        }

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeatActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new savebook().execute();
                Intent i = new Intent(SeatActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        new GetSeat().execute();


    }

    private class GetSeat extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://10.0.2.2:8089/schedule/avail/"+scheduleid;
            String jsonStr = sh.makeServiceCall(url);
            System.out.println(jsonStr);
            if (jsonStr != null) {
                try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                String availseat = jsonObj.getString("AvailSeat");
                availseat = availseat.substring(1, availseat.length()-1);

                String[] listavailseat = availseat.split(",");
                    System.out.println(listavailseat);
                arraylistavailseat = Arrays.asList(listavailseat);
                System.out.println("ARRAYY" + arraylistavailseat);


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
            for(int i=0; i<30;i++){
                System.out.println(i);
                System.out.println(arraylistavailseat);
                if (!(arraylistavailseat.contains('\"'+String.valueOf(i+1)+'\"'))){
                    System.out.println("UDH ADA"+i);
                    listbutton.get(i).setEnabled(false);
                    listbutton.get(i).setBackgroundResource(R.drawable.bgdarkgrey);
                }
            }
        }
    }

    private class savebook extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://10.0.2.2:8089/booking/"+scheduleid+"/"+seatchoosen+"/"+nim;
            String result = sh.makeServiceCall(url);
            if (result != null) {
//                Toast.makeText(getApplicationContext(),"Success Booking Bus",Toast.LENGTH_LONG).show();
//                Intent i = new Intent(SeatActivity.this, UpcomingActivity.class);
//                startActivity(i);

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
    }
}



