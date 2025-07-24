package com.symbol.eumsung2pop.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.symbol.eumsung2pop.databinding.ActivityInputBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.Input;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.CommonMethod;
import com.symbol.eumsung2pop.view.MC3300X;
import com.symbol.eumsung2pop.view.adapter.InputAdapter;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InputActivity extends BaseActivity {

    ActivityInputBinding binding;
    InputAdapter adapter;
    //BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    MC3300X mc3300X;
    Input input;
    String lastPart;//마지막에 추가된 품목,세부규격
    ArrayList<Input> inputArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input);
        //barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        inputArrayList = new ArrayList<>();
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu3) : getString(R.string.menu3_eng));
        setView();
        setBar();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new InputAdapter(new ArrayList<>(), this, resultLauncher, lastPart, commonViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }


    public void observerViewModel() {
        commonViewModel.data.observe(this, data -> {//GetStockOut
            if (data != null) {
                inputArrayList = data.InputList;
                //stockOutDetailArrayList = data.StockOutDetailList;
                //adapter = new StockOutAdapter(stockOutDetailArrayList, StockOutActivity.this, resultLauncher, lastPart, stockOut.StockOutNo);
                adapter.updateAdapter(data.InputList, lastPart);

                ArrayAdapter<String> partArrayAdapter;
                ArrayList<String> partArrayList = new ArrayList<>();
                partArrayList.add(Users.Language==0 ? "전체": "ALL");
                for (int i = 0; i < data.PartNameList.size(); i++) {
                    partArrayList.add(data.PartNameList.get(i));
                }

                partArrayAdapter = new ArrayAdapter<>(InputActivity.this, R.layout.list_item, partArrayList);
                binding.spinnerPart.setAdapter(partArrayAdapter);

                binding.spinnerPart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        adapter.getFilter().filter(binding.spinnerPart.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                adapter.getFilter().filter(binding.spinnerPart.getSelectedItem().toString());

                DecimalFormat numFormatter = new DecimalFormat("###,###");
                /*int totalQty = 0;
                int totalScanQty = 0;

                for (int j = 0; j < data.StockOutDetailList.size(); j++) {
                    totalQty += Integer.parseInt(data.StockOutDetailList.get(j).OutQty);
                    totalScanQty += Integer.parseInt(data.StockOutDetailList.get(j).ScanQty);
                }
                binding.txtInstructQty.setText("지시(" + numFormatter.format(totalQty) + " EA)");
                binding.txtStockOutQty.setText("출고(" + numFormatter.format(totalScanQty) + " EA)");
                binding.edtInput.setText("");*/
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {//SetInput
            if (data != null) {
                lastPart = data.StrResult;
                Toast.makeText(getBaseContext(), "임시투입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                GetMainData();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data3.observe(this, data -> {//DeleteInput
            if (data != null) {
                Toast.makeText(getBaseContext(), "임시투입한 TAG가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                GetMainData();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        /*

        commonViewModel.data3.observe(this, data -> {//DeleteStockOut
            if (data != null) {
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
        });*/

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


    private void setView() {
        if (Users.Language == 1) {
            binding.textView1.setText("Tag No");
            binding.textView3.setText("Date");
            binding.textView4.setText("Part");
            binding.textView6.setText("Spec");
            binding.textView7.setText("Qty");
            binding.textView8.setText("Weight");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        commonViewModel.Get("GetInput", sc);
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
        boolean existData = false;
        //리스트에 있으면 삭제, 없으면 등록
        for (int i=0;i<inputArrayList.size();i++){
            if(inputArrayList.get(i).ItemTag.equals(itemTag)){
                //존재
                existData = true;
            }
        }
        if(existData){//삭제
            MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(InputActivity.this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
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
        }
    }

    private void setInput(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get2("SetInput", sc);
    }

    private void deleteInput(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.ItemTag = itemTag;
        sc.UserCode = Users.UserID;
        commonViewModel.Get3("DeleteInput", sc);
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

    /*public void scanQR() {
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
    }*/

    /**
     * 공통 끝
     */
}
