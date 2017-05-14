package andoop.android.amstory.net.work;

import java.util.List;

import andoop.android.amstory.net.HttpBean;
import andoop.android.amstory.net.work.bean.WorksVo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsy on 17/5/14.
 */

public interface IWorkService {
    /**
     * 获取某个用户的作品列表
     *
     * @param userId
     * @return
     */
    @GET("/user/getWorksById")
    Observable<HttpBean<List<WorksVo>>> getWorksById(@Query("userId") String userId);

    /**
     * 获取一个故事的所有作品列表(按照点赞数降序)
     *
     * @param storyId
     * @return
     */
    @GET("/user/getWorksListByStoryId")
    Observable<HttpBean<List<WorksVo>>> getWorksListByStoryId(@Query("storyId") String storyId);
}
