package com.symbol.eumsung2pop.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {
    public static String[] permissionList;

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 200;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_DATA = 201;


    /*
     * true : all permission granted
     * false : some permission deni
     * */
    public static boolean haveAllpermission(Context c , String[] s){
        boolean ispms = true;
        for (String pms : s ){
            if (ContextCompat.checkSelfPermission(c, pms) != PackageManager.PERMISSION_GRANTED  ) {
                ispms = false;
                break;
            }
        }
        return ispms;
    }

    /*
     * true : 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
     * false : “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
     * */
    public static boolean recheckPermission (Activity activity , String[] permissions){
        boolean isrpms = false;
        for (String s : permissions){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, s)){
                isrpms = true;
                break;
            }
        }
        return isrpms;
    }


}
