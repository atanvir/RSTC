package com.example.rsrtcs.model.request;

public class ApplicationModel {
    private String ApplicantId;
    private String name;
    private String middle_name;
    private String last_name;
    private String mobile;
    private String phoneNo;
    private String email;
    private String address;
    private String id_proof;
    private String title;
    private String gen;
    private String idproof;
    private String proofDetail;

    private String dob;



    public ApplicationModel(String applicantId, String name, String middle_name, String last_name, String mobile,String phoneNo, String email, String address, String id_proof, String title, String gen, String idproof,String proofDetail, String dob) {
        ApplicantId = applicantId;
        this.name = name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.mobile = mobile;
        this.phoneNo=phoneNo;
        this.email = email;
        this.address = address;
        this.id_proof = id_proof;
        this.title = title;
        this.gen = gen;
        this.idproof = idproof;
        this.proofDetail=proofDetail;
        this.dob = dob;
    }

    public String getApplicantId() {
        return ApplicantId;
    }

    public void setApplicantId(String applicantId) {
        ApplicantId = applicantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_proof() {
        return id_proof;
    }

    public void setId_proof(String id_proof) {
        this.id_proof = id_proof;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getIdproof() {
        return idproof;
    }

    public void setIdproof(String idproof) {
        this.idproof = idproof;
    }

    public String getProofDetail() {
        return proofDetail;
    }

    public void setProofDetail(String proofDetail) {
        this.proofDetail = proofDetail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }



}
