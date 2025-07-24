package com.symbol.eumsung2pop.model;

import com.google.gson.annotations.SerializedName;

public class SearchCondition {
    @SerializedName("FromDate")
    public String FromDate;

    @SerializedName("ToDate")
    public String ToDate;

    @SerializedName("ContractNo")
    public String ContractNo;

    @SerializedName("AppCode")
    public String AppCode;

    @SerializedName("AndroidID")
    public String AndroidID;

    @SerializedName("Model")
    public String Model;

    @SerializedName("PhoneNumber")
    public String PhoneNumber;

    @SerializedName("DeviceName")
    public String DeviceName;

    @SerializedName("DeviceOS")
    public String DeviceOS;

    @SerializedName("AppVersion")
    public String AppVersion;

    @SerializedName("Remark")
    public String Remark;

    @SerializedName("SaleType")
    public String SaleType;

    @SerializedName("UserID")
    public String UserID;

    @SerializedName("UserCode")
    public String UserCode;

    @SerializedName("PassWord")
    public String PassWord;

    @SerializedName("Language")
    public int Language;

    @SerializedName("NType")
    public String NType;

    @SerializedName("CustomerCode")
    public String CustomerCode;
    @SerializedName("Barcode")
    public String Barcode;
    @SerializedName("BusinessClassCode")
    public int BusinessClassCode;

    @SerializedName("PicFlag")
    public int PicFlag;

    @SerializedName("RepairNo")
    public String RepairNo;

    @SerializedName("SeqNo")
    public String SeqNo;

    @SerializedName("ZoneSeqNo")
    public String ZoneSeqNo;
    @SerializedName("Zone")
    public String Zone;

    @SerializedName("RepairPhoto1")
    public String RepairPhoto1;

    @SerializedName("RepairPhoto2")
    public String RepairPhoto2;

    @SerializedName("RepairPhoto3")
    public String RepairPhoto3;

    @SerializedName("RepairPhoto4")
    public String RepairPhoto4;

    @SerializedName("PicNo")
    public int PicNo;

    @SerializedName("InitFlag")
    public boolean InitFlag;

    @SerializedName("ItemTag")
    public String ItemTag;

    @SerializedName("ItemCnt")
    public double ItemCnt;

    @SerializedName("UseFlag")
    public int UseFlag;

    @SerializedName("StockOutNo")
    public String StockOutNo;

    @SerializedName("PartCode")
    public String PartCode;

    @SerializedName("PartSpec")
    public String PartSpec;

    @SerializedName("StockCommitNo")
    public String StockCommitNo;

    @SerializedName("CarNumber")
    public String CarNumber;

    /*@SerializedName("ScanList")
    public List<ScanListViewItem> ScanList;

    @SerializedName("ScanList2")
    public List<ScanListViewItem2> ScanList2;*/

    public SearchCondition(){}

}
