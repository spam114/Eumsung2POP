package com.symbol.eumsung2pop.model.object;

import com.google.gson.annotations.SerializedName;

public class StockIn {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("PartSpec")
    public String PartSpec="";

    @SerializedName("InQty")
    public double InQty;

    @SerializedName("ActualWeight")
    public double ActualWeight;

    @SerializedName("ShortNo")
    public String ShortNo="";

    @SerializedName("PartName")
    public String PartName="";

    @SerializedName("MakeDate")
    public String MakeDate="";

    @SerializedName("CheckDate")
    public String CheckDate="";

    @SerializedName("StockInNo")
    public String StockInNo="";

    @SerializedName("PartCode")
    public String PartCode="";

    @SerializedName("WorkingGroup")
    public double WorkingGroup;

    @SerializedName("WorkingMachine")
    public double WorkingMachine;
}
