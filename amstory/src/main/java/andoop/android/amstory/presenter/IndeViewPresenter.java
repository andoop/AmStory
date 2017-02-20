package andoop.android.amstory.presenter;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：indexview 对应persenter
* * * * * * * * * * * * * * * * * * */

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.presenter.view.IIndexView;

public class IndeViewPresenter extends BasePresenter<IIndexView> {

    public IndeViewPresenter(Context context, IIndexView iview) {
        super(context, iview);
    }

    /**
     * 加载故事数据
     */
    public void loadStoryData(){

        showloading();
        //从本地文件中加载txt数据

        String basepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File amstorysfile = new File(basepath, "amstory/storys");
        if(!amstorysfile.exists()){
            amstorysfile.mkdirs();
        }

        File[] listFiles = amstorysfile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("amstory") && name.endsWith(".txt");
            }
        });

        //检查是否存在数据
        if(listFiles.length<1){
            showEmpty();
            return;
        }
        //执行线程，加载本地txt
        new AsyncTask<File[],Void,List<StoryModule>>(){
            @Override
            protected List<StoryModule> doInBackground(File[]... params) {
                File[] files = params[0];
                List<StoryModule> list=new ArrayList<StoryModule>();
                for(File file:files){
                    BufferedReader bufferedReader=null;
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        String msg=null;
                        //第一行中是本story的说明信息，只读取一行即可
                        do{
                            msg=bufferedReader.readLine();
                        }while (TextUtils.isEmpty(msg));
                        //解析数据
                        Uri parse = Uri.parse(msg);
                        String id = parse.getQueryParameter("_id");
                        if(TextUtils.isEmpty(id))
                            continue;
                        StoryModule storyModule = new StoryModule();
                        storyModule._id=id;
                        storyModule.story_name=parse.getQueryParameter("story_name");
                        storyModule.story_pic=parse.getQueryParameter("story_pic");
                        storyModule.story_content=file.getAbsolutePath();
                        list.add(storyModule);
                        Log.e("----->" + "IndeViewPresenter", "doInBackground:" + list.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("----->" + "IndeViewPresenter", "doInBackground:" + e.toString());
                    }finally {
                        if(bufferedReader!=null){
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                Log.e("----->" + "IndeViewPresenter", "doInBackground:" + e.toString());
                            }
                        }
                    }
                }

                return list;
            }
            @Override
            protected void onPostExecute(List<StoryModule> storyModules) {
                super.onPostExecute(storyModules);
                if(storyModules.size()<1){
                    showEmpty();
                    return;
                }
                mView.showData(storyModules);
            }
        }.execute(listFiles);

    }
}
