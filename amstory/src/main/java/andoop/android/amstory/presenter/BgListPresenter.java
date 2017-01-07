package andoop.android.amstory.presenter;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.module.LocalMusicModule;
import andoop.android.amstory.presenter.view.IBgListView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/1/7
* explain：
* * * * * * * * * * * * * * * * * * */

public class BgListPresenter extends BasePresenter<IBgListView>{
    public BgListPresenter(Context context, IBgListView iview) {
        super(context, iview);
    }

    public void loadData(String filepath) {
        showloading();
        //从本地文件中加载数据
        File file = new File(filepath);
        if(!file.exists()){
            file.mkdirs();
        }


        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".wav")||name.endsWith(".mp3");
            }
        });

        if(files==null||files.length<=0){
            showEmpty();
            return;
        }
        List<LocalMusicModule> datas=new ArrayList<>();
        for(File f:files){
            LocalMusicModule localMusicModule = new LocalMusicModule();
            localMusicModule.name=f.getName();
            localMusicModule.path=f.getAbsolutePath();
            datas.add(localMusicModule);
        }

        mView.showData(datas);

    }
}
