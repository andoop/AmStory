package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.base.BaseActivity;
import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.presenter.MRecordListPresenter;
import andoop.android.amstory.presenter.view.IMyRecordListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MRecordListActivity extends BaseActivity<MRecordListPresenter> implements IMyRecordListView {
    @InjectView(R.id.rcv_mrecord)
    RecyclerView mList;

    List<StoryModule> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrecord_list);
        ButterKnife.inject(this);
        setTitle("我的录制");
        mData=new ArrayList<>();
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(new MRecordAdapter());

        mPresenter.loadData();
    }
    @Override
    protected MRecordListPresenter initPresenter() {
        return new MRecordListPresenter(this,this);
    }
    @Override
    public void showData(List<StoryModule> data) {
        stoploading();
        mData.clear();
        mData.addAll(data);
        mList.getAdapter().notifyDataSetChanged();
    }
    private class MRecordAdapter extends RecyclerView.Adapter<MRecordViewHolder>{
        @Override
        public MRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MRecordViewHolder(View.inflate(MRecordListActivity.this,R.layout.item_list_mrecord,null));
        }

        @Override
        public void onBindViewHolder(MRecordViewHolder holder, int position) {

            StoryModule storyModule = mData.get(position);
            holder.name.setText(storyModule.story_name);
            holder.time.setText(storyModule.time);
            holder.cardView.setTag(storyModule);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
    public class MRecordViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tv_list_mr_name)
        TextView name;
        @InjectView(R.id.tv_list_mr_time)
        TextView time;
        @InjectView(R.id.cv_list_mr)
        CardView cardView;
        public MRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    StoryModule storyModule= (StoryModule) tag;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("story_data",storyModule);
                    Intent intent = new Intent(MRecordListActivity.this, MPlayerActivity.class);
                    intent.putExtras(bundle);
                    MRecordListActivity.this.startActivity(intent);
                }
            });
        }
    }
}
