package andoop.android.amstory.net.user;

import andoop.android.amstory.net.BaseCallback;
import andoop.android.amstory.net.BaseStatus;
import andoop.android.amstory.net.HttpBean;
import andoop.android.amstory.net.NetConfig;
import andoop.android.amstory.net.RetrofitFactory;
import andoop.android.amstory.net.user.bean.User;
import andoop.android.amstory.net.user.bean.WxLoginResponseVo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wsy on 17/5/14.
 */

public class NetUserHandler {
    private static NetUserHandler instance;

    public static NetUserHandler getInstance() {
        if (instance == null)
            instance = new NetUserHandler();
        return instance;
    }

    private NetUserHandler() {
        userService = RetrofitFactory.createRetrofit(NetConfig.IP, null)
                .create(IUserService.class);
    }

    private IUserService userService;

    public Subscription getUserInfo(BaseCallback<User> baseCallback) {
        return userService.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription loginByWeChat(String appId, String code, BaseCallback<WxLoginResponseVo> baseCallback) {
        return userService.loginByWeChat(appId, code)
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
