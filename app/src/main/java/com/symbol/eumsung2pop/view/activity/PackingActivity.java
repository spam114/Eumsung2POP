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
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.ActivityPackingBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.Packing;
import com.symbol.eumsung2pop.model.object.PackingMaster;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.CommonMethod;
import com.symbol.eumsung2pop.view.MC3300X;
import com.symbol.eumsung2pop.view.adapter.PackingAdapter;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PackingActivity extends BaseActivity {
    ActivityPackingBinding binding;
    PackingAdapter adapter;
    //BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    MC3300X mc3300X;
    PackingMaster packingMaster;
    String lastPart;//마지막에 추가된 품목,세부규격

    ArrayList<Packing> packingArrayList;
    //ArrayList<StockOutDetail> stockOutDetailArrayList = new ArrayList<>();//출고(지시)디테일리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packing);
        //barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        packingArrayList = new ArrayList<>();
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu4) : getString(R.string.menu4_eng));
        this.packingMaster = (PackingMaster) getIntent().getSerializableExtra("packingMaster");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new PackingAdapter(new ArrayList<>(), this, resultLauncher, lastPart, this.packingMaster.StockCommitNo, commonViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView5.setText("Part");
            binding.textView.setText("Spec");
            binding.textView1.setText("Req");
            binding.textView2.setText("Pack");
            binding.textInputLayout.setHint("Enter if it's not recognized");
        }
        binding.txtMode.setText(this.packingMaster.LocationName);
        binding.txtInfo.setText(this.packingMaster.StockCommitNo);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.StockCommitNo = this.packingMaster.StockCommitNo;
        commonViewModel.Get("GetPacking", sc);
    }


    public void observerViewModel() {
        commonViewModel.data.observe(this, data -> {//GetPacking
            if (data != null) {
                //stockOutDetailArrayList = data.StockOutDetailList;
                packingArrayList = data.PackingList;
                adapter.updateAdapter(data.PackingList, lastPart);
                //adapter.getFilter().filter(binding.edtInput.getText().toString());
                /*DecimalFormat numFormatter = new DecimalFormat("###,###");
                int totalQty=0;
                int totalScanQty=0;

                for(int j=0;j<data.StockOutDetailList.size();j++){
                    totalQty+=Integer.parseInt(data.StockOutDetailList.get(j).OutQty);
                    totalScanQty+=Integer.parseInt(data.StockOutDetailList.get(j).ScanQty);
                }
                binding.txtInstructQty.setText("지시("+numFormatter.format(totalQty)+" EA)");
                binding.txtStockOutQty.setText("출고("+numFormatter.format(totalScanQty)+" EA)");
                binding.edtInput.setText("");*/
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {//JudgeSetOrDeletePacking
            if (data != null) {
                if (data.StrResult.equals("등록")) {
                    setPacking(data.StrResult2);
                } else {
                    MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(PackingActivity.this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                    alertBuilder.setTitle("포장 취소");
                    alertBuilder.setMessage("포장한 TAG를 취소하시겠습니까?");
                    alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletePacking(data.StrResult2);
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
                }
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data5.observe(this, data -> {//SetPacking
            if (data != null) {
                lastPart = data.Packing.PartCode + "-" + data.Packing.PartSpec;
                Toast.makeText(getBaseContext(), "포장 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                GetMainData();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data6.observe(this, data -> {//DeletePacking
            if (data != null) {
                lastPart = data.Packing.PartCode + "-" + data.Packing.PartSpec;
                Toast.makeText(getBaseContext(), "포장 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                GetMainData();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data4.observe(this, data -> {//세부항목 클릭시
            if (data != null) {
                if (data.PackingList.isEmpty())
                    return;

                DecimalFormat numFormatter = new DecimalFormat("###,###");
                MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(PackingActivity.this);

                List<String> itemList = new ArrayList<>();
                String partCode = "";
                String partSpec = "";
                String partName = "";

                for (int i = 0; i < data.PackingList.size(); i++) {
                    itemList.add("   " + data.PackingList.get(i).ItemTag + "        " + numFormatter.format(data.PackingList.get(i).PackingQty) + " EA");
                    partCode = data.PackingList.get(i).PartCode;
                    partSpec = data.PackingList.get(i).PartSpec;
                    partName = data.PackingList.get(i).PartName;
                }


                //alertBuilder.setIcon(R.drawable.ic_launcher);
                alertBuilder.setTitle(partName + "(" + partSpec + ")");

                // List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(PackingActivity.this,
                        android.R.layout.simple_list_item_1);

                for (int i = 0; i < data.PackingList.size(); i++) {
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

    public void ActionScan(String itemTag) {
        judgeSetOrDeletePacking(itemTag);
        /*boolean existData = false;
        //리스트에 있으면 삭제, 없으면 등록
        for (int i=0;i<packingArrayList.size();i++){
            if(packingArrayList.get(i).ItemTag.equals(itemTag)){
                //존재
                existData = true;
            }
        }
        if(existData){//삭제
            MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(PackingActivity.this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
            alertBuilder.setTitle("임시투입 삭제");
            alertBuilder.setMessage("임시투입한 TAG를 삭제하시겠습니까?");
            alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteInput(itemTag);
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
        }
        else{//등록
            setInput(itemTag);
        }*/
    }

    private void judgeSetOrDeletePacking(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get2("JudgeSetOrDeletePacking", sc);
    }

    private void setPacking(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.StockCommitNo = this.packingMaster.StockCommitNo;
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get5("SetPacking", sc);
    }

    private void deletePacking(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.StockCommitNo = this.packingMaster.StockCommitNo;
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get6("DeletePacking", sc);
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
    protected void onPause() {
        super.onPause();
        mc3300X.unRegisterReceivers();
        unregisterReceiver(mc3300GetReceiver);
    }

    BroadcastReceiver mc3300GetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result = "";
            if (intent.getAction().equals("mycustombroadcast")) {
                result = bundle.getString("mcx3300result");
            } else if (intent.getAction().equals("scan.rcv.message")) {
                result = bundle.getString("barcodeData");
            }
            if (result.equals(""))
                return;
            insertStockOutPOP(result);
        }
    };

    private void insertStockOutPOP(String result) {

    }

    public void scanQR() {
        String comment = getString(R.string.qr_state_stockout2);
        IntentIntegrator intentIntegrator = new IntentIntegrator(PackingActivity.this);
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