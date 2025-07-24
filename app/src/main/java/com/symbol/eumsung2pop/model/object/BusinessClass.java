package com.symbol.eumsung2pop.model.object;
import com.google.gson.annotations.SerializedName;

public class BusinessClass {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("BusinessClassCode")
    public double BusinessClassCode;

    @SerializedName("CompanyName")
    public String CompanyName="";

    @SerializedName("CustomerCode")
    public String CustomerCode="";

    @SerializedName("LocationNo")
    public double LocationNo;
}
