package andoop.android.amstory.data;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import andoop.android.amstory.module.Banner;
import andoop.android.amstory.module.Story;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/15
* explain：故事数据管理类
* * * * * * * * * * * * * * * * * * */
public class DataManager {
    private Context context;
    private static DataManager INSTANCE;

    private DataManager(Context context) {
        this.context = context;
    }

    public static DataManager newInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (DataManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataManager(context);
                }
            }

        }
        return INSTANCE;
    }
    //获取数据
    public void getStories(StoryDataListener<List<Story>> storyDataListener,int type,int page){

        String jsonstr = JsonReportory.newInstance(context).getStory(type,page);
        try {
            JSONObject jsonObject = dealWithErr(storyDataListener, page, jsonstr);
            if (jsonObject == null){
                if(storyDataListener!=null){
                    storyDataListener.onSuccess(null,page);
                }
                return;
            }

            JSONObject data = jsonObject.optJSONObject("data");
            JSONArray storys = data.optJSONArray("storys");
            if(storys==null){
                if(storyDataListener!=null){
                    storyDataListener.onSuccess(null,page);
                }
                return;
            }

            List<Story> storyList = new Story().parse(storys);

                if(storyDataListener!=null){
                    storyDataListener.onSuccess(storyList,page);
                }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("----->" + "DataManager", "getStories:" + e.toString());
        }


    }

    public void getBanners(StoryDataListener<List<Banner>> storyDataListener){
        String jsonstr = JsonReportory.newInstance(context).getBanner();
        try {
            JSONObject jsonObject = dealWithErr(storyDataListener, 0, jsonstr);
            if (jsonObject == null) return;

            JSONObject data = jsonObject.optJSONObject("data");
            JSONArray banners = data.optJSONArray("banner");
            if(banners==null){
                if(storyDataListener!=null){
                    storyDataListener.onSuccess(null,0);
                }
                return;
            }
            List<Banner> storyList = Banner.parse(banners);
            if(storyDataListener!=null){
                storyDataListener.onSuccess(storyList,0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("----->" + "DataManager", "getBanners:" + e.toString());
        }
    }

    @Nullable
    private JSONObject dealWithErr(StoryDataListener storyDataListener, int page, String jsonstr) throws JSONException {

        if(TextUtils.isEmpty(jsonstr)){
            if(storyDataListener!=null){
                storyDataListener.onSuccess(null,page);
            }
            return null;
        }

        JSONObject jsonObject = new JSONObject(jsonstr);
        JSONObject error = jsonObject.optJSONObject("error");
        if(error!=null&&(error.optInt("code")!=200)){
            String msg = error.optString("msg");
            if(storyDataListener!=null)
                storyDataListener.onFail(msg);
            return null;
        }
        return jsonObject;
    }

    public  interface StoryDataListener<T>{
        void onSuccess(T data,int page);
        void onFail(String error);
    }
}
