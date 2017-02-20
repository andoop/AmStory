package andoop.android.amstory.presenter;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import andoop.android.amstory.module.Story;
import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.presenter.view.IStoryMakeView;
/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：
* * * * * * * * * * * * * * * * * * */

public class StoryMakeViewPresenter extends BasePresenter<IStoryMakeView> {
    public StoryMakeViewPresenter(Context context, IStoryMakeView iview) {
        super(context, iview);
    }

    /**
     * 加载本地txt文本内容
     * @param storyModule
     */
    public void loadData(final Story storyModule) {
        mView.showData(storyModule);
        //showloading();
        //根据路径，读取txt文件内容

//        new AsyncTask<String,Void,String>(){
//            @Override
//            protected String doInBackground(String... params) {
//
//                File file = new File(storyModule.content);
//                BufferedReader reader=null;
//                String result="";
//                String line="";
//                try {
//                    reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//                    do{}while (reader.readLine()==null);
//                    while ((line=reader.readLine())!=null){
//                        result+=line;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    if(reader!=null){
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                storyModule.text=s;
//                mView.showData(storyModule);
//            }
//        }.execute(storyModule.story_content);story_content
    }
}
