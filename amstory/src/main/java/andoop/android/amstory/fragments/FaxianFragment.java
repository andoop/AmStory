package andoop.android.amstory.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andoop.andooptabframe.AndoopPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.StoryDetailActivity;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Story;
import butterknife.ButterKnife;
import butterknife.InjectView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/12
* explain：发现页面
* * * * * * * * * * * * * * * * * * */

public class FaxianFragment extends BasePager {
    @InjectView(R.id.rv_faxian_fragment)
    RecyclerView recyclerView;
    private List<Story> mData;
    @Override
    protected View initGui(LayoutInflater inflater) {
        return inflater.inflate(R.layout.faxian_fragment_layout,null);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this,view);
    }



    @Override
    protected void initData() {
        mData=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MRVAdapter());

        DataManager.newInstance(getActivity()).getStories(new DataManager.StoryDataListener<List<Story>>() {
            @Override
            public void onSuccess(List<Story> data, int page) {
                if(data!=null){
                    mData.clear();
                    mData.addAll(data);
                    recyclerView.getAdapter().notifyDataSetChanged();

                }else {
                    Toast.makeText(FaxianFragment.this.getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFail(String error) {
                Toast.makeText(FaxianFragment.this.getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        },DataManager.TYPE_FAXIAN,0);

    }

    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {
        Log.e("----->" + "FaxianFragment", "onSelect:" + pos);
    }

    private class MRVAdapter extends RecyclerView.Adapter<MViewHolder>{
        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MViewHolder(View.inflate(FaxianFragment.this.getActivity(),R.layout.item_rv_faxian,null));
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {

            final Story story = mData.get(position);
            Picasso.with(FaxianFragment.this.getActivity()).load(story.img).into(holder.iv_icon);
            holder.mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FaxianFragment.this.openDetail(FaxianFragment.this.getActivity(),story);
                }
            });

            holder.mTitle.setText(story.title);
            if(TextUtils.isEmpty(story.author)) {

                story.author = "未知";
            }
            holder.mAuthor.setText(story.author);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class MViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.iv_item_rv_faxian_icon)
        ImageView iv_icon;
        @InjectView(R.id.tv_title)
        TextView mTitle;
        @InjectView(R.id.tv_author)
        TextView mAuthor;
        View mview;
        public MViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            mview=itemView;
        }
    }


    private void openDetail(Context context, Story story){
        Intent intent=new Intent(context, StoryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("story_data",story);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
