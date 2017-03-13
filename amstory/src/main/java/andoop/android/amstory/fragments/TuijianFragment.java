package andoop.android.amstory.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoop.andooptabframe.AndoopPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.customview.jazzyviewpager.JazzyViewPager;
import andoop.android.amstory.customview.jazzyviewpager.OutlineContainer;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Story;
import butterknife.ButterKnife;
import butterknife.InjectView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/12
* explain：推荐页面
* * * * * * * * * * * * * * * * * * */

public class TuijianFragment extends BasePager {
    @InjectView(R.id.vp_tuijian)
    JazzyViewPager jazzyViewPager;
    private List<Story> mData;
    @Override
    protected View initGui(LayoutInflater inflater) {
        return inflater.inflate(R.layout.tuijian_fragment_layout,null);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this,view);
    }

    @Override
    protected void initData() {
        mData=new ArrayList<>();
        //设置adataper
        jazzyViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                if (view instanceof OutlineContainer) {
                    return ((OutlineContainer) view).getChildAt(0) == object;
                } else {
                    return view == object;
                }
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=initItemView(position);
                container.addView(view);
                jazzyViewPager.setObjectForPosition(view, position);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
               container.removeView(jazzyViewPager.findViewFromObject(position));
            }
        });

        //设置切换动效
        jazzyViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);

        //开始加载数据，显示loading
        showloading();
        //获取数据
        DataManager.newInstance(getActivity()).getStories(new DataManager.StoryDataListener<List<Story>>() {
            @Override
            public void onSuccess(List<Story> data, int page) {
                //停止加载
                stoploading();
                mData.clear();
                mData.addAll(data);
                jazzyViewPager.getAdapter().notifyDataSetChanged();

            }
            @Override
            public void onFail(String error) {
                showError(error);
            }
        },DataManager.TYPE_TUIJIAN,0);
    }

    //初始化itme视图
    private View initItemView(int position) {
        Story story = mData.get(position);
        View view = View.inflate(getActivity(), R.layout.tuijian_fragment_vp_item, null);
        ImageView icon= (ImageView) view.findViewById(R.id.iv_icon_tuijian_vp_item);
        TextView name= (TextView) view.findViewById(R.id.tv_name_tuijian_item);
        TextView author= (TextView) view.findViewById(R.id.tv_author_tuijian_item);
        TextView con= (TextView) view.findViewById(R.id.tv_content_tuijian_item);

        icon.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(getActivity()).load(story.img).into(icon);
        name.setText(story.title);
        author.setText(story.author );
        con.setText(story.content.replace("&&&",""));

        return view;
    }


    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {
        Log.e("----->" + "TuijianFragment", "onSelect:" + pos);
    }


}
