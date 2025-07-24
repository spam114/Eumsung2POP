package com.symbol.eumsung2pop.model.object;

import java.util.ArrayList;

public class Common {
    public int SeqNo;
    public ArrayList<Zones> ZoneList;
    public ArrayList<String> PartNameList;
    public ArrayList<InventorySurvey> InventorySurveyList;
    public String ErrorCheck = "";
    //public ScanResult ScanResult;
    //public ScanResult2 ScanResult2;
    public ArrayList<ItemTags> ItemTagList;
    public StockOut StockOutData;
    public PackingMaster PackingMaster;
    public StockOutDetail StockOutDetailData;
    public Packing Packing;
    public ArrayList<StockOutDetail> StockOutDetailList;
    public ArrayList<Input> InputList;
    public ArrayList<Packing> PackingList;
    public String StrResult = "";
    public String StrResult2 = "";

    //public ArrayList<Dept> DeptList;
    //public ArrayList<Customer> CustomerList;
    //public ArrayList<Area> AreaList;
    //public ArrayList<StockOutDetail> StockOutDetailList;
    //public ArrayList<ScanListViewItem2> ScanListViewItem2List;
    //public ArrayList<Bundle> BundleList;
}