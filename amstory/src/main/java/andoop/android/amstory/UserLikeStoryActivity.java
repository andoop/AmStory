package andoop.android.amstory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import andoop.android.amstory.adapter.UserLikeStoryAdapter;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserLikeStoryActivity extends AppCompatActivity {

    @InjectView(R.id.like_story_back) //返回
    ImageView mBack;
    @InjectView(R.id.like_story_back_more) //更多
    ImageView mMore;
    @InjectView(R.id.like_story_recycler)
    RecyclerView mRecycler;
    private UserLikeStoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_like_story);
        ButterKnife.inject(this);

        initView();
        initListener();
        initData();
    }

    @OnClick({R.id.like_story_back,R.id.like_story_back_more})
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.like_story_back :
                finish();
                break;
            case R.id.like_story_back_more :

                ToastUtils.showToast("更多");
                break;
        }
    }

    private void initView() {

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserLikeStoryAdapter(this,2);
        mRecycler.setAdapter(mAdapter);
    }

    private void initListener() {

        mAdapter.setItemClickListener(new UserLikeStoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast("点击了 item"+position);
            }
        });

    }

    private void initData() {

    }
}
