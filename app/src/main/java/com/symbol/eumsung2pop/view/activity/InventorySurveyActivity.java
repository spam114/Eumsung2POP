package com.symbol.eumsung2pop.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.ActivityInventorySurveyBinding;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.adapter.InventorySurveyAdapter;
import com.symbol.eumsung2pop.viewmodel.CommonViewModel;

import java.util.ArrayList;

public class InventorySurveyActivity extends BaseActivity {

    ActivityInventorySurveyBinding binding;
    InventorySurveyAdapter adapter;
    CommonViewModel commonViewModel;
    //BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_survey);
        //barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        setView();
        setResultLauncher();
        adapter = new InventorySurveyAdapter(new ArrayList<>(), this, resultLauncher, commonViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.imvQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR();
            }
        });
        binding.edtNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // IME_ACTION_SEARCH , IME_ACTION_GO
                    Users.SoundManager.playSound(0, 0, 3);//에러
                    setInventorySurvey("E7-" + v.getText().toString());
                }
                return false;
            }
        });
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.txtCoil.setText("Tag No");
            binding.txtPart.setText("Part/Spec");
            binding.txtQty.setText("Qty");
            binding.edtNumber.setHint("Please enter. if it is not recognized.");
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        commonViewModel.Get("GetZone", sc);
    }

    public void observerViewModel() {

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                ArrayAdapter<String> zoneArrayAdapter;
                ArrayList<String> zoneArrayList = new ArrayList<>();
                for (int i = 0; i < data.ZoneList.size(); i++) {
                    zoneArrayList.add(data.ZoneList.get(i).Zone);
                }

                zoneArrayAdapter = new ArrayAdapter<>(InventorySurveyActivity.this, R.layout.list_item, zoneArrayList);
                binding.spinnerZone.setAdapter(zoneArrayAdapter);

                binding.spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SearchCondition sc = new SearchCondition();
                        sc.Zone = binding.spinnerZone.getSelectedItem().toString();
                        commonViewModel.Get2("GetZoneSeqNo", sc);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                /*Users.SeqNo = data.SeqNo;
                ArrayList<MainMenuItem> menuItemArrayList = getMainMenuItem();
                mainAdapter = new MainAdapter(menuItemArrayList, this);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerView.setAdapter(mainAdapter);*/

                /*DecimalFormat numFormatter = new DecimalFormat("###,###");
                binding.tvStockOutOrderQty.setText(numFormatter.format(num1));
                binding.tvStockOutQty.setText(numFormatter.format(num2));*/
                /*adapter.updateAdapter(data.CustomerList);
                adapter.getFilter().filter(binding.edtInput.getText().toString());*/
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                ArrayAdapter<String> zoneSeqNoArrayAdapter;
                ArrayList<String> zoneSeqNoArrayList = new ArrayList<>();

                for (int i = 0; i < data.ZoneList.size(); i++) {
                    zoneSeqNoArrayList.add(data.ZoneList.get(i).ZoneSeqNo);
                }
                zoneSeqNoArrayAdapter = new ArrayAdapter<>(InventorySurveyActivity.this, R.layout.list_item, zoneSeqNoArrayList);
                binding.spinnerZoneSeqNo.setAdapter(zoneSeqNoArrayAdapter);

                binding.spinnerZoneSeqNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getInventorySurvey("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                adapter.updateAdapter(data.InventorySurveyList, data.StrResult);
                /*if(!data.StrResult.equals(""))//스캔한 TAG의 배경색을 변경한다.
                    adapter.changeBackGroundColor(data.StrResult);*/
                //adapter.getFilter().filter(binding.edtInput.getText().toString());

            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data4.observe(this, data -> {
            if (data != null) {
                getInventorySurvey(data.StrResult);
                if (data.StrResult.equals("Delete"))
                    Toast.makeText(this, Users.Language == 0 ? "삭제되었습니다." : "Deleted successfully.", Toast.LENGTH_SHORT).show();
                //adapter.updateAdapter(data.InventorySurveyList);
                //adapter.getFilter().filter(binding.edtInput.getText().toString());

            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
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
    }

    public void getInventorySurvey(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.Zone = binding.spinnerZone.getSelectedItem().toString();
        sc.ZoneSeqNo = binding.spinnerZoneSeqNo.getSelectedItem().toString();
        sc.SeqNo = Integer.toString(Users.SeqNo);
        sc.ItemTag = itemTag;
        commonViewModel.Get3("GetInventorySurvey", sc);
    }

    public void setInventorySurvey(String itemTag) {
        SearchCondition sc = new SearchCondition();
        sc.ItemTag = itemTag;
        sc.Zone = binding.spinnerZone.getSelectedItem().toString();
        sc.ZoneSeqNo = binding.spinnerZoneSeqNo.getSelectedItem().toString();
        sc.UserID = Users.UserID;
        sc.SeqNo = Integer.toString(Users.SeqNo);
        commonViewModel.Get4("SetInventorySurvey", sc);
    }


    /**
     * 공용부분
     */
    private void setResultLauncher() {
        //이것은 서버TAG인식 로직
        //resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);

        //이것은 인식한 TAG 그대로
        resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            Users.SoundManager.playSound(0, 0, 3);
                            String scanResult = intentResult.getContents();
                            setInventorySurvey(scanResult);
                            return;
                        }
                        if (result.getResultCode() == 100) {

                        }
                    }
                });
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

    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;

        SearchCondition sc = new SearchCondition();
        sc.Barcode = result;
        sc.BusinessClassCode = Users.BusinessClassCode;
        //sc.CustomerCode = Users.CustomerCode;
        //scanViewModel.GetScanMain(sc);
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
                scanQR();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void scanQR() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(InventorySurveyActivity.this);
        intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
        //intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
        intentIntegrator.setPrompt(InventorySurveyActivity.this.getString(R.string.qr_state_common));
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