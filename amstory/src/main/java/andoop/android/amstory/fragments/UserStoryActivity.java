package andoop.android.amstory.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import andoop.android.amstory.R;
import andoop.android.amstory.adapter.UserLikeStoryAdapter;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserStoryActivity extends AppCompatActivity {

    @InjectView(R.id.story_back) //返回
            ImageView mBack;
    @InjectView(R.id.story_more) //更多
            ImageView mMore;
    @InjectView(R.id.story_recycler)
    RecyclerView mRecycler;
    private UserLikeStoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story);

        ButterKnife.inject(this);

        initView();
        initListener();
        initData();
    }

    @OnClick({R.id.story_back,R.id.story_more})
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.story_back :
                finish();
                break;
            case R.id.story_more :

                ToastUtils.showToast(UserStoryActivity.this,"更多");
                break;
        }
    }

    private void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserLikeStoryAdapter(this,1); //第二个参数是判断，1是我的作品，2是喜欢的作品
        mRecycler.setAdapter(mAdapter);
    }

    private void initListener() {

        mAdapter.setItemClickListener(new UserLikeStoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast(UserStoryActivity.this,"点击了 item"+position);
            }
        });

    }

    private void initData() {

    }
}
