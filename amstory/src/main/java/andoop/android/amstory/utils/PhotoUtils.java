package andoop.android.amstory.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.fragments.UserSettingActivity;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/4/2.
 */

public class PhotoUtils {

    private static PhotoUtils photoUtils = new PhotoUtils();

    private Context context;
    public static PhotoUtils getInstance(){

        return photoUtils;
    }

    public PhotoUtils init(Context context){
        this.context = context;
        return photoUtils;
    }

    private String names[] = {"相册","相机"};
    private ImageView imageView;
    private String spname;

    public void showPhoto(ImageView imageView, final Activity activity,String spname){

        this.imageView = imageView;
        this.spname = spname;
        //设置默认头像
        //设置默认头像
        String headImage = SpUtils.getInstace().getString(spname, "");
        if (!headImage.equals("")){
            Picasso.with(context)
                    .load(new File(headImage))
                    .transform(new CropCircleTransformation())
                    .into(imageView);
        }else{
            Picasso.with(context)
                    .load(R.drawable.my_user_default)
                    .transform(new CropCircleTransformation())
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("请选择方式")
                        .setItems(names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    getXiangce();
                                }else{
                                    getXiangJi();
                                }
                            }
                        })
                        .show();

            }
        });
    }

    //相机
    private void getXiangJi() {
        GalleryFinal.openCamera(1, mOnHanlderResultCallback);
    }
    //相册
    private void getXiangce() {
        GalleryFinal
                .openGallerySingle(0, mOnHanlderResultCallback);
    }

    GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (reqeustCode == 0 || reqeustCode == 1){
                Picasso.with(context)
                        .load(new File(resultList.get(0).getPhotoPath()))
                        .transform(new CropCircleTransformation())
                        .into(imageView);
                SpUtils.getInstace().save(spname,resultList.get(0).getPhotoPath());
            }
        }
        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Picasso.with(context)
                    .load(R.drawable.my_user_default)
                    .transform(new CropCircleTransformation())
                    .into(imageView);
        }
    };
}
