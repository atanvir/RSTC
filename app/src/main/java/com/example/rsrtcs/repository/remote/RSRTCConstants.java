package com.example.rsrtcs.repository.remote;

import java.io.Serializable;

public interface RSRTCConstants extends Serializable {
    String SIGNUP="MobileLoginRegistor";
    String LOGIN="MobileLogin";
    String FORGOT="ForgetPass";
    String CARD_DATA="GetCardData";
    String ONLINE_RECHARGE="ReportOnlineRecharge";
    String DEPOT="GetDepotMaster";
    String PROOF="GetProofMaster";
    String CONCESSION_TYPE="GetConcessionTypeMaster";
    String SEND_SMS="SendSMSServiceNew";

    String STOP_NAME="stopName";
    String ROUTE_DETAIL="getRouteDetail";
    String SAVE_REGISTRATION="SaveRegistration";
    String GET_CONCESSION_MASTER="GetConcessionMaster";
    String BILL_DESK_REQUEST="BillDeskRequest";
    String BILL_DESK_RESPONSE="BillDeskResponse";
    String DOCUMENT_TYPE="GetDocumentType";
    String DOCUMENT_CODE="GetConcessionDoc";
    String BILLDESK_PAYLOAD="GetCheckSum";
}
