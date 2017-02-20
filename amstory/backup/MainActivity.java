package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.base.BaseActivity;
import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.presenter.IndeViewPresenter;
import andoop.android.amstory.presenter.view.IIndexView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity<IndeViewPresenter> implements IIndexView{
    @InjectView(R.id.rcv_list_main)
    RecyclerView recyclerView_list;
    private List<StoryModule> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mData=new ArrayList<>();
        //加载数据
        mPresenter.loadStoryData();
        recyclerView_list.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView_list.setAdapter(new MainRcvAdapter());

    }

    @Override
    protected IndeViewPresenter initPresenter() {
        return new IndeViewPresenter(this,this);
    }


    @Override
    public void showData(List<StoryModule> data) {
        //停止加载
       stoploading();
        mData.clear();
        mData.addAll(data);
        recyclerView_list.getAdapter().notifyDataSetChanged();

    }

    private class MainRcvAdapter extends RecyclerView.Adapter<MViewHodler>{
        @Override
        public MViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MViewHodler(View.inflate(MainActivity.this,R.layout.item_list_story,null));
        }

        @Override
        public void onBindViewHolder(MViewHodler holder, int position) {

            StoryModule storyModule = mData.get(position);
            holder.textView.setText(storyModule.story_name);
            Picasso.with(MainActivity.this).load(storyModule.story_pic).into(holder.icon);
            holder.icon.setTag(storyModule);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class MViewHodler extends RecyclerView.ViewHolder{
        @InjectView(R.id.list_icon)
        ImageView icon;
        @InjectView(R.id.list_name)
        TextView textView;
        public MViewHodler(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if(tag instanceof StoryModule){
                        StoryModule storyModule= (StoryModule) tag;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("story_data",storyModule);
                        Intent intent = new Intent(MainActivity.this, StoryMakeActivity.class);
                        intent.putExtras(bundle);
                        MainActivity.this.startActivity(intent);
                    }
                }
            });
        }
    }

}
