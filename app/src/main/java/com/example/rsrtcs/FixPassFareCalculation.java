package com.example.rsrtcs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FixPassFareCalculation extends AppCompatActivity {

    TextView tvTransDate,tvExpiryDate,tvCardFees;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences pref;
    String endYear;

    Button back,next2,clear,save,btnPay;
    String appId,name,mName,lName,mob,eml,addr,proof,title,gen,id,dob,bmImg,imgUri;
    String uriImg;

   // byte[] bmImg1;

    Connection connect;
    String ConnectionResult = "";
    String transDate,endDate,cardNo = null;
    public static String fees = "40";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_pass_fare_calculation);


        tvTransDate = findViewById(R.id.trans_date);
        tvExpiryDate = findViewById(R.id.exp_date);
        tvCardFees = findViewById(R.id.card_fees);
        btnPay = findViewById(R.id.btn_pay);

//        back = findViewById(R.id.btn_back);
        next2 = findViewById(R.id.btn_next);
//        clear = findViewById(R.id.btn_clear);
        save = findViewById(R.id.btn_save);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        endYear = pref.getString("ed", "end Date");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy ");
        transDate = format.format(date);
      //  tvTransDate.setText(format.format(date));
        tvTransDate.setText(transDate);
        tvExpiryDate.setText(endYear);

       tvCardFees.setText(fees);

//
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPay.setVisibility(View.GONE);
                save.setVisibility(View.GONE);

                Intent intent = new Intent(FixPassFareCalculation.this,UploadDocuments.class);
                startActivity(intent);

                save.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                next2.setVisibility(View.VISIBLE);
            }
        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FixPassFareCalculation.this,ConcessionDetail.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(FixPassFareCalculation.this,UploadDocuments.class);
//                startActivity(intent);
//                finish();


                btnPay.setVisibility(View.GONE);
                next2.setVisibility(View.GONE);

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("transDate", transDate);
                editor.commit();

                getAllRegisterData();

               // bmImg1 = bmImg.getBytes();

                inserDataToSQL(appId,name,mName,lName,mob,eml,addr,proof,title,gen,id,dob,uriImg);  //dob

               // storedProcedure();
//                Intent intent = new Intent(FixPassFareCalculation.this,UploadDocuments.class);
//                startActivity(intent);

                Toast.makeText(FixPassFareCalculation.this, "Record Save Successfully!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(FixPassFareCalculation.this,UploadDocuments.class));
            }
        });

        //==============================================

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  payNowCalled();

                save.setVisibility(View.GONE);
                next2.setVisibility(View.GONE);

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("cardNo", cardNo);
                editor.commit();

                Intent intent = new Intent(FixPassFareCalculation.this,PaymentActivity.class);
                startActivity(intent);

                save.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                next2.setVisibility(View.VISIBLE);
            }
        });

        //=======================================================

    }

    private void storedProcedure() {

        try {
            ConnectionHelper helper = new ConnectionHelper();
            connect = helper.connectionClass();

            //  fName,mName,lName,mob,eml,addres,proofs;

            if (connect != null){
                Log.e("ApplicantID= "+ "", appId);
                Log.e("fName= "+ "", name);
                Log.e("mName= "+ "", mName);
                Log.e("lName= "+ "", lName);

                //  String query = "INSERT INTO MMemberNewRegistration(ApplicantID,FName,MName,LName) VALUES('"+ appId +"','"+ name +"','"+ mName +"','"+ lName +"')";
//                String query = "INSERT INTO MMemberNewRegistration(ApplicantID,NameTitle,FName,MName,LName,Gender,DOB,MobileNo,EmailID,Address,ProofID,ProofDetails)" +
//                        " VALUES('"+ appId +"','"+ title +"','"+ name +"','"+ mName +"','"+ lName +"','"+ gen +"','"+ dob +"','"+ mob +"','"+ eml +"','"+ addr +"','"+ id +"','"+ proof +"')" ;

                String query = "INSERT INTO MMemberNewRegistration(ApplicantID,NameTitle,FName,MName,LName,Gender,DOB,MobileNo,EmailID,Address,ProofID,ProofDetails)" +
                        " VALUES('"+ appId +"','"+ title +"','"+ name +"','"+ mName +"','"+ lName +"','"+gen +"',convert(DATETIME,Convert(NVARCHAR(10),'" + dob + "',103),103),'"+ mob +"','"+eml +"','"+addr +"','"+ id+"','"+ proof +"')" ;

                ConnectionResult = "Success";
//                isSuccess = true;
                connect.close();
            }else {
                ConnectionResult = "Failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void inserDataToSQL(String appId, String name, String mName, String lName, String mob, String eml, String addr, String proof, String title, String gen, String id, String dob, String uriImg) {

        //============================================================

//        try {
//            ConnectionHelper helper = new ConnectionHelper();
//            connect = helper.connectionClass();
//
//            //  fName,mName,lName,mob,eml,addres,proofs;
//
//            if (connect != null){
//                Log.e("ApplicantID= "+ "", appId);
//                Log.e("fName= "+ "", name);
//                Log.e("mName= "+ "", mName);
//                Log.e("lName= "+ "", lName);
//
//                //  String query = "INSERT INTO MMemberNewRegistration(ApplicantID,FName,MName,LName) VALUES('"+ appId +"','"+ name +"','"+ mName +"','"+ lName +"')";
////                String query = "INSERT INTO MMemberNewRegistration(ApplicantID,NameTitle,FName,MName,LName,Gender,DOB,MobileNo,EmailID,Address,ProofID,ProofDetails)" +
////                        " VALUES('"+ appId +"','"+ title +"','"+ name +"','"+ mName +"','"+ lName +"','"+ gen +"','"+ dob +"','"+ mob +"','"+ eml +"','"+ addr +"','"+ id +"','"+ proof +"')" ;
//
//                String query = "INSERT INTO MMemberNewRegistration(ApplicantID,NameTitle,FName,MName,LName,Gender,DOB,MobileNo,EmailID,Address,ProofID,ProofDetails)" +
//                        " VALUES('"+ appId +"','"+ title +"','"+ name +"','"+ mName +"','"+ lName +"','"+gen +"',convert(DATETIME,Convert(NVARCHAR(10),'" + dob + "',103),103),'"+ mob +"','"+eml +"','"+addr +"','"+ id+"','"+ proof +"')" ;
//
//               // CallableStatement cstmt = connect.prepareCall("{call SP_InsertUpdateMemberDetailsMaster()}");
//                // " VALUES('res ','fName','mName','lName')" ;
//                // "'"+mob +"' '"+eml +"' '"+addres +"' '"+proofs +"' ) ";
//
//                Statement statement = connect.createStatement();
//                statement.executeQuery(query);
//                ResultSet rs = statement.executeQuery(query);
//                Log.e("ress : ", String.valueOf(rs));
////
////                String res = null;
////                while (rs.next()){
////
////                    res = (rs.getString(""));   // get Allpicant ID
//////                            et.append(res + "\n");
////
////                    //  str.add(rs.getString("Concession_Type_Name"));
////
////                    //   datass.add(rs.getString("Concession_Type_Name"));
//////                            Map<String,String> dtList = new HashMap<String, String>();
////                }
////                Log.e("ressssss : ", String.valueOf(res));
////                applicant_Id.setText(res);    // set Allpicant ID
//
//                ConnectionResult = "Success";
////                isSuccess = true;
//                connect.close();
//            }else {
//                ConnectionResult = "Failed";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//=========================================

        //, String dob

        StringBuilder fromStop = new StringBuilder();
        String dp = "",dp2 = "", dp3 = "";

        URL url = null;
        try {
            url = new URL("https://rsrtcrfidsystem.co.in/rsrtcapi/SaveRegistration");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            List<NameValuePair> params = new ArrayList<NameValuePair>();

//            String sDate = "convert(DATETIME,Convert(NVARCHAR(10),'" + dob + "',103),103)";
//            Log.e("fromStop= "+ "", sDate);

//            params.add(new BasicNameValuePair("ApplicantID",appId )); //enterFromStop
//            params.add(new BasicNameValuePair("NameTitle", title));
//            params.add(new BasicNameValuePair("FName", name));
//            params.add(new BasicNameValuePair("MName", mName));
//            params.add(new BasicNameValuePair("LName", lName));
//            params.add(new BasicNameValuePair("Gender", gen));
//          //  params.add(new BasicNameValuePair("DOB", "EXP"));
//            params.add(new BasicNameValuePair("MobileNo", mob));
//            params.add(new BasicNameValuePair("EmailID", eml));
//            params.add(new BasicNameValuePair("Address", addr));
//            params.add(new BasicNameValuePair("ProofID", id));
//            params.add(new BasicNameValuePair("ProofDetails", proof));


            params.add(new BasicNameValuePair("DEPOID","29"));
            params.add(new BasicNameValuePair("POINTOFSALEID","1"));
            params.add(new BasicNameValuePair(  "ApplicantID",appId));  //appId
                    params.add(new BasicNameValuePair( "Title",title));
                    params.add(new BasicNameValuePair( "FName", name));
                    params.add(new BasicNameValuePair( "MName",mName));
                    params.add(new BasicNameValuePair("LName", lName));
                    params.add(new BasicNameValuePair( "Gender",gen));
                   // params.add(new BasicNameValuePair("DOB","6/30/1979"));
                    params.add(new BasicNameValuePair("DOB",dob));
                    params.add(new BasicNameValuePair("MobileNo",mob));
                    params.add(new BasicNameValuePair("EmailID",eml));
                    params.add(new BasicNameValuePair("PhoneNo","7788997755"));
                    params.add(new BasicNameValuePair("Address",addr));
                    params.add(new BasicNameValuePair("ProofID",id));
                    params.add(new BasicNameValuePair("ProofDetails",proof));
                    params.add(new BasicNameValuePair("Photo", uriImg));
                    params.add(new BasicNameValuePair("PhotoIDProofID","2"));
                    params.add(new BasicNameValuePair("photoIDProofData",""));
                    params.add(new BasicNameValuePair("ConcessionApplicableDocumentProofID","4"));
                    params.add(new BasicNameValuePair("ConcessionApplicableDocumentProofFileData",""));
                    params.add(new BasicNameValuePair("AddressProofID",""));
                    params.add(new BasicNameValuePair("AddressProofFileData",""));
                    params.add(new BasicNameValuePair("Remarks",""));
                    params.add(new BasicNameValuePair("CreatedBy","Online"));
                  //  params.add(new BasicNameValuePair("CreatedOn","6/2/2021"));
                    params.add(new BasicNameValuePair("CreatedOn",transDate));
                    params.add(new BasicNameValuePair("PassType","Fixed Passes"));
                    params.add(new BasicNameValuePair("PassHolder",""));
                    params.add(new BasicNameValuePair("RFIDPassType",""));
                    params.add(new BasicNameValuePair("PassValidity",""));
                    params.add(new BasicNameValuePair("FromStop",""));
                    params.add(new BasicNameValuePair("FromStopVal",""));
                    params.add(new BasicNameValuePair("TillStop",""));
                    params.add(new BasicNameValuePair("TillStopVal",""));
                    params.add(new BasicNameValuePair("BusType",""));
                    params.add(new BasicNameValuePair("RouteNo",""));
                    params.add(new BasicNameValuePair("RouteName",""));
                    params.add(new BasicNameValuePair("KM","0"));
                    params.add(new BasicNameValuePair("Depot",""));
                    params.add(new BasicNameValuePair("TotalFare","0"));
                    params.add(new BasicNameValuePair("AdminFees","0"));
                    params.add(new BasicNameValuePair("CardFees","0"));
//                    params.add(new BasicNameValuePair("TransactionDate","6/2/2021"));,tvExpiryDate
                    params.add(new BasicNameValuePair("TransactionDate",transDate));
                //    params.add(new BasicNameValuePair("ExpiryDate","6/2/2022"));
                    params.add(new BasicNameValuePair("ExpiryDate",endYear));
                    params.add(new BasicNameValuePair("BaseFare","0"));
                    params.add(new BasicNameValuePair("Acc","0"));
                    params.add(new BasicNameValuePair("HR","0"));
                    params.add(new BasicNameValuePair("Octroi","0"));
                    params.add(new BasicNameValuePair("IT","0"));
                    params.add(new BasicNameValuePair("Toll","0"));
                    params.add(new BasicNameValuePair("ConcessionType","1"));
                    params.add(new BasicNameValuePair("ConcessionCode","MCT"));
                    params.add(new BasicNameValuePair("PassPeriod","5"));
                  //  params.add(new BasicNameValuePair("StartDate","6/2/2021"));
                    params.add(new BasicNameValuePair("StartDate",transDate));
                    params.add(new BasicNameValuePair("OPFlag","I"));
                    params.add(new BasicNameValuePair("HexPhoto","gjhgjhg"));
                    params.add(new BasicNameValuePair("OutPara",""));
                    params.add(new BasicNameValuePair("AadharNo","4544545454554545"));



            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getRoute(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK  || responseCode == 201) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    fromStop.append(line);
                    dp = dp + fromStop;
                    Log.e("fromStop= "+ "", line.toString());
                }

                JSONArray array = new JSONArray(dp);

                String fss = null,fss2 = null;
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    fss = object.getString("outMsg");
                    fss2 = object.getString("appId");

                         Log.e("cardNo = "+ "", ""+fss2);
                }
                cardNo = fss2;
                Log.e("outMsg = "+ "", ""+fss);
                Log.e("appId = "+ "", ""+fss2);

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
            //result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            if (pair.getName() != "ExpiryDate" && pair.getName()!="TransactionDate" && pair.getName()!="DOB" && pair.getName()!="EmailID" && pair.getName()!="CreatedOn" && pair.getName()!="StartDate") {
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            } else {
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

    private void getAllRegisterData() {

            pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            appId = pref.getString("ApplicantId", "appid");
            name = pref.getString("name", "name_defined");//" name defined" is the default value.
            mName = pref.getString("middle_name", "mn");
            lName = pref.getString("last_name", "ln");
            mob = pref.getString("mobile", "mb");
            eml = pref.getString("email", "em");
            addr = pref.getString("address", "add");
            proof = pref.getString("id_proof", "prf");
            title = pref.getString("title", "ttl");
            gen = pref.getString("gen", "gender");
            id = pref.getString("idproof", "proof");
            dob = pref.getString("dob", "db");
           String  bmImgg = pref.getString("hex1", "hex1");

        uriImg = bmImgg;
         //   imgUri = pref.getString("uriImage", "image2");
            //   imgUri = pref.getString("image_uri", "uri");

            Log.e("ApplicantId : ", appId);
            Log.e("ud_name : ", name);
            Log.e("ud_idName : ", mName);
            Log.e("last_name : ", lName);
            Log.e("mob : ", mob);
            Log.e("eml : ", eml);
            Log.e("addr : ", addr);
            Log.e("proof : ", proof);
          //  Log.e("image1 : ", bmImg);
         //   Log.e("image2 : ", imgUri);
            //  Log.e("imgUri : ", imgUri);

        }

}