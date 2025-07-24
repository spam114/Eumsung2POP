package com.symbol.eumsung2pop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.model.AppVersionService;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.AppVersion;
import com.symbol.eumsung2pop.model.object.LoginInfo;
import com.symbol.eumsung2pop.model.object.Users;
import com.symbol.eumsung2pop.view.application.ApplicationClass;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AppVersionViewModel extends ViewModel {// 사용자에게 보여줄 국가 데이터
    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    // 에러메시지
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    public MutableLiveData<AppVersion> versionlist = new MutableLiveData<>();
    public MutableLiveData<AppVersion> userDataList = new MutableLiveData<>();
    public MutableLiveData<LoginInfo> loginInfoList = new MutableLiveData<>();
    // 서버에서 발생한 에러의 내용
    //public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    public AppVersionService service = AppVersionService.getInstance();
    // os에 의해 앱의 프로세스가 죽거는 등의 상황에서
    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<String> userImage = new MutableLiveData<>();

    // 뷰에서 데이터를 가져오기 위해 호출하는 함수

    public void CheckAppVersion() {
        // 서버로부터 데이터를 받아오는 동안에 로딩 스피너를 보여주기 위
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
        loading.setValue(true);
        disposable.add(
                // service.getData()를 Unit Test에서 다루기 힘듬 -> 서비스를 새로운 스레드에서 구하기 때문
                // exedcutor rules
                // 백그라운드 스레드를 호툴할 때 즉시 안드로이드 스케줄를 위한 같은 것을 반환한다.
                service.CheckAppVersion(searchCondition) // Single<List<T>>를 반환한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<AppVersion>() {
                            @Override
                            public void onSuccess(@NonNull AppVersion models) {
                                if (models.ErrorCheck != null) {
                                    errorMsg.setValue(models.ErrorCheck);
                                    loadError.setValue(true);
                                    loading.setValue(false);
                                    return;
                                }
                                versionlist.setValue(models);
                                loadError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                errorMsg.setValue(Users.Language == 0 ? "서버 오류 발생" : "Server error occurred");
                                loadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }


    public void InsertAppLoginHistory() {
        // 서버로부터 데이터를 받아오는 동안에 로딩 스피너를 보여주기 위
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
        searchCondition.AndroidID = Users.AndroidID;
        searchCondition.Model = Users.Model;
        searchCondition.PhoneNumber = Users.PhoneNumber;
        searchCondition.DeviceName = Users.DeviceName;
        searchCondition.DeviceOS = Users.DeviceOS;
        searchCondition.AppVersion = Integer.toString(Users.VersionCode);
        searchCondition.Remark = "";
        searchCondition.UserID = Users.PhoneNumber;

        loading.setValue(true);
        disposable.add(
                // service.getData()를 Unit Test에서 다루기 힘듬 -> 서비스를 새로운 스레드에서 구하기 때문
                // exedcutor rules
                // 백그라운드 스레드를 호툴할 때 즉시 안드로이드 스케줄를 위한 같은 것을 반환한다.
                service.InsertAppLoginHistory(searchCondition) // Single<List<T>>를 반환한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<AppVersion>() {
                            @Override
                            public void onSuccess(@NonNull AppVersion models) {
                                if (models.ErrorCheck != null) {
                                    errorMsg.setValue(models.ErrorCheck);
                                    loadError.setValue(true);
                                    loading.setValue(false);
                                    return;
                                }
                                userDataList.setValue(models);
                                loadError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                errorMsg.setValue(Users.Language == 0 ? "서버 오류 발생" : "Server error occurred");
                                loadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void checkAppProgramsPowerAndLoginHistory() {
        // 서버로부터 데이터를 받아오는 동안에 로딩 스피너를 보여주기 위
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);//todo
        searchCondition.AndroidID = Users.AndroidID;//todo
        searchCondition.Model = Users.Model;//todo
        searchCondition.PhoneNumber = Users.PhoneNumber;//todo
        searchCondition.DeviceName = Users.DeviceName;//todo
        searchCondition.DeviceOS = Users.DeviceOS;//todo
        searchCondition.AppVersion = Integer.toString(Users.VersionCode);//todo
        searchCondition.Remark = "";//todo
        searchCondition.UserID = Users.PhoneNumber;//todo

        loading.setValue(true);
        disposable.add(
                // service.getData()를 Unit Test에서 다루기 힘듬 -> 서비스를 새로운 스레드에서 구하기 때문
                // exedcutor rules
                // 백그라운드 스레드를 호툴할 때 즉시 안드로이드 스케줄를 위한 같은 것을 반환한다.
                service.checkAppProgramsPowerAndLoginHistory(searchCondition) // Single<List<T>>를 반환한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<LoginInfo>() {//todo
                            @Override
                            public void onSuccess(@NonNull LoginInfo models) {
                                if (models.ErrorCheck != null) {
                                    errorMsg.setValue(models.ErrorCheck);
                                    loadError.setValue(true);
                                    loading.setValue(false);
                                    return;
                                }
                                loginInfoList.setValue(models);
                                loadError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                loadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    public void GetUserImage() {
        loading.setValue(true);
        disposable.add(service.GetUserImage()
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String models) {
                        userImage.setValue(models);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        errorMsg.setValue(Users.Language == 0 ? "서버 오류 발생" : "Server error occurred");
                        loadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                })
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        // 앱이 통신 중에 프로세스가 종료될 경우(앱이 destory됨)
        // 메모리 손실을 최소화 하기 위해 백그라운드 스레드에서 통신 작업을 중단한다.
        disposable.clear();
    }
}
