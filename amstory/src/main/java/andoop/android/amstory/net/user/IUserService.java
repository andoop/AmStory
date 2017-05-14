package andoop.android.amstory.net.user;

import andoop.android.amstory.net.HttpBean;
import andoop.android.amstory.net.user.bean.User;
import andoop.android.amstory.net.user.bean.WxLoginResponseVo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsy on 17/5/14.
 */

public interface IUserService {

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("/user/getUserInfo")
    Observable<HttpBean<User>> getUserInfo();

    /**
     * 用户登录
     *
     * @param appId
     * @param code
     * @return
     */
    @GET("/user/loginByWeChat")
    Observable<HttpBean<WxLoginResponseVo>> loginByWeChat(@Query("appId") String appId, @Query("code") String code);
}
