package andoop.android.amstory.presenter;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/28
* explain：
* * * * * * * * * * * * * * * * * * */

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.presenter.view.IMyRecordListView;

public class MRecordListPresenter extends BasePresenter<IMyRecordListView>{
    public MRecordListPresenter(Context context, IMyRecordListView iview) {
        super(context, iview);
    }

    //从本地数据库和本地存储的文件中加载数据
    public void loadData() {
       showloading();

        //从文件中读取数据
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "amstory/amstory_audios";
        File savefile = new File(savePath);
        if(!savefile.exists()){
            showEmpty();
            return;
        }
        File[] files = savefile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".wav") || name.endsWith(".m4a");
            }
        });

        if(files==null&&files.length<1){
            showEmpty();
            return;
        }
        List<StoryModule> storyModules=new ArrayList<>();
        for(File file:files){
            StoryModule storyModule = new StoryModule();
            String[] split = file.getName().split("_");
            if(split.length==2){
                //解析名称和时间
                storyModule.story_name=split[0];
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d1=new Date(Long.parseLong(split[1].substring(0,split[1].length()-4)));
                String time=format.format(d1);
                storyModule.time=time;
                storyModule.voice_file=file.getAbsolutePath();
                storyModules.add(storyModule);
            }
        }
        mView.showData(storyModules);
    }
}
