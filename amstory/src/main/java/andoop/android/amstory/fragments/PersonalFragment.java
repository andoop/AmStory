package andoop.android.amstory.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.andoop.andooptabframe.AndoopPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.base.BasePager;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Story;
import butterknife.ButterKnife;
import butterknife.InjectView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/12
* explain：个人中心页面
* * * * * * * * * * * * * * * * * * */

public class PersonalFragment extends BasePager {
    @InjectView(R.id.iv_st_ct)
    ImageView iv_st;
    @InjectView(R.id.rv_pf)
    RecyclerView recyclerView;
    List<Story> mData;
    @Override
    protected View initGui(LayoutInflater inflater) {
        return inflater.inflate(R.layout.personal_fragment_layout,null);
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this,view);
        iv_st.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mData=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new PersonalFragment.MRVAdapter());
        DataManager.newInstance(getActivity()).getStories(new DataManager.StoryDataListener<List<Story>>() {
            @Override
            public void onSuccess(List<Story> data, int page) {
                if(data!=null){
                    mData.clear();
                    mData.addAll(data);
                    recyclerView.getAdapter().notifyDataSetChanged();

                }else {
                    Toast.makeText(PersonalFragment.this.getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFail(String error) {
                Toast.makeText(PersonalFragment.this.getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        },DataManager.TYPE_FAXIAN,0);
    }

    @Override
    public void onSelect(AndoopPage andoopPage, int pos) {

    }


    private class MRVAdapter extends RecyclerView.Adapter<PersonalFragment.MViewHolder>{
        @Override
        public PersonalFragment.MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PersonalFragment.MViewHolder(View.inflate(PersonalFragment.this.getActivity(),R.layout.item_rv_personal,null));
        }

        @Override
        public void onBindViewHolder(PersonalFragment.MViewHolder holder, int position) {

            Story story = mData.get(position);
            Picasso.with(PersonalFragment.this.getActivity()).load(story.img).into(holder.iv_icon);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class MViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.iv_item_pf)
        ImageView iv_icon;
        public MViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }

}
