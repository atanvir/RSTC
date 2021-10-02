package com.example.rsrtcs.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationModel {

    @SerializedName("DEPOID")
    @Expose
    private String depoid;
    @SerializedName("POINTOFSALEID")
    @Expose
    private String pointofsaleid;
    @SerializedName("ApplicantID")
    @Expose
    private String applicantID;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("FName")
    @Expose
    private String first_name;
    @SerializedName("MName")
    @Expose
    private String middle_name;
    @SerializedName("LName")
    @Expose
    private String last_name;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("ProofID")
    @Expose
    private String proofID;
    @SerializedName("ProofDetails")
    @Expose
    private String proofDetails;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("PhotoIDProofID")
    @Expose
    private String photoIDProofID;
    @SerializedName("photoIDProofData")
    @Expose
    private String photoIDProofData;
    @SerializedName("ConcessionApplicableDocumentProofID")
    @Expose
    private String concessionApplicableDocumentProofID;
    @SerializedName("ConcessionApplicableDocumentProofFileData")
    @Expose
    private String concessionApplicableDocumentProofFileData;
    @SerializedName("AddressProofID")
    @Expose
    private String addressProofID;
    @SerializedName("AddressProofFileData")
    @Expose
    private String addressProofFileData;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("PassType")
    @Expose
    private String passType;
    @SerializedName("PassHolder")
    @Expose
    private String passHolder;
    @SerializedName("RFIDPassType")
    @Expose
    private String rFIDPassType;
    @SerializedName("PassValidity")
    @Expose
    private String passValidity;
    @SerializedName("FromStop")
    @Expose
    private String fromStop;
    @SerializedName("FromStopVal")
    @Expose
    private String fromStopVal;
    @SerializedName("TillStop")
    @Expose
    private String tillStop;
    @SerializedName("TillStopVal")
    @Expose
    private String tillStopVal;
    @SerializedName("BusType")
    @Expose
    private String busType;
    @SerializedName("RouteNo")
    @Expose
    private String routeNo;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("KM")
    @Expose
    private String km;
    @SerializedName("Depot")
    @Expose
    private String depot;
    @SerializedName("TotalFare")
    @Expose
    private String totalFare;
    @SerializedName("AdminFees")
    @Expose
    private String adminFees;
    @SerializedName("CardFees")
    @Expose
    private String cardFees;
    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("ExpiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("BaseFare")
    @Expose
    private String baseFare;
    @SerializedName("Acc")
    @Expose
    private String acc;
    @SerializedName("HR")
    @Expose
    private String hr;
    @SerializedName("Octroi")
    @Expose
    private String octroi;
    @SerializedName("IT")
    @Expose
    private String it;
    @SerializedName("Toll")
    @Expose
    private String toll;
    @SerializedName("ConcessionType")
    @Expose
    private String concessionType;
    @SerializedName("ConcessionCode")
    @Expose
    private String concessionCode;
    @SerializedName("PassPeriod")
    @Expose
    private String passPeriod;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("OPFlag")
    @Expose
    private String oPFlag;
    @SerializedName("HexPhoto")
    @Expose
    private String hexPhoto;
    @SerializedName("OutPara")
    @Expose
    private String outPara;
    @SerializedName("AadharNo")
    @Expose
    private String aadharNo;

    public ApplicationModel(){

    }

    public ApplicationModel(String depoid, String pointofsaleid, String applicantID, String title, String first_name, String middle_name, String last_name, String gender, String dob, String mobileNo, String emailID, String phoneNo, String address, String proofID, String proofDetails, String photo, String photoIDProofID, String photoIDProofData, String concessionApplicableDocumentProofID, String concessionApplicableDocumentProofFileData, String addressProofID, String addressProofFileData, String remarks, String createdBy, String createdOn, String passType, String passHolder, String rFIDPassType, String passValidity, String fromStop, String fromStopVal, String tillStop, String tillStopVal, String busType, String routeNo, String routeName, String km, String depot, String totalFare, String adminFees, String cardFees, String transactionDate, String expiryDate, String baseFare, String acc, String hr, String octroi, String it, String toll, String concessionType, String concessionCode, String passPeriod, String startDate, String oPFlag, String hexPhoto, String outPara, String aadharNo) {
        this.depoid = depoid;
        this.pointofsaleid = pointofsaleid;
        this.applicantID = applicantID;
        this.title = title;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.gender = gender;
        this.dob = dob;
        this.mobileNo = mobileNo;
        this.emailID = emailID;
        this.phoneNo = phoneNo;
        this.address = address;
        this.proofID = proofID;
        this.proofDetails = proofDetails;
        this.photo = photo;
        this.photoIDProofID = photoIDProofID;
        this.photoIDProofData = photoIDProofData;
        this.concessionApplicableDocumentProofID = concessionApplicableDocumentProofID;
        this.concessionApplicableDocumentProofFileData = concessionApplicableDocumentProofFileData;
        this.addressProofID = addressProofID;
        this.addressProofFileData = addressProofFileData;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.passType = passType;
        this.passHolder = passHolder;
        this.rFIDPassType = rFIDPassType;
        this.passValidity = passValidity;
        this.fromStop = fromStop;
        this.fromStopVal = fromStopVal;
        this.tillStop = tillStop;
        this.tillStopVal = tillStopVal;
        this.busType = busType;
        this.routeNo = routeNo;
        this.routeName = routeName;
        this.km = km;
        this.depot = depot;
        this.totalFare = totalFare;
        this.adminFees = adminFees;
        this.cardFees = cardFees;
        this.transactionDate = transactionDate;
        this.expiryDate = expiryDate;
        this.baseFare = baseFare;
        this.acc = acc;
        this.hr = hr;
        this.octroi = octroi;
        this.it = it;
        this.toll = toll;
        this.concessionType = concessionType;
        this.concessionCode = concessionCode;
        this.passPeriod = passPeriod;
        this.startDate = startDate;
        this.oPFlag = oPFlag;
        this.hexPhoto = hexPhoto;
        this.outPara = outPara;
        this.aadharNo = aadharNo;
    }

    public String getDepoid() {
        return depoid;
    }

    public void setDepoid(String depoid) {
        this.depoid = depoid;
    }

    public String getPointofsaleid() {
        return pointofsaleid;
    }

    public void setPointofsaleid(String pointofsaleid) {
        this.pointofsaleid = pointofsaleid;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProofID() {
        return proofID;
    }

    public void setProofID(String proofID) {
        this.proofID = proofID;
    }

    public String getProofDetails() {
        return proofDetails;
    }

    public void setProofDetails(String proofDetails) {
        this.proofDetails = proofDetails;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoIDProofID() {
        return photoIDProofID;
    }

    public void setPhotoIDProofID(String photoIDProofID) {
        this.photoIDProofID = photoIDProofID;
    }

    public String getPhotoIDProofData() {
        return photoIDProofData;
    }

    public void setPhotoIDProofData(String photoIDProofData) {
        this.photoIDProofData = photoIDProofData;
    }

    public String getConcessionApplicableDocumentProofID() {
        return concessionApplicableDocumentProofID;
    }

    public void setConcessionApplicableDocumentProofID(String concessionApplicableDocumentProofID) {
        this.concessionApplicableDocumentProofID = concessionApplicableDocumentProofID;
    }

    public String getConcessionApplicableDocumentProofFileData() {
        return concessionApplicableDocumentProofFileData;
    }

    public void setConcessionApplicableDocumentProofFileData(String concessionApplicableDocumentProofFileData) {
        this.concessionApplicableDocumentProofFileData = concessionApplicableDocumentProofFileData;
    }

    public String getAddressProofID() {
        return addressProofID;
    }

    public void setAddressProofID(String addressProofID) {
        this.addressProofID = addressProofID;
    }

    public String getAddressProofFileData() {
        return addressProofFileData;
    }

    public void setAddressProofFileData(String addressProofFileData) {
        this.addressProofFileData = addressProofFileData;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getPassHolder() {
        return passHolder;
    }

    public void setPassHolder(String passHolder) {
        this.passHolder = passHolder;
    }

    public String getrFIDPassType() {
        return rFIDPassType;
    }

    public void setrFIDPassType(String rFIDPassType) {
        this.rFIDPassType = rFIDPassType;
    }

    public String getPassValidity() {
        return passValidity;
    }

    public void setPassValidity(String passValidity) {
        this.passValidity = passValidity;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getFromStopVal() {
        return fromStopVal;
    }

    public void setFromStopVal(String fromStopVal) {
        this.fromStopVal = fromStopVal;
    }

    public String getTillStop() {
        return tillStop;
    }

    public void setTillStop(String tillStop) {
        this.tillStop = tillStop;
    }

    public String getTillStopVal() {
        return tillStopVal;
    }

    public void setTillStopVal(String tillStopVal) {
        this.tillStopVal = tillStopVal;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getAdminFees() {
        return adminFees;
    }

    public void setAdminFees(String adminFees) {
        this.adminFees = adminFees;
    }

    public String getCardFees() {
        return cardFees;
    }

    public void setCardFees(String cardFees) {
        this.cardFees = cardFees;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getOctroi() {
        return octroi;
    }

    public void setOctroi(String octroi) {
        this.octroi = octroi;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getToll() {
        return toll;
    }

    public void setToll(String toll) {
        this.toll = toll;
    }

    public String getConcessionType() {
        return concessionType;
    }

    public void setConcessionType(String concessionType) {
        this.concessionType = concessionType;
    }

    public String getConcessionCode() {
        return concessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        this.concessionCode = concessionCode;
    }

    public String getPassPeriod() {
        return passPeriod;
    }

    public void setPassPeriod(String passPeriod) {
        this.passPeriod = passPeriod;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getoPFlag() {
        return oPFlag;
    }

    public void setoPFlag(String oPFlag) {
        this.oPFlag = oPFlag;
    }

    public String getHexPhoto() {
        return hexPhoto;
    }

    public void setHexPhoto(String hexPhoto) {
        this.hexPhoto = hexPhoto;
    }

    public String getOutPara() {
        return outPara;
    }

    public void setOutPara(String outPara) {
        this.outPara = outPara;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }


}
