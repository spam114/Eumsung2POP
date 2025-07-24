package com.symbol.eumsung2pop.model;

import com.symbol.eumsung2pop.model.object.AppVersion;
import com.symbol.eumsung2pop.model.object.Common;
import com.symbol.eumsung2pop.model.object.LoginInfo;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface DataApi {
    // @Headers({"Content-Type: application/json"})
    //@FormUrlEncoded
    @POST("CheckAppVersion")
    Single<AppVersion> CheckAppVersion(@Body SearchCondition searchCondition);

    @POST("checkAppProgramsPowerAndLoginHistory")
    Single<LoginInfo> checkAppProgramsPowerAndLoginHistory(@Body SearchCondition searchCondition);

    @POST("InsertAppLoginHistory")
    Single<AppVersion> InsertAppLoginHistory(@Body SearchCondition searchCondition);

    @POST("GetNoticeData2")
    Single<Object> GetNoticeData2(@Body SearchCondition searchCondition);

    @POST("GetUserImage")
    Single<String> GetUserImage(@Body SearchCondition searchCondition);

    @POST("GetRegRepairPhotoData")
    Single<Common> GetRegRepairPhotoData(@Body SearchCondition searchCondition);

    @POST("GetRegRepairPhotoDataDetail")
    Single<Common> GetRegRepairPhotoDataDetail(@Body SearchCondition searchCondition);

    @POST("UpdateRegRepairPhoto")
    Single<Common> UpdateRegRepairPhoto(@Body SearchCondition searchCondition);

    @POST("DeleteRegRepairPhoto")
    Single<Common> DeleteRegRepairPhoto(@Body SearchCondition searchCondition);

    @POST("GetItemTag")
    Single<Common> GetItemTag(@Body SearchCondition searchCondition);

    @POST("UpdateItemTag")
    Single<Common> UpdateItemTag(@Body SearchCondition searchCondition);

    @POST("UpdateItemTagDisable")
    Single<Common> UpdateItemTagDisable(@Body SearchCondition searchCondition);

    @POST("GetStockOutMaster")
    Single<Common> GetStockOutMaster(@Body SearchCondition searchCondition);

    @POST("GetStockOut")
    Single<Common> GetStockOut(@Body SearchCondition searchCondition);

    @POST("SetStockOut")
    Single<Common> SetStockOut(@Body SearchCondition searchCondition);

    @POST("DeleteStockOut")
    Single<Common> DeleteStockOut(@Body SearchCondition searchCondition);

    @POST("GetOneItemDataNew")
    Single<Common> GetOneItemDataNew(@Body SearchCondition searchCondition);

    @POST("GetInput")
    Single<Common> GetInput(@Body SearchCondition searchCondition);

    @POST("SetInput")
    Single<Common> SetInput(@Body SearchCondition searchCondition);

    @POST("DeleteInput")
    Single<Common> DeleteInput(@Body SearchCondition searchCondition);

    @POST("GetPackingMaster")
    Single<Common> GetPackingMaster(@Body SearchCondition searchCondition);

    @POST("GetPacking")
    Single<Common> GetPacking(@Body SearchCondition searchCondition);

    @POST("SetOrDeletePacking")
    Single<Common> SetOrDeletePacking(@Body SearchCondition searchCondition);

    @POST("JudgeSetOrDeletePacking")
    Single<Common> JudgeSetOrDeletePacking(@Body SearchCondition searchCondition);
    @POST("SetPacking")
    Single<Common> SetPacking(@Body SearchCondition searchCondition);
    @POST("DeletePacking")
    Single<Common> DeletePacking(@Body SearchCondition searchCondition);
    @POST("GetOneItemDataPacking")
    Single<Common> GetOneItemDataPacking(@Body SearchCondition searchCondition);

    @POST("GetInventorySurveySeqNo")
    Single<Common> GetInventorySurveySeqNo(@Body SearchCondition searchCondition);

    @POST("GetZone")
    Single<Common> GetZone(@Body SearchCondition searchCondition);

    @POST("GetZoneSeqNo")
    Single<Common> GetZoneSeqNo(@Body SearchCondition searchCondition);

    @POST("GetInventorySurvey")
    Single<Common> GetInventorySurvey(@Body SearchCondition searchCondition);

    @POST("SetInventorySurvey")
    Single<Common> SetInventorySurvey(@Body SearchCondition searchCondition);

    @POST("DeleteInventorySurvey")
    Single<Common> DeleteInventorySurvey(@Body SearchCondition searchCondition);

    @POST("ChangeCarNumber")
    Single<Common> ChangeCarNumber(@Body SearchCondition searchCondition);


}

