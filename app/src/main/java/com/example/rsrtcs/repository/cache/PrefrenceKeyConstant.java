package com.example.rsrtcs.repository.cache;

import java.io.Serializable;
import java.util.Locale;

public interface PrefrenceKeyConstant extends Serializable {
    String PREF_NAME="MyPrefs";
    String FULL_NAME="full_name";
    String BUS_TYPE="EXP";

    // Registration
    String Application_id="ApplicantId";
    String MM="mm";
    String ED="ed";
    String SELECTED_1="selected1";
    String SELECTED_2="selected2";
    String SELECTED_3="selected3";
    String FIRST_NAME="name";
    String MIDDLE_NAME="middle_name";
    String LAST_NAME="last_name";
    String EMAIL_ID="email";
    String PHONE_NO="mobile";
    String ADDRESS="address";
    String ID_PROOFS="id_proof";
    String TITLE="title";
    String GENDER="gen";
    String ID_PROOF="idproof";
    String DOB="dob";

    String FEES ="40" ;
    String REQUEST_TYPE_BILLDESK = "MobileApp";
    String BDSKUATY = "RSRTCRFID";
    String BILL_DESK_DUMP_URL = "https://rsrtcrfidsystem.co.in/pg_dump.aspx";
//    String BILL_DESK_DUMP_URL = "https://uat.billdesk.com/pgidsk/pgijsp/banksimulator.jsp";
}
