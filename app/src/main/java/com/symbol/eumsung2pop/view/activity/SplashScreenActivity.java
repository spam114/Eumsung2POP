package com.symbol.eumsung2pop.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.databinding.ActivitySplashScreenBinding;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.CommonMethod;
import com.symbol.eumsung2pop.view.PermissionUtil;
import com.symbol.eumsung2pop.view.application.ApplicationClass;
import com.symbol.eumsung2pop.viewmodel.AppVersionViewModel;
import com.symbol.eumsung2pop.view.SoundManager;

import java.io.File;

public class SplashScreenActivity extends BaseActivity {
    ActivitySplashScreenBinding binding;
    AppVersionViewModel viewModel;

    //버전다운로드 관련 변수
    DownloadManager mDm;
    long mId = 0;
    //Handler mHandler;
    //String serverVersion;
    String downloadUrl;
    ProgressDialog mProgressDialog;
    //버전 변수 끝

    SharedPreferences _pref;
    Boolean isShortcut = false;//아이콘의 생성

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setPermissionList();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        _pref = getSharedPreferences("kumkang", MODE_PRIVATE);//sharedPreferences 이름: "kumkang"에 저장
        isShortcut = _pref.getBoolean("isShortcut", false);//"isShortcut"에 들어있는값을 가져온다.
        if (!isShortcut)//App을 처음 깔고 시작했을때 이전에 깐적이 있는지없는지 검사하고, 이름과 아이콘을 설정한다.
        {
            addShortcut(this);
        }
        Users.VersionCode = CommonMethod.getAppVersionCode(this);
        Users.ServiceAddress = ApplicationClass.getResourses().getString(R.string.service_address);
        Users.LoginServiceAddress = ApplicationClass.getResourses().getString(R.string.service_address_login);
        viewModel = new ViewModelProvider(this).get(AppVersionViewModel.class);
        viewModel.CheckAppVersion();
        observerViewModel();
        Users.SoundManager = new SoundManager(this);
    }

    /**
     * 버전에 따라 Permission 리스트를 셋팅한다.
     */
    private void setPermissionList() {
        //int permissionCount;
        /*if (Build.VERSION.SDK_INT < 31){
            PermissionUtil.permissionList=new String[4];
            PermissionUtil.permissionList[0]=Manifest.permission.READ_PHONE_STATE;
            PermissionUtil.permissionList[1]=Manifest.permission.READ_PHONE_NUMBERS;
            PermissionUtil.permissionList[2]=Manifest.permission.CAMERA;
            PermissionUtil.permissionList[3]=Manifest.permission.READ_EXTERNAL_STORAGE;//33이상부터 분할 READ_MEDIA_AUDIO, READ_MEDIA_IMAGES, READ_MEDIA_VIDEO
        }*/
        if (Build.VERSION.SDK_INT < 33){
            PermissionUtil.permissionList=new String[4];
            PermissionUtil.permissionList[0]=Manifest.permission.READ_PHONE_STATE;
            PermissionUtil.permissionList[1]=Manifest.permission.READ_PHONE_NUMBERS;
            PermissionUtil.permissionList[2]=Manifest.permission.CAMERA;
            PermissionUtil.permissionList[3]=Manifest.permission.READ_EXTERNAL_STORAGE;//33이상부터 분할 READ_MEDIA_AUDIO, READ_MEDIA_IMAGES, READ_MEDIA_VIDEO
        }
        else {//최신
            PermissionUtil.permissionList=new String[3];
            PermissionUtil.permissionList[0]=Manifest.permission.READ_PHONE_STATE;
            PermissionUtil.permissionList[1]=Manifest.permission.READ_PHONE_NUMBERS;
            PermissionUtil.permissionList[2]=Manifest.permission.CAMERA;
            //PermissionUtil.permissionList[3]=Manifest.permission.READ_MEDIA_IMAGES;//안돼 해결책을 찾아라
        }


    }

    private void observerViewModel() {
        /*  *
         *  뷰(메인 화면)에 라이브 데이터를 붙인다.
         *  메인 화면에서 관찰할 데이터를 설정한다.
         *  라이브 데이터가 변경됐을 때 변경된 데이터를 가지고 UI를 변경한다.*/
        viewModel.versionlist.observe(this, models -> {
            // 데이터 값이 변할 때마다 호출된다.
            if (models != null) {
                downloadUrl = models.AppUrl;
                String serverVersion = models.AppVersion;

                if (Double.parseDouble(serverVersion) > getCurrentVersion()) {//좌측이 DB에 있는 버전
                    downloadNewVersion();
                } else {
                    CheckPermission();
                }
            } else {
                finish();
            }
        });
        viewModel.loginInfoList.observe(this, models -> {
            if (models != null) {
                Users.AppAuthorityList = models.AppAuthorityList;
                Users.UserID = models.UserID;
                Users.UserName = models.UserName;

               /* Intent intent;
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);*/


                //Users.CustomerCodeSammi = models.CustomerCodeSammi;
                viewModel.GetUserImage();


            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 유저이미지를 받은 후, MainActivity로 들어간다.
         */
        viewModel.userImage.observe(this, models -> {
            if(models!=null){
                Users.UserImage = models;
            }
            else{
                Users.UserImage = "fail";
            }
            Intent intent;
            intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            //intent.putExtra("FirstFlag", true);
            startActivity(intent);
        });

        //에러발생
        viewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                progressOFF2();
            }
        });

        viewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                // 로딩 중이라는 것을 보여준다.
                if (isLoading) {
                    startProgress();
                } else {
                    progressOFF2();
                }
            }
        });
    }

    private void downloadNewVersion() {
        new android.app.AlertDialog.Builder(SplashScreenActivity.this).setMessage("새로운 버전이 있습니다. 다운로드 할까요?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mProgressDialog = ProgressDialog.show(SplashScreenActivity.this, "다운로드", "잠시만 기다려주세요");

                        Uri uri = Uri.parse(downloadUrl);
                        DownloadManager.Request req = new DownloadManager.Request(uri);
                        req.setTitle("음성2공장 POP 다운로드");
                        req.setDestinationInExternalFilesDir(SplashScreenActivity.this, Environment.DIRECTORY_DOWNLOADS, "KUMKANG.apk");

                        //req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pathSegments.get(pathSegments.size() - 1));
                        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();


                        req.setDescription("음성2공장 POP 설치파일을 다운로드 합니다.");
                        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        mId = mDm.enqueue(req);
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            registerReceiver(mDownComplete2, filter, RECEIVER_EXPORTED);
                        }else {
                            registerReceiver(mDownComplete2, filter);
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SplashScreenActivity.this, "최신버전으로 업데이트 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
                        //showErrorDialog(SplashScreenActivity.this, "최신버전으로 업데이트 하시기 바랍니다.", 2);
                        ActivityCompat.finishAffinity(SplashScreenActivity.this);
                    }
                }).show();
    }

    /* *
     * 다운로드 완료 이후의 작업을 처리한다.(다운로드 파일 열기)*/

    BroadcastReceiver mDownComplete2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Toast.makeText(context, "다운로드 완료", Toast.LENGTH_SHORT).show();
            //showErrorDialog(SplashScreenActivity.this, "다운로드 완료",1);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mId);
            Cursor cursor = mDm.query(query);
            if (cursor.moveToFirst()) {

                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);

                //String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                //int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    @SuppressLint("Range") String fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    openFile(fileUri);
                }
            }
        }
    };

    protected void openFile(String uri) {

        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri)).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Intent open = new Intent(Intent.ACTION_VIEW);
        open.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if(open.getFlags()!=Intent.FLAG_GRANT_READ_URI_PERMISSION){//권한 허락을 안한다면
            Toast.makeText(getBaseContext(), "Look!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//누가 버전 이상이라면 FileProvider를 사용한다.
            //Toast.makeText(getBaseContext(), "test1", Toast.LENGTH_SHORT).show();
            uri = uri.substring(7);
            File file = new File(uri);
            Uri u = FileProvider.getUriForFile(this, getApplication().getPackageName() + ".fileprovider", file);
            open.setDataAndType(u, mimetype);
        } else {
            open.setDataAndType(Uri.parse(uri), mimetype);
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }


        Toast.makeText(getBaseContext(), "설치 완료 후, 어플리케이션을 다시 시작하여 주십시요.", Toast.LENGTH_SHORT).show();
        startActivity(open);
        // finish();//startActivity 전일까 후일까 잘판단

    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.haveAllpermission(this, PermissionUtil.permissionList)) {//모든 퍼미션 허용
                checkAppProgramsPowerAndLoginHistory();
            } else {//퍼미션 하나라도 허용 안함
                ActivityCompat.requestPermissions(this, PermissionUtil.permissionList, PermissionUtil.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {//낮은 버전이면 바로 번호 받기가능
            checkAppProgramsPowerAndLoginHistory();
        }
    }

    @SuppressLint("MissingPermission")
    private void checkAppProgramsPowerAndLoginHistory() {
        try {
            TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Users.AndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            if (Users.AndroidID == null)
                Users.AndroidID = "";
            Users.Model = Build.MODEL;
            if (Users.Model == null)
                Users.Model = "";
            Users.PhoneNumber = systemService.getLine1Number();//없으면 null이들어갈수도있다 -> if(Users.PhoneNumber==null) 으로 활용가능
            //Users.PhoneNumber = "010-6737-5288";//없으면 null이들어갈수도있다 -> if(Users.PhoneNumber==null) 으로 활용가능
            if (Users.PhoneNumber == null)
                Users.PhoneNumber = "";
            else
                Users.PhoneNumber = Users.PhoneNumber.replace("+82", "0");
            Users.DeviceOS = Build.VERSION.RELEASE;
            if (Users.DeviceOS == null)
                Users.DeviceOS = "";
            Users.Remark = "";
            /*android.bluetooth.BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            String deviceName;
            if (bluetoothAdapter == null) {
                deviceName = "";
            } else {
                deviceName = bluetoothAdapter.getName();
            }
            Users.DeviceName = deviceName;//블루투스 권한(BLUETOOTH_CONNECT) 필요*/
        } catch (Exception e) {
            String str = e.getMessage();
            String str2 = str;
        } finally {
            viewModel.checkAppProgramsPowerAndLoginHistory();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE &&
                grantResults.length == PermissionUtil.permissionList.length) {

            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            //허용
            if (check_result) {
                //허용했을때 로직
                checkAppProgramsPowerAndLoginHistory();
            }
            //거부 ,재거부
            else {
                if (PermissionUtil.recheckPermission(this, PermissionUtil.permissionList)) {
                    //거부 눌렀을 때 로직
                    Toast.makeText(this, "앱에 로그인하기 위해 반드시 필요합니다.", Toast.LENGTH_LONG).show();

                    Intent intent = getIntent();
                    setResult(RESULT_CANCELED, intent);
                    finish();

                } else {
                    //재거부 눌렀을 때 로직
                    Toast.makeText(this, "앱에 로그인하기 위해 반드시 필요합니다.", Toast.LENGTH_LONG).show();

                    Intent intent = getIntent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        }
        //end region

    }

    public int getCurrentVersion() {

        int version;

        try {
            mDm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = i.versionCode;
            //Users.CurrentVersion = version;

            return version;

        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    private void addShortcut(Context context) {

        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.setClassName(context, getClass().getName());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        //FLAG_ACTIVITY_NEW_TASK: 실행한 액티비티와 관련된 태스크가 존재하면 동일한 태스크내에서 실행하고, 그렇지 않으면 새로운 태스크에서 액티비티를 실행하는 플래그
        //FLAG_ACTIVITY_RESET_TASK_IF_NEEDED: 사용자가 홈스크린이나 "최근 실행 액티비티목록"에서 태스크를 시작할 경우 시스템이 설정하는 플래그, 이플래그는 새로 태스크를
        //시작하거나 백그라운드 태스크를 포그라운드로 가지고 오는 경우가 아니라면 영향을 주지 않는다, "최근 실행 액티비티 목록":  홈 키를 오랫동안 눌렀을 떄 보여지는 액티비티 목록

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);//putExtra(이름, 실제값)
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "KUMKANGREADER");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.logo2));
        //Intent.ShortcutIconResource.fromContext(context, R.drawable.img_kumkang);
        intent.putExtra("duplicate", false);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        sendBroadcast(intent);
        SharedPreferences.Editor editor = _pref.edit();
        editor.putBoolean("isShortcut", true);

        editor.commit();
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
}