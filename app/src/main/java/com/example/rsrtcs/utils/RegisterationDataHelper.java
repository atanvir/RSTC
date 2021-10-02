package com.example.rsrtcs.utils;

import com.example.rsrtcs.model.request.ApplicationModel;

public class RegisterationDataHelper {

    private ApplicationModel model;
    public static RegisterationDataHelper registerationDataHelper;
    private RegisterationDataHelper(){

    }

    public  static RegisterationDataHelper getInstance(){
        if(registerationDataHelper==null) registerationDataHelper=new RegisterationDataHelper();
        return registerationDataHelper;
    }

    public void setApplicationData(ApplicationModel model){
        this.model=model;
    }

    public ApplicationModel getApplicationData(){
         return model;
    }
}
