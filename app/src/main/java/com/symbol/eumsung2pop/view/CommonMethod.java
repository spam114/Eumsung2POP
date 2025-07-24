package com.symbol.eumsung2pop.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.activity.BaseActivity;
import com.symbol.eumsung2pop.view.activity.MainActivity;

public class CommonMethod {
    public static String keyResult;

    public static boolean onCreateOptionsMenu(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        menu.findItem(R.id.itemQR).setVisible(false);
        menu.findItem(R.id.itemKeyboard).setVisible(false);
        return true;
    }

    /**
     * 상단바 사업장 숨기기
     *
     * @param activity
     * @param menu
     * @return
     */
    public static boolean onCreateOptionsMenu2(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        return true;
    }

    /**
     * 사업장, 키보드 ,QR 숨기기
     * @param activity
     * @param menu
     * @return
     */
    public static boolean onCreateOptionsMenu3(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        menu.findItem(R.id.itemKeyboard).setVisible(false);
        menu.findItem(R.id.itemQR).setVisible(false);
        return true;
    }

    /**
     * 상단바 전부사용
     *
     * @param activity
     * @param menu
     * @return
     */
    public static boolean onCreateOptionsMenu4(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public static boolean onOptionsItemSelected(BaseActivity activity, MenuItem item, ActivityResultLauncher<Intent> resultLauncher, int type) {
        switch (item.getItemId()) {
            case R.id.itemKeyboard:
                LayoutInflater inflater = LayoutInflater.from(activity);
                final View dialogView;
                if (type == 1)
                    dialogView = inflater.inflate(R.layout.dialog_key_in, null);
                else
                    dialogView = inflater.inflate(R.layout.dialog_key_in2, null);
                AlertDialog.Builder buider = new AlertDialog.Builder(activity); //AlertDialog.Builder 객체 생성
                //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                final AlertDialog dialog = buider.create();
                //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                //Dialog 보이기
                dialog.show();
                TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
                TextInputLayout textInputLayout = dialogView.findViewById(R.id.textInputLayout);
                Button btnOK = dialogView.findViewById(R.id.btnOK);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                if(Users.Language ==0){
                    tvTitle.setText("TAG번호 입력");
                    textInputLayout.setHint("TAG 번호");
                    btnOK.setText("확인");
                    btnCancel.setText("닫기");
                }
                else{
                    tvTitle.setText("Enter TAG number");
                    textInputLayout.setHint("TAG No");
                    btnOK.setText("OK");
                    btnCancel.setText("Cancel");
                }

                TextInputEditText edtTagNo = dialogView.findViewById(R.id.edtTagNo);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tagNo = edtTagNo.getText().toString();

                        if (tagNo.equals("")) {
                            Toast.makeText(activity, "Please enter TAG number.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        activity.getKeyInResult(tagNo);
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            /*case R.id.itemLogout:
                //로그아웃
                MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(activity, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                alertBuilder.setTitle("로그아웃");
                alertBuilder.setMessage("로그아웃 하시겠습니까?");
                alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.setBoolean(activity, "AutoLogin", false);
                        PreferenceManager.setString(activity, "ID", "");
                        PreferenceManager.setString(activity, "PW", "");
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
                //((TextView)findViewById(R.id.textView)).setText("SEARCH") ;
                return true;*/
            case R.id.itemQR:
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리
                intentIntegrator.setPrompt(activity.getString(R.string.qr_state_common));
                intentIntegrator.setOrientationLocked(true);
                // intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
                //intentIntegrator.initiateScan();
                intentIntegrator.setRequestCode(7);
                resultLauncher.launch(intentIntegrator.createScanIntent());
                return true;
            /*case R.id.action_settings :
                //((TextView)findViewById(R.id.textView)).setText("SETTINGS") ;
                return true ;*/
            case android.R.id.home:
                activity.showFloatingNavigationView();

            default:
                return false;
        }
    }

    public static void setBar(BaseActivity activity) {
        Drawable drawable = activity.getResources().getDrawable(R.drawable.baseline_menu_black_36);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, 36, 36, true));
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(newdrawable);
    }

    /*public static String GetPackingNo(){

        commonViewModel.GetStr("GetPackingNo",new SearchCondition());


        return "";
    }*/

    public static FloatingNavigationView setFloatingNavigationView(BaseActivity activity) {
        View fnvHeader;
        FloatingNavigationView mFloatingNavigationView = (FloatingNavigationView) activity.findViewById(R.id.floating_navigation_view);
        fnvHeader = mFloatingNavigationView.getHeaderView(0);

        /**
         * 글자색 강제변경
         */
        Menu menu = mFloatingNavigationView.getMenu();
        /*MenuItem mi = menu.findItem(R.id.logOut);
        MenuItem mi2 = menu.findItem(R.id.pcPrinter);*/
        MenuItem mi3 = menu.findItem(R.id.language);
        mi3.setTitle("언어설정(Language)");
        /*if(Users.Language ==0){
            mi.setTitle("로그아웃");
            //mi2.setTitle("출력PC 연결");
            mi3.setTitle("언어설정(Language)");
        }
        else{
            mi.setTitle("Sign Out");
            //mi2.setTitle("Link Output PC");

        }*/

        /*SpannableString s = new SpannableString(mi.getTitle());
        SpannableString s2 = new SpannableString(mi2.getTitle());*/
        SpannableString s3 = new SpannableString(mi3.getTitle());
        //s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        //s2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s2.length(), 0);
        s3.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s3.length(), 0);
        //mi.setTitle(s);
        //mi2.setTitle(s2);
        mi3.setTitle(s3);

        TextView txtUserName = fnvHeader.findViewById(R.id.txtUserName);
        txtUserName.setText(Users.UserName);
        TextView txtUserID = fnvHeader.findViewById(R.id.txtUserID);
        txtUserID.setText(Users.UserID);
        ImageView userImage = fnvHeader.findViewById(R.id.userImage);
        TextView txtPCName = fnvHeader.findViewById(R.id.txtPCName);
        /*if (Users.PCCode.equals("")){
            if(Users.Language ==0){
                txtPCName.setText("출력PC 없음");
            }
            else{
                txtPCName.setText("NO PC connected");
            }
        }

        else
            txtPCName.setText(Users.PCName + "(" + Users.PCCode + ")");
*/

        Object obUserImage =Users.UserImage;

        if(obUserImage == null){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            userImage.setLayoutParams(layoutParams);
            userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
        }
        else if (Users.UserImage.equals("fail")) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            userImage.setLayoutParams(layoutParams);
            userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
        } else {
            try {
                byte[] array5 = Base64.decode(Users.UserImage, Base64.DEFAULT);
                userImage.setBackground(new ShapeDrawable(new OvalShape()));
                userImage.setClipToOutline(true);
                userImage.setImageBitmap(BitmapFactory.decodeByteArray(array5, 0, array5.length));
            } catch (Exception ex) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
                userImage.setLayoutParams(layoutParams);
                userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
            }
        }


        /*LoginViewModel loginViewModel = new ViewModelProvider(activity).get(LoginViewModel.class);
        SearchCondition sc = new SearchCondition();


        loginViewModel.loading.observe(activity, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중

                } else {//로딩끝
                    if (Users.PCCode.equals("")){
                        if(Users.Language ==0){
                            txtPCName.setText("출력PC 없음");
                        }
                        else{
                            txtPCName.setText("NO PC connected");
                        }
                    }
                    else
                        txtPCName.setText(Users.PCName + "(" + Users.PCCode + ")");
                }
            }
        });

        loginViewModel.printErrorMsg.observe(activity, models -> {
            if (models != null) {
                Toast.makeText(activity, models, Toast.LENGTH_SHORT).show();
            }
        });

        if (Users.UserImage.equals("fail")) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            userImage.setLayoutParams(layoutParams);
            userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
        } else {
            try {
                byte[] array5 = Base64.decode(Users.UserImage, Base64.DEFAULT);
                userImage.setBackground(new ShapeDrawable(new OvalShape()));
                userImage.setClipToOutline(true);
                userImage.setImageBitmap(BitmapFactory.decodeByteArray(array5, 0, array5.length));
            } catch (Exception ex) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
                userImage.setLayoutParams(layoutParams);
                userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
            }
        }*/

        ImageView imvClose = fnvHeader.findViewById(R.id.imvClose);
        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingNavigationView.close();
            }
        });


        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /*if (item.getItemId() == R.id.logOut) {
                    //로그아웃
                    MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(activity, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);

                    String titleKor="로그아웃";
                    String titleEng="Sign Out";
                    String messageKor="로그아웃 하시겠습니까?";
                    String messageEng="Are you sure you want to Sign out?";
                    String poButtonKor="확인";
                    String poButtonEng="OK";
                    String negaButtonKor="취소";
                    String negaButtonEng="Cancel";

                    String strTitle;
                    String strMessage;
                    String strPoButton;
                    String strNegaButton;

                    if(Users.Language ==0){
                        strTitle=titleKor;
                        strMessage=messageKor;
                        strPoButton=poButtonKor;
                        strNegaButton=negaButtonKor;
                    }
                    else{
                        strTitle=titleEng;
                        strMessage=messageEng;
                        strPoButton=poButtonEng;
                        strNegaButton=negaButtonEng;
                    }

                    alertBuilder.setTitle(strTitle);
                    alertBuilder.setMessage(strMessage);
                    alertBuilder.setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PreferenceManager.setBoolean(activity, "AutoLogin", false);
                            PreferenceManager.setString(activity, "ID", "");
                            PreferenceManager.setString(activity, "PW", "");
                            PreferenceManager.setString(activity, "PCCode", "");
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.putExtra("FirstFlag", false);


                            //Users.
                            activity.startActivity(intent);
                            activity.finish();
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.show();
                } else if (item.getItemId() == R.id.pcPrinter) {
                    //출력PC 연결
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    final View dialogView = inflater.inflate(R.layout.dialog_key_in, null);
                    AlertDialog.Builder buider = new AlertDialog.Builder(activity); //AlertDialog.Builder 객체 생성
                    //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                    buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                    final AlertDialog dialog = buider.create();
                    //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                    dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                    //Dialog 보이기
                    dialog.show();
                    TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
                    Button btnOK = dialogView.findViewById(R.id.btnOK);
                    Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                    TextInputLayout textInputLayout = dialogView.findViewById(R.id.textInputLayout);
                    if(Users.Language ==0){
                        textInputLayout.setHint("PC번호");
                        tvTitle.setText("PC번호 입력");
                        btnOK.setText("확인");
                        btnCancel.setText("취소");
                    }
                    else{
                        textInputLayout.setHint("PC NO");
                        tvTitle.setText("Enter PC number");
                        btnOK.setText("OK");
                        btnCancel.setText("Cancel");
                    }


                    TextInputEditText edtTagNo = dialogView.findViewById(R.id.edtTagNo);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String pcNo = edtTagNo.getText().toString();

                            if (pcNo.equals("")) {

                                if(Users.Language ==0){
                                    Toast.makeText(activity, "연결할 PC의 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(activity, "Enter the number of the PC to be connected.", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                            sc.PCCode = pcNo;
                            loginViewModel.GetPrintPCData(sc, activity);

                            dialog.dismiss();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }*/
                if (item.getItemId() == R.id.language) {
                    //언어설정
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    final View dialogView = inflater.inflate(R.layout.dialog_language, null);
                    AlertDialog.Builder buider = new AlertDialog.Builder(activity); //AlertDialog.Builder 객체 생성
                    //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                    buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                    final AlertDialog dialog = buider.create();
                    //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                    dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                    //Dialog 보이기
                    dialog.show();
                    TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
                    tvTitle.setText("언어(Language)");
                    RadioButton rbKor = dialogView.findViewById(R.id.rbKor);
                    RadioButton rbEng = dialogView.findViewById(R.id.rbEng);
                    Button btnOK = dialogView.findViewById(R.id.btnOK);
                    Button btnCancel = dialogView.findViewById(R.id.btnCancel);

                    if(Users.Language==0){
                        rbKor.setChecked(true);
                    }

                    else {
                        rbEng.setChecked(true);
                    }

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rbKor.isChecked()) {
                                PreferenceManager.setInt(activity, "Language", 0);
                                Users.Language = 0;
                            }
                            else {
                                PreferenceManager.setInt(activity, "Language", 1);
                                Users.Language = 1;
                            }
                            dialog.dismiss();

                            Intent i = new Intent(activity, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(i);
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                return false;
            }
        });


        return mFloatingNavigationView;

    }

    public static int getAppVersionCode(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return (int) context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0)
                        .getLongVersionCode();
            } else {
                return context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0)
                        .versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * QR코드로 인식
     *
     * @param context
     * @param barcodeConvertPrintViewModel
     * @return
     */
    /*public static ActivityResultLauncher<Intent> FNBarcodeConvertPrint(Context context, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
     *//**
     * QR코드 시작
     *//*
     *//**
     * QR코드 끝
     *//*
        ActivityResultLauncher<Intent> resultLauncher = ((BaseActivity) context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        *//**
     * QR코드 시작
     *//*
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String barcode = intentResult.getContents();
                            FNBarcodeConvertPrint(barcode, barcodeConvertPrintViewModel);
                            return;
                        }
                        *//**
     * QR코드 끝
     *//*
                        if (result.getResultCode() == 100) {

                        }
                    }
                });

        return resultLauncher;
    }*/

    /**
     * 직접인식
     *
     * @param barcode
     * @param barcodeConvertPrintViewModel
     */
    /*public static void FNBarcodeConvertPrint(String barcode, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
        SearchCondition sc = new SearchCondition();
        sc.Barcode = barcode;
        sc.LocationNo = Users.LocationNo;
        sc.PCCode = Users.PCCode;
        sc.UserID = Users.UserID;
        barcodeConvertPrintViewModel.FNBarcodeConvertPrint(sc);
        return;
    }*/

    /**
     * Print 공통
     *
     //* @param context
     * @param barcodeConvertPrintViewModel
    //* @param printDivision
    //* @param printNo
     */
    /*public static void FNSetPrintOrderData(Context context, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel, int printDivision, String printNo) {

        String titleKor="출력";
        String titleEng="Print";
        String messageKor="출력 번호: " + printNo + "\n출력작업을 진행하시겠습니까?";
        String messageEng="Print No: " + printNo + "\nDo you want to print?";
        String poButtonKor="확인";
        String poButtonEng="OK";
        String negaButtonKor="취소";
        String negaButtonEng="Cancel";

        String strTitle;
        String strMessage;
        String strPoButton;
        String strNegaButton;

        if(Users.Language ==0){
            strTitle=titleKor;
            strMessage=messageKor;
            strPoButton=poButtonKor;
            strNegaButton=negaButtonKor;
        }
        else{
            strTitle=titleEng;
            strMessage=messageEng;
            strPoButton=poButtonEng;
            strNegaButton=negaButtonEng;
        }




        new MaterialAlertDialogBuilder(context)
                .setTitle(strTitle)
                .setMessage(strMessage)
                .setCancelable(true)
                .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        if (Users.PCCode.equals("")) {
                            if(Users.Language ==0){
                                Toast.makeText(context, "출력 PC가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "There is no PC connected.", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }

                        SearchCondition sc = new SearchCondition();
                        sc.PCCode = Users.PCCode;
                        sc.PrintDivision = printDivision;
                        sc.PrintNo = printNo;
                        sc.UserID = Users.UserID;
                        sc.Language = Users.Language;
                        barcodeConvertPrintViewModel.FNSetPrintOrderData(sc);
                        return;
                    }
                })
                .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }*/


    /*public static void FNSetPackingPDAData(BarcodeConvertPrintViewModel barcodeConvertPrintViewModel, int WorkDiv, String PackingNo, String Tag, int ErrorDiv) {
        SearchCondition sc = new SearchCondition();
        sc.WorkDiv = WorkDiv;
        sc.PackingNo = PackingNo;
        sc.Tag = Tag;
        sc.ErrorDiv = ErrorDiv;
        sc.LocationNo = Users.LocationNo;
        sc.UserID = Users.UserID;
        barcodeConvertPrintViewModel.FNSetPackingPDAData(sc);
    }

    public static void FNSetBundleData(BarcodeConvertPrintViewModel barcodeConvertPrintViewModel, int WorkDiv, String BundleNo, String PackingNo) {
        SearchCondition sc = new SearchCondition();
        sc.WorkDiv = WorkDiv;
        sc.BundleNo = BundleNo;
        sc.PackingNo = PackingNo;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;
        barcodeConvertPrintViewModel.FNSetBundleData(sc);
    }*/
}