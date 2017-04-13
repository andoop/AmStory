package andoop.android.amstory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andoop.andooptabframe.AndoopPage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.MainActivity;
import andoop.android.amstory.R;
import andoop.android.amstory.SearchActivity;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.utils.SpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/12
* explain：个人中心页面
* * * * * * * * * * * * * * * * * * */

public class PersonalFragment extends BasePager {
    @InjectView(R.id.rv_pf)
    RecyclerView recyclerView;
    List<Story> mData;
    @InjectView(R.id.iv_pf_head)
    ImageView ivPfHead;
    @InjectView(R.id.tv_pf_name)
    TextView tvPfName;
    @InjectView(R.id.child_iv)
    ImageView childIv;
    private ImageView ivStCt;

    @Override
    protected View initGui(LayoutInflater inflater) {
        return inflater.inflate(R.layout.personal_fragment_layout, null);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        String headImage = SpUtils.getInstace().getString(SpUtils.HEAD_IMAGE,"");
        if (!headImage.equals("")){
            Picasso.with(getContext()).load(new File(headImage))
                    .transform(new CropCircleTransformation())
                    .into(ivPfHead);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.my_user_default)
                    .transform(new CropCircleTransformation())
                    .into(ivPfHead);
        }
        String childImage = SpUtils.getInstace().getString(SpUtils.HEAD_IMAGE,"");
        if (!childImage.equals("")){
            Picasso.with(getContext()).load(new File(childImage))
                    .transform(new CropCircleTransformation())
                    .into(childIv);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.my_user_default)
                    .transform(new CropCircleTransformation())
                    .into(childIv);
        }




    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity)getActivity();
        //mainActivity.showIvSetting();
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MRVAdapter());
        DataManager.newInstance(getActivity()).getStories(new DataManager.StoryDataListener<List<Story>>() {
            @Override
            public void onSuccess(List<Story> data, int page) {
                if (data != null) {
                    mData.clear();
                    mData.addAll(data);
                    recyclerView.getAdapter().notifyDataSetChanged();

                } else {
                    Toast.makeText(PersonalFragment.this.getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(PersonalFragment.this.getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        }, DataManager.TYPE_FAXIAN, 0);

        //设置监听
        initListener();
    }

    private void initListener() {

    }

    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private class MRVAdapter extends RecyclerView.Adapter<MViewHolder> {
        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MViewHolder(
                    LayoutInflater.from(getContext()).inflate(R.layout.item_rv_personal,parent,false));
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {

            Story story = mData.get(position);
            Picasso.with(PersonalFragment.this.getActivity())
                    .load(story.img)
                    .into(holder.iv_icon);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_item_pf)
        ImageView iv_icon;

        public MViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
