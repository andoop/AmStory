package andoop.android.amstory;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.umeng.analytics.MobclickAgent;

import andoop.android.amstory.utils.UILImageLoader;
import cn.bmob.v3.Bmob;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by liuqi on 2017/2/26.
 */

public class ImApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MultiDex.install(this);
        initGallery();
        initUmeng();
        initBmob();
    }

    //初始化
    private void initBmob() {
        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "bdf96571ec27fd2fa5b24a562ee1fd15");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    private void initUmeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    private void initGallery() {
        //设置主题
        // ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder().build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        //配置imageloader
        ImageLoader imageloader = new UILImageLoader();
        CoreConfig coreConfig =
                new CoreConfig.Builder(context, imageloader, theme)
                        .setFunctionConfig(functionConfig)
                        .build();
        GalleryFinal.init(coreConfig);
    }

    public static Context getContext() {
        return context;
    }
}
