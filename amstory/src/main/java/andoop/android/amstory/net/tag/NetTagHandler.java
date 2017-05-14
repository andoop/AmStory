package andoop.android.amstory.net.tag;

import android.util.Log;

import java.util.List;

import andoop.android.amstory.net.BaseCallback;
import andoop.android.amstory.net.BaseStatus;
import andoop.android.amstory.net.NetConfig;
import andoop.android.amstory.net.RetrofitFactory;
import andoop.android.amstory.net.tag.bean.StoryTag;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsy on 17/5/14.
 */

public class NetTagHandler {
    private static final String TAG = NetTagHandler.class.getSimpleName();

    private static NetTagHandler instance;

    private NetTagHandler() {
        Log.i(TAG, "NetTagHandler() called");
        tagService = RetrofitFactory.createRetrofit(NetConfig.IP, null)
                .create(ITagService.class);
    }

    public static NetTagHandler getInstance() {
        if (instance == null)
            instance = new NetTagHandler();
        return instance;
    }

    private ITagService tagService;

    public Subscription getAllOneLevelTags(BaseCallback<List<StoryTag>> baseCallback) {
        return tagService.getAllOneLevelTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    baseCallback.result(bean.getStatus(), bean.getObj());
                }, throwable -> {
                    throwable.printStackTrace();
                    baseCallback.result(BaseStatus.ERROR, null);
                });
    }

    public Subscription getTagListByParentId(String parentId, BaseCallback<List<StoryTag>> baseCallback) {
        return tagService.getTagListByParentId(parentId)
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
