package com.example.rstc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FareCalculation extends AppCompatActivity {

    StringBuilder fromStop = new StringBuilder();
    StringBuilder conRate = new StringBuilder();
    String dataParsed1 = "";
    String dataParsed2 = "";
    List<String> list = new ArrayList<>();

    Button back,next,clear, fareCalculate;

    String intrastate,interstate,acc,hr,octroi,it,toll;
    String concessRate,busTypecd,concessCode;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences pref;
    String month,mon,frmStop,tilStop,bsType;

    Float finalAmount,amount1,amount2,amt,finalAmount2,totalFare;
    Float ac,tl,oct,hrr;
    Float Acs,Toll,Octroi,Hr;

    TextView tvTotalFare,tvAdminFees,tvCardFees,tvTransDate,tvExpiryDate;
    TextView tvBaseFare,tvAcs,tvHr,tvOctroi,tvIt,tvToll;

    Date oneMonth,twoMonth,threeMonth;
    SimpleDateFormat format;

    String aa = "40";
    String cardFees;
    String frmdata,tilldata,busTypedata,rootNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvTotalFare = findViewById(R.id.total_fare);
        tvAdminFees = findViewById(R.id.admin_fees);
        tvCardFees = findViewById(R.id.card_fees);
        tvTransDate = findViewById(R.id.trans_date);
        tvExpiryDate = findViewById(R.id.exp_date);


        tvBaseFare = findViewById(R.id.base_fare);
        tvAcs = findViewById(R.id.acc);
        tvHr = findViewById(R.id.hr);
        tvOctroi = findViewById(R.id.octroi);
        tvIt = findViewById(R.id.it);
        tvToll = findViewById(R.id.toll);

        back = findViewById(R.id.btn_back);
        next = findViewById(R.id.btn_next);
        clear = findViewById(R.id.btn_clear);
        fareCalculate = findViewById(R.id.calculate);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        month = pref.getString("mm", "selected moths");
        frmStop = pref.getString("selected1", "selected FromStop");
        tilStop = pref.getString("selected2", "selected TillStop");
        bsType = pref.getString("selected3", "selected BusType");
        Log.e("month : ", month);
        Log.e("frmStop : ", frmStop);
        Log.e("tilStop : ", tilStop);
        Log.e("bsType : ", bsType);

         mon = month.substring(0,2);
        Log.e("mon : ", mon);

        String data1 = frmStop;
     //   String str1 = data.substring(0,4);
        String[] items1 = data1.split("\\(");
        frmdata = items1[0];
        Log.e("frmdata : ", frmdata);

         String data2 = tilStop;
        //   String str1 = data.substring(0,4);
        String[] items2 = data2.split("\\(");
         tilldata = items2[0];
        Log.e("tilldata : ", tilldata);

        if (bsType.equals("EXPRESS")){
            busTypedata = "EXP";
        }else {
            Toast.makeText(this, "Please Select Express Bus Type!", Toast.LENGTH_SHORT).show();
        }


        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        pojoClass root = (pojoClass) bundle.getSerializable("rootData");
        rootNo = root.getRouteno();
        Log.e("root : ", root.getRouteno());

                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy ");


        fareCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mon.equals("30")){
                    finalAmount2 = 26 * finalAmount;
                    Acs = 26 * ac;
                    Toll = 26 * tl;
                    Octroi = 26 * oct;
                    Hr = 26 * hrr;

                    calendar.add(Calendar.MONTH, 1); // to get previous year add -1
                    oneMonth = calendar.getTime();
                    tvExpiryDate.setText(format.format(oneMonth));

//                    Calendar calendar = Calendar.getInstance();
//                    Date date = calendar.getTime();
//                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy ");
//                     tvTransDate.setText(format.format(date));

                }else if (mon.equals("60")){
                    finalAmount2 = 52 * finalAmount;
                    Acs = 52 * ac;
                    Toll = 52 * tl;
                    Octroi = 52 * oct;
                    Hr = 52 * hrr;
                    calendar.add(Calendar.MONTH, 2); // to get previous year add -1
                    twoMonth = calendar.getTime();
                    tvExpiryDate.setText(format.format(twoMonth));

                }else if (mon.equals("90")){
                    finalAmount2 = 78 * finalAmount;
                    Acs = 78 * ac;
                    Toll = 78 * tl;
                    Octroi = 78 * oct;
                    Hr = 78 * hrr;
                    calendar.add(Calendar.MONTH, 3); // to get previous year add -1
                    threeMonth = calendar.getTime();
                    tvExpiryDate.setText(format.format(threeMonth));

                }

                //  ,tvAcc,tvHr,tvOctroi,tvIt,tvToll;
                //  tvExpiryDate.setText(format1.format(oneMonth));
                tvBaseFare.setText((finalAmount2.toString()));
                tvAcs.setText(Acs.toString());
                tvHr.setText(Hr.toString());
                tvOctroi.setText(Octroi.toString());
                tvToll.setText(Toll.toString());

                totalFare = finalAmount2 + Acs + Toll + Octroi + Hr;
                tvTotalFare.setText(totalFare.toString());

                tvTransDate.setText(format.format(date));

                tvAdminFees.setText(aa);
                cardFees = totalFare + aa;
                tvCardFees.setText(cardFees);


//                Calendar calendar = Calendar.getInstance();
////                Date today = cal.getTime();

//                Log.e("oneMonth = "+ "", ""+ oneMonth);
//                format = new SimpleDateFormat("dd/MM/yyyy");
//                String formattedDate = format.format(calendar);

//                Log.e("formattedDate = "+ "", ""+ formattedDate);


            }
        });




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FareCalculation.this,UploadDocuments.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FareCalculation.this,PassTypeActivity.class);
                startActivity(intent);
            }
        });

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getFareCalculation();
        concessionRate();

        fareCalculate();

    }

    private void concessionRate() {

        URL url = null;
        try {
            url = new URL("http://115.124.127.204/rsrtcapi/GetConcession");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ConcessionCode", "MTP"));
            params.add(new BasicNameValuePair("BusType", "EXP"));
//            params.add(new BasicNameValuePair("routeNo", "17"));
//            params.add(new BasicNameValuePair("busType", "VOL"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getConcession(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();
            Log.e("responseCode "+ "", String.valueOf(responseCode));

            if ((responseCode == HttpsURLConnection.HTTP_OK) || (responseCode ==201)) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    conRate.append(line);
                    //  textView.setText(output);
                    dataParsed2 = dataParsed2 + conRate;   //Json responce
                    Log.e("conRate= "+ "", line.toString());
                }
                //

//                JSONObject ob = new JSONObject(dataParsed1);
//                intrastate = ob.getString("intrastate");
//                interstate = ob.getString("interstate");
//                acc = ob.getString("acc");
//                hr = ob.getString("hr");
//                octroi = ob.getString("octroi");
//                it = ob.getString("it");
//                toll = ob.getString("toll");


//                Log.e("intrastate = "+ "", ""+intrastate);
//                Log.e("interstate = "+ "", ""+interstate);
//                Log.e("acc = "+ "", ""+acc);
//                Log.e("hr = "+ "", ""+hr);
//                Log.e("octroi = "+ "", ""+octroi);
//                Log.e("it = "+ "", ""+it);
//                Log.e("toll = "+ "", ""+toll);

                JSONArray array = new JSONArray(dataParsed2);
                // list.add("Select Start Stop");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    //  String intrastate = object.getString("intrastate");
                     concessRate = object.getString("concessionRate");
                     busTypecd = object.getString("busTypeCd");
                     concessCode = object.getString("concessionCode");

                    Log.e("concessRate = "+ "", ""+concessRate);
                Log.e("busTypecd = "+ "", ""+busTypecd);
                Log.e("concessCode = "+ "", ""+concessCode);

                }
            }
            else {
                Log.e("conRate code = "+ "", "");
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

    private String getConcession(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append("{");
        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append(",");

            result.append("'");
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("'");
            result.append(":");
            result.append("'");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            result.append("'");

//          Log.e("responce= "+ "", result.toString());
        }
        result.append("}");
        Log.e("Request String= "+ "", result.toString());
        JSONObject obj = new JSONObject(result.toString());
        Log.d("Json Parameter : ", obj.toString());

//        return JSON.stringify(result.toString());
//        return "{\"stopName\":\"jai\"}";
        return obj.toString();


    }

    private void getFareCalculation() {

        URL url = null;
        try {
            url = new URL("http://122.15.66.234:8080/RFIDAPI/getFare");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fromStop", frmdata));
            params.add(new BasicNameValuePair("tillStop", tilldata));
            params.add(new BasicNameValuePair("routeNo", rootNo));
            params.add(new BasicNameValuePair("busType", busTypedata));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getFares(params));
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
                    dataParsed1 = dataParsed1 + fromStop;   //Json responce
                    Log.e("fromStop= "+ "", line.toString());
                }
             //

                JSONObject ob = new JSONObject(dataParsed1);
                 intrastate = ob.getString("intrastate");
                 interstate = ob.getString("interstate");
                 acc = ob.getString("acc");
                 hr = ob.getString("hr");
                 octroi = ob.getString("octroi");
                 it = ob.getString("it");
                 toll = ob.getString("toll");


                Log.e("intrastate = "+ "", ""+intrastate);
                Log.e("interstate = "+ "", ""+interstate);
                Log.e("acc = "+ "", ""+acc);
                Log.e("hr = "+ "", ""+hr);
                Log.e("octroi = "+ "", ""+octroi);
                Log.e("it = "+ "", ""+it);
                Log.e("toll = "+ "", ""+toll);

//                JSONArray array = new JSONArray(dataParsed1);
//                // list.add("Select Start Stop");
//                for (int i = 0; i < array.length(); i++){
//                    JSONObject object = array.getJSONObject(i);
//
//                  //  String intrastate = object.getString("intrastate");
//                    String interstate = object.getString("interstate");
//                    String acc = object.getString("acc");
//                    String hr = object.getString("hr");
//                    String octroi = object.getString("octroi");
//                    String it = object.getString("it");
//                    String toll = object.getString("toll");


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

    private String getFares(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append("{");
        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append(",");

            result.append("'");
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("'");
            result.append(":");
            result.append("'");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            result.append("'");

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

    //===================================Fare Calculation===============================

    private void fareCalculate() {

         ac = Float.parseFloat(acc) * 2;
         tl = Float.parseFloat(toll) * 2;
         oct = Float.parseFloat(octroi) * 2;
         hrr = Float.parseFloat(hr) * 2;

        Log.e("ac = "+ "", ""+ ac);
        Log.e("tl = "+ "", ""+ tl);
        Log.e("oct = "+ "", ""+ oct);
        Log.e("hrr = "+ "", ""+ hrr);

         amount1 = Float.parseFloat(interstate );
         amount2 = Float.parseFloat( intrastate);
         amt = amount1 + amount2;
        Log.e("amount = "+ "", ""+ amt);

         finalAmount = 2 * (amt - (amt * Float.parseFloat(concessRate)/100));

        Log.e("finalAmount = "+ "", ""+ finalAmount);

    }

//==============================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();  // using back press button
        }
        return super.onOptionsItemSelected(item);
    }
}