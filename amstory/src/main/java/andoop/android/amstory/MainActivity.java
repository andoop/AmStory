package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.andoop.andooptabframe.AndoopFrameListener;
import com.andoop.andooptabframe.AndoopPage;
import com.andoop.andooptabframe.AndoopTabFrame;
import com.andoop.andooptabframe.core.AndoopFrame;
import com.andoop.andooptabframe.core.TabFrameConfig;

import andoop.android.amstory.fragments.FaxianFragment;
import andoop.android.amstory.fragments.PersonalFragment;
import andoop.android.amstory.fragments.TinggushiFragment;
import andoop.android.amstory.fragments.TuijianFragment;
import andoop.android.amstory.fragments.UserSettingActivity;
import andoop.android.amstory.jni.AudioDataProcessor;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {

    @InjectView(R.id.iv_title_search)
    ImageView ivTitleSearch;
    @InjectView(R.id.iv_play_ct)
    ImageView ivPlayCt;
    @InjectView(R.id.iv_st_ct)
    ImageView ivStCt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Log.e("----->" + "MainActivity", "onCreate:" + new AudioDataProcessor().test());

        TabFrameConfig tabFrameConfig = new TabFrameConfig.Builder()
                .tabColorString("#ffffff")
                .canScroll(false)
                .setCacheCount(3)
                .build();
        AndoopTabFrame.getInstance().init(tabFrameConfig);
        AndoopTabFrame.getInstance().build(this, R.id.fl_content, new AndoopFrameListener() {
            @Override
            public void onReady(AndoopFrame andoopFrame) {
                andoopFrame.addPage(new TuijianFragment(), R.drawable.tuijian_selector, "推荐");
                andoopFrame.addPage(new TinggushiFragment(), R.drawable.tinggushi_selector, "听故事");
                andoopFrame.addPage(new FaxianFragment(), R.drawable.faxian_icon_selector, "发现");
                andoopFrame.addPage(new PersonalFragment(), R.drawable.geren_selector, "个人");
                andoopFrame.commit();
            }

            @Override
            public void onSelect(AndoopPage andoopPage, int pos) {

            }
        });
    }


    public void init() {
        //设置点击事件
        ivTitleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    public ImageView getIvStCt(){
        return ivStCt;
    }
}
