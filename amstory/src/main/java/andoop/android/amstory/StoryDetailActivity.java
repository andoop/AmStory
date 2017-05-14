package andoop.android.amstory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import andoop.android.amstory.db.PlayRecordDao;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StoryDetailActivity extends AppCompatActivity {
    @InjectView(R.id.tv_asd_title)
    TextView tv_title;
    @InjectView(R.id.tv_asd_author)
    TextView tv_author;
    @InjectView(R.id.iv_asd_icon)
    ImageView iv_icon;
    @InjectView(R.id.tv_sd_content)
    TextView tv_content;
    @InjectView(R.id.iv_share)
    ImageView mShare;
    @InjectView(R.id.iv_like)
    ImageView mLike;

    private Story mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        ButterKnife.inject(this);

        mStory = resoveDataFromIntent();
        if (mStory == null) {
            Toast.makeText(this, "数据解析失败", Toast.LENGTH_SHORT).show();
           // finish();
            return;
        }

        findViewById(R.id.iv_back_ct_title02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //绑定数据
        bindData();

        //保存记录
        PlayRecordDao playRecordDao = new PlayRecordDao(this);
        boolean b1 = playRecordDao.addPlayRecord(mStory);
        ToastUtils.showToast(this,"and"+b1);
    }

    //绑定事件
    @OnClick({R.id.iv_share,R.id.iv_like})
    public void onClick(View view){
        switch (view.getId()) {
            //发分享
            case  R.id.iv_share:

                ToastUtils.showToast(StoryDetailActivity.this,"分享");
                break;
            case  R.id.iv_like:

                ToastUtils.showToast(StoryDetailActivity.this,"喜欢");
                break;
        }
    }

    //解析intent数据
    private Story resoveDataFromIntent() {

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return null;
        } else {
            return (Story) extras.getSerializable("story_data");
        }
    }



    private void bindData() {
        tv_title.setText(mStory.title);
        tv_author.setText(mStory.author);
        Picasso.with(this).load(mStory.img).into(iv_icon);
        tv_content.setText("        "+mStory.content.replace("&&&",""));
    }

    public void toPlay(View view){
        toPlay(this,mStory);
    }
    public void toRecord(View view){
        toRecord(this,mStory);
    }

    private void toPlay(Context context, Story story){
        Intent intent=new Intent(context, MPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("story_data",story);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void toRecord(Context context, Story story){
        Intent intent=new Intent(context, StoryMakeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("story_data",story);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
