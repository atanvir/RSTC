package com.example.rsrtcs.ui.activity.main;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import androidx.core.view.GravityCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.rsrtcs.ConnectionHelper;
import com.example.rsrtcs.ui.activity.capture.ImageCaptureActivity;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityMainBinding;
import com.example.rsrtcs.model.request.ApplicationModel;
import com.example.rsrtcs.model.request.SpinnerDataModel;
import com.example.rsrtcs.repository.cache.PrefrenceHelper;
import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.ui.activity.auth.login.LoginActivity;
import com.example.rsrtcs.ui.activity.drawer.card.RechargeCardActivity;
import com.example.rsrtcs.utils.CommonUtils;
import  com.example.rsrtcs.ui.activity.drawer.report.ReportActivity;
import com.example.rsrtcs.utils.MultiTextWatcher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, MultiTextWatcher.TextWatcherWithInstance {
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private Calendar calendar = Calendar.getInstance();


    @Override
    protected ActivityMainBinding getActivityBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        depotApi();
    }

    @Override
    protected void initCtrl() {
        binding.ivHumberger.setOnClickListener(this);
        binding.clDrawer.tvRechargeCard.setOnClickListener(this);
        binding.clDrawer.tvReport.setOnClickListener(this);
        binding.clDrawer.tvLogout.setOnClickListener(this);
        binding.tieDOB.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.clear.setOnClickListener(this);

        binding.titleSpinner.setOnItemSelectedListener(this);
        binding.genderSpinner.setOnItemSelectedListener(this);
        binding.idProofSpinner.setOnItemSelectedListener(this);
        binding.passSpinner.setOnItemSelectedListener(this);

        new MultiTextWatcher().registerEditText(binding.tieFirstName)
                              .registerEditText(binding.tieMiddleName)
                              .registerEditText(binding.tieLastName)
                              .registerEditText(binding.tieDOB)
                              .registerEditText(binding.tieMobileNo)
                              .registerEditText(binding.tiePhoneNo)
                              .registerEditText(binding.tieAddress)
                              .registerEditText(binding.tieProofDetail)
                              .setCallback(this);
    }


    private String generateApplicationId() {
        String res="";
        try {
            ConnectionHelper helper = new ConnectionHelper();
            Connection connect = helper.connectionClass();

            if (connect != null){
                String query = "Select isnull(max(cast(ApplicantID as integer))+1,1) from MMemberNewRegistration ";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                  res = (rs.getString(""));
                }
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getData().getTitle().equalsIgnoreCase("") || binding.getData().getTitle().equalsIgnoreCase("Title")){
            ret=false;
            showSnackBar(binding.getRoot(),"Please select title");
            binding.titleSpinner.requestFocus();
        }
        else if(binding.getData().getName().trim().equalsIgnoreCase("")){
            ret=false;
            binding.tilFirstName.setErrorEnabled(true);
            binding.tilFirstName.setError("Please enter first name");
            binding.tilFirstName.requestFocus();
        }
    /* else if(binding.getData().getMiddle_name().trim().equalsIgnoreCase("")){
            ret=false;
            binding.tilMiddleName.setErrorEnabled(true);
            binding.tilMiddleName.setError("Please enter middle name");
            binding.tilMiddleName.requestFocus();
        }*/
        else if(binding.getData().getLast_name().trim().equalsIgnoreCase("")){
            ret=false;
            binding.tilLastName.setErrorEnabled(true);
            binding.tilLastName.setError("Please enter last name");
            binding.tilLastName.requestFocus();
        }
        else if(binding.getData().getGen().equalsIgnoreCase("") || binding.getData().getGen().equalsIgnoreCase("Gender")){
            ret=false;
            showSnackBar(binding.getRoot(),"Please select gender");
            binding.genderSpinner.requestFocus();
        } else if(binding.getData().getDob().equalsIgnoreCase("")){
            ret=false;
            binding.tilDOB.setErrorEnabled(true);
            binding.tilDOB.setError("Please select DOB");
            binding.tilDOB.requestFocus();
        }
        else if(binding.getData().getMobile().equalsIgnoreCase("")){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter mobile number");
            binding.tilMobileNo.requestFocus();
        }
        else if(binding.getData().getMobile().length()!=10){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter valid mobile number");
            binding.tilMobileNo.requestFocus();
        }else if(binding.getData().getEmail().equalsIgnoreCase("")){
            ret=false;
            binding.tilEmailId.setErrorEnabled(true);
            binding.tilEmailId.setError("Please enter email id");
            binding.tilEmailId.requestFocus();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmail()).matches()){
            ret=false;
            binding.tilEmailId.setErrorEnabled(true);
            binding.tilEmailId.setError("Please enter valid email id");
            binding.tilEmailId.requestFocus();
        }
        else if(binding.getData().getPhoneNo().length()>0 && binding.getData().getPhoneNo().trim().length()!=10){
            ret=false;
            binding.tilPhoneNo.setErrorEnabled(true);
            binding.tilPhoneNo.setError("Please enter valid phone no");
            binding.tilPhoneNo.requestFocus();
        }

//        else if(binding.getData().getMo().equalsIgnoreCase("")){
//            ret=false;
//            binding.tilPhoneNo.setErrorEnabled(true);
//            binding.tilPhoneNo.setError("Please enter phone number");
//            binding.tilPhoneNo.requestFocus();
//        }
        else if(binding.getData().getAddress().equalsIgnoreCase("")){
            ret=false;
            binding.tilAddress.setErrorEnabled(true);
            binding.tilAddress.setError("Please enter address");
            binding.tilAddress.requestFocus();
        }
        else if(binding.getData().getIdproof().equalsIgnoreCase("") || binding.getData().getIdproof().equalsIgnoreCase("Select Proof")){
            ret=false;
            showSnackBar(binding.getRoot(),"Please select proof ");
        }
        else if(binding.getData().getProofDetail().equalsIgnoreCase("")){
            ret=false;
            binding.tilProofDetail.setErrorEnabled(true);
            binding.tilProofDetail.setError("Please enter proof detail");
            binding.tilProofDetail.requestFocus();
        }

        else if(binding.getData().getId_proof().equalsIgnoreCase("") || binding.getData().getId_proof().equalsIgnoreCase("Select Depot")){
            ret=false;
            showSnackBar(binding.getRoot(),"Please select depot");
        }
        return ret;
    }


    private void datePicker() {
        new DatePickerDialog(this,this,
                              calendar.get(Calendar.YEAR),
                              calendar.get(Calendar.MONTH),
                              calendar.get(Calendar.DATE)).show();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
         case R.id.title_spinner: binding.getData().setTitle(parent.getItemAtPosition(position).toString()); break;
         case R.id.gender_spinner: binding.getData().setGen(genderApiKey(parent.getItemAtPosition(position).toString()));  break;
         case R.id.id_proof_spinner: binding.getData().setIdproof(/*parent.getItemAtPosition(position).toString()*/"1"); break;
         case R.id.pass_spinner: binding.getData().setId_proof(parent.getItemAtPosition(position).toString()); break;
         }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        showSnackBar(binding.getRoot(),"Error");
    }


    private String genderApiKey(String gender) {
        String key="";
        switch (gender){
            case "Male" : key="M"; break;
            case "Female" : key="F"; break;
            default: key="O"; break;
        }
        return key;
    }


    private void depotApi() {
        showLoadingDialog(this);
        apiInterface.depotApi().enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    List<String> list = new ArrayList<String>();
                    for(int i=0;i<response.body().size();i++){ list.add(response.body().get(i).getDepotName()); }
                    list.add(0,"Select Depot");
                    proofApi(list);
                }else{
                    dismissLoadingDialog();
                    showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private void proofApi(List<String> depotList) {
        apiInterface.getProofApi().enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                dismissLoadingDialog();
                if(response.isSuccessful()){
                List<String> list = new ArrayList<String>();
                for(int i=0;i<response.body().size();i++){ list.add(response.body().get(i).getProofName()); }
                list.add(0,"Select Proof");


                // All Spinner
                CommonUtils.setSpinner(binding.titleSpinner, R.array.title);
                CommonUtils.setSpinner(binding.genderSpinner, R.array.gender1);
                CommonUtils.setSpinner(binding.idProofSpinner,list);
                CommonUtils.setSpinner(binding.passSpinner,depotList);

                binding.setData(new ApplicationModel(""+(Long.parseLong(generateApplicationId())+(PrefrenceHelper.getPrefrenceIntValue(MainActivity.this,"count")+1)),"","","",PrefrenceHelper.getPrefrenceStringValue(MainActivity.this, PrefrenceKeyConstant.PHONE_NO),"",PrefrenceHelper.getPrefrenceStringValue(MainActivity.this, PrefrenceKeyConstant.EMAIL_ID),"","","","","","",""));
                }
                else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivHumberger: binding.drawerLayout.openDrawer(GravityCompat.START); break;
            case R.id.tvRechargeCard: startNewActivity(RechargeCardActivity.class); break;
            case R.id.tvReport: startNewActivity(ReportActivity.class); break;
            case R.id.tvLogout: startNewActivity(LoginActivity.class); break;
            case R.id.tieDOB: datePicker(); break;
            case R.id.next:
            if(checkValidation()){
               startNewActivity(ImageCaptureActivity.class);
               PrefrenceHelper.saveStepOneData(this,binding.getData());
            }
            break;
            case R.id.clear: clearText(); break;
        }
    }


    private void clearText() {
        binding.tieFirstName.getText().clear();
        binding.tieMiddleName.getText().clear();
        binding.tieLastName.getText().clear();
        binding.tieMobileNo.getText().clear();
        binding.tieEmailId.getText().clear();
        binding.tieAddress.getText().clear();
        binding.tieProofDetail.getText().clear();
        binding.tiePhoneNo.getText().clear();
        binding.tieDOB.getText().clear();
    }

    private void startNewActivity(Class className) {
        startActivity(new Intent(this,className));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat formattedDate=new SimpleDateFormat("dd/MM/yyyy");
        try {
            Calendar selectedDate=Calendar.getInstance();
            selectedDate.set(year,month,dayOfMonth);

            if(calendar.get(Calendar.YEAR)-selectedDate.get(Calendar.YEAR)<=5){
                binding.tilDOB.setErrorEnabled(true);
                binding.tilDOB.setError("Please select valid dob");
            }
            else{
                binding.tilDOB.setErrorEnabled(false);
//                binding.getData().setDob(formattedDate.format(formattedDate.parse(dayOfMonth+"/"+month+"/"+year)));
                binding.tieDOB.setText(formattedDate.format(formattedDate.parse(dayOfMonth+"/"+month+"/"+year)));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        switch (editText.getId()){
            case R.id.tieFirstName: binding.tilFirstName.setErrorEnabled(false); break;
            case R.id.tieMiddleName: binding.tilMiddleName.setErrorEnabled(false); break;
            case R.id.tieLastName: binding.tilLastName.setErrorEnabled(false); break;
            case R.id.tieDOB: binding.tilDOB.setErrorEnabled(false); break;
            case R.id.tieMobileNo: binding.tilMobileNo.setErrorEnabled(false); break;
            case R.id.tiePhoneNo: binding.tilPhoneNo.setErrorEnabled(false); break;
            case R.id.tieEmailId: binding.tilEmailId.setErrorEnabled(false); break;
            case R.id.tieAddress: binding.tilAddress.setErrorEnabled(false); break;
            case R.id.tieProofDetail: binding.tilProofDetail.setErrorEnabled(false); break;
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}