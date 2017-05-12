package andoop.android.amstory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andoop.andooptabframe.AndoopPage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.StoryDetailActivity;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.db.PlayRecordDao;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.utils.SpUtils;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/12
* explain：个人中心页面
* * * * * * * * * * * * * * * * * * */

public class PersonalFragment extends BasePager {
    @InjectView(R.id.rv_pf)
    RecyclerView recyclerView;
    @InjectView(R.id.tv_no_data)
    TextView tv_no_data;
    List<Story> mData;
    @InjectView(R.id.iv_pf_head)
    ImageView ivPfHead;
    @InjectView(R.id.tv_pf_name)
    TextView tvPfName;
    @InjectView(R.id.person_works) //作品
    LinearLayout mPersonWorks;
    @InjectView(R.id.person_link) //喜欢
    LinearLayout mPersonLinks;
    @InjectView(R.id.person_follow) //关注
    LinearLayout mPersonFollow;
    @InjectView(R.id.person_fans) //粉丝
    LinearLayout mPersonFans;
//    @InjectView(R.id.child_iv) //宝宝头像
//    ImageView childIv;
    @InjectView(R.id.tv_del)
    TextView mTvDel;
    private ImageView ivStCt;
    private PlayRecordDao playRecordDao;

    @OnClick({R.id.person_works,R.id.person_link,R.id.person_follow,R.id.person_fans})
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.person_works :

                ToastUtils.showToast(getActivity(),"作品");
                break;
            case R.id.person_link :
                ToastUtils.showToast(getActivity(),"喜欢");
                break;
            case R.id.person_follow :
                ToastUtils.showToast(getActivity(),"关注");
                break;
            case R.id.person_fans :
                ToastUtils.showToast(getActivity(),"粉丝");
                break;
        }
    }

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
        //先取消了宝宝的头像显示
//        String childImage = SpUtils.getInstace().getString(SpUtils.CHILD_IMAGE,"");
//        if (!childImage.equals("")){
//            Picasso.with(getContext()).load(new File(childImage))
//                    .transform(new CropCircleTransformation())
//                    .into(childIv);
//        }else{
//            Picasso.with(getContext())
//                    .load(R.drawable.my_user_default)
//                    .transform(new CropCircleTransformation())
//                    .into(childIv);
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //MainActivity mainActivity = (MainActivity)getActivity();
        //mainActivity.showIvSetting();
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        linearLayoutManager.setReverseLayout(true);//列表翻转
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MRVAdapter());

        //获取历史记录数据
        playRecordDao = new PlayRecordDao(getActivity());

        getPlayRecord();

//        DataManager.newInstance(getActivity()).getStories(new DataManager.StoryDataListener<List<Story>>() {
//            @Override
//            public void onSuccess(List<Story> data, int page) {
//                if (data != null) {
//                    mData.clear();
//                    mData.addAll(data);
//                    recyclerView.getAdapter().notifyDataSetChanged();
//
//                } else {
//                    Toast.makeText(PersonalFragment.this.getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                Toast.makeText(PersonalFragment.this.getActivity(), error, Toast.LENGTH_SHORT).show();
//            }
//        }, DataManager.TYPE_FAXIAN, 0);
    }


    //获取听过的记录
    private void getPlayRecord() {
        ArrayList<Story> playRecord = playRecordDao.getPlayRecord();
//        ToastUtils.showToast(getActivity(),playRecord.size()+"");
        if(playRecord.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
            mData.clear();
            mData.addAll(playRecord);
            recyclerView.getAdapter().notifyDataSetChanged();

        }else {
            recyclerView.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_del})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.tv_del :

                if(playRecordDao != null && null != getActivity() ) {
                    playRecordDao.delAlerts();
                    getPlayRecord();
                }
                break;
        }
    }

    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {

        if(pos == 3 && playRecordDao != null) {

            getPlayRecord();
        }
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

            final Story story = mData.get(position);
            Picasso.with(PersonalFragment.this.getActivity())
                    .load(story.img)
                    .into(holder.iv_icon);
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), StoryDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("story_data", story);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
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
