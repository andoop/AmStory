package andoop.android.amstory.module;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/15
* explain：故事类
* * * * * * * * * * * * * * * * * * */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Story implements Serializable{

    /* "author": "",
                "content": "",
                "id": "",
                "img": "",
                "title": "",
                "voice": ""*/

    public String id;
    public String title;
    public String img;
    public String content;
    public String voice;
    public String author;

    public  List<Story> parse(JSONArray json) throws JSONException {

       List<Story> data=new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = json.optJSONObject(i);
            Story story = new Story();
            story.id=jsonObject.optString("id");
            story.content=jsonObject.getString("content");
            story.title=jsonObject.getString("title");
            story.img=jsonObject.getString("img");
            story.author=jsonObject.optString("author");
            story.voice=jsonObject.getString("voice");

            data.add(story);
        }
        return data;
    }
}
