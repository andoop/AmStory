package andoop.android.amstory.net.story;

import android.util.Log;

import java.util.List;

import andoop.android.amstory.net.BaseCallback;
import andoop.android.amstory.net.BaseStatus;
import andoop.android.amstory.net.NetConfig;
import andoop.android.amstory.net.RetrofitFactory;
import andoop.android.amstory.net.story.bean.StoryWithTagAndSentenceVo;
import andoop.android.amstory.net.story.bean.StoryWithTagVo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsy on 17/5/14.
 */

public class NetStoryHandler {
    private static final String TAG = NetStoryHandler.class.getSimpleName();

    private static NetStoryHandler instance;

    private NetStoryHandler() {
        Log.i(TAG, "NetStoryHandler() called");
        storyService = RetrofitFactory.createRetrofit(NetConfig.IP, null)
                .create(IStoryService.class);
    }

    public static NetStoryHandler getInstance() {
        if (instance == null)
            instance = new NetStoryHandler();
        return instance;
    }

    private IStoryService storyService;

    public Subscription getStoryById(String id, BaseCallback<StoryWithTagAndSentenceVo> baseCallback) {
        return storyService.getStoryById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getAllStory(BaseCallback<List<StoryWithTagVo>> baseCallback) {
        return storyService.getAllStory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getStoryIdListByOneLevelTagId(String tagId, BaseCallback<List<StoryWithTagVo>> baseCallback) {
        return storyService.getStoryIdListByOneLevelTagId(tagId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getStoryIdListByTwoLevelTagId(String tagId, BaseCallback<StoryWithTagVo> baseCallback) {
        return storyService.getStoryIdListByTwoLevelTagId(tagId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getStoryIdListByTitle(String title, BaseCallback<List<StoryWithTagVo>> baseCallback) {
        return storyService.getStoryIdListByTitle(title)
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
