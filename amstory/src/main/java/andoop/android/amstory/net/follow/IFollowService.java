package andoop.android.amstory.net.follow;

import java.util.List;

import andoop.android.amstory.net.HttpBean;
import andoop.android.amstory.net.follow.bean.UserBaseVo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsy on 17/5/14.
 */

public interface IFollowService {
    /**
     * 关注某人
     *
     * @param userId
     * @param followeeId
     * @return
     */
    @GET("/user/follow")
    Observable<HttpBean<Boolean>> follow(@Query("userId") String userId, @Query("followeeId") String followeeId);

    /**
     * 取消关注某人
     *
     * @param userId
     * @param followeeId
     * @return
     */
    @GET("/user/unfollow")
    Observable<HttpBean<Boolean>> unfollow(@Query("userId") String userId, @Query("followeeId") String followeeId);

    /**
     * 得到某个用户的粉丝列表
     *
     * @param userId
     * @return
     */
    @GET("/user/getFollowerListByUserId")
    Observable<HttpBean<List<UserBaseVo>>> getFollowerListByUserId(@Query("userId") String userId);

    /**
     * 得到某个用户的关注列表
     *
     * @param userId
     * @return
     */
    @GET("/user/getFolloweeListByUserId")
    Observable<HttpBean<List<UserBaseVo>>> getFolloweeListByUserId(@Query("userId") String userId);
}
