package com.example.rsrtcs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

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

public class StatusActivity extends AppCompatActivity {

    TextView status;

    String mStatus[];

    StringBuilder fromStop = new StringBuilder();
    String dataParsed1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        status = (TextView) findViewById(R.id.status);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = this.getIntent().getExtras();
        mStatus = bundle.getString("status").toString().split("\\|");

       // status.setText(bundle.getString("status"));

        String statusResponce = bundle.getString("status");

        statusSavedInSQL(statusResponce);

        status.setText(statusResponce);
        finish();


    }

    private void statusSavedInSQL(String statusResponce) {

        URL url = null;
        String responceStatus = null;
        try {
            url = new URL("https://rsrtcrfidsystem.co.in/rsrtcapi/BillDeskResponse");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Msg", statusResponce));
//            aparams.add(new BasicNameValuePair("CardNo","00000000000149" )); //enterFromStop
//            prams.add(new BasicNameValuePair("UserId", "12"));
//            params.add(new BasicNameValuePair("RequestType", "MobileApp"));

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
                    Log.e("line= "+ "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed1);
                //  JSONObject array = new JSONObject(dataParsed1);


                //   fs = array.getString("result");
                //   result = fs;
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    // list1.add(object.getString("busStopName"));
                    //  list11.add(object.getString("busStopCode"));

                    responceStatus = object.getString("msg");

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                // etFromStop.setText(fs);
                Log.e("responceStatus = "+ "", ""+responceStatus);

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

            result.append("'");
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("'");
            result.append(":");
            result.append("'");
            if (pair.getName() != "Msg") {
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }else {
                result.append(pair.getValue());
            }
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
}