package andoop.android.amstory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UploadOKActivity extends AppCompatActivity {

    @InjectView(R.id.login_wx) //微信好友
    LinearLayout mWxLogin;
    @InjectView(R.id.login_yk) //微信朋友圈
    LinearLayout mYkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ok);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.login_wx,R.id.login_yk})
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.login_wx :

                ToastUtils.showToast("微信好友");
                finish();

                break;

            case R.id.login_yk :

                ToastUtils.showToast("微信朋友圈");
                finish();

                break;
        }
    }
}
