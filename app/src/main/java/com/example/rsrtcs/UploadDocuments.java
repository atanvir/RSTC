package com.example.rsrtcs;

import static com.example.rsrtcs.FixPassFareCalculation.fees;
import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.billdesk.sdk.PaymentOptions;
import com.example.rsrtcs.model.request.SpinnerDataModel;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.utils.CommonUtils;
import com.example.rsrtcs.utils.FilePath;
import com.example.rsrtcs.utils.ImageUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocuments extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();

    TextView textView1,textView2,textView3;
    Button btnUpload, selectBtn;

    private String encodedPDF;
    public static final int BROWSE_IMAGE = 1;
    private String path;
    Context context;

    Spinner idProof,concession,addrProof;
    String strPGMsg = "BDSKUATY|ARP1553593909862|NA|1|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgijsp/banksimulator.jsp";
    // String strPGMsg11 = "|NA|1|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://115.124.127.204/pg_dump.aspx";
    String strPGMsg11 = "|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://115.124.127.204/pg_dump.aspx";

    String A1 = "BDSKUATY";

    //// String strTokenMsg = "AIRMTST|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|723938585|CP1005!AIRMTST!D1DDC94112A3B939A4CFC76B5490DC1927197ABBC66E5BC3D59B12B552EB5E7DF56B964D2284EBC15A11643062FD6F63!NA!NA!NA";
    //String strTokenMsg="Test msg";
    String strTokenMsg = null;
    String cardNumber = null;


    Button back,register,clear;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences pref;

    String appId,name,mName,lName,mob,eml,addr,proof,title,gen,id,dob,imgUri;
    Connection connect;
    String ConnectionResult = "";

    String img1,img2,img3;

    byte[] byteArray1 = null,byteArray2 = null,byteArray3 = null;
    String transDate,endYear;
    ProgressBar progressBar;
    Uri img;
    String hex1,hex2,hex3;
    Date dt = new Date();
    StringBuilder fromStop = new StringBuilder();
    String dataParsed1 = "";
    private int requestCode=1;
    private int requestCode2=2;
    private int requestCode3=3;
    private String applicationId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        cardNumber = pref.getString("cardNo", "card");

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



        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        back = findViewById(R.id.btn_back);
        register = findViewById(R.id.btn_register);
        clear = findViewById(R.id.btn_clear);
      //  progressBar = findViewById(R.id.simpleProgressBar);
        //   btnUpload = findViewById(R.id.btn_select_doc);
      //  selectBtn = findViewById(R.id.select_doc);
        
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        appId = pref.getString("ApplicantId", "appid");
        endYear = pref.getString("ed", "end Date");
        transDate = pref.getString("transDate", "trans Date");
//        name = pref.getString("name", "name_defined");//" name defined" is the default value.
//        mName = pref.getString("middle_name", "mn");
//        lName = pref.getString("last_name", "ln");
//        mob = pref.getString("mobile", "mb");
//        eml = pref.getString("email", "em");
//        addr = pref.getString("address", "add");
//        proof = pref.getString("id_proof", "prf");
//        title = pref.getString("title", "ttl");
//        gen = pref.getString("gen", "gender");
//        id = pref.getString("idproof", "proof");
//        dob = pref.getString("dob", "db");
        //   imgUri = pref.getString("image_uri", "uri");

        Log.e("ApplicantId : ", appId);

//        StrictMode.ThreadPolicy policy = new
//                StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        idProof = (Spinner) findViewById(R.id.spinner_photo_id);
        concession = (Spinner) findViewById(R.id.spinner_concession);
        addrProof = (Spinner) findViewById(R.id.spinner_addressProof);

        idProof.setOnItemSelectedListener(this);
        concession.setOnItemSelectedListener(this);
        addrProof.setOnItemSelectedListener(this);

        showLoadingDialog(this);
        apiInterface.getConcessionTypeMaster().enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                dismissLoadingDialog();
                if(response.isSuccessful()){
                    List<String> list = new ArrayList<String>();
                    for(int i=0;i<response.body().size();i++){ list.add(response.body().get(i).getConcessionName()); }
                    list.add(0,"Select Proof ID");

                    CommonUtils.setSpinner(idProof,list);

//                    ArrayAdapter<CharSequence> passHolderAdapter1 = ArrayAdapter<String>(UploadDocuments.this, R.array.id_proof, android.R.layout.simple_spinner_item);
//                    passHolderAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    idProof.setAdapter(passHolderAdapter1);

                    ArrayAdapter<CharSequence> passHolderAdapter2 = ArrayAdapter.createFromResource(UploadDocuments.this, R.array.Concession_Proof, android.R.layout.simple_spinner_item);
                    passHolderAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    concession.setAdapter(passHolderAdapter2);


                    ArrayAdapter<CharSequence> passHolderAdapter3 = ArrayAdapter.createFromResource(UploadDocuments.this, R.array.Addresss_Proof, android.R.layout.simple_spinner_item);
                    passHolderAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    addrProof.setAdapter(passHolderAdapter3);

                }else{
                    dismissLoadingDialog();
                    Toast.makeText(UploadDocuments.this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                dismissLoadingDialog();
                Toast.makeText(UploadDocuments.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });








            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (idProof.getSelectedItemPosition() == 0) {
                        Toast.makeText(UploadDocuments.this, "Please Select Proof ID!", Toast.LENGTH_SHORT).show();
                      //  return;
                    } else {
                        if(checkPermission(requestCode)){
                            takePhotoIntent(requestCode);
                        }

                    }
                }
            });



            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (concession.getSelectedItemPosition() == 0) {
                        Toast.makeText(UploadDocuments.this, "Please Select Concession Proof!", Toast.LENGTH_SHORT).show();

                    } else {

                        if(checkPermission(requestCode2)){
                            takePhotoIntent(requestCode2);
                        }
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(galleryIntent,  2);
                    }
                }
            });

            textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (addrProof.getSelectedItemPosition() == 0) {
                        Toast.makeText(UploadDocuments.this, "Please Select Address Proof!", Toast.LENGTH_SHORT).show();
                    } else {


//
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
                        if(checkPermission(requestCode3)){
                            takePhotoIntent(requestCode3);
                        }
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(galleryIntent,  3);

                    }
                }
            });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(register.getText().toString().equals("Register")){


                if (idProof.getSelectedItemPosition() > 0 && concession.getSelectedItemPosition() > 0 && addrProof.getSelectedItemPosition() > 0 && hex1!=null && hex2!=null && hex3!=null) {
                    getAllRegisterData();
                    register.setText("Pay Now");
                    inserDataToSQL(appId, hex1, hex2, hex3);
                    Toast.makeText(UploadDocuments.this, "Documents Uploaded Successfully!", Toast.LENGTH_SHORT).show();
//                    finish();
                    startActivity(new Intent(UploadDocuments.this,PaymentActivity.class));

                }
                else {
                    Toast.makeText(UploadDocuments.this, "Please Select Proof Details!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                   startActivity(new Intent(UploadDocuments.this,PaymentActivity.class));
            }
            }
        });




        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("Choose File");
                textView2.setText("Choose File");
                textView3.setText("Choose File");
                register.setText("Register");
                hex1=null;
                hex2=null;
                hex3=null;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadDocuments.this,FixPassFareCalculation.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private boolean checkPermission(int requestCode) {
        boolean ret=true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, requestCode);
            }
        }

        return  ret;
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
                if(pair.getValue()!=null)
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
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
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

    //=========================================To Server================

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
     //   imgUri = pref.getString("image_uri", "uri");

        Log.e("ApplicantId : ", appId);
        Log.e("ud_name : ", name);
        Log.e("ud_idName : ", mName);
        Log.e("last_name : ", lName);
        Log.e("mob : ", mob);
        Log.e("eml : ", eml);
        Log.e("addr : ", addr);
        Log.e("proof : ", proof);
      //  Log.e("imgUri : ", imgUri);


    }

 //   }

  //  private void inserDataToSQL(String appId, String name, String mName, String lName, String mob, String eml, String addr, String proof, String title, String gen, String id, String dob) {
    private void inserDataToSQL(String appId, String hex1, String hex2, String hex3) {

//        ProgressDialog pdLoading = new ProgressDialog(UploadDocuments.this);
//
//        pdLoading.setMessage("\tLoading...");
//        pdLoading.show();


//              try {
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
//                String updatequery1 = "UPDATE MMemberNewRegistration SET PhotoIDProofDocument ='"+ hexxx1 + "',ConcessionApplicableProofDocument = '" + hexxx2 + "',AddressProofDocument = '" + hexxx3 + "' where ApplicantID='"+ appId +"'" ;
//                 //       ,Address='gavaccs',ProofID='1',ProofDetails='Driving License' where ApplicantID='"+ res +"'"";
//
//
//                Statement statement = connect.createStatement();
//                statement.executeQuery(updatequery1);
//
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
            params.add(new BasicNameValuePair(  "ApplicantID", appId));  //appId
            params.add(new BasicNameValuePair("PhotoIDProofDocument",hex1));
            params.add(new BasicNameValuePair("ConcessionApplicableProofDocument",hex2));
            params.add(new BasicNameValuePair(  "AddressProofDocument", hex3));  //appId

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
                    applicationId=fss2;

                    Toast.makeText(UploadDocuments.this, ""+applicationId, Toast.LENGTH_SHORT).show();

                    Log.e("cardNo = "+ "", ""+fss2);
                }
              //  cardNo = fss2;
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

    private String getRoute2(List<NameValuePair> params) throws UnsupportedEncodingException, JSONException {
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
//            if (pair.getName() != "ExpiryDate" && pair.getName()!="TransactionDate" && pair.getName()!="DOB" && pair.getName()!="EmailID" && pair.getName()!="CreatedOn" && pair.getName()!="StartDate") {
//                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
//            } else {
//                result.append(pair.getValue());
//            }
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


//===========================================================

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        //  if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

        if (requestCode == this.requestCode) {
            if (data != null) {
                path = FilePath.getPath(this, Uri.parse(data.getDataString()));
            }
//            File file = new Compressor(UploadDocuments.this).compressToFile(new File(path));

            try{
                Bitmap bitmap=ImageUtil.getBitmapFromUri2(UploadDocuments.this,new File(path));
                if(ImageUtil.checkImageSize(bitmap)){
                    hex1=ImageUtil.convertBaseString(bitmap);
                    textView1.setText("Selected");
                    img=Uri.parse(path);
                }else{
                    Toast.makeText(UploadDocuments.this, "Please upload photo upto 2 MB", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if(requestCode == this.requestCode2){
            if (data != null) {
                path = FilePath.getPath(this, Uri.parse(data.getDataString()));
            }

            try{
                Bitmap bitmap=ImageUtil.getBitmapFromUri2(UploadDocuments.this,new File(path));
                if(ImageUtil.checkImageSize(bitmap)){
                    hex2=ImageUtil.convertBaseString(bitmap);

                    textView2.setText("Selected");
                    img=Uri.parse(path);
                }else{
                    Toast.makeText(UploadDocuments.this, "Please upload photo upto 2 MB", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(requestCode == this.requestCode3) {
            if (data != null) {
                path = FilePath.getPath(this, Uri.parse(data.getDataString()));
            }

            try{
                Bitmap bitmap=ImageUtil.getBitmapFromUri2(UploadDocuments.this,new File(path));
                if(ImageUtil.checkImageSize(bitmap)){
                    hex3=ImageUtil.convertBaseString(bitmap);

                    textView3.setText("Selected");
                    img=Uri.parse(path);
                }else{
                    Toast.makeText(UploadDocuments.this, "Please upload photo upto 2 MB", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("NonConstantResourceId")
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String pos = parent.getItemAtPosition(position).toString();
//        int pp = Integer.parseInt(pos);

        String mm = null;
//        String gen = parent.getItemAtPosition(0).toString();
//         item = parent.getItemAtPosition(1).toString();
//        mPassHolder.setTag(gen);

        switch (parent.getId()){
            case R.id.spinner_photo_id :
                img1 = parent.getItemAtPosition(position).toString();
                Log.e("img1= "+ "", img1);
                break;

            case R.id.spinner_concession :
                img2 = parent.getItemAtPosition(position).toString();
                Log.e("img2= "+ "", img2);
                break;

            case R.id.spinner_addressProof :
                img3 = parent.getItemAtPosition(position).toString();
                Log.e("img3= "+ "", img3);
                break;

//                if (position == 1){
//                    // if (parent.getItemAtPosition(1).equals("Monthly")){
//                    //  mm = "30D";
//                    tv_month.setText("30D");
//                    break;
//                }else if (position == 2){
//                    tv_month.setText("60D");
//                    mm = "60D";
//                    break;
//                }else if (position == 3) {
//                    tv_month.setText("90D");
//                    break;
//                }
//

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller1 extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(UploadDocuments.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected String doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            StringBuilder hex = new StringBuilder(byteArray1.length * 2);
            for (byte b : byteArray1)
                hex.append(String.format("%02x", b));

           String hexxx1 = String.valueOf(hex);
           hex1 = hexxx1;
            //   return hex.ToString();

           // Log.e("hex1="+ "", String.valueOf(hex));
            Log.e("hexxx1="+ "", hexxx1);
            return hexxx1.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            //this method will be running on UI thread
            Toast.makeText(UploadDocuments.this, "Image Loaded!", Toast.LENGTH_SHORT).show();
           // Toast.makeText(MainActivity.this, hexxx1, Toast.LENGTH_SHORT).show();
            Log.e("hex111111111111111="+ "", String.valueOf(hex1));

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller2 extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(UploadDocuments.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected String doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            StringBuilder hex = new StringBuilder(byteArray2.length * 2);
            for (byte b : byteArray2)
                hex.append(String.format("%02x", b));

           String hexxx2 = String.valueOf(hex);
            hex2 = hexxx2;
            //   return hex.ToString();

          //  Log.e("hex2="+ "", String.valueOf(hex));
            Log.e("hexxx2="+ "", hexxx2);
            return hexxx2;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            //this method will be running on UI thread
            Toast.makeText(UploadDocuments.this, "Image Loaded!", Toast.LENGTH_SHORT).show();
            // Toast.makeText(MainActivity.this, hexxx1, Toast.LENGTH_SHORT).show();
            Log.e("hex222222222222222="+ "", String.valueOf(hex2));

        }

    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller3 extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(UploadDocuments.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected String doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            StringBuilder hex = new StringBuilder(byteArray3.length * 2);
            for (byte b : byteArray3)
                hex.append(String.format("%02x", b));

           String hexxx3 = String.valueOf(hex);
            hex3 = hexxx3;
            //   return hex.ToString();

          //  Log.e("hex3="+ "", String.valueOf(hex));
            Log.e("hexxx3="+ "", hexxx3);
            return hexxx3;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            //this method will be running on UI thread
            Toast.makeText(UploadDocuments.this, "Image Loaded!", Toast.LENGTH_SHORT).show();
            // Toast.makeText(MainActivity.this, hexxx1, Toast.LENGTH_SHORT).show();
            Log.e("hex333333333333333333="+ "", String.valueOf(hex3));

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isPermissionDenied=false;
        for(int i=0;i<grantResults.length;i++)
        {
            if(!(grantResults[i]==PackageManager.PERMISSION_GRANTED))
            {isPermissionDenied=true;
                break;
            }
        }
        if(!isPermissionDenied)
        {
            takePhotoIntent(requestCode);
        }
        }


    private void takePhotoIntent(int code) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/RSRTC/Images/";
        File file = new File(capture_dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        path = capture_dir + System.currentTimeMillis() + ".jpg";
        Uri imageFileUri = FileProvider.getUriForFile(Objects.requireNonNull(this.getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtils().getPickIntent(this,imageFileUri);
        startActivityForResult(intent, code);
    }


}


