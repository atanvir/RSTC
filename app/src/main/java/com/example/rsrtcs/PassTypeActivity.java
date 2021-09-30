package com.example.rsrtcs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PassTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {  //implements AdapterView.OnItemSelectedListener

    RadioGroup radioGroup1;
    RadioButton distance_pass, fixedPass;
    Spinner mPassHolder,mPassType,mPassValidity,mFromStop,mTillStop,mBusType;
    LinearLayout dist_ll,fix_ll;

    List<String> list1 = new ArrayList<String>();
    List<String> list11 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    List<String> list22 = new ArrayList<String>();
    List<String> list3 = new ArrayList<String>();
    List<String> list33 = new ArrayList<String>();

    TextView tv_month;
    String item;

    String [] depo = {};

    StringBuilder fromStop = new StringBuilder();
    StringBuilder tillStop = new StringBuilder();
    StringBuilder busType = new StringBuilder();
    String dataParsed1 = "";
    String dataParsed2 = "";
    String dataParsed3 = "";

    AutoCompleteTextView textView;
    EditText etFromStop,etTilllStop;
    String enterFromStop,enterTillStop;

    Button searchRoute,btnfrmStop,btnTillStop;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    String mm1,mm2,mm3;
    String mm = null;

    String selected1,selected2,selected3;
    String fs,ts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_type);

     //   radioGroup1 = findViewById(R.id.radio_group);
//        distance_pass = findViewById(R.id.distance_pass);
//        fixedPass = findViewById(R.id.fixed_pass);

//        dist_ll = findViewById(R.id.ll_visible);
//        fix_ll = findViewById(R.id.con_detail);
        tv_month = findViewById(R.id.tv_pass_validity);
        etFromStop = findViewById(R.id.et_from_stop);
        etTilllStop = findViewById(R.id.et_till_stop);
        searchRoute = findViewById(R.id.search_route);
        btnfrmStop = findViewById(R.id.from_stop);
        btnTillStop = findViewById(R.id.till_stop);
      //  textView = findViewById(R.id.tv_from_stop);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        mPassHolder = (Spinner)findViewById(R.id.pass_holder_spinner);
        mPassType = (Spinner)findViewById(R.id.pass_type_spinner);
       // mFromStop = (Spinner)findViewById(R.id.spinner_from_stop);
       // mTillStop = (Spinner)findViewById(R.id.spinner_till_stop);
        mBusType = (Spinner)findViewById(R.id.spinner_bus_type);
    //    ll = findViewById(R.id.linear);

//        int radio_id = radioGroup1.getCheckedRadioButtonId();
//
//        if (radio_id == R.id.distance_pass){
//            ll.setVisibility(View.VISIBLE);
//        }else if (radio_id == R.id.fixed_pass){
//            ll.setVisibility(View.INVISIBLE);
//        }else {
//
//        }


//        mSpinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String gen = parent.getItemAtPosition(position).toString();
//                mSpinn.setTag(gen);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        mPassHolder.setOnItemSelectedListener(this);
        mPassType.setOnItemSelectedListener(this);
     //   mFromStop.setOnItemSelectedListener(this);
      //  mTillStop.setOnItemSelectedListener(this);
        mBusType.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> passHolderAdapter = ArrayAdapter.createFromResource(this,R.array.pass_holder, android.R.layout.simple_spinner_item);
        passHolderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPassHolder.setAdapter(passHolderAdapter);

        ArrayAdapter<CharSequence> passTypeAdapter = ArrayAdapter.createFromResource(this,R.array.pass_type, android.R.layout.simple_spinner_item);
        passTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPassType.setAdapter(passTypeAdapter);


      //  postStopName();
      //  postTillStop();
        getBusType();


        btnfrmStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterFromStop = etFromStop.getText().toString();
                if (enterFromStop.isEmpty()){
                    Toast.makeText(PassTypeActivity.this, "Please Enter From Stop!", Toast.LENGTH_SHORT).show();
                }
                postStopName(enterFromStop);
            }
        });

        btnTillStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterTillStop = etTilllStop.getText().toString();
                if (enterTillStop.isEmpty()){
                    Toast.makeText(PassTypeActivity.this, "Please Enter Till Stop!", Toast.LENGTH_SHORT).show();
                }
                postTillStop(enterTillStop);
            }
        });


        searchRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (mPassHolder.getSelectedItemPosition() == 0){
//                    Toast.makeText(PassTypeActivity.this, "Please Select PassHolder!", Toast.LENGTH_SHORT).show();
//                    // titleSpinner.getSelectedItemPosition(0).e
//                    mPassHolder.requestFocus();
//                    return;
//                }
//                if (mPassType.getSelectedItemPosition() == 0){
//                    Toast.makeText(PassTypeActivity.this, "Please Select Pass Type!", Toast.LENGTH_SHORT).show();
//                    // titleSpinner.getSelectedItemPosition(0).e
//                    mPassType.requestFocus();
//                    return;
//                }
//                if (mFromStop.getSelectedItemPosition() == 0){
//                    Toast.makeText(PassTypeActivity.this, "Please Select From Stop!", Toast.LENGTH_SHORT).show();
//                    // titleSpinner.getSelectedItemPosition(0).e
//                    mFromStop.requestFocus();
//                    return;
//                }
//                if (mTillStop.getSelectedItemPosition() == 0){
//                    Toast.makeText(PassTypeActivity.this, "Please Select Till Stop!", Toast.LENGTH_SHORT).show();
//                    // titleSpinner.getSelectedItemPosition(0).e
//                    mTillStop.requestFocus();
//                    return;
//                }
//                if (mBusType.getSelectedItemPosition() == 0){
//                    Toast.makeText(PassTypeActivity.this, "Please Select Bus Type!", Toast.LENGTH_SHORT).show();
//                    // titleSpinner.getSelectedItemPosition(0).e
//                    mBusType.requestFocus();
//                    return;
//                }


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("mm", String.valueOf(mm));
                editor.putString("selected1", fs);
                editor.putString("selected2", ts);
                editor.putString("selected3", selected3);
//                editor.putString("mm2", String.valueOf(mm2));
//                editor.putString("mm3", String.valueOf(mm3));
                editor.commit();

                Intent intent = new Intent(PassTypeActivity.this,SelectRouteActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String pos = parent.getItemAtPosition(position).toString();
//        int pp = Integer.parseInt(pos);


//        String gen = parent.getItemAtPosition(0).toString();
//         item = parent.getItemAtPosition(1).toString();
//        mPassHolder.setTag(gen);

        switch (parent.getId()){
            case R.id.pass_holder_spinner :
                break;

            case R.id.pass_type_spinner :

                if (position == 1){
               // if (parent.getItemAtPosition(1).equals("Monthly")){
                    mm = "30D";
                  //  tv_month.setText("30D");
                    tv_month.setText(mm);
                  //  mm1 = 30;
                  //  mm1 = String.valueOf(tv_month);
                    Log.e("month : ", mm);
                    break;
                }else if (position == 2){
                    mm = "60D";
                  //  tv_month.setText("60D");
                    tv_month.setText(mm);
                   // mm2 = 60;
                    break;
                }else if (position == 3) {
                    mm = "90D";
                   // tv_month.setText("90D");
                    tv_month.setText(mm);
                  //  mm3 = 90;
                    break;
                }

//            case R.id.spinner_from_stop :
//                 selected1 = parent.getItemAtPosition(position).toString();
//                Log.e("selectedFromStop = "+ "", selected1);
//                break;

//            case R.id.spinner_till_stop :
//                 selected2 = parent.getItemAtPosition(position).toString();
//                Log.e("selectedTillStop = "+ "", selected2);
//                break;

            case R.id.spinner_bus_type :
                 selected3 = parent.getItemAtPosition(position).toString();
                Log.e("selectedBusType = "+ "", selected3);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void postStopName(String enterFromStop) {//String enterFromStop

        String ss = enterFromStop;
        Log.e("enterFromStop= "+ "", enterFromStop);

        URL url = null;
        try {
            url = new URL("https://rsrtcrfidsystem.co.in/RFIDAPI/stopName");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("stopName",enterFromStop )); //enterFromStop
//            params.add(new BasicNameValuePair("tillStop", "SKR"));
//            params.add(new BasicNameValuePair("busType", "EXP"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getRoute(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    fromStop.append(line);
                  //  textView.setText(output);
                    dataParsed1 = dataParsed1 + fromStop;
                    Log.e("fromStop= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed1);

                list1.add("Select Start Stop");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    list1.add(object.getString("busStopName"));
                    list11.add(object.getString("busStopCode"));

                     fs = object.getString("busStopName");

               //     Log.e("arrayList = "+ "", ""+list);
                }
                etFromStop.setText(fs);
                Log.e("fromStop = "+ "", ""+list1);
                Log.e("frombusStopCode = "+ "", ""+list11);
                Log.e("fs = "+ "", ""+fs);
                String [] stopname = list1.toArray(new String[0]);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, list);
//                textView.setAdapter(adapter);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, list1);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mFromStop.setAdapter(adapter);

            }
            else {
                Log.e("responceJKT= "+ "", "");
            }

            conn.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getRoute(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append("{");
        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append(",");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append(":");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

//          Log.e("responce= "+ "", result.toString());
        }
        result.append("}");
        Log.e("result responce= "+ "", result.toString());
        JSONObject obj = new JSONObject(result.toString());
        Log.d("Json Parameter : ", obj.toString());

//        return JSON.stringify(result.toString());
//        return "{\"stopName\":\"jai\"}";
        return obj.toString();

    }

    private void postTillStop(String enterTillStop) {

        URL url = null;
        try {
            url = new URL("https://rsrtcrfidsystem.co.in/RFIDAPI/stopName");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("stopName",enterTillStop ));//"SIK"
//            params.add(new BasicNameValuePair("tillStop", "SKR"));
//            params.add(new BasicNameValuePair("busType", "EXP"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getTillStop(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    tillStop.append(line);
                    //  textView.setText(output);
                    dataParsed2 = dataParsed2 + tillStop;
                    Log.e("tillStop= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed2);

               // list2.add("Select End Stop");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    list2.add(object.getString("busStopName"));
                    list22.add(object.getString("busStopCode"));

                    ts = object.getString("busStopName");
                    break;

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                String[] aa = list1.toArray(new String[0]);
                String cc = aa[0];
                Log.e("cc = "+ "", ""+cc);

                etTilllStop.setText(ts);
                Log.e("tillStop = "+ "", ""+list2);
                Log.e("tillbusStopCode = "+ "", ""+list22);
                Log.e("tt = "+ "", ""+ts);
                String [] stopname = list2.toArray(new String[0]);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, list);
//                textView.setAdapter(adapter);

//                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, list2);
//                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mTillStop.setAdapter(adapter2);

            }
            else {
                Log.e("responceJKT= "+ "", "");
            }

            conn.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getTillStop(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append("{");
        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append(",");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append(":");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

//          Log.e("responce= "+ "", result.toString());
        }
        result.append("}");
        Log.e("result responce= "+ "", result.toString());
        JSONObject obj = new JSONObject(result.toString());
        Log.d("Json Parameter : ", obj.toString());

//        return JSON.stringify(result.toString());
//        return "{\"stopName\":\"jai\"}";
        return obj.toString();

    }

    private void getBusType() {

        URL url = null;
        try {
            url = new URL("https://rsrtcrfidsystem.co.in/RFIDAPI/busType");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    busType.append(line);
                    //  textView.setText(output);
                    dataParsed3 = dataParsed3 + busType;
                    Log.e("busType= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed3);

                list3.add("Select Bus Type");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    list3.add(object.getString("busTypeName"));
                    list33.add(object.getString("busTypeCd"));

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                Log.e("busType = "+ "", ""+list3);
                Log.e("busTypeCdEXp = "+ "", ""+list33);
                String [] stopname = list3.toArray(new String[0]);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, list);
//                textView.setAdapter(adapter);

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list3);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBusType.setAdapter(adapter3);

            }
            else {
                Log.e("responceJKT= "+ "", "");
            }

            conn.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clear,menu);
       // getMenuInflater().inflate(R.menu.clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear) {

            String vv = "";
           // mPassHolder.setAdapter();

        }
        return super.onOptionsItemSelected(item);
    }


}