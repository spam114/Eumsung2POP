package com.symbol.eumsung2pop.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.QuickContactBadge;
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
import com.symbol.eumsung2pop.databinding.ActivityStockoutBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.StockOut;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.CommonMethod;
import com.symbol.eumsung2pop.view.MC3300X;
import com.symbol.eumsung2pop.view.adapter.StockOutAdapter;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StockOutActivity extends BaseActivity {
    ActivityStockoutBinding binding;
    StockOutAdapter adapter;
    //BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    MC3300X mc3300X;
    StockOut stockOut;
    int mode;
    String lastPart;//마지막에 추가된 품목,세부규격
    //ArrayList<StockOutDetail> stockOutDetailArrayList = new ArrayList<>();//출고(지시)디테일리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stockout);
        //barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu2) : getString(R.string.menu2_eng));
        this.mode = 1;
        this.stockOut=(StockOut)getIntent().getSerializableExtra("stockOut");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new StockOutAdapter(new ArrayList<>(), this, resultLauncher, lastPart, this.stockOut.StockOutNo, commonViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView5.setText("Part");
            binding.textView.setText("Spec");
            binding.textView1.setText("Ship");
            binding.textView2.setText("Inst");
            binding.textInputLayout.setHint("Enter if it's not recognized");
        }
        binding.txtInfo.setText(this.stockOut.CustomerLocation+"/"+this.stockOut.AreaCarNumber);
        if(!this.stockOut.CarNumber.equals(""))
            binding.txtCarNumber.setText(this.stockOut.CarNumber);
        else
            binding.txtCarNumber.setText("미입력");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = this.stockOut.StockOutNo;
        commonViewModel.Get("GetStockOut", sc);
    }


    public void observerViewModel() {
        commonViewModel.data.observe(this, data -> {//GetStockOut
            if (data != null) {
                //stockOutDetailArrayList = data.StockOutDetailList;
                //adapter = new StockOutAdapter(stockOutDetailArrayList, StockOutActivity.this, resultLauncher, lastPart, stockOut.StockOutNo);
                adapter.updateAdapter(data.StockOutDetailList, lastPart);
                adapter.getFilter().filter(binding.edtInput.getText().toString());
                DecimalFormat numFormatter = new DecimalFormat("###,###");
                int totalQty=0;
                int totalScanQty=0;

                for(int j=0;j<data.StockOutDetailList.size();j++){
                    totalQty+=Integer.parseInt(data.StockOutDetailList.get(j).OutQty);
                    totalScanQty+=Integer.parseInt(data.StockOutDetailList.get(j).ScanQty);
                }
                binding.txtInstructQty.setText("지시("+numFormatter.format(totalQty)+" EA)");
                binding.txtStockOutQty.setText("출고("+numFormatter.format(totalScanQty)+" EA)");
                binding.edtInput.setText("");
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {//SetStockOut
            if (data != null) {
                lastPart=data.StockOutDetailData.PartCode+"-"+data.StockOutDetailData.PartSpec;
                Toast.makeText(getBaseContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                GetMainData();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data3.observe(this, data -> {//DeleteStockOut
            if (data != null) {
                lastPart=data.StockOutDetailData.PartCode+"-"+data.StockOutDetailData.PartSpec;
                Toast.makeText(getBaseContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                GetMainData();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data4.observe(this, data -> {//세부항목 클릭시
            if (data != null) {
                if(data.ItemTagList.isEmpty())
                    return;

                DecimalFormat numFormatter = new DecimalFormat("###,###");
                MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(StockOutActivity.this);

                List<String> itemList = new ArrayList<>();
                String partCode ="";
                String partSpec="";
                String partName = "";

                for (int i=0; i<data.ItemTagList.size();i++){
                    itemList.add("   " + data.ItemTagList.get(i).ItemTag + "        " +numFormatter.format(data.ItemTagList.get(i).ItemCnt) + " EA");
                    partCode = data.ItemTagList.get(i).PartCode;
                    partSpec = data.ItemTagList.get(i).PartSpec;
                    partName = data.ItemTagList.get(i).PartName;
                }


                //alertBuilder.setIcon(R.drawable.ic_launcher);
                alertBuilder.setTitle(partName + "(" + partSpec + ")");

                // List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(StockOutActivity.this,
                        android.R.layout.simple_list_item_1);

                for (int i = 0; i < data.ItemTagList.size(); i++) {
                    adapter.add(itemList.get(i));
                }

                // 버튼 생성
                alertBuilder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });

                // Adapter 셋팅
                alertBuilder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                            }
                        });
                alertBuilder.show();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });


        commonViewModel.data5.observe(this, data -> {//ChangeCarNumber
            if (data != null) {
                Toast.makeText(getBaseContext(), Users.Language == 0 ? "변경되었습니다." : "It has been changed.", Toast.LENGTH_SHORT).show();
                binding.txtCarNumber.setText(data.StrResult);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
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
    }

    private void setListener() {
        /**
         * forfilter
         */
        binding.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(StockOutActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_change_car_number, null);

                AlertDialog.Builder buider = new AlertDialog.Builder(StockOutActivity.this); //AlertDialog.Builder 객체 생성
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
                TextInputEditText edtTagNo = dialogView.findViewById(R.id.edtTagNo);
                if(Users.Language ==0){
                    tvTitle.setText("차량 번호 입력");
                    textInputLayout.setHint("차량 번호");
                    btnOK.setText("확인");
                    btnCancel.setText("닫기");
                }
                else{
                    tvTitle.setText("Enter Car number");
                    textInputLayout.setHint("Car No");
                    btnOK.setText("OK");
                    btnCancel.setText("Cancel");
                }

                edtTagNo.setText(binding.txtCarNumber.getText().toString());

                edtTagNo.setFocusable(true);
                edtTagNo.setFocusableInTouchMode(true);
                edtTagNo.requestFocus();
                edtTagNo.selectAll();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    //View view = ((RouteWipPartActivity)(context)).getCurrentFocus();
                    View view = edtTagNo;
                    if (view != null) {
                        view.getWindowInsetsController().show(WindowInsets.Type.ime());
                    }
                }
                else{
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tagNo = edtTagNo.getText().toString();
                        changeCarNumber(tagNo);
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    /**
     * 공통 시작
     */
    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return CommonMethod.onCreateOptionsMenu2(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher, 1);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                GetMainData();
                //result.getResultCode() 기본값은 0
                /*if (result.getResultCode() == RESULT_OK) {//-1
                }
                if (result.getResultCode() == RESULT_CANCELED) {//0
                }*/
            }
    );

    /**
     * 스캔 인식 (이 액티비티는 별도로 작업한다.)
     */
    private void setResultLauncher() {
        //이것은 인식한 TAG 그대로
        resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
         //QR코드 시작
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            ActionScan(scanResult);
                            return;
                        }         //QR코드 끝
                        if (result.getResultCode() == 100) {

                        }
                    }
                });
    }

    public void ActionScan(String itemTag){

        if(itemTag.equals("1")){//입력모드
            this.mode=1;
            binding.txtMode.setText("입력모드");
            //this.txtMode.setTextColor(Color.parseColor("#FFEB3B"));
        }
        else if(itemTag.equals("2")){//삭제모드
            this.mode=2;
            binding.txtMode.setText("삭제모드");
            //this.txtMode.setTextColor(Color.RED);
        }
        else if(itemTag.equals("3")){//완료
            this.mode=3;
            this.finish();
        }
        else{
            if(this.mode==1) {//입력
                setStockOut(itemTag);
            }
            else if(this.mode==2){//삭제
                deleteStockOut(itemTag);
            }
            else if(this.mode==3){//완료(닫기)
                this.finish();
            }
        }
    }

    private void setStockOut(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = this.stockOut.StockOutNo;
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get2("SetStockOut", sc);
    }

    private void deleteStockOut(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = this.stockOut.StockOutNo;
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get3("DeleteStockOut", sc);
    }

    private void changeCarNumber(String carNumber) {
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = this.stockOut.StockOutNo;
        sc.CarNumber = carNumber;
        sc.UserCode = Users.UserID;
        commonViewModel.Get5("ChangeCarNumber", sc);
    }

    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        ActionScan("E7-" + result);
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

    private void SetMC3300X() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"), RECEIVER_EXPORTED);
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"), RECEIVER_EXPORTED);
        } else {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"));
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"));
        }
        this.mc3300X = new MC3300X(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"), RECEIVER_EXPORTED);
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"), RECEIVER_EXPORTED);
        } else {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"));
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"));
        }
        mc3300X.registerReceivers();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mc3300X.unRegisterReceivers();
        unregisterReceiver(mc3300GetReceiver);
    }

    BroadcastReceiver mc3300GetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result = "";
            if(intent.getAction().equals("mycustombroadcast")){
                result = bundle.getString("mcx3300result");
            }
            else if(intent.getAction().equals("scan.rcv.message")){
                result = bundle.getString("barcodeData");
            }
            if (result.equals(""))
                return;
            insertStockOutPOP(result);
        }
    };

    private void insertStockOutPOP(String result){

    }

    public void scanQR(){
        String comment = getString(R.string.qr_state_stockout2);
        IntentIntegrator intentIntegrator = new IntentIntegrator(StockOutActivity.this);
        intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
        //intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
        intentIntegrator.setPrompt(comment);
        intentIntegrator.setOrientationLocked(true);
        // intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
        //intentIntegrator.initiateScan();
        intentIntegrator.setRequestCode(7);
        resultLauncher.launch(intentIntegrator.createScanIntent());
    }

    /**
     * 공통 끝
     */
}