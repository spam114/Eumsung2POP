package com.symbol.eumsung2pop.view;

import com.google.gson.internal.LinkedTreeMap;
import com.symbol.eumsung2pop.model.object.AppVersion;

import java.lang.reflect.Field;

/**
 * Object 형을 원하는 Type으로 변경한다.
 */
public class TypeChanger {

    public static AppVersion changeTypeAppVersion(Object object) {
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
        Field fields[] = AppVersion.class.getFields();
        AppVersion rData = new AppVersion();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(rData, linkedTreeMap.get(fields[i].getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rData;
    }

    /*public static StockIn changeTypeStockIn(Object object) {
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
        Field fields[] = StockIn.class.getFields();
        StockIn rData = new StockIn();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(rData, linkedTreeMap.get(fields[i].getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rData;
    }

    public static WoPartHist changeTypeWoPartHist(Object object) {
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
        Field fields[] = WoPartHist.class.getFields();
        WoPartHist rData = new WoPartHist();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(rData, linkedTreeMap.get(fields[i].getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rData;
    }

    public static ArrayList<BusinessClass> changeTypeBusinessClassList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<BusinessClass> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) arrayList.get(i);
                Field fields[] = BusinessClass.class.getFields();
                BusinessClass rData = new BusinessClass();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static ArrayList<StockIn> changeTypeStockInList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<StockIn> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = arrayList.get(i);
                Field fields[] = StockIn.class.getFields();
                StockIn rData = new StockIn();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static ArrayList<WoPartHist> changeTypeWoPartHistList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<WoPartHist> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = arrayList.get(i);
                Field fields[] = WoPartHist.class.getFields();
                WoPartHist rData = new WoPartHist();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }


    public static ArrayList<SalesOrder> changeTypeSalesOrderList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<SalesOrder> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = arrayList.get(i);
                Field fields[] = SalesOrder.class.getFields();
                SalesOrder rData = new SalesOrder();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static ArrayList<CheckTag> changeTypeCheckTagList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<CheckTag> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = arrayList.get(i);
                Field fields[] = CheckTag.class.getFields();
                CheckTag rData = new CheckTag();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }


    public static ArrayList<NumConvertData> changeTypeNumConvertDataList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<NumConvertData> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = arrayList.get(i);
                Field fields[] = NumConvertData.class.getFields();
                NumConvertData rData = new NumConvertData();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }*/
}
