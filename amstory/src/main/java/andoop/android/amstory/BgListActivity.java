package andoop.android.amstory;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.base.BaseActivity;
import andoop.android.amstory.fragments.music.MusicPage;
import andoop.android.amstory.module.LocalMusicModule;
import andoop.android.amstory.module.MusicCat;
import andoop.android.amstory.presenter.BgListPresenter;
import andoop.android.amstory.presenter.view.IBgListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BgListActivity extends BaseActivity<BgListPresenter> implements IBgListView{
    @InjectView(R.id.vp_choose_bg)
    ViewPager mVp;
    @InjectView(R.id.tv_title)
    TextView title;
    @InjectView(R.id.tl_ab_cats)
    TabLayout tabLayout;
    private List<MusicPage> mData;
    @InjectView(R.id.tv_bg_add)
    TextView tvadd;
    String mPath;
    String mResult;
    //所有分类
    private List<MusicCat> catList;
    private BroadcastReceiver dataReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("music_choose_data")){
                mResult=intent.getExtras().getString("path");
                tvadd.setBackgroundColor(Color.parseColor("#303F9F"));
                tvadd.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    };
    public static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_list);
        ButterKnife.inject(this);
        //注册广播
        IntentFilter filter=new IntentFilter("music_choose_data");
        LocalBroadcastManager.getInstance(this).registerReceiver(dataReceiver,filter);
        type = getIntent().getIntExtra("type", 1);
        if(type ==1){
            initBgMusic();
        }else if(type ==2){
            initMusic();
        }
        //mData=new ArrayList();
        //TODO 更改
        //mPresenter.loadData(mPath);
        //初始化分类选项卡和对应页面
        initCateTagAndPages();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dataReceiver);
        super.onDestroy();
    }

    private void initMusic() {
        title.setText("特殊音效");
        mPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/addata/effect";
        catList=mPresenter.getAllMusicCate();

    }

    private void initBgMusic() {
        title.setText("背景音乐");
        mPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/addata/backgroud";
        catList=mPresenter.getAllBgMusicCate();
    }

    private void initCateTagAndPages() {

        mData=new ArrayList<>();
        if(catList!=null&&catList.size()>0){

            for(MusicCat cate:catList){

                MusicPage musicPage = new MusicPage();
                Bundle args=new Bundle();
                args.putString("path",mPath+"/"+cate.catePath);
                musicPage.setArguments(args);
                mData.add(musicPage);
            }

        }

        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mData.get(position);
            }

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return catList.get(position).cateName;
            }
        });

        mVp.setOffscreenPageLimit(catList.size()-1);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mVp);
        mVp.getAdapter().notifyDataSetChanged();

    }


    //添加所选音效
    public void addMusic(View view){
        if(TextUtils.isEmpty(mResult)){
            Toast.makeText(this, "请选择要添加的音效或背景音乐", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(BgListActivity.this, StoryMakeActivity.class);
        intent.putExtra("path",mResult);
        BgListActivity.this.setResult(100,intent);
        BgListActivity.this.finish();
    }

    @Override
    protected BgListPresenter initPresenter() {
        return new BgListPresenter(this,this);
    }

    @Override
    public void showData(List<LocalMusicModule> data) {
        //stoploading();
        //mData.clear();
       // mData.addAll(data);
      //  recyclerView.getAdapter().notifyDataSetChanged();
    }

    public static void start(Activity context, Bundle bundle){
        Intent intent = new Intent(context, BgListActivity.class);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
    }
    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    //点击半透明的View的时候finish
    public void onFinish(View v) {

        finish();
    }

}
