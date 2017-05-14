package andoop.android.amstory.net.work;

import android.util.Log;

import java.util.List;

import andoop.android.amstory.net.BaseCallback;
import andoop.android.amstory.net.BaseStatus;
import andoop.android.amstory.net.NetConfig;
import andoop.android.amstory.net.RetrofitFactory;
import andoop.android.amstory.net.work.bean.WorksVo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsy on 17/5/14.
 */

public class NetWorkHandler {
    private static final String TAG = NetWorkHandler.class.getSimpleName();
    private static NetWorkHandler instance;

    private NetWorkHandler() {
        Log.i(TAG, "NetWorkHandler() called");
        workService = RetrofitFactory.createRetrofit(NetConfig.IP, null)
                .create(IWorkService.class);
    }

    public static NetWorkHandler getInstance() {
        if (instance == null)
            instance = new NetWorkHandler();
        return instance;
    }

    private IWorkService workService;

    public Subscription getWorksById(String userId, BaseCallback<List<WorksVo>> baseCallback) {
        return workService.getWorksById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getWorksListByStoryId(String storyId, BaseCallback<List<WorksVo>> baseCallback) {
        return workService.getWorksListByStoryId(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }
}
