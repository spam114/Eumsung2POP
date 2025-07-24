package com.symbol.eumsung2pop.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.eumsung2pop.model.CommonService;
import com.symbol.eumsung2pop.model.SearchCondition;
import com.symbol.eumsung2pop.model.object.Common;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CommonViewModel extends ViewModel {
    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();//false면 성공, true면 에러
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    // 서버 객체 호출 : api를 통해 서버와 연결된다.
    public CommonService service = CommonService.getInstance();//todo

    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<Common> data = new MutableLiveData<>();//todo
    public MutableLiveData<Common> data2 = new MutableLiveData<>();//todo
    public MutableLiveData<Common> data3 = new MutableLiveData<>();//todo
    public MutableLiveData<Common> data4 = new MutableLiveData<>();//todo
    public MutableLiveData<Common> data5 = new MutableLiveData<>();//todo
    public MutableLiveData<Common> data6 = new MutableLiveData<>();//todo
    /*public void GetTest() {
        loading.setValue(true);
        disposable.add(service.GetTest()
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        data.setValue(models);
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
    }*/

    public void Get(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data.setValue(models);
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

    public void Get2(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data2.setValue(models);
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

    public void Get3(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data3.setValue(models);
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

    public void Get4(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data4.setValue(models);
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

    public void Get5(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data5.setValue(models);
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

    public void Get6(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data6.setValue(models);
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

    public void NoStartLoadingBar(String apiName, SearchCondition sc) {
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data.setValue(models);
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

    public void NoStartLoadingBar2(String apiName, SearchCondition sc) {
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data2.setValue(models);
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

    public void NoEndLoadingBar(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data.setValue(models);
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

    public void NoEndLoadingBar2(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data2.setValue(models);
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

    public void NoEndLoadingBar3(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Common>() {
                    @Override
                    public void onSuccess(@NonNull Common models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        data3.setValue(models);
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

    /*public void GetStr(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.GetStr(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<SData>() {
                    @Override
                    public void onSuccess(@NonNull SData models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        str.setValue(models);
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

    public void GetStr2(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.GetStr(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<SData>() {
                    @Override
                    public void onSuccess(@NonNull SData models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        str2.setValue(models);
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

    public void GetStr3(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.GetStr(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<SData>() {
                    @Override
                    public void onSuccess(@NonNull SData models) {
                        if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }
                        str3.setValue(models);
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
    }*/

}
