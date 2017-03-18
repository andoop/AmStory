package andoop.android.amstory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.andoop.andooptabframe.AndoopPage;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.AllCatesActivity;
import andoop.android.amstory.R;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.view.DataListPager;
import butterknife.ButterKnife;
import butterknife.InjectView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/12
* explain：听故事页面
* * * * * * * * * * * * * * * * * * */
public class TinggushiFragment extends BasePager {
    @InjectView(R.id.tl_tinggushi)
    TabLayout tabLayout;
    @InjectView(R.id.vp_tingghushi)
    ViewPager viewPager;
    @InjectView(R.id.tv_tinggushi_all_cat)
    TextView tv_allcat;
    private List<DataListPager> pagers;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private String[] titles=new String[]{
            "睡前故事",
            "健康教育",
            "品格教育",
            "经典诗文"};

    @Override
    protected View initGui(LayoutInflater inflater) {
        return inflater.inflate(R.layout.tingshushi_fragment_layout,null);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this,view);
        tv_allcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去全部分类页面
                toAllCats();
            }
        });

    }

    private void toAllCats() {
        getActivity().startActivity(new Intent(getActivity(), AllCatesActivity.class));
    }

    @Override
    protected void initData() {
        //初始化标签页面
        pagers=new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            DataListPager dataListPager = new DataListPager();
            Bundle args=new Bundle();
            args.putInt("type",i+1);
            args.putInt("page",1);
            dataListPager.setArguments(args);
            pagers.add(dataListPager);
        }
        //初始化adapter
        fragmentPagerAdapter=new FragmentPagerAdapter(getFragmentManager()){
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
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        fragmentPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {
        Log.e("----->" + "TinggushiFragment", "onSelect:" + pos);
    }
}
