package andoop.android.amstory.net.follow;

import java.util.List;

import andoop.android.amstory.net.BaseCallback;
import andoop.android.amstory.net.BaseStatus;
import andoop.android.amstory.net.NetConfig;
import andoop.android.amstory.net.RetrofitFactory;
import andoop.android.amstory.net.follow.bean.UserBaseVo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsy on 17/5/14.
 */

public class NetFollowHandler {
    private static NetFollowHandler instance;

    public static NetFollowHandler getInstance() {
        if (instance == null)
            instance = new NetFollowHandler();
        return instance;
    }

    private NetFollowHandler() {
        followService = RetrofitFactory.createRetrofit(NetConfig.IP, null)
                .create(IFollowService.class);
    }

    private IFollowService followService;

    public Subscription follow(String userId, String followeeId, BaseCallback<Boolean> baseCallback) {
        return followService.follow(userId, followeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription unfollow(String userId, String followeeId, BaseCallback<Boolean> baseCallback) {
        return followService.unfollow(userId, followeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getFollowerListByUserId(String userId, BaseCallback<List<UserBaseVo>> baseCallback) {
        return followService.getFollowerListByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getFolloweeListByUserId(String userId, BaseCallback<List<UserBaseVo>> baseCallback) {
        return followService.getFolloweeListByUserId(userId)
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
