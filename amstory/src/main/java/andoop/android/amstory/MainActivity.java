package andoop.android.amstory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.andoop.andooptabframe.AndoopFrameListener;
import com.andoop.andooptabframe.AndoopPage;
import com.andoop.andooptabframe.AndoopTabFrame;
import com.andoop.andooptabframe.core.AndoopFrame;
import com.andoop.andooptabframe.core.TabFrameConfig;

import andoop.android.amstory.fragments.FaxianFragment;
import andoop.android.amstory.fragments.PersonalFragment;
import andoop.android.amstory.fragments.TinggushiFragment;
import andoop.android.amstory.fragments.TuijianFragment;
import andoop.android.amstory.view.ListenPager;
import andoop.android.amstory.view.RecordPager;

public class MainActivity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabFrameConfig tabFrameConfig = new TabFrameConfig.Builder()
                .tabColorString("#ffffff")
                .canScroll(false)
               // .setCacheCount(2)
                .build();
        AndoopTabFrame.getInstance().init(tabFrameConfig);
        AndoopTabFrame.getInstance().build(this, R.id.fl_content, new AndoopFrameListener() {
            @Override
            public void onReady(AndoopFrame andoopFrame) {
                andoopFrame.addPage(new TuijianFragment(),R.drawable.tuijian_selector,"推荐");
                andoopFrame.addPage(new TinggushiFragment(),R.drawable.tinggushi_selector,"听故事");
                andoopFrame.addPage(new FaxianFragment(),R.drawable.faxian_icon_selector,"发现");
                andoopFrame.addPage(new PersonalFragment(),R.drawable.geren_selector,"个人");
                andoopFrame.commit();
            }

            @Override
            public void onSelect(AndoopPage andoopPage, int pos) {

            }
        });
    }
}
