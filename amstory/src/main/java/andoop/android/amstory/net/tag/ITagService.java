package andoop.android.amstory.net.tag;

import java.util.List;

import andoop.android.amstory.net.HttpBean;
import andoop.android.amstory.net.tag.bean.StoryTag;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsy on 17/5/14.
 */

public interface ITagService {
    /**
     * 获得所有一级标签
     *
     * @return
     */
    @GET("/user/getAllOneLevelTags")
    Observable<HttpBean<List<StoryTag>>> getAllOneLevelTags();

    /**
     * 根据父标签ID（目前是根据一级标签id或者二级标签）获得下一级标签列表
     *
     * @param parentId
     * @return
     */
    @GET("/user/getTagListByParentId")
    Observable<HttpBean<List<StoryTag>>> getTagListByParentId(@Query("parentId") String parentId);
}
