package andoop.android.amstory.net;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsy on 17/5/13.
 */

public interface IUserService {
    /**
     * 获取所有故事列表
     *
     * @return
     */
    @GET("/user/getAllStory")
    Observable<HttpBean> getAllStory();

    /**
     * 根据一级标签获得故事列表
     *
     * @param tagId
     * @return
     */
    @GET("/user/getStoryIdListByOneLevelTagId")
    Observable<HttpBean> getStoryIdListByOneLevelTagId(@Query("tagId") String tagId);
}