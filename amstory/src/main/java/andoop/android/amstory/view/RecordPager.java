package andoop.android.amstory.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andoop.andooptabframe.AndoopPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.customview.FoldView;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Banner;
import andoop.android.amstory.utils.DensityUtil;
import butterknife.ButterKnife;
import butterknife.InjectView;
/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/15
* explain：tab 讲故事
* * * * * * * * * * * * * * * * * * */

public class RecordPager extends BasePager {
    @InjectView(R.id.vp_record_list)
    ViewPager cateVp;
    @InjectView(R.id.vp_listen_banner)
    ViewPager bannerVp;
    @InjectView(R.id.tl_listen_list_title)
    TabLayout tabLayout;
    @InjectView(R.id.ll_below)
    FoldView  ll_below;
    @InjectView(R.id.ll_above)
    FoldView  ll_above;
    @InjectView(R.id.ll_listen_vp_points)
    LinearLayout ll_points;

    private List<DataListPager> pagers;
    private String[] titles=new String[]{
            "今日推荐",
            "睡前故事",
            "健康教育",
            "品格教育",
            "经典诗文"};

    private String ACTION_ZD="cn.andoop.zhedie.record";
    private String ACTION_ZK="cn.andoop.zhankai.record";

    //通知视图折叠和展开的广播接收者
    private BroadcastReceiver mBroadCastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_ZD)){
                //if(ll_above.getVisibility())
                ll_above.setVisibility(View.GONE);
            }else if(action.equals(ACTION_ZK)){
                ll_above.setVisibility(View.VISIBLE);
            }
        }
    };
    private List<Banner> bannerData;
    //banner viewpager的adapter
    private PagerAdapter bannerAdapter=new PagerAdapter() {
        @Override
        public int getCount() {
            return bannerData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(getActivity(), R.layout.item_vp_banner, null);
            container.addView(view);
            Banner banner = bannerData.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_vp_banner_img);
            Picasso.with(getActivity()).load(banner.img).into(imageView);

            return view;
        }
    };

    //leak
    private  Handler mhandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5:
                    int currentItem = bannerVp.getCurrentItem();
                    currentItem++;
                    if(currentItem>bannerData.size()-1){
                        currentItem=0;
                    }
                    bannerVp.setCurrentItem(currentItem);

                    break;
            }
            mhandler.sendEmptyMessageDelayed(5,3000);
        }
    };

    @Override
    protected View initGui(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_record_page,null);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this,view);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadCastReceiver);
        mhandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    @Override
    protected void initData() {
        //注册广播
        IntentFilter filter=new IntentFilter();
        filter.addAction(ACTION_ZD);
        filter.addAction(ACTION_ZK);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadCastReceiver,filter);

        //初始化banner数据
        initBanners();

        //初始化标签页面
        pagers=new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            DataListPager dataListPager = new DataListPager();
            Bundle args=new Bundle();
            args.putInt("type",i+1);
            args.putInt("page",2);
            dataListPager.setArguments(args);
            pagers.add(dataListPager);
        }

        //初始化adapter
        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getFragmentManager()){
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return pagers.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };
        cateVp.setAdapter(fragmentPagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(cateVp);
        fragmentPagerAdapter.notifyDataSetChanged();



    }

    /**
     * 初始化banner数据
     */
    private void initBanners() {
        bannerData=new ArrayList<>();
        bannerVp.setAdapter(bannerAdapter);
        bannerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ll_points.getChildCount(); i++) {
                    if(i==position)
                        ll_points.getChildAt(i).setSelected(true);
                    else
                        ll_points.getChildAt(i).setSelected(false);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        DataManager.newInstance(getActivity()).getBanners(new DataManager.StoryDataListener<List<Banner>>() {
            @Override
            public void onSuccess(List<Banner> data, int page) {
                Log.e("----->" + "ListenPager", "onSuccess:banner_size:" + data.size());
                bannerData.clear();
                bannerData.addAll(data);
                bannerAdapter.notifyDataSetChanged();
                initPoints();
            }

            @Override
            public void onFail(String error) {
                Log.e("----->" + "ListenPager", "onFail:" + error);
            }
        });
    }

    //初始化vp知识点
    private void initPoints() {
        for (int i = 0; i < bannerData.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.point_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin= DensityUtil.dip2px(getActivity(),3);
            layoutParams.rightMargin=DensityUtil.dip2px(getActivity(),3);
            ll_points.addView(imageView,layoutParams);
            if(i==0)
                imageView.setSelected(true);
        }

        if(bannerData.size()>0){
            mhandler.sendEmptyMessageDelayed(5,3000);
        }
    }

    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {

    }
}
