package andoop.android.amstory.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import andoop.android.amstory.presenter.BasePresenter;
import andoop.android.amstory.utils.DialogUtils;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：activity基类
* * * * * * * * * * * * * * * * * * */

public abstract class BaseActivity<P extends BasePresenter> extends Activity {

    private Dialog loadingView;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=initPresenter();
    }

    protected abstract P initPresenter();

    public void showError(String error) {
        if(loadingView!=null)
            loadingView.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void showloading() {
        if(loadingView!=null){
            loadingView.show();
        }else {
            loadingView = DialogUtils.showLoadingView(this);
        }
    }

    public void showEmpty() {
        if(loadingView!=null)
            loadingView.dismiss();
        Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
    }

    public void stoploading() {
        if(loadingView!=null)
            loadingView.dismiss();
    }
}
