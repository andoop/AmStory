package andoop.android.amstory.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andoop.andooptabframe.AndoopPage;
import com.andoop.andooptabframe.AndoopTabFrame;
import com.andoop.andooptabframe.core.AndoopFrame;

import andoop.android.amstory.utils.DialogUtils;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：页面基类
* * * * * * * * * * * * * * * * * * */

public abstract class BasePager extends AndoopPage {
    private Dialog loadingView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initGui(inflater);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    //初始化页面
    protected abstract View initGui(LayoutInflater inflater);
    //初始化view
    protected abstract void initView(View view);
    //初始化数据
    protected abstract void initData();


    public void showError(String error) {
        if(loadingView!=null)
            loadingView.dismiss();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    public void showloading() {
        if(loadingView!=null){
            loadingView.show();
        }else {
            loadingView = DialogUtils.showLoadingView(getActivity());
        }
    }

    public void showEmpty() {
        if(loadingView!=null)
            loadingView.dismiss();
        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
    }

    public void stoploading() {
        if(loadingView!=null)
            loadingView.dismiss();
    }
}
