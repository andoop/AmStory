package andoop.android.amstory.presenter;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：persenter基类
* * * * * * * * * * * * * * * * * * */

import android.content.Context;

import andoop.android.amstory.presenter.view.IBaseView;

public class BasePresenter<T extends IBaseView> {
    protected T mView;
    private Context mContext;
    public BasePresenter(Context context,T iview){
        mView=iview;
        mContext=context;
    }
    protected void showloading(){
        if(mView!=null)
            mView.showloading();
    }
    protected void showError(String error){
        if(mView!=null)
            mView.showError(error);
    }
    protected void showEmpty(){
        if(mView!=null)
            mView.showEmpty();
    }



}
