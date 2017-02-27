package andoop.android.amstory;

import android.app.Application;
import android.content.Context;

/**
 * Created by liuqi on 2017/2/26.
 */

public class ImApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
