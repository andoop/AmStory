package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.base.BaseActivity;
import andoop.android.amstory.module.LocalMusicModule;
import andoop.android.amstory.presenter.BasePresenter;
import andoop.android.amstory.presenter.BgListPresenter;
import andoop.android.amstory.presenter.view.IBgListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BgListActivity extends BaseActivity<BgListPresenter> implements IBgListView{
    @InjectView(R.id.rv_choose_bg)
    RecyclerView recyclerView;
    private List<LocalMusicModule> mData;
    String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_list);
        ButterKnife.inject(this);

        int type = getIntent().getIntExtra("type", 1);
        if(type==1){
            initBgMusic();
        }else if(type==2){
            initMusic();
        }
        mData=new ArrayList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MBgChooseAdapter());
        mPresenter.loadData(mPath);
    }

    private void initMusic() {
        setTitle("选择音效");
        mPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/amstory_music";

    }

    private void initBgMusic() {
        setTitle("选择背景音乐");
        mPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/amstory_bgmusic";
    }

    @Override
    protected BgListPresenter initPresenter() {
        return new BgListPresenter(this,this);
    }

    @Override
    public void showData(List<LocalMusicModule> data) {
        stoploading();
        mData.clear();
        mData.addAll(data);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private class MBgChooseAdapter extends RecyclerView.Adapter<BgListActivity.MBgChooseViewHolder>{
        @Override
        public BgListActivity.MBgChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BgListActivity.MBgChooseViewHolder(View.inflate(BgListActivity.this,R.layout.item_list_choosebg,null));
        }

        @Override
        public void onBindViewHolder(BgListActivity.MBgChooseViewHolder holder, int position) {
            LocalMusicModule localMusicModule = mData.get(position);
            holder.name.setText(localMusicModule.name);
            holder.cardView.setTag(localMusicModule.path);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
    public class MBgChooseViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tv_listchoose_mr_name)
        TextView name;
        @InjectView(R.id.tv_listchoose_mr_size)
        TextView time;
        @InjectView(R.id.cv_listchoose_mr)
        CardView cardView;
        public MBgChooseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BgListActivity.this, StoryMakeActivity.class);
                    intent.putExtra("path",v.getTag().toString());
                    BgListActivity.this.setResult(100,intent);
                    BgListActivity.this.finish();
                }
            });
        }
    }
}
