package andoop.android.amstory.net.story;

import java.util.List;

import andoop.android.amstory.net.HttpBean;
import andoop.android.amstory.net.story.bean.StoryWithTagAndSentenceVo;
import andoop.android.amstory.net.story.bean.StoryWithTagVo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsy on 17/5/13.
 */

public interface IStoryService {

    /**
     * 获取ID获取故事
     *
     * @param id
     * @return
     */
    @GET("/user/getStoryById")
    Observable<HttpBean<StoryWithTagAndSentenceVo>> getStoryById(@Query("id") String id);

    /**
     * 获取所有故事列表
     *
     * @return
     */
    @GET("/user/getAllStory")
    Observable<HttpBean<List<StoryWithTagVo>>> getAllStory();

    /**
     * 根据一级标签获得故事列表
     *
     * @param tagId
     * @return
     */
    @GET("/user/getStoryIdListByOneLevelTagId")
    Observable<HttpBean<List<StoryWithTagVo>>> getStoryIdListByOneLevelTagId(@Query("tagId") String tagId);

    /**
     * 根据二级标签获得故事列表
     *
     * @param tagId
     * @return
     */
    @GET("/user/getStoryIdListByTwoLevelTagId")
    Observable<HttpBean<StoryWithTagVo>> getStoryIdListByTwoLevelTagId(@Query("tagId") String tagId);

    /**
     * 根据标题获得故事列表
     *
     * @param query
     * @return
     */
    @GET("/user/getStoryIdListByTitle")
    Observable<HttpBean<List<StoryWithTagVo>>> getStoryIdListByTitle(@Query("query") String query);
}