package andoop.android.amstory.module;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/14
* explain：分类模型
* * * * * * * * * * * * * * * * * * */
public class Cates {

    public String cat;
    public String[] list;




    public static List<Cates> parse(JSONArray cates) {
        List<Cates> data=new ArrayList<>();
        for (int i = 0; i < cates.length(); i++) {
            JSONObject jsonObject = cates.optJSONObject(i);
            Cates cate = new Cates();
            cate.cat=jsonObject.optString("cat");
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            if(jsonArray!=null&&jsonArray.length()>0){
                cate.list=new String[jsonArray.length()];
                for (int j = 0; j < jsonArray.length(); j++) {
                    cate.list[j]=jsonArray.optString(j);
                }
            }
            data.add(cate);
        }
        return data;
    }
}
