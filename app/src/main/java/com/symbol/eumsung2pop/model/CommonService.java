package com.symbol.eumsung2pop.model;


import com.symbol.eumsung2pop.model.object.Common;
import com.symbol.eumsung2pop.model.object.Users;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommonService {

    // 통신 설정 (우리 서버와 연결하는 내용)
    private static final String BASE_URL = Users.ServiceAddress; // 서버 주소 설정
    private static CommonService instance;//todo
    public static DataApi api = new Retrofit.Builder() // 서버와 연결
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class); // 서버와 연결하여 하나의 객체 형태로 통신 객체를 생성 -> 해당 api 객체를 통해 서버의 메소드를 호출 (DataApi 라는 인터페이스에 우리가 호출하는 메소드와 서버에서 작동하는 메소드를 기록)
    // 해당 코드를 통해 api가 서버와 연결되어 통신객체로 존재하게된다. api객체만 호출하면 서버와 연결되어있으므로 서버 함수 호출

    public static CommonService getInstance() { // getInstance 호출 시 본인 객체를 반환하며 api 서버 객체를 생성한다.
        if (instance == null) {
            instance = new CommonService();
            //통신 에러로그 확인을 위한 코드
            /*OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(DataApi.class);*/
        }
        return instance;
    }

    /*public Single<Common> GetTest() {// 객체의 사용//todo
        SearchCondition sc = new SearchCondition();
        sc.UserID = Users.UserID;
        return api.GetTest(sc);//todo
    }*/

    public Single<Common> Get(String apiName, SearchCondition sc) {// 객체의 사용//todo
        if (apiName.equals("GetRegRepairPhotoData")) {
            return api.GetRegRepairPhotoData(sc);
        }
        else if(apiName.equals("GetRegRepairPhotoDataDetail")){
            return api.GetRegRepairPhotoDataDetail(sc);
        }
        else if(apiName.equals("UpdateRegRepairPhoto")){
            return api.UpdateRegRepairPhoto(sc);
        }
        else if(apiName.equals("DeleteRegRepairPhoto")){
            return api.DeleteRegRepairPhoto(sc);
        }
        else if(apiName.equals("GetItemTag")){
            return api.GetItemTag(sc);
        }
        else if(apiName.equals("UpdateItemTag")){
            return api.UpdateItemTag(sc);
        }
        else if(apiName.equals("UpdateItemTagDisable")){
            return api.UpdateItemTagDisable(sc);
        }
        else if(apiName.equals("GetStockOutMaster")){
            return api.GetStockOutMaster(sc);
        }
        else if(apiName.equals("GetStockOut")){
            return api.GetStockOut(sc);
        }
        else if(apiName.equals("SetStockOut")){
            return api.SetStockOut(sc);
        }
        else if(apiName.equals("DeleteStockOut")){
            return api.DeleteStockOut(sc);
        }
        else if(apiName.equals("GetOneItemDataNew")){
            return api.GetOneItemDataNew(sc);
        }

        else if(apiName.equals("GetInput")){
            return api.GetInput(sc);
        }

        else if(apiName.equals("SetInput")){
            return api.SetInput(sc);
        }

        else if(apiName.equals("DeleteInput")){
            return api.DeleteInput(sc);
        }

        else if(apiName.equals("GetPackingMaster")){
            return api.GetPackingMaster(sc);
        }

        else if(apiName.equals("GetPacking")){
            return api.GetPacking(sc);
        }

        else if(apiName.equals("SetOrDeletePacking")){
            return api.SetOrDeletePacking(sc);
        }
        else if(apiName.equals("JudgeSetOrDeletePacking")){
            return api.JudgeSetOrDeletePacking(sc);
        }

        else if(apiName.equals("SetPacking")){
            return api.SetPacking(sc);
        }
        else if(apiName.equals("DeletePacking")){
            return api.DeletePacking(sc);
        }
        else if(apiName.equals("GetOneItemDataPacking")){
            return api.GetOneItemDataPacking(sc);
        }
        else if(apiName.equals("GetInventorySurveySeqNo")){
            return api.GetInventorySurveySeqNo(sc);
        }
        else if(apiName.equals("GetZone")){
            return api.GetZone(sc);
        }
        else if(apiName.equals("GetZoneSeqNo")){
            return api.GetZoneSeqNo(sc);
        }
        else if(apiName.equals("GetInventorySurvey")){
            return api.GetInventorySurvey(sc);
        }
        else if(apiName.equals("SetInventorySurvey")){
            return api.SetInventorySurvey(sc);
        }
        else if(apiName.equals("DeleteInventorySurvey")){
            return api.DeleteInventorySurvey(sc);
        }
        else if(apiName.equals("ChangeCarNumber")){
            return api.ChangeCarNumber(sc);
        }


        else {
            return null;
        }

        /*public Single<SData> GetStr(String apiName, SearchCondition sc) {// 객체의 사용//todo
            if (apiName.equals("GetPackingNo")) {
                return api.GetPackingNo();
            }
            else {
                return null;
            }
        }*/
    }
}
