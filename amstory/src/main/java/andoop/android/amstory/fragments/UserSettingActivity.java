package andoop.android.amstory.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import andoop.android.amstory.R;
import andoop.android.amstory.utils.PhotoUtils;
import andoop.android.amstory.utils.SpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserSettingActivity extends AppCompatActivity {

    @InjectView(R.id.iv_back_ct_title02)
    ImageView ivBackCtTitle02;
    @InjectView(R.id.iv_play_ct_title02)
    ImageView ivPlayCtTitle02;
    @InjectView(R.id.iv_pf_head)
    ImageView ivPfHead;
    @InjectView(R.id.activity_person_setting)
    LinearLayout activityPersonSetting;
    @InjectView(R.id.child_iv)
    ImageView childIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);
        ButterKnife.inject(this);


        initData();
    }


    private void initData() {
        PhotoUtils.getInstance().init(this).showPhoto(ivPfHead, this, SpUtils.HEAD_IMAGE);
        ivBackCtTitle02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PhotoUtils.getInstance().init(this).showPhoto(childIv,this,SpUtils.CHILD_IMAGE);
    }


}
