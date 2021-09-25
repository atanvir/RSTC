package com.example.rsrtcs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.billdesk.sdk.PaymentOptions;

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
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import static com.example.rsrtcs.FixPassFareCalculation.MyPREFERENCES;
import static com.example.rsrtcs.FixPassFareCalculation.fees;

public class PaymentActivity extends AppCompatActivity {

    // String strPGMsg = "AIRMTST|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|892409133";
    String strPGMsg = "BDSKUATY|ARP1553593909862|NA|1|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgijsp/banksimulator.jsp";
   // String strPGMsg11 = "|NA|1|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://115.124.127.204/pg_dump.aspx";
    String strPGMsg11 = "|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://115.124.127.204/pg_dump.aspx";

    String A1 = "BDSKUATY";

    //// String strTokenMsg = "AIRMTST|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|723938585|CP1005!AIRMTST!D1DDC94112A3B939A4CFC76B5490DC1927197ABBC66E5BC3D59B12B552EB5E7DF56B964D2284EBC15A11643062FD6F63!NA!NA!NA";
    //String strTokenMsg="Test msg";
    String strTokenMsg = null;

    Button btnPayNow;
    EditText etEmail, etPhone;

    Date dt = new Date();
    StringBuilder fromStop = new StringBuilder();
    String dataParsed1 = "";
    SharedPreferences pref;
    String cardNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if(getIntent().getStringExtra("amount")!=null){
            fees=getIntent().getStringExtra("amount");
        }


        btnPayNow = (Button) findViewById(R.id.btnPayNow);
//        etEmail = findViewById(R.id.email);
//        etPhone = findViewById(R.id.mobile);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        cardNumber = pref.getString("cardNo", "card");

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssms");
        String gUid = df.format(dt);
        Log.e("gUid", "" + gUid);

      //  A1 = A1 + "|" + gUid + strPGMsg11;
        A1 = A1 + "|" + gUid + "|" + "NA" + "|" + fees + strPGMsg11;
        Log.e("A1", "" + A1);

      //  String msg1111 = checksumGenerate(strPGMsg);
        String msg1111 = checksumGenerate(A1);
        Log.e("msg1111", "" + msg1111);

     //   strPGMsg = strPGMsg + "|" + msg1111;

        A1 = A1 + "|" + msg1111;
        Log.e("A11111", "" + A1);



        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                billDeskRequestSaveInSql(A1,cardNumber);

                payNowCalled(A1);
            }
        });
    }

    private void billDeskRequestSaveInSql(String pgMsg, String cardNumber ) {

        URL url = null;
        String requestStatus = null;
        try {
            url = new URL("https://rsrtcrfidsystem.co.in/rsrtcapi/BillDeskRequest");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CardNo",cardNumber )); //enterFromStop
                params.add(new BasicNameValuePair("UserId", "12"));
                params.add(new BasicNameValuePair("Msg", pgMsg));
                params.add(new BasicNameValuePair("RequestType", "MobileApp"));

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

                    requestStatus = object.getString("msg");

                    //     Log.e("arrayList = "+ "", ""+list);
                }
                Log.e("requestStatus = "+ "", ""+requestStatus);

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

    //=====================================================

    private String checksumGenerate(String msg) {
        String checksumKey = "G3eAmyVkAzKp8jFq0fqPEqxF4agynvtJ";
        // String msg = "BDSKUATY|ARP1553593909862|NA|10.00|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgijsp/banksimulator.jsp";
        String CP_MSG = "CP1005!......!NA!NA!NA";// or provide null value.

        Log.e("checksumKey = ", "" + checksumKey);
        Log.e("msg1 = ", "" + msg);

        String result = HmacSHA256(msg, checksumKey);
        return result;

    }
        private String HmacSHA256(String message, String secret) {

        // MessageDigest md = null;
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),
                    "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte raw[] = sha256_HMAC.doFinal(message.getBytes());

            StringBuffer ls_sb = new StringBuffer();
            for (int i = 0; i < raw.length; i++)
                ls_sb.append(char2hex(raw[i]));
            Log.e("sha_ls_sb = ", "" + ls_sb);
            return ls_sb.toString(); // step 6
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String char2hex(byte x){

        char arr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};

        char c[] = {arr[(x & 0xF0) >> 4], arr[x & 0x0F]};
        return (new String(c));

    }
    //======================================================

    private void payNowCalled(String strPGMsg) {
        Log.e("payNowCalled : ", "");
//        String email = etEmail.getText().toString();
//        String mobile = etPhone.getText().toString();

        SampleCallBack objSampleCallBack = new SampleCallBack();

        Intent sdkIntent = new Intent(this, PaymentOptions.class);
        sdkIntent.putExtra("msg", strPGMsg);
        Log.e("strPGMsg1111 = ","" + strPGMsg);
        if(strTokenMsg != null && strTokenMsg.length() > strPGMsg.length()) {

            sdkIntent.putExtra("token",strTokenMsg);
        }
        sdkIntent.putExtra("user-email","test@bd.com");
        sdkIntent.putExtra("user-mobile","9800000000");
        sdkIntent.putExtra("callback",objSampleCallBack);

//        sdkIntent.putExtra("user-email",email);
//        sdkIntent.putExtra("user-mobile",mobile);
//        sdkIntent.putExtra("callback",objSampleCallBack);

        startActivity(sdkIntent);
        finish();
    }

}