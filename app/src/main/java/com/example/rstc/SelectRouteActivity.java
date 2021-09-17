package com.example.rstc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

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

public class SelectRouteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager manager;
    CustomAdapter adapter;
    List<pojoClass> list = new ArrayList<>();
    StringBuilder fromStop = new StringBuilder();
    String dataParsed1 = "";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences pref;
    String month,mon,frmStop,tilStop,bsType;
    String frmdata,tilldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        recyclerView = findViewById(R.id.recyclerView);

//        manager = new GridLayoutManager(this,4);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new CustomAdapter(this,list);
        recyclerView.setAdapter(adapter);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        month = pref.getString("mm", "selected moths");
        frmStop = pref.getString("selected1", "selected FromStop");
        tilStop = pref.getString("selected2", "selected TillStop");
        bsType = pref.getString("selected3", "selected BusType");

        String data1 = frmStop;
        //   String str1 = data.substring(0,4);
        String[] items1 = data1.split("\\(");
        frmdata = items1[0];
        Log.e("selectRoutefrmStop : ", frmdata);

        String data2 = tilStop;
        //   String str1 = data.substring(0,4);
        String[] items2 = data2.split("\\(");
        tilldata = items2[0];
        Log.e("selectRouteTillStop : ", tilldata);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        loadDataFromServer(frmdata,tilldata);
    }

    private void loadDataFromServer(String frmdata, String tilldata) {

        URL url = null;
        try {
            url = new URL("http://122.15.66.234:8080/RFIDAPI/getRouteDetail");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fromStop",frmdata ));//"JPR"
            params.add(new BasicNameValuePair("tillStop",tilldata ));//"SKR"
            params.add(new BasicNameValuePair("busType", "EXP"));

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

               // list.add("Select Start Stop");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    pojoClass pc = new pojoClass();

                    pc.setRoutename(object.getString("routeName"));
                    pc.setRouteno(object.getString("routeNo"));
                    pc.setKm(object.getString("km"));
                    pc.setDepotcd(object.getString("depotCd"));

                    list.add(pc);
                    adapter.notifyDataSetChanged();
                    //list.add(object.getString("routeName"));

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                Log.e("fromStop1 = "+ "", ""+list);
             //   String [] stopname = list.toArray(new String[0]);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, list);
//                textView.setAdapter(adapter);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, list);
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
}