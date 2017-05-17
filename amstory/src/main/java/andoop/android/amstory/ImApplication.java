package andoop.android.amstory;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import andoop.android.amstory.utils.ToastUtils;
import andoop.android.amstory.utils.UILImageLoader;
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
//        MultiDex.install(this);
        initGallery();
        initUmeng();
        ToastUtils.context = this;
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
