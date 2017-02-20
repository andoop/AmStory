package andoop.android.amstory.module;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/16
* explain：banner数据
* * * * * * * * * * * * * * * * * * */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Banner {

    /*"desc": "banner01",
                "img": "http://img.dahe.cn/qf/2017/2/16/920U0RFP3.jpg",
                "url": "http://news.dahe.cn/2017/02-16/108287701.html"*/

    public String desc;
    public String img;
    public String url;

    public static List<Banner> parse(JSONArray banners) throws JSONException {
        List<Banner> data=new ArrayList<>();
        for (int i = 0; i < banners.length(); i++) {
            JSONObject jsonObject = banners.optJSONObject(i);
            Banner banner = new Banner();
            banner.desc=jsonObject.optString("desc");
            banner.img=jsonObject.getString("img");
            banner.url=jsonObject.getString("url");
            data.add(banner);
        }
        return data;
    }
}
