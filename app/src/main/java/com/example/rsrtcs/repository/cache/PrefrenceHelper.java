package com.example.rsrtcs.repository.cache;

import static com.example.rsrtcs.repository.cache.PrefrenceKeyConstant.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rsrtcs.model.request.ApplicationModel;
import com.example.rsrtcs.model.request.LoginModel;
import com.example.rsrtcs.model.response.AuthModel;
import com.example.rsrtcs.ui.activity.concession.ConcessionDetail;

public class PrefrenceHelper {

    public static void saveUserData(Context context, AuthModel model){
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.EMAIL_ID,model.getEmailId()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.PHONE_NO,model.getMobileNo()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.FULL_NAME,model.getUserName()).apply();
    }

    public static String getPrefrenceStringValue(Context context, String KEY){
        return  context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY, "");
    }

    public static int getPrefrenceIntValue(Context context, String KEY){
        return  context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(KEY, 0);
    }



    public static SharedPreferences.Editor writePrefrencesValue(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
    }


    public static void saveStepOneData(Context context, ApplicationModel model){
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.Application_id,model.getApplicantId()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.FIRST_NAME,model.getName()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.MIDDLE_NAME,model.getMiddle_name()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.LAST_NAME,model.getLast_name()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.PHONE_NO,model.getMobile()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.EMAIL_ID,model.getEmail()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ADDRESS,model.getAddress()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ID_PROOFS,model.getId_proof()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.TITLE,model.getTitle()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.GENDER,model.getGen()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ID_PROOF,model.getIdproof()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.DOB,model.getDob()).apply();
    }


    public static void saveConcessionDetail(Context context, String MM, String ED, String SELECTED_1, String SELECTED_2, String SELECTED_3) {
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.MM,MM).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ED,ED).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.SELECTED_1,SELECTED_1).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.SELECTED_2,SELECTED_2).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.SELECTED_3,SELECTED_3).apply();

    }
}
