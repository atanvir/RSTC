package com.example.rstc.repository;

import static com.example.rstc.repository.RSRTCConstants.CARD_DATA;
import static com.example.rstc.repository.RSRTCConstants.FORGOT;
import static com.example.rstc.repository.RSRTCConstants.ONLINE_RECHARGE;
import static com.example.rstc.repository.RSRTCConstants.SEND_SMS;
import static com.example.rstc.repository.RSRTCConstants.LOGIN;
import static com.example.rstc.repository.RSRTCConstants.SIGNUP;

import com.example.rstc.model.request.CardDataModel;
import com.example.rstc.model.request.ForgotModel;
import com.example.rstc.model.request.ReportModel;
import com.example.rstc.model.response.AuthModel;
import com.example.rstc.model.request.LoginModel;
import com.example.rstc.model.request.SMSModel;
import com.example.rstc.model.request.SignupModel;
import com.example.rstc.model.response.CardModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RSRTCInterface {

    @POST(SIGNUP)
    Call<AuthModel> signup(@Body SignupModel model);


    @POST(LOGIN)
    Call<List<AuthModel>> login(@Body LoginModel model);

    @POST(SEND_SMS)
    Call<String> sendSMS(@Body SMSModel model);

    @POST(FORGOT)
    Call<AuthModel> forgotPassword(@Body ForgotModel data);


    @POST(CARD_DATA)
    Call<CardModel>  getCardData(@Body CardDataModel model);


    @POST(ONLINE_RECHARGE)
    Call<List<CardModel>>  report(@Body ReportModel model);
}
