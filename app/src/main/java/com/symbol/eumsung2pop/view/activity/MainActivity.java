package com.symbol.eumsung2pop.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.ActivityMainBinding;
import com.symbol.eumsung2pop.databinding.DialogNoticeBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.AppVersion;
import com.symbol.eumsung2pop.model.object.MainMenuItem;
import com.symbol.eumsung2pop.model.object.PackingMaster;
import com.symbol.eumsung2pop.model.object.StockOut;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.BackPressControl;
import com.symbol.eumsung2pop.view.CommonMethod;
import com.symbol.eumsung2pop.view.PreferenceManager;
import com.symbol.eumsung2pop.view.TypeChanger;
import com.symbol.eumsung2pop.view.adapter.MainAdapter;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;
import com.symbol.eumsung2pop.viewmodel.SimpleDataViewModel;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    SimpleDataViewModel simpleDataViewModel;
    CommonViewModel commonViewModel;
    //ArrayAdapter businessAdapter;
    MainAdapter mainAdapter;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;
    BackPressControl backpressed;
    int selectedMenu;//1: TAG 수정, 2: 출고 등록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        simpleDataViewModel.GetSimpleData("GetNoticeData2");
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        commonViewModel.Get3("GetInventorySurveySeqNo", new SearchCondition());//재고조사 순번을 가져온다.
        //barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        //scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        setBar();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        ArrayList<MainMenuItem> menuItemArrayList = getMainMenuItem();
        mainAdapter = new MainAdapter(menuItemArrayList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mainAdapter);
        backpressed = new BackPressControl(this);
        /*if (Users.GboutSourcing) {//외주처의 경우 출고검수 사용금지
            mainAdapter.removeItem(17);
            //mainAdapter.notifyDataSetChanged();
        }*/
    }


    private void setListener() {

        /*binding.button9.setOnClickListener(new View.OnClickListener() {//A급대기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity9000.class);
                startActivity(intent);
            }
        });

        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity1000.class);
                startActivity(intent);
            }
        });*/
    }

    public void observerViewModel() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                //LinkedTreeMap linkedTreeMap = (LinkedTreeMap) data;
                //Object errorCheck = linkedTreeMap.get("ErrorCheck");
                AppVersion appVersion = TypeChanger.changeTypeAppVersion(data);
                Object errorCheck = appVersion.ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    boolean noticeFlag = PreferenceManager.getBoolean(this, "NoShowNotice");
                    //Object remark = linkedTreeMap.get("Remark");
                    Object remark = appVersion.Remark;
                    if (!noticeFlag)
                        viewNotice(remark.toString());
                }
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /*simpleDataViewModel.list.observe(this, list -> {
            if (list != null) {
                businessClassList = TypeChanger.changeTypeBusinessClassList(list);
                Object errorCheck = businessClassList.get(0).ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    for (int i = 0; i < businessClassList.size(); i++) {
                        stringArrayList.add((int) businessClassList.get(i).BusinessClassCode + "-" + businessClassList.get(i).CompanyName.replace("금강공업(주)", ""));
                    }
                    final ArrayAdapter adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, stringArrayList);
                    businessAdapter = adapter;

                    MenuItem searchItem = binding.toolbar.getMenu().findItem(R.id.itemBusiness);
                    Spinner yourSpinnerName = (Spinner) searchItem.getActionView();
                    //Log.i("스피너순서", "실행");
                    yourSpinnerName.setAdapter(adapter);

                    yourSpinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//사업장이 변경될때마다, CustomerCode, LocationNo를 바꿔준다.
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Users.BusinessClassCode = (int)businessClassList.get(position).BusinessClassCode;
                            if(!Users.GboutSourcing){
                                Users.CustomerCode = businessClassList.get(position).CustomerCode;
                            }
                            Users.LocationNo = (int)businessClassList.get(position).LocationNo;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    //Log.i("스피너순서", "구성");
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });*/

        //에러메시지
        simpleDataViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                /*PreferenceManager.setBoolean(this, "AutoLogin", false);
                PreferenceManager.setString(this, "ID", "");
                PreferenceManager.setString(this, "PW", "");
                progressOFF2();*/
            }
        });

        simpleDataViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                StockOut stockOut = new StockOut();
                stockOut.StockOutNo = data.StockOutData.StockOutNo;
                stockOut.CustomerLocation = data.StockOutData.CustomerLocation;
                stockOut.AreaCarNumber = data.StockOutData.AreaCarNumber;
                stockOut.CarNumber = data.StockOutData.CarNumber;

                Intent intent = new Intent(MainActivity.this, StockOutActivity.class);
                intent.putExtra("stockOut", stockOut);
                resultLauncher.launch(intent);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                PackingMaster packingMaster = new PackingMaster();
                packingMaster.StockCommitNo = data.PackingMaster.StockCommitNo;
                packingMaster.LocationNo = data.PackingMaster.LocationNo;
                packingMaster.LocationName = data.PackingMaster.LocationName;

                Intent intent = new Intent(MainActivity.this, PackingActivity.class);
                intent.putExtra("packingMaster", packingMaster);
                resultLauncher.launch(intent);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                Users.SeqNo = data.SeqNo;//재고조사 순번셋팅
                ArrayList<MainMenuItem> menuItemArrayList = getMainMenuItem();
                mainAdapter = new MainAdapter(menuItemArrayList, this);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerView.setAdapter(mainAdapter);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //에러메시지
        commonViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                progressOFF2();
            }
        });

        commonViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });


        /*barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals("")) return;

                SearchCondition sc = new SearchCondition();
                sc.Barcode = barcode.Barcode;
                sc.BusinessClassCode = Users.BusinessClassCode;
                sc.CustomerCode = Users.CustomerCode;
                scanViewModel.GetScanMain(sc);
            }
            else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
        });

        scanViewModel.data.observe(this, data -> {
            if (data != null) {
                int activityFlag = data.ActivityFlag;//1: Activity2300실행, 2: 종료, 3: Activity0010실행 + Get한 주문서 데이터 처리
                if(activityFlag==1){//1: Activity2300실행 + barcode
                    String barCode = data.Barcode;
                    Intent intent = new Intent(MainActivity.this, Activity2300.class);
                    intent.putExtra("barCode", barCode);
                    startActivity(intent);
                }
                else if(activityFlag==2){//2: 종료
                    return;
                }
                else if(activityFlag==3){//3: Activity0010실행 + Get한 주문서 데이터 처리(SalesOrderList)

                    Intent intent = new Intent(this, Activity0010.class);
                    intent.putExtra("saleOrderNo", data.SalesOrderList.get(0).SaleOrderNo);
                    activityResultLauncher.launch(intent);
                }
            }
        });*/

    }

    private void viewNotice(String remark) {
        DialogNoticeBinding dialogNoticeBinding = DataBindingUtil.inflate(LayoutInflater.from(getBaseContext()), R.layout.dialog_notice, null, false);
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        //buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
        buider.setView(dialogNoticeBinding.getRoot());
        try {
            if (Users.Language == 0) {
                dialogNoticeBinding.btnOK.setText("확인");
                dialogNoticeBinding.chkNoView.setText("다시보지 않기");
                dialogNoticeBinding.tvTitle.setText("변경사항(version " + getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName + ")");
            } else {
                dialogNoticeBinding.btnOK.setText("OK");
                dialogNoticeBinding.chkNoView.setText("Don't watch it again");
                dialogNoticeBinding.tvTitle.setText("Changes(version " + getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName + ")");
            }
        } catch (PackageManager.NameNotFoundException e) {
            dialogNoticeBinding.tvTitle.setText(Users.Language == 0 ? "변경사항" : "Changes");
        }
        dialogNoticeBinding.tvContent.setText(remark);
        final AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);////Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.show();
        dialogNoticeBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogNoticeBinding.chkNoView.isChecked()) {
                    PreferenceManager.setBoolean(MainActivity.this, "NoShowNotice", true);
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 공용부분
     */

    @Override
    public ArrayList<MainMenuItem> getMainMenuItem() {
        return super.getMainMenuItem();
    }

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setResultLauncher() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        //QR코드 시작
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            if(selectedMenu == 1){//TAG 수정
                                goEditTagActivity(scanResult);
                            }

                            else if(selectedMenu == 2){//출고 등록
                                goStockOutActivity(scanResult);
                            }

                            else if(selectedMenu == 4){//포장 등록
                                goPackingActivity(scanResult);
                            }
                            return;
                        }
                        //QR코드 끝
                        if (result.getResultCode() == 100) {

                        }
                    }
                });
    }

    private void goEditTagActivity(String scanResult){
        Intent intent = new Intent(MainActivity.this, EditTagActivity.class);
        intent.putExtra("result", scanResult);
        resultLauncher.launch(intent);
    }

    private void goStockOutActivity(String scanResult){
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = scanResult;
        commonViewModel.Get("GetStockOutMaster", sc);
    }

    private void goPackingActivity(String scanResult){
        SearchCondition sc = new SearchCondition();
        sc.StockCommitNo = scanResult;
        commonViewModel.Get2("GetPackingMaster", sc);
    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
    }

    public void showFloatingNavigationView() {
        try {
            mFloatingNavigationView.open();
        } catch (Exception et) {
            String test = "3";
            test = "4";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //simpleDataViewModel.GetSimpleDataList("GetBusinessClassData");
        return CommonMethod.onCreateOptionsMenu3(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher, 2);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //result.getResultCode()를 통하여 결과값 확인
                if (result.getResultCode() == RESULT_OK) {
                    //ToDo
                }
                if (result.getResultCode() == RESULT_CANCELED) {
                    //ToDo
                }
            }
    );

    public void LoadScannerOrKeyboard(int selectedMenu) {

        this.selectedMenu = selectedMenu;
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle(Users.Language == 0 ? "입력방식 선택" : "Select the input method");
        CharSequence[] sequences = new CharSequence[2];
        sequences[0] = Users.Language == 0 ? "카메라로 인식" : "Camera recognition";
        sequences[1] = Users.Language == 0 ? "키보드로 입력" : "Enter with keyboard";
        materialAlertDialogBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {//카메라로 인식
                    ScanQR();
                } else if (which == 1) {//키보드로 인식

                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    final View dialogView;
                    dialogView = inflater.inflate(R.layout.dialog_key_in, null);
                    AlertDialog.Builder buider = new AlertDialog.Builder(MainActivity.this); //AlertDialog.Builder 객체 생성
                    //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                    buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                    final AlertDialog dialog2 = buider.create();
                    //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                    dialog2.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                    //Dialog 보이기
                    dialog2.show();
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
                                Toast.makeText(MainActivity.this, "Please enter TAG number.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            MainActivity.this.getKeyInResult(tagNo);
                            dialog.dismiss();
                            dialog2.dismiss();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog2.dismiss();
                        }
                    });
                }
            }
        });
        materialAlertDialogBuilder.setCancelable(true);
        materialAlertDialogBuilder.show();
    }


    private void DrawKeyBoard(){

    }


    public void ScanQR(){
        String comment = getString(R.string.qr_state_common);
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
        //intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
        if (selectedMenu == 1) {//TAG수정
            comment = getString(R.string.qr_state_common);
        }
        if (selectedMenu == 2) {//출고등록
            comment = getString(R.string.qr_state_stockout);
        }
        if (selectedMenu == 4) {
            comment = getString(R.string.qr_state_packing);
        }
        intentIntegrator.setPrompt(comment);
        intentIntegrator.setOrientationLocked(true);
        // intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
        //intentIntegrator.initiateScan();
        intentIntegrator.setRequestCode(7);

        resultLauncher.launch(intentIntegrator.createScanIntent());
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {//qr코드 리딩 결과
            if (result.getContents() == null) {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                //showErrorDialog(this, "취소 되었습니다.",1);
            } else {
                String scanResult;
                scanResult = result.getContents();
                Toast.makeText(this, scanResult, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        if(selectedMenu == 1){//TAG 수정
            goEditTagActivity("E7-"+result);
        }

        else if(selectedMenu == 2){//출고 등록
            goStockOutActivity("E3-"+result);
        }

        else if(selectedMenu == 4){//포장 등록
            goPackingActivity("EC-"+result);
        }


        /*SearchCondition sc = new SearchCondition();
        sc.Barcode = result;
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.CustomerCode = Users.CustomerCode;
        scanViewModel.GetScanMain(sc);*/
    }

    private void startProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF2();
            }
        }, 5000);
        progressON("Loading...", handler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
                //intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
                intentIntegrator.setPrompt(this.getString(R.string.qr_state_common));
                intentIntegrator.setOrientationLocked(true);
                // intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
                //intentIntegrator.initiateScan();
                intentIntegrator.setRequestCode(7);
                resultLauncher.launch(intentIntegrator.createScanIntent());
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        backpressed.onBackPressed();
    }

    /**
     * 공용부분 END
     */
}