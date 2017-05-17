package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import andoop.android.amstory.adapter.UserLikeStoryAdapter;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class OthersActivity extends AppCompatActivity {

    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.others_back) //返回
    ImageView mBack;
    @InjectView(R.id.others_more) //更多
    ImageView mMore;
    @InjectView(R.id.others_interact) //关注
    TextView mInteract;
    @InjectView(R.id.others_head_icon) //头像
    ImageView mHeadIcon;
    @InjectView(R.id.person_works) //作品
    LinearLayout mPersonWorks;
    @InjectView(R.id.person_link) //喜欢
    LinearLayout mPersonLinks;
    @InjectView(R.id.person_follow) //关注
    LinearLayout mPersonFollow;
    @InjectView(R.id.person_fans) //粉丝
    LinearLayout mPersonFans;
    private UserLikeStoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        ButterKnife.inject(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //第二个参数是判断，1隐藏录制者，2显示录制者
        mAdapter = new UserLikeStoryAdapter(this, 1);
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

        //设置头像
        Picasso.with(this)
                .load(R.drawable.header)
                .transform(new CropCircleTransformation())
                .into(mHeadIcon);
    }

    @OnClick({R.id.others_back,R.id.others_more,R.id.others_interact,R.id.person_works,R.id.person_link,R.id.person_follow,R.id.person_fans})
    public void onClick(View view){

        Intent intent;

        switch (view.getId()) {
            case R.id.others_back :
                finish();
                break;
            case R.id.others_more :

                ToastUtils.showToast("更多");
                break;
            case R.id.others_interact :

                ToastUtils.showToast("关注");
                break;
            case R.id.person_works :
                intent = new Intent(OthersActivity.this,UserStoryActivity.class);
                startActivity(intent);
                ToastUtils.showToast("作品");
                break;
            case R.id.person_link :
                intent = new Intent(OthersActivity.this,UserLikeStoryActivity.class);
                startActivity(intent);
                ToastUtils.showToast("喜欢");
                break;
            case R.id.person_follow :
                intent = new Intent(OthersActivity.this,InteractActivity.class);
                intent.putExtra("type","1"); //type 1.关注；2.粉丝
                startActivity(intent);
                ToastUtils.showToast("关注");
                break;
            case R.id.person_fans :
                intent = new Intent(OthersActivity.this,InteractActivity.class);
                intent.putExtra("type","2"); //type 1.关注；2.粉丝
                startActivity(intent);
                ToastUtils.showToast("粉丝");
                break;
        }
    }
}
