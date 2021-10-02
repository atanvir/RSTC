package com.example.rsrtcs.ui.activity.concession;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.rsrtcs.repository.local.ConnectionHelper;
import com.example.rsrtcs.ui.activity.calculation.FixPassFareCalculation;
import com.example.rsrtcs.R;
import com.example.rsrtcs.ui.activity.route.SelectRouteActivity;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityConcessionDetailBinding;
import com.example.rsrtcs.model.request.ConcessionCodeModel;
import com.example.rsrtcs.model.request.SpinnerDataModel;
import com.example.rsrtcs.model.request.StopModel;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.utils.CommonUtils;
import com.example.rsrtcs.utils.RegisterationDataHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcessionDetailActivity extends BaseActivity<ActivityConcessionDetailBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private RSRTCInterface apiInterface= new RSRTCConnection().createServiceRoute();
    private RSRTCInterface apiInterfaceRSTC= new RSRTCConnection().createService();
    private String concessStdPassenger;
    private List<SpinnerDataModel> concessionCodeList= new ArrayList<>();

    @Override
    protected ActivityConcessionDetailBinding getActivityBinding() {
        return ActivityConcessionDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        RegisterationDataHelper.getInstance().getApplicationData().setBusType("EXP");
        CommonUtils.setSpinner(binding.pasPeriod,R.array.pass_period);
        CommonUtils.setSpinner(binding.spinnerBusType1,R.array.bus_type);
        CommonUtils.setSpinner(binding.conTypeSpinner,getConsessionType());
    }

    @Override
    protected void initCtrl() {
        binding.conTypeSpinner.setOnItemSelectedListener(this);
        binding.conCodeSpinner1.setOnItemSelectedListener(this);
        binding.pasPeriod.setOnItemSelectedListener(this);
        binding.spinnerBusType1.setOnItemSelectedListener(this);
        binding.btnNext.setOnClickListener(this);
        binding.fromStop1.setOnClickListener(this);
        binding.tillStop1.setOnClickListener(this);
    }

    private void postStopNameApi(String stop,String type) {
        showLoadingDialog(this);
        apiInterface.stopName(new StopModel(stop)).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                dismissLoadingDialog();
                if(response.isSuccessful()){
//                    concessionCodeList=response.body();
                    for(SpinnerDataModel model: response.body()){
                        if(type.equalsIgnoreCase("from")) {
                          if(stop.toLowerCase(Locale.ROOT).equals(model.getBusStopCode().toLowerCase(Locale.ROOT)))  {
                              RegisterationDataHelper.getInstance().getApplicationData().setFromStop(model.getBusStopCode());
                              RegisterationDataHelper.getInstance().getApplicationData().setFromStopVal(model.getBusStopName());
                              break;
                          }
                        }
                        else{
                            if(stop.toLowerCase(Locale.ROOT).equals(model.getBusStopCode().toLowerCase(Locale.ROOT)))  {
                                RegisterationDataHelper.getInstance().getApplicationData().setTillStop(model.getBusStopCode());
                                RegisterationDataHelper.getInstance().getApplicationData().setTillStopVal(model.getBusStopName());
                                break;
                            }

                        }
                    }
                    if(type.equalsIgnoreCase("from")) binding.etFromStop1.setText(RegisterationDataHelper.getInstance().getApplicationData().getFromStopVal());
                    else binding.etTillStop1.setText(RegisterationDataHelper.getInstance().getApplicationData().getTillStopVal());
                }else{
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

    private List<String> getConsessionType() {
       List<String> list=new ArrayList<>();
        try {
            ConnectionHelper helper = new ConnectionHelper();
            Connection connect = helper.connectionClass();
            if (connect != null){
                String query = "select * from Master_Consession_Type ";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                list.add(0,"Select");
                while (rs.next()){
                    list.add(rs.getString("Concession_Type_Name"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private void concessionCodeApi(String ConcessionType) {
        showLoadingDialog(this);
        apiInterfaceRSTC.getConcessionMaster(new ConcessionCodeModel(ConcessionType)).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                dismissLoadingDialog();
                if(response.isSuccessful()){
                    RegisterationDataHelper.getInstance().getApplicationData().setConcessionType(ConcessionType);
                    concessionCodeList=response.body();
                    List<String> list=new ArrayList<>();
                    list.add(0,"Please select");
                    for(SpinnerDataModel model: response.body()){
                        list.add(model.getConcessionName());
                    }
                    CommonUtils.setSpinner(binding.conCodeSpinner1,list);
                }else{
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.con_type_spinner : checkConcessionType(parent.getItemAtPosition(position).toString()); break;

            case R.id.con_code_spinner1 :
            if(position>0) RegisterationDataHelper.getInstance().getApplicationData().setConcessionCode(concessionCodeList.get(position - 1).getConcessionCode());
            checkConcessionCode(parent.getItemAtPosition(position).toString());
            break;

            case R.id.pas_period :
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, position);
            binding.datePick.setText(format.format(cal.getTime()));
            RegisterationDataHelper.getInstance().getApplicationData().setPassPeriod(""+position);
            RegisterationDataHelper.getInstance().getApplicationData().setPassValidity(""+position);
            RegisterationDataHelper.getInstance().getApplicationData().setExpiryDate(binding.datePick.getText().toString());
            break;

            case R.id.spinner_bus_type1: Log.e("a","a"); break;
        }

    }



    private void checkConcessionType(String type) {
        switch (type){
            case "CONCESSION":  concessionCodeApi("1"); break;
            case "PASS": binding.llSelectStop.setVisibility(View.GONE); concessionCodeApi("2"); break;
        }
    }

    private void checkConcessionCode(String type) {
        concessStdPassenger = type;
        switch (type){
            case "Student Passenger":
            binding.llSelectStop.setVisibility(View.VISIBLE);
            binding.etFromStop1.getText().clear();
            binding.etTillStop1.getText().clear();
            break;

            case "Rajasthan Police Force":
            binding.llPosition.setVisibility(View.VISIBLE);
            break;

            default:
            binding.llSelectStop.setVisibility(View.GONE);
            binding.llPosition.setVisibility(View.GONE);
            break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.from_stop1:
            if (binding.etFromStop1.getText().toString().isEmpty()) showSnackBar(binding.getRoot(),"Please Enter From Stop!");
            else postStopNameApi(binding.etFromStop1.getText().toString(),"from");
            break;

            case R.id.till_stop1:
            if (binding.etTillStop1.getText().toString().isEmpty()) showSnackBar(binding.getRoot(), "Please Enter End Stop!");
            else postStopNameApi(binding.etTillStop1.getText().toString(),"till");
            break;

            case R.id.btn_next:
            if(checkValidation()) startActivity(new Intent(this,FixPassFareCalculation.class));
            break;

        }
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.conTypeSpinner.getSelectedItemPosition()==0 || binding.conCodeSpinner1.getSelectedItemPosition()==0 || binding.pasPeriod.getSelectedItemPosition()==0){
            ret=false;
            showSnackBar(binding.getRoot(),"Please Select All Fields!");
        }else if(concessStdPassenger.equals("Student Passenger")){
            ret=false;
            if (binding.etFromStop1.getText().toString().isEmpty() || binding.etTillStop1.getText().toString().isEmpty()) showSnackBar(binding.getRoot(), "Please Enter Stops!");
            else if (binding.spinnerBusType1.getSelectedItemPosition() == 0) showSnackBar(binding.getRoot(), "Please Select Express Bus type!");
            else startActivity(new Intent(this,SelectRouteActivity.class));
        }
        return ret;
    }
}