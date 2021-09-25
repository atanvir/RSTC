package com.example.rsrtcs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rsrtcs.ui.activity.auth.login.LoginActivity;
import com.example.rsrtcs.ui.activity.drawer.RechargeCardActivity;
import com.example.rsrtcs.ui.activity.report.ReportActivity;

import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner, idSpinner, titleSpinner, passSpinner;
    String[] gender = {"MALE", "FEMALE", "OTHER", "UNKNOWN"};
    // AutoCompleteTextView textView;
    TextView date_text_view, applicant_Id;
    Button uNext, uClear;
    String df, mf, dob;
    String res = null;

    StringBuilder output = new StringBuilder();
    TextView textView;

    ArrayList<String> arrayList = new ArrayList<>();
    String depo;

    String dataParsed = "";
    String singleParsed = "";
    ListView lv;

    EditText firstName, middleName, lastName, mobile, email, addr, proofDetl, phoneNo;

    String fName, mName, lName, mob, eml, addres, proofs, date;

    // Spinner spinner;

    List<String> list = new ArrayList<String>();

    Connection connect;
    String ConnectionResult = "";

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    String title, gen, idproof;
    ProgressBar progressBar;

    StringBuilder fromStop = new StringBuilder();
    String dataParsed1 = "";
    List<String> list1 = new ArrayList<String>();
    ImageView ivHumberger;
    DrawerLayout drawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicant_Id = findViewById(R.id.id);
        drawerLayout = findViewById(R.id.drawerLayout);
        ivHumberger = findViewById(R.id.ivHumberger);
        firstName = findViewById(R.id.first_name);
        middleName = findViewById(R.id.middle_name);
        lastName = findViewById(R.id.last_name);
        mobile = findViewById(R.id.mobile_no);
        email = findViewById(R.id.email_id);
        addr = findViewById(R.id.address);
        phoneNo = findViewById(R.id.phn_no);
        proofDetl = findViewById(R.id.proof_details);
        progressBar = findViewById(R.id.simpleProgressBar);
        findViewById(R.id.tvRechargeCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RechargeCardActivity.class));
            }
        });
        findViewById(R.id.tvReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            }
        });
        findViewById(R.id.tvLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        date_text_view = findViewById(R.id.date_pick);
        uNext = findViewById(R.id.next);
        uClear = findViewById(R.id.clear);
        //  uClear = findViewById(R.id.clear);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        titleSpinner = findViewById(R.id.title_spinner);
        spinner = findViewById(R.id.gender_spinner);
        idSpinner = findViewById(R.id.id_proof_spinner);
        passSpinner = findViewById(R.id.pass_spinner);

        ApplicantId(); // Generate automatic applicant id.

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        textView = findViewById(R.id.gender_textView);
//        textView = getResources()
        titleSpinner.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(this);
        idSpinner.setOnItemSelectedListener(this);
        passSpinner.setOnItemSelectedListener(this);
        ivHumberger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //   ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,gender);

        ArrayAdapter<CharSequence> title_Adapter = ArrayAdapter.createFromResource(this, R.array.title, android.R.layout.simple_spinner_item);
        title_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(title_Adapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.id_proof, android.R.layout.simple_spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        idSpinner.setAdapter(adapter2);

        getDepotInfo();  // API Method call
        getProofId();   // proofId API

        date_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });


        uNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                fName = firstName.getText().toString();
                mName = middleName.getText().toString();
                lName = lastName.getText().toString();
                mob = mobile.getText().toString();
                eml = email.getText().toString();
                addres = addr.getText().toString();
                date = date_text_view.getText().toString();
                proofs = proofDetl.getText().toString();


                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("ApplicantId", res);
                editor.putString("name", fName);
                editor.putString("middle_name", mName);
                editor.putString("last_name", lName);
                editor.putString("mobile", mob);
                editor.putString("email", eml);
                editor.putString("address", addres);
                editor.putString("id_proof", proofs);
                editor.putString("title", title);
                editor.putString("gen", gen);
                editor.putString("idproof", idproof);
                editor.putString("dob", dob);
                editor.commit();

                // inserDataToSQL(res,fName,mName,lName,mob);
                //  inserDataToSQL(res,fName,mName,lName,mob,eml,addres,proofs,title,gen,idproof,dob);


                //int pos = titleSpinner.getPositionForView(0)

                      validations(fName,mName,lName,mob,eml,addres,proofs);

//                Intent intent = new Intent(MainActivity.this, ImageCaptureActivity.class);
//                startActivity(intent);
//                finish();

                //  progressBar.setVisibility(View.GONE);
                //    Toast.makeText(MainActivity.this, "Data Uploaded Successfully!", Toast.LENGTH_SHORT).show();

            }
        });

        uClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName.getText().clear();
                middleName.getText().clear();
                lastName.getText().clear();
                mobile.getText().clear();
                email.getText().clear();
                addr.getText().clear();
                proofDetl.getText().clear();
                phoneNo.getText().clear();


                //   inserDataToSQL(res,fName,mName,lName,mob,eml,addres,proofs,title,gen,idproof,dob);

            }
        });

    }

    //======================
    private void getProofId() {

        URL url = null;
        try {
            url = new URL("http://115.124.127.204/rsrtcapi/GetProofMaster");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");  // charset=UTF-8
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("stopName",enterFromStop )); //enterFromStop
//            params.add(new BasicNameValuePair("tillStop", "SKR"));
//            params.add(new BasicNameValuePair("busType", "EXP"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            //  writer.write(getRoute(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == 201) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    fromStop.append(line);
                    //  textView.setText(output);
                    dataParsed1 = dataParsed1 + fromStop;
                    Log.e("ID= " + "", line.toString());
                }
                JSONArray array = new JSONArray(dataParsed1);

                list1.add("Select ID");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    list1.add(object.getString("proofName"));
                }
                //  etFromStop.setText(fs);
                Log.e("ID = " + "", "" + list1);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list1);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idSpinner.setAdapter(adapter);

            } else {
                Log.e("responceJKT= " + "", "");
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


//======

    // private void inserDataToSQL(String res,String fName, String mName, String lName,String mob) {
    private void inserDataToSQL(String res, String fName, String mName, String lName, String mob, String eml, String addres, String proofs, String title, String gen, String idproof, String dob) {

        try {
            ConnectionHelper helper = new ConnectionHelper();
            connect = helper.connectionClass();

          //  fName,mName,lName,mob,eml,addres,proofs;

            if (connect != null){
                Log.e("ApplicantID= "+ "", res);
                Log.e("fName= "+ "", fName);
                Log.e("mName= "+ "", mName);
                Log.e("lName= "+ "", lName);
                Log.e("mob= "+ "", mob);

//                String query = "INSERT INTO MMemberNewRegistration(ApplicantID,FName,MName,LName,MobileNo)" +
//                        " VALUES('"+ res +"','"+ fName +"','"+ mName +"','"+ lName +"','"+ mob +"')" ;

                String query = "INSERT INTO MMemberNewRegistration(ApplicantID,NameTitle,FName,MName,LName,Gender,DOB,MobileNo,EmailID,Address,ProofID,ProofDetails)" +
                        " VALUES('"+ res +"','"+ title +"','"+ fName +"','"+ mName +"','"+ lName +"','"+gen +"',convert(DATETIME,Convert(NVARCHAR(10),'" + dob + "',103),103),'"+ mob +"','"+eml +"','"+addres +"','"+ idproof+"','"+ proofs +"')" ;

//                String updatequery1 = "MMemberNewRegistration SET NameTitle='"+ title +"',FName='"+ fName +"',MName='"+ mName +"',LName='"+ lName +"',Gender='"+gen +"',DOB=convert(DATETIME,Convert(NVARCHAR(10)'" + dob + "',103),103),MobileNo='"+ mob +"',EmailID='"+eml +"',Address='"+addres +"',ProofID='"+ idproof+"',ProofDetails='"+ proofs +"')" +
//                        " where ApplicantID= '"+ res +"'" ;

//                String updatequery1 = "UPDATE MMemberNewRegistration SET NameTitle='"+ res +"',FName='Devanand',MName='ABC',LName='Dharashive',Gender='M',DOB=convert(DATETIME,Convert(NVARCHAR(10),'20/08/2021',103),103),MobileNo='64979764994',EmailID='gsvb@gmail.com'
//                        ,Address='gavaccs',ProofID='1',ProofDetails='Driving License' where ApplicantID='"+ res +"'"";

                // " VALUES('res ','fName','mName','lName')" ;

                Statement statement = connect.createStatement();
                statement.executeQuery(query);
//                ResultSet rs = statement.executeQuery(query);
//                Log.e("ress : ", String.valueOf(rs));
//
//                String res = null;
//                while (rs.next()){
//
//                    res = (rs.getString(""));   // get Allpicant ID
////                            et.append(res + "\n");
//
//                    //  str.add(rs.getString("Concession_Type_Name"));
//
//                    //   datass.add(rs.getString("Concession_Type_Name"));
////                            Map<String,String> dtList = new HashMap<String, String>();
//                }
//                Log.e("ressssss : ", String.valueOf(res));
//                applicant_Id.setText(res);    // set Allpicant ID

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

    private void ApplicantId() {

        try {
            ConnectionHelper helper = new ConnectionHelper();
            connect = helper.connectionClass();

            if (connect != null){
                String query = "Select isnull(max(cast(ApplicantID as integer))+1,1) from MMemberNewRegistration ";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                Log.e("ress : ", String.valueOf(rs));

                while (rs.next()){

                            res = (rs.getString(""));   // get Allpicant ID
//                            et.append(res + "\n");

                  //  str.add(rs.getString("Concession_Type_Name"));

                    //   datass.add(rs.getString("Concession_Type_Name"));
//                            Map<String,String> dtList = new HashMap<String, String>();
                }
                Log.e("ressssss : ", String.valueOf(res));
                applicant_Id.setText(res);    // set Allpicant ID

                ConnectionResult = "Success";
//                isSuccess = true;
                connect.close();
            }else {
                ConnectionResult = "Failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  return data;
        //  return datass

    }

    //==================================================================

    private void validations(String fName, String mName, String lName, String mob, String eml, String addres, String proofs) {

        progressBar.setVisibility(View.GONE);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if (fName.isEmpty() || !fName.matches("[a-zA-Z]{2,15}")){
           // if (fName.isEmpty() || !fName.matches("[a-zA-Z]"{3,30}""){
                firstName.setError("Enter Valid First Name!");
                firstName.requestFocus();
                return;
            }
            if (mName.isEmpty() || !mName.matches("[a-zA-Z]{2,15}")){
                middleName.setError("Enter Valid Middle Name!");
                middleName.requestFocus();
                return;
            }
            if (lName.isEmpty() || !lName.matches("[a-zA-Z]{2,15}")){
                lastName.setError("Enter Valid Last Name!");
                lastName.requestFocus();
                return;
            }
            if (mob.isEmpty() ){
                mobile.setError("Enter Mobile Number!");
                mobile.requestFocus();
                return;
            }
            if ( mob.equals("0000000000")){
                mobile.setError("Enter Valid Mobile Number!");
                mobile.requestFocus();
                return;
            }
            if (mob.length() !=10 || !mob.matches("[0-9]{10}")){
                mobile.setError("Mobile number must be 10 digits.");
                mobile.requestFocus();
                return;
            }
           // if (eml.isEmpty() || !eml.matches(emailPattern)){
            if (eml.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(eml).matches()){
                email.setError("Invalid email address!");
                email.requestFocus();
                return;
            }
            if (addres.isEmpty()){
                addr.setError("Enter Address Details!");
                addr.requestFocus();
                return;
            }
            if (proofs.isEmpty()){
                proofDetl.setError("Enter Proof Details!");
                proofDetl.requestFocus();
                return;
            }
            if (date.isEmpty()){
                Toast.makeText(this, "Please Select Date!", Toast.LENGTH_SHORT).show();
//                date.set("Please Select Date!");
//                date.requestFocus();
              //  date.isEmpty()
                return;
            }

            if (titleSpinner.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Please Select Title!", Toast.LENGTH_SHORT).show();

                // titleSpinner.getSelectedItemPosition(0).e
                titleSpinner.requestFocus();
                return;
            }
         if (spinner.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please Select Gender!", Toast.LENGTH_SHORT).show();

            // titleSpinner.getSelectedItemPosition(0).e
            spinner.requestFocus();
            return;
        }
        if (idSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please Select Proof ID!", Toast.LENGTH_SHORT).show();

            // titleSpinner.getSelectedItemPosition(0).e
            idSpinner.requestFocus();
            return;
        }
        if (passSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please Select Depot!", Toast.LENGTH_SHORT).show();

            // titleSpinner.getSelectedItemPosition(0).e
            passSpinner.requestFocus();
            return;
        }

            Intent intent = new Intent(MainActivity.this,ImageCaptureActivity.class);
            startActivity(intent);
        }


    private void SelectDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                if (dayOfMonth < 10){
                    df = "0" + dayOfMonth;
                } else {
                    df = String.valueOf(dayOfMonth);
                }
                if ((month + 1 < 10)){
                    mf = "0" + (month + 1);
                } else {
                    mf = String.valueOf(month + 1);
                }

              //  date_text_view.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                date_text_view.setText(df + "/" + (mf) + "/" + year);
                dob = df + "/" + (mf) + "/" + year;
                Log.e("dob : ", dob);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            String gen = parent.getItemAtPosition(position).toString();
//            spinner.setTag(gen);

            switch (parent.getId()){

                case R.id.title_spinner:

//                    if (position == 0){
//                        Toast.makeText(this, "Please Select Title!", Toast.LENGTH_SHORT).show();
//                    }else {
                        title = parent.getItemAtPosition(position).toString();
                        Log.e("title= "+ "", title);
                   // }
                    break;

                case R.id.gender_spinner:
                    String gen1 = parent.getItemAtPosition(position).toString();
                    if (gen1.equals("Male")){
                        gen = "M";
                        Log.e("gen= "+ "", gen);
                    }else if (gen1.equals("Female")){
                        gen = "F";
                        Log.e("gen= "+ "", gen);
                    }else {
                        gen = "O";
                        Log.e("gen= "+ "", gen);
                    }

                    break;

                case R.id.id_proof_spinner:
                  String idproof1 = parent.getItemAtPosition(position).toString();
                  idproof = "1";
                    Log.e("idproof= "+ "", idproof);
                    break;


            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getDepotInfo() {

        URL url = null;
        try {
           // url = new URL("http://122.15.66.234:8080/RFIDAPI/getDepotInfo");
            url = new URL("http://115.124.127.204/rsrtcapi/GetDepotMaster");
            //  url= new URL("http://122.15.66.234:8080/RFIDAPI/busType");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
         //   conn.setRequestMethod("GET");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == 201) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    output.append(line);
                    dataParsed = dataParsed + output;
                    Log.e("responceJKT= "+ "", line.toString());

                }

                JSONArray array = new JSONArray(dataParsed);

                list.add("Select Depot");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    //   depo =  object.getString("depotName") + "\n";
                    list.add(object.getString("depotName"));
                    // +    object.getString("depotCd") + "\n" +

                    singleParsed = singleParsed + depo + "\n";
                    // arrayList.add(depo);
                    //    Log.e("arrayList = "+ "", ""+depo);
                    Log.e("arrayList = "+ "", ""+singleParsed);
                }
                // arrayList.add(singleParsed);
                //         list.add(singleParsed);
                Log.e("Real arrayList = "+ "", ""+arrayList);

//                for (int j = 0; j < arrayList.size(); j++){
//                    list.add(arrayList.get(j) + "\n");
//                }

                //  spinner.setAdapter(new ArrayAdapter<String>(SpinnerActivity.this, android.R.layout.simple_spinner_dropdown_item, list));

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                passSpinner.setAdapter(adapter);
                //  ArrayAdapter adapter = new ArrayAdapter(MainActivity2.this,)
//                    arrayAdapter = new ArrayAdapter<String>(this,R.layout.jsondata, R.id.tv_data, Collections.singletonList(singleParsed));
//

            }
            else {
                Log.e("error = "+ "", "");
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
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
        //       1
    }

    private String getDepot(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException
    {
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

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

        String phNo = phoneNo.getText().toString();

        firstName.setText(fName);
        middleName.setText(mName);
        lastName.setText(lName);
        mobile.setText(mob);
        email.setText(eml);
        addr.setText(addres);
        proofDetl.setText(proofs);
        phoneNo.setText(phNo);
    }
}