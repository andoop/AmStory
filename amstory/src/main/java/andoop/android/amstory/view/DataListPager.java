package andoop.android.amstory.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.StoryDetailActivity;
import andoop.android.amstory.StoryMakeActivity;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/15
* explain：标签页面
* * * * * * * * * * * * * * * * * * */

public class DataListPager extends Fragment {
    @InjectView(R.id.rlv_data_list_pager)
    RecyclerView recyclerView;
    @InjectView(R.id.iv_datalist_empty)
    FrameLayout frameLayout_empty;
    //当前页
    private int currentPage = 0;
    private List<Story> mData;
    private boolean is_zhedie = false;
    private boolean is_zhankai = true;
    private int page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_list_pager, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            int type = arguments.getInt("type");
            page = arguments.getInt("page");
            Log.e("----->" + "DataListPager", "onActivityCreated:page:" + page);
            //根据类型初始化数据
            initData(type);
        } else {
            showEmpty();
        }
    }

    private void showEmpty() {
        frameLayout_empty.setVisibility(View.VISIBLE);
    }


    private void initData(int type) {
        Log.e("----->" + "DataListPager", "initData:" + type);
       /* if (type==0) {
            showEmpty();
            return_icon;
        }*/

        mData = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(new DataListRcvAdapter());
        //滑动监听，折叠或者展开视图
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("----->" + "DataListPager", "onScrollStateChanged:" + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //向上滑 dy为正数  向下滑为负数
                Log.e("----->" + "DataListPager", "onScrolled:" + dx + ":" + dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View childAt = layoutManager.getChildAt(0);
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                //向上滑，并且第一个显示的是0位置item，通知视图折叠
                if (dy > 5 && recyclerView.getScrollState() == 2) {
                    if (!is_zhedie) {
                        if(page==1){
                            LocalBroadcastManager.getInstance(DataListPager.this.getActivity()).sendBroadcast(new Intent("cn.andoop.zhedie"));

                        }else if(page==2){
                            LocalBroadcastManager.getInstance(DataListPager.this.getActivity()).sendBroadcast(new Intent("cn.andoop.zhedie.record"));

                        }
                        is_zhedie = true;
                        is_zhankai = false;
                        Log.e("----->" + "DataListPager", "onScrolled:" + "折叠");
                    }

                    return;
                }

                if (dy <= 0 && firstVisibleItemPosition == 0 && recyclerView.getScrollState() == 2) {
                    if (!is_zhankai) {
                        if(page==1){
                            LocalBroadcastManager.getInstance(DataListPager.this.getActivity()).sendBroadcast(new Intent("cn.andoop.zhankai"));

                        }else if(page==2){
                            LocalBroadcastManager.getInstance(DataListPager.this.getActivity()).sendBroadcast(new Intent("cn.andoop.zhankai.record"));

                        }
                        is_zhedie = false;
                        is_zhankai = true;
                        Log.e("----->" + "DataListPager", "onScrolled:" + "展开");
                    }
                    return;
                }


                //向下滑，并且第一个显示的是0位置item，通知展开
            }
        });
        //获取故事数据
        DataManager.newInstance(getActivity()).getStories(new DataManager.StoryDataListener<List<Story>>() {
            @Override
            public void onSuccess(List<Story> stories, int page) {
                if (stories == null) {
                    showEmpty();
                    return;
                }
                mData.addAll(stories);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFail(String error) {

            }
        }, type, 0);
    }


    private class DataListRcvAdapter extends RecyclerView.Adapter<MViewHodler> {
        @Override
        public MViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MViewHodler(LayoutInflater.from(getContext()).inflate(R.layout.item_list_story,parent,false));
        }

        @Override
        public void onBindViewHolder(MViewHodler holder, int position) {

            Story story = mData.get(position);
            holder.textView.setText(story.title);
            if(TextUtils.isEmpty(story.author)) {

                story.author = "未知";
            }
            holder.author.setText(story.author);
            Picasso.with(DataListPager.this.getActivity())
                    .load(story.img)
                    .into(holder.icon);
            holder.icon.setTag(story);

            holder.content.setText(story.content.replace("&&&", ""));
            holder.title.setText(story.title+" — "+story.author);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class MViewHodler extends RecyclerView.ViewHolder {
        @InjectView(R.id.list_icon)
        ImageView icon;
        @InjectView(R.id.list_name)
        TextView textView;
        @InjectView(R.id.iv_share)
        ImageView share;
        @InjectView(R.id.iv_like)
        ImageView like;
        @InjectView(R.id.iv_praise)
        ImageView praise;
        @InjectView(R.id.tv_content)
        TextView content;
        @InjectView(R.id.tv_title)
        TextView title;
        @InjectView(R.id.list_author)
        TextView author;

        public MViewHodler(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if (tag instanceof Story) {
                        Story storyModule = (Story) tag;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("story_data", storyModule);
                        Intent intent = null;
                        if(page==1){
                            intent=new Intent(DataListPager.this.getActivity(), StoryDetailActivity.class);
                        }else {
                            intent=new Intent(DataListPager.this.getActivity(), StoryMakeActivity.class);
                        }
                        intent.putExtras(bundle);
                        DataListPager.this.getActivity().startActivity(intent);
                    }
                }
            });
        }

        @OnClick({R.id.iv_share,R.id.iv_like,R.id.iv_praise})
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.iv_share :

                    ToastUtils.showToast("分享");
                    break;
                case R.id.iv_like :

                    ToastUtils.showToast("喜欢");
                    break;
                case R.id.iv_praise :

                    ToastUtils.showToast("点赞");
                    break;
            }
        }
    }


}
