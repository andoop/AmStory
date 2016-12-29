package andoop.android.amstory.presenter.view;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：view的基类接口
* * * * * * * * * * * * * * * * * * */
public interface IBaseView<T> {
    void showloading();
    void showError(String error);
    void showData(T data);
    void showEmpty();
}
