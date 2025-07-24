package com.symbol.eumsung2pop.view.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.model.object.MainMenuItem;
import com.symbol.eumsung2pop.model.object.Users;

import java.util.ArrayList;

/**
 * 공통으로 쓰는 메소드들이 담겨져있다.
 */
public class ApplicationClass extends Application {
    private static ApplicationClass baseApplication;
    private static Resources res;
    AppCompatDialog progressDialog;
    Handler handler;

    public static ApplicationClass getInstance() {
        return baseApplication;
    }

    public static Resources getResourses() {
        return res;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        res = getResources();
    }

    /**
     * 인터넷 연결 상태를 확인한다.
     *
     * @return
     */
    public boolean checkInternetConnect() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }
        return true;
    }

    /**
     * 스캔한 태그의 종류를 알아낸다.
     * -1: 유효하지 않은 태그
     * 0: 제품
     * 1: 출고
     *
     * @return
     */
    public int checkTagState(String tag) {

        if (tag.substring(0, 2).equals("EI")) {//제품
            return 0;
        } else if (tag.substring(0, 2).equals("E3")) {//출고(상차)
            return 1;
        } else {
            return -1;
        }
    }

    public void HideKeyBoard(Activity activity) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public void progressON(Activity activity, String message) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();
        }
        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }

    public void progressON(Activity activity, String message, Handler handler) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (this.handler != null) {
            //기존에 돌고있던 handler를 cancel 시켜준다.
            this.handler.removeCallbacksAndMessages(null);
        }
        this.handler = handler;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();
        }
        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }

    public void progressSET(String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }

    public void showErrorDialog(Context context, String message, int type) {
        MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(context, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
        if (type == 1) {
            alertBuilder.setTitle("작업 성공");
        } else {
            alertBuilder.setTitle("에러 발생");
        }


        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.show();
    }

    public void progressOFF2() {
        if (progressDialog != null && progressDialog.isShowing()) {
            //Log.i("로딩바OFF", className);
            progressDialog.dismiss();
            //this.handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 전체(관리자)권한
     *
     * @return
     */
    public ArrayList<MainMenuItem> getMainMenuItem() {

        ArrayList<MainMenuItem> menuItemArrayList = new ArrayList<>();

        boolean booleanAdmin = false;
        boolean booleanPOP = false;

        for (int i = 0; i < Users.AppAuthorityList.size(); i++) {
            if (Users.AppAuthorityList.get(i).Authority == 0) {//관리자
                booleanAdmin = true;
            } else if (Users.AppAuthorityList.get(i).Authority == 1) {//POP
                booleanPOP = true;
            }
        }

        menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu1) : getString(R.string.menu1_eng), false, R.drawable.edit_square_48px));
        menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu3) : getString(R.string.menu3_eng), false, R.drawable.keyboard_double_arrow_left_48px));
        menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu4) : getString(R.string.menu4_eng), false, R.drawable.package_2_48px));
        menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu2) : getString(R.string.menu2_eng), false, R.drawable.local_shipping_48px));
        menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu5) : getString(R.string.menu5_eng), false, R.drawable.inventory_48px));


        /*if (booleanAdmin) {//관리자: 모든권한
            menuItemArrayList.add(new MainMenuItem(1, 1, Users.Language == 0 ? getString(R.string.menu1) : getString(R.string.menu1_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu2) : getString(R.string.menu2_eng), false, R.drawable.sprinkler_48px));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu3) : getString(R.string.menu3_eng), true, R.drawable.next_plan_48px));

            menuItemArrayList.add(new MainMenuItem(1, 2, Users.Language == 0 ? getString(R.string.menu4) : getString(R.string.menu4_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu5) : getString(R.string.menu5_eng), false, R.drawable.monitor_weight_48px));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu6) : getString(R.string.menu6_eng), false, R.drawable.safety_divider_48px));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu7) : getString(R.string.menu7_eng), false, R.drawable.sprinkler_48px));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu8) : getString(R.string.menu8_eng), false, R.drawable.list_alt_48px));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu9) : getString(R.string.menu9_eng), false, R.drawable.local_shipping_48px));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu16) : getString(R.string.menu16_eng), false, R.drawable.package_48px));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu17) : getString(R.string.menu17_eng), true, R.drawable.inventory_2_48px));

            menuItemArrayList.add(new MainMenuItem(1, 3, Users.Language == 0 ? getString(R.string.menu10) : getString(R.string.menu10_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 3, Users.Language == 0 ? getString(R.string.menu11) : getString(R.string.menu11_eng), false, R.drawable.inventory_2_48px));
            menuItemArrayList.add(new MainMenuItem(2, 3, Users.Language == 0 ? getString(R.string.menu12) : getString(R.string.menu12_eng), false, R.drawable.inventory_48px));
            menuItemArrayList.add(new MainMenuItem(2, 3, Users.Language == 0 ? getString(R.string.menu13) : getString(R.string.menu13_eng), false, R.drawable.keyboard_double_arrow_left_48px));
            menuItemArrayList.add(new MainMenuItem(2, 3, Users.Language == 0 ? getString(R.string.menu18) : getString(R.string.menu18_eng), false, R.drawable.keyboard_double_arrow_right_48px));
            //menuItemArrayList.add(new MainMenuItem(2,3,getString(R.string.menu18),false, R.drawable.keyboard_double_arrow_left_48px));
            menuItemArrayList.add(new MainMenuItem(2, 3, Users.Language == 0 ? getString(R.string.menu14) : getString(R.string.menu14_eng), false, R.drawable.local_shipping_48px));

            menuItemArrayList.add(new MainMenuItem(1, 4, Users.Language == 0 ? getString(R.string.menu21) : getString(R.string.menu21_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 4, Users.Language == 0 ? getString(R.string.menu22) : getString(R.string.menu22_eng), false, R.drawable.delete_sweep_24px));
            //menuItemArrayList.add(new MainMenuItem(2, 4, Users.Language == 0 ? getString(R.string.menu23) : getString(R.string.menu23_eng), false, R.drawable.flex_direction_24px));
            //menuItemArrayList.add(new MainMenuItem(2,3,getString(R.string.menu19),false, R.drawable.checklist_rtl_48px));
            //menuItemArrayList.add(new MainMenuItem(2,3,getString(R.string.menu15),false, R.drawable.download_48px));
            //menuItemArrayList.add(new MainMenuItem(2,3,getString(R.string.menu20),true, R.drawable.file_upload_48px));
        } else if (booleanPOP) {//POP권한
            *//*menuItemArrayList.add(new MainMenuItem(1, 1, Users.Language == 0 ? getString(R.string.menu1) : getString(R.string.menu1_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu2) : getString(R.string.menu2_eng), false, R.drawable.sprinkler_48px));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu3) : getString(R.string.menu3_eng), true, R.drawable.next_plan_48px));*//*

            menuItemArrayList.add(new MainMenuItem(1, 1, Users.Language == 0 ? getString(R.string.menu10) : getString(R.string.menu10_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu11) : getString(R.string.menu11_eng), false, R.drawable.inventory_2_48px));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu12) : getString(R.string.menu12_eng), false, R.drawable.inventory_48px));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu13) : getString(R.string.menu13_eng), false, R.drawable.keyboard_double_arrow_left_48px));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu18) : getString(R.string.menu18_eng), false, R.drawable.keyboard_double_arrow_right_48px));
            //menuItemArrayList.add(new MainMenuItem(2,3,getString(R.string.menu18),false, R.drawable.keyboard_double_arrow_left_48px));
            menuItemArrayList.add(new MainMenuItem(2, 1, Users.Language == 0 ? getString(R.string.menu14) : getString(R.string.menu14_eng), false, R.drawable.local_shipping_48px));

            menuItemArrayList.add(new MainMenuItem(1, 2, Users.Language == 0 ? getString(R.string.menu21) : getString(R.string.menu21_eng), false, -1));
            menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu22) : getString(R.string.menu22_eng), false, R.drawable.delete_sweep_24px));
            //menuItemArrayList.add(new MainMenuItem(2, 2, Users.Language == 0 ? getString(R.string.menu23) : getString(R.string.menu23_eng), false, R.drawable.flex_direction_24px));

        }*/


        return menuItemArrayList;
    }
    /**
     * POP 포장/출고 권한
     * @return
     */
    /*public ArrayList<MainMenuItem> getMainMenuItem2(){

        ArrayList<MainMenuItem> menuItemArrayList= new ArrayList<>();
        menuItemArrayList.add(new MainMenuItem(1,3,Users.Language == 0 ? getString(R.string.menu10):getString(R.string.menu10_eng),false,-1));
        menuItemArrayList.add(new MainMenuItem(2,3,Users.Language == 0 ? getString(R.string.menu11):getString(R.string.menu11_eng),false, R.drawable.inventory_2_48px));
        menuItemArrayList.add(new MainMenuItem(2,3,Users.Language == 0 ? getString(R.string.menu12):getString(R.string.menu12_eng),false, R.drawable.inventory_48px));
        menuItemArrayList.add(new MainMenuItem(2,3,Users.Language == 0 ? getString(R.string.menu13):getString(R.string.menu13_eng),false, R.drawable.keyboard_double_arrow_left_48px));
        menuItemArrayList.add(new MainMenuItem(2,3,Users.Language == 0 ? getString(R.string.menu18):getString(R.string.menu18_eng),false, R.drawable.keyboard_double_arrow_right_48px));
        menuItemArrayList.add(new MainMenuItem(2,3,Users.Language == 0 ? getString(R.string.menu14):getString(R.string.menu14_eng),false, R.drawable.local_shipping_48px));
        return menuItemArrayList;
    }*/
}
