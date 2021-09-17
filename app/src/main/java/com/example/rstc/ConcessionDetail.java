package com.example.rstc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ConcessionDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinnerConType,spinnerConCode,spinnerPassPeriod, spinnerBusType;
    Connection connect;
    TextView endDate;
    List<String> str = new ArrayList<>();
    List<String> code = new ArrayList<>();

    Date oneYear,twoYear,threeYear,fourYear,fiveYear;
    SimpleDateFormat  format1,format2,format3,format4,format5;

    private Button btnBack,btnNext;

    List<String> list = new ArrayList<>();
    StringBuilder fromStop = new StringBuilder();
    String dataParsed1 = "";

    List<String> list2 = new ArrayList<>();
    StringBuilder fromStop2 = new StringBuilder();
    String dataParsed2 = "";

    LinearLayout linearLayout,positionLinearLayout;
    String str1,ed;

    EditText etFromStop1,etTilllStop1;
  //  String enterFromStop1,enterTillStop1;
    Button searchRoute,btnfrmStop1,btnTillStop1;
    List<String> fromlist = new ArrayList<String>();
    List<String> tilllist = new ArrayList<String>();
    List<String> btlist = new ArrayList<String>();
    String fs,ts;
//    StringBuilder fromStop1 = new StringBuilder();
//    StringBuilder tillStop1 = new StringBuilder();
//    StringBuilder busType1 = new StringBuilder();
//    String dp1 = "",dp2 = "", dp3 = "";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String mm = null;
    String selected11,selected22,selected33,btSelect;
    String concessStdPassenger,passStdPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concession_detail);

        endDate = findViewById(R.id.date_pick);
      //  btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);
        linearLayout = findViewById(R.id.ll_select_stop);
        positionLinearLayout = findViewById(R.id.ll_position);
        etFromStop1 = findViewById(R.id.et_from_stop1);
        etTilllStop1 = findViewById(R.id.et_till_stop1);
        btnfrmStop1 = findViewById(R.id.from_stop1);
        btnTillStop1 = findViewById(R.id.till_stop1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        datasss.add(rs.getString("Consession_Type_Name"));
//        Log.e("datasss = "+ "", ""+datasss);

//        StrictMode.ThreadPolicy policy = new
//                StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        spinnerConType = (Spinner)findViewById(R.id.con_type_spinner);
        spinnerConCode = (Spinner)findViewById(R.id.con_code_spinner1);
        spinnerPassPeriod = (Spinner)findViewById(R.id.pas_period);
        spinnerBusType = (Spinner)findViewById(R.id.spinner_bus_type1);

        spinnerConType.setOnItemSelectedListener(this);
        spinnerConCode.setOnItemSelectedListener(this);
        spinnerPassPeriod.setOnItemSelectedListener(this);
        spinnerBusType.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> passHolderAdapter = ArrayAdapter.createFromResource(this,R.array.pass_period, android.R.layout.simple_spinner_item);
        passHolderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassPeriod.setAdapter(passHolderAdapter);

        ArrayAdapter<CharSequence> passHolderAdapter1 = ArrayAdapter.createFromResource(this,R.array.bus_type, android.R.layout.simple_spinner_item);
        passHolderAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBusType.setAdapter(passHolderAdapter1);

        concesType();
   //     concesCode();
    //    getConcessCode();

       // getBusType();

        btnfrmStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String enterFromStop1;
                String enterFromStop1 = etFromStop1.getText().toString();
                if (enterFromStop1.isEmpty()){
                    Toast.makeText(ConcessionDetail.this, "Please Enter From Stop!", Toast.LENGTH_SHORT).show();
                }
                postStopName(enterFromStop1);
               // enterFromStop1 = null;
               // enterFromStop1.contains()
            }
        });

        btnTillStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String enterTillStop1 = etTilllStop1.getText().toString();
                if (enterTillStop1.isEmpty()){
                    Toast.makeText(ConcessionDetail.this, "Please Enter End Stop!", Toast.LENGTH_SHORT).show();
                }
                postTillStop(enterTillStop1);
               // etTilllStop1 = null;
                //etTilllStop1.getText().clear();
            }
        });



//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ConcessionDetail.this,PassRouteSelection.class);
//                startActivity(intent);
//            }
//        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("mm", String.valueOf(mm));
                editor.putString("ed", ed);
                editor.putString("selected1", fs);
                editor.putString("selected2", ts);
                editor.putString("selected3", selected33);
//                editor.putString("mm2", String.valueOf(mm2));
//                editor.putString("mm3", String.valueOf(mm3));
                editor.commit();

                String st1 = etFromStop1.getText().toString();
                String st2 = etTilllStop1.getText().toString();

//                if (st1.isEmpty() || st2.isEmpty()) {
//                    Toast.makeText(ConcessionDetail.this, "Please Enter Stops!", Toast.LENGTH_SHORT).show();
//                }
//                if (btSelect.equals("ORDINARY")){
//                    Toast.makeText(ConcessionDetail.this, "Please Select Express Bus type!", Toast.LENGTH_SHORT).show();
//                }

                 if(spinnerConType.getSelectedItemPosition() == 0 || spinnerConCode.getSelectedItemPosition() == 0 || spinnerPassPeriod.getSelectedItemPosition() == 0) {
                     Toast.makeText(ConcessionDetail.this, "Please Select All Fields!", Toast.LENGTH_SHORT).show();
                 }else if (concessStdPassenger.equals("Student Passenger")) {

                     if (st1.isEmpty() || st2.isEmpty()) {
                         Toast.makeText(ConcessionDetail.this, "Please Enter Stops!", Toast.LENGTH_SHORT).show();
                        return;
                      }else if (spinnerBusType.getSelectedItemPosition() == 0 || spinnerBusType.getSelectedItemPosition() == 2){
                         Toast.makeText(ConcessionDetail.this, "Please Select Express Bus type!", Toast.LENGTH_SHORT).show();
                        return;
                     }else {
                          Intent intent = new Intent(ConcessionDetail.this, SelectRouteActivity.class);
                          startActivity(intent);
                      }
                }
                 else {
                    Intent intent1 = new Intent(ConcessionDetail.this, FixPassFareCalculation.class);
                    startActivity(intent1);
                }
            }
        });
    }

//========================
    private void postStopName(String enterFromStop1) {

        StringBuilder fromStop1 = new StringBuilder();
        StringBuilder tillStop1 = new StringBuilder();
        StringBuilder busType1 = new StringBuilder();
        String dp1 = "",dp2 = "", dp3 = "";


        String ss = enterFromStop1;
        Log.e("enterFromStop1= "+ "", enterFromStop1);

        URL url = null;
        try {
            url = new URL("http://122.15.66.234:8080/RFIDAPI/stopName");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("stopName",enterFromStop1 )); //enterFromStop
//            params.add(new BasicNameValuePair("tillStop", "SKR"));
//            params.add(new BasicNameValuePair("busType", "EXP"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getfrmRoute(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    fromStop1.append(line);
                    //  textView.setText(output);
                    dp1 = dp1 + fromStop1;
                    Log.e("fromStop= "+ "", line.toString());
                    Log.e("fromStopppp= "+ "", String.valueOf(fromStop1));
                }
                JSONArray array = new JSONArray(dp1);

                String fss=null;
              //  fromlist.add("Select Start Stop");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    fromlist.add(object.getString("busStopName"));
                   // list11.add(object.getString("busStopCode"));

                     fss = object.getString("busStopName");

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                fs = fss;
                etFromStop1.setText(fs);
              //  fromStop1=null;
                Log.e("fromStop1 = "+ "", ""+fromlist);
               // Log.e("frombusStopCode = "+ "", ""+list11);
                Log.e("fs = "+ "", ""+fs);
              //  String [] stopname = list1.toArray(new String[0]);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, list);
//                textView.setAdapter(adapter);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, list1);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mFromStop.setAdapter(adapter);

              //  fss = null;
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

    private String getfrmRoute(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
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

    private void postTillStop(String enterTillStop1) {

        StringBuilder fromStop1 = new StringBuilder();
        StringBuilder tillStop1 = new StringBuilder();
        StringBuilder busType1 = new StringBuilder();
        String dp1 = "",dp2 = "", dp3 = "";


        URL url = null;
        try {
            url = new URL("http://122.15.66.234:8080/RFIDAPI/stopName");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("stopName",enterTillStop1 ));//"SIK"
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
                    tillStop1.append(line);
                    //  textView.setText(output);
                    dp2 = dp2 + tillStop1;
                    Log.e("tillStop= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dp2);

                String fss=null;
                // list2.add("Select End Stop");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    tilllist.add(object.getString("busStopName"));
                  //  list22.add(object.getString("busStopCode"));

                    fss = object.getString("busStopName");
                    break;

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                ts = fss;
                etTilllStop1.setText(ts);
                Log.e("tillStop = "+ "", ""+tilllist);
//                Log.e("tillbusStopCode = "+ "", ""+list22);
//                Log.e("tt = "+ "", ""+ts);
//                String [] stopname = list2.toArray(new String[0]);

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
        StringBuilder fromStop1 = new StringBuilder();
        StringBuilder tillStop1 = new StringBuilder();
        StringBuilder busType1 = new StringBuilder();
        String dp1 = "",dp2 = "", dp3 = "";


        URL url = null;
        try {
            url = new URL("http://122.15.66.234:8080/RFIDAPI/busType");
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
                    busType1.append(line);
                    //  textView.setText(output);
                    dp3 = dp3 + busType1;
                    Log.e("busType= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dp3);

                btlist.add("Select Bus Type");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    btlist.add(object.getString("busTypeName"));
                  //  list33.add(object.getString("busTypeCd"));

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                Log.e("busType = "+ "", ""+btlist);
//                Log.e("busTypeCdEXp = "+ "", ""+list33);
//                String [] stopname = list3.toArray(new String[0]);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, list);
//                textView.setAdapter(adapter);

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, btlist);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBusType.setAdapter(adapter3);

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

 //===================

    private void concesType() {

        try {
            ConnectionHelper helper = new ConnectionHelper();
            connect = helper.connectionClass();

            if (connect != null){
                String query = "select * from Master_Consession_Type ";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);

                str.add("Select");
                while (rs.next()){
//                    tvvv.setText(rs.getString("Consession_Type_Name"));
//                    Log.e("tvvv = "+ "", ""+tvvv);

                    str.add(rs.getString("Concession_Type_Name"));

                 //   tv2.setText(rs.getString(4));
//                    tv.setText(rs.getString(3));
//                    tv.setText(rs.getString(4));
                }
                Log.e("str : ", String.valueOf(str));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, str);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerConType.setAdapter(adapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getConcessCode() {

        URL url = null;
        try {
            url = new URL("http://115.124.127.204/rsrtcapi/GetConcessionMaster");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ConcessionType","1" ));//"JPR"
            params.add(new BasicNameValuePair("busType", "EXP"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getRoute(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == 201) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    fromStop.append(line);
                    //  textView.setText(output);
                    dataParsed1 = dataParsed1 + fromStop;
                    Log.e("fromStop= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed1);

                 list.add("Select");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);


                    list.add(object.getString("concessionName"));
                }
                Log.e("concessionName = "+ "", ""+list);

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerConCode.setAdapter(adapter3);

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

    private void getPassConcessCode() {

        URL url = null;
        try {
            url = new URL("http://115.124.127.204/rsrtcapi/GetConcessionMaster");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ConcessionType","2" ));//"JPR"
            params.add(new BasicNameValuePair("busType", "EXP"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPassRoute(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == 201) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    fromStop2.append(line);
                    //  textView.setText(output);
                    dataParsed2 = dataParsed2 + fromStop2;
                    Log.e("fromStop= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed2);

                list2.add("Select");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);


                    list2.add(object.getString("concessionName"));
                }
                Log.e("PassconcessionName = "+ "", ""+list2);

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerConCode.setAdapter(adapter3);

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

    private String getPassRoute(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
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




    //=========================================================================

    private void concesCode() {

        try {
            ConnectionHelper helper = new ConnectionHelper();
            connect = helper.connectionClass();

            if (connect != null){
              //  String query = "select * from Master_Consession ";
                String query = "Select Distinct Concession_Code,Concession_Name from [Master_Consession] Order by Concession_Name";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);

                code.add("Select");
                while (rs.next()){
//                    tvvv.setText(rs.getString("Consession_Type_Name"));
//                    Log.e("tvvv = "+ "", ""+tvvv);

                    code.add(rs.getString("Concession_Name"));

                    //   tv2.setText(rs.getString(4));
//                    tv.setText(rs.getString(3));
//                    tv.setText(rs.getString(4));
                }
                Log.e("code : ", String.valueOf(code));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, code);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerConCode.setAdapter(adapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//======================================================================


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.con_type_spinner :

                String str = parent.getItemAtPosition(position).toString();
                if (str.equals("CONCESSION")){
                    list.clear();
                    list2.clear();
                    getConcessCode();
                    str1 = "CONCESSION";

                }else if (str.equals("PASS")){
                    linearLayout.setVisibility(View.GONE);
                    list.clear();
                    list2.clear();
                    getPassConcessCode();

                }

//                if (position == 0){
//                    list.clear();
//                    list2.clear();
//                }
//                 if (position == 1){
//                    list.clear();
//                    list2.clear();
//                    getConcessCode();
//                }else if (position == 2){
//                    list.clear();
//                    list2.clear();
//                    getPassConcessCode();
//                }

                break;

            case R.id.con_code_spinner1 :
               // String str = "CONCESSION";
                concessStdPassenger = parent.getItemAtPosition(position).toString();
                if (concessStdPassenger.equals("Student Passenger")){
                    linearLayout.setVisibility(View.VISIBLE);
                    etFromStop1.getText().clear();
                    etTilllStop1.getText().clear();

                }else if (concessStdPassenger.equals("Rajasthan Police Force")){
                    positionLinearLayout.setVisibility(View.VISIBLE);

                }else {
                    linearLayout.setVisibility(View.GONE);
                    positionLinearLayout.setVisibility(View.GONE);
                }

//                if (position == 1){
//                    linearLayout.setVisibility(View.GONE);
//                }else if (position == 2 || list.equals(str1)){
//                linearLayout.setVisibility(View.VISIBLE);
//                }
//                else if (position == 3){
//                    if (list2.equals())
//                    linearLayout.setVisibility(View.GONE);
//                }

                break;

            case R.id.pas_period :

                if (position == 1){
                    Calendar cal = Calendar.getInstance();
                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, 1); // to get previous year add -1
                    oneYear = cal.getTime();
                    format1 = new SimpleDateFormat("dd/MM/yyyy");
                    endDate.setText(format1.format(oneYear));
                    ed = format1.format(oneYear);
                    break;
                }else if (position == 2){
                    Calendar cal = Calendar.getInstance();
                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, 2); // to get previous year add -2
                    twoYear = cal.getTime();
                    format2 = new SimpleDateFormat("dd/MM/yyyy");
                    endDate.setText(format2.format(twoYear));
                    ed = format2.format(twoYear);
                break;

                }else if (position == 3){
                    Calendar cal = Calendar.getInstance();
                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, 3); // to get previous year add -3
                    threeYear = cal.getTime();
                    format3 = new SimpleDateFormat("dd/MM/yyyy");
                    endDate.setText(format3.format(threeYear));
                    ed = format3.format(threeYear);
                    break;
                }else if (position == 4){
                    Calendar cal = Calendar.getInstance();
                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, 4); // to get previous year add -4
                    fourYear = cal.getTime();
                    format4 = new SimpleDateFormat("dd/MM/yyyy");
                    endDate.setText(format4.format(fourYear));
                    ed = format4.format(fourYear);
                    break;

                }else if (position == 5){
                    Calendar cal = Calendar.getInstance();
                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, 5); // to get previous year add -5
                    fiveYear = cal.getTime();
                    format5 = new SimpleDateFormat("dd/MM/yyyy");
                    endDate.setText(format5.format(fiveYear));
                    ed = format5.format(fiveYear);
                    break;
                }

            case R.id.spinner_bus_type1:
                selected33 = parent.getItemAtPosition(position).toString();
                Log.e("selectedBusType = "+ "", selected33);
                if (selected33.equals("EXPRESS")){
                    btSelect = selected33;
                }else if (selected33.equals("ORDINARY")){
                 //   Toast.makeText(this, "Please Select Express Bus type!", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        etFromStop1.getText().clear();
        etTilllStop1.getText().clear();
    }
}