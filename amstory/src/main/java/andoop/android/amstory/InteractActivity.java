package andoop.android.amstory;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import andoop.android.amstory.adapter.InteractVpAdapter;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class InteractActivity extends AppCompatActivity {

    @InjectView(R.id.interact_back) //返回
    ImageView mBack;
    @InjectView(R.id.interact_add) //添加
    ImageView mAdd;
    @InjectView(R.id.interact_radiogroup) //title的选择器
    RadioGroup mRadioGroup;
    @InjectView(R.id.interact_viewpager)
    ViewPager mViewPager;
    private InteractVpAdapter vpAdapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);
        ButterKnife.inject(this);

        type = getIntent().getStringExtra("type");

        initView();
        initData();
        initListener();
    }

    private void initData() {

    }

    private void initView() {

        vpAdapter = new InteractVpAdapter(this,getSupportFragmentManager());
        mViewPager.setAdapter(vpAdapter);
    }

    private void initListener() {

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case  R.id.interact_attention:

                        mViewPager.setCurrentItem(0);
                        break;
                    case  R.id.interact_fans:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0) {
                    mRadioGroup.check(R.id.interact_attention);
                }else {
                    mRadioGroup.check(R.id.interact_fans);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if("1".equals(type)) {

            mRadioGroup.check(R.id.interact_attention);
        }else {
            mRadioGroup.check(R.id.interact_fans);
        }
    }

    @OnClick({R.id.interact_back,R.id.interact_add})
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.interact_back :

                finish();
                break;
            case R.id.interact_add :

                ToastUtils.showToast("添加");
                break;
        }
    }


}
