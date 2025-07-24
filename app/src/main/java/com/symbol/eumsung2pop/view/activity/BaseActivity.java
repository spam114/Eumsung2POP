package com.symbol.eumsung2pop.view.activity;


import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.symbol.eumsung2pop.model.object.MainMenuItem;
import com.symbol.eumsung2pop.view.application.ApplicationClass;

import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {

    public void progressON() {
        ApplicationClass.getInstance().progressON(this, null);
    }

    public void HideKeyBoard() {
        ApplicationClass.getInstance().HideKeyBoard(this);
    }


    public void progressON(String message) {
        ApplicationClass.getInstance().progressON(this, message);
    }

    public void progressON(String message, Handler handler) {
        ApplicationClass.getInstance().progressON(this, message, handler);
    }

    public void progressOFF2() {
        ApplicationClass.getInstance().progressOFF2();
    }

    public void getKeyInResult(String result){}

    public void showFloatingNavigationView(){}

    public ArrayList<MainMenuItem> getMainMenuItem(){return ApplicationClass.getInstance().getMainMenuItem();}
}