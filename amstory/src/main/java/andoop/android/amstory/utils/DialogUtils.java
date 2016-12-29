package andoop.android.amstory.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import andoop.android.amstory.R;
import andoop.android.amstory.customview.ProgressWheel;


/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/12/27
* explain：进度对话框工具
* * * * * * * * * * * * * * * * * * */

public class DialogUtils {

    public static Dialog showLoadingView(Context context){
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        View inflate = View.inflate(context, R.layout.loading_dialog_layout, null);
        ProgressWheel progressWheel= (ProgressWheel) inflate.findViewById(R.id.wheel);
        progressWheel.spin();
        progressWheel.setBarColor(R.color.colorPrimary);
        progressWheel.setBarWidth(10);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }
}
