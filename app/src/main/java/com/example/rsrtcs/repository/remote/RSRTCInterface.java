package com.example.rsrtcs.repository.remote;

import static com.example.rsrtcs.repository.remote.RSRTCConstants.BILL_DESK_REQUEST;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.BILL_DESK_RESPONSE;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.CARD_DATA;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.CONCESSION_TYPE;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.DEPOT;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.DOCUMENT_CODE;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.DOCUMENT_TYPE;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.FORGOT;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.GET_CONCESSION_MASTER;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.ONLINE_RECHARGE;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.PROOF;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.ROUTE_DETAIL;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.SAVE_REGISTRATION;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.SEND_SMS;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.LOGIN;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.SIGNUP;
import static com.example.rsrtcs.repository.remote.RSRTCConstants.STOP_NAME;

import android.widget.Spinner;

import com.example.rsrtcs.model.request.ApplicationModel;
import com.example.rsrtcs.model.request.BillDeskRequestModel;
import com.example.rsrtcs.model.request.BillDeskResponseModel;
import com.example.rsrtcs.model.request.ConcessionCodeModel;
import com.example.rsrtcs.model.request.RouteModel;
import com.example.rsrtcs.model.request.SpinnerDataModel;
import com.example.rsrtcs.model.request.CardDataModel;
import com.example.rsrtcs.model.request.ForgotModel;
import com.example.rsrtcs.model.request.ReportModel;
import com.example.rsrtcs.model.request.SpinnerRequestModel;
import com.example.rsrtcs.model.request.StopModel;
import com.example.rsrtcs.model.response.AuthModel;
import com.example.rsrtcs.model.request.LoginModel;
import com.example.rsrtcs.model.request.SMSModel;
import com.example.rsrtcs.model.request.SignupModel;
import com.example.rsrtcs.model.response.BillDeskModel;
import com.example.rsrtcs.model.response.CardModel;
import com.example.rsrtcs.model.response.RegistrationModel;

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
    Call<List<AuthModel>> forgotPassword(@Body ForgotModel data);


    @POST(CARD_DATA)
    Call<List<CardModel>>  getCardData(@Body CardDataModel model);


    @POST(ONLINE_RECHARGE)
    Call<List<CardModel>>  report(@Body ReportModel model);


    @POST(DEPOT)
    Call<List<SpinnerDataModel>> depotApi();

    @POST(PROOF)
    Call<List<SpinnerDataModel>> getProofApi();


    @POST(CONCESSION_TYPE)
    Call<List<SpinnerDataModel>> getConcessionTypeMaster();


    @POST(STOP_NAME)
    Call<List<SpinnerDataModel>> stopName(@Body StopModel model);

    @POST(GET_CONCESSION_MASTER)
    Call<List<SpinnerDataModel>> getConcessionMaster(@Body ConcessionCodeModel model);


    @POST(ROUTE_DETAIL)
    Call<List<com.example.rsrtcs.model.response.RouteModel>> getRouteDetailApi(@Body RouteModel model);

    @POST(SAVE_REGISTRATION)
    Call<List<RegistrationModel>> saveRegistration(@Body ApplicationModel model);

    @POST(BILL_DESK_REQUEST)
    Call<List<BillDeskModel>> billDeskRequest(@Body BillDeskRequestModel model);

    @POST(BILL_DESK_RESPONSE)
    Call<List<BillDeskModel>> billDeskResponse(@Body BillDeskResponseModel model);

    @POST(DOCUMENT_TYPE)
    Call<List<SpinnerDataModel>> getDocumentType(@Body SpinnerRequestModel model);


    @POST(DOCUMENT_CODE)
    Call<List<SpinnerDataModel>> getConcessionDoc(@Body SpinnerRequestModel model);
}
