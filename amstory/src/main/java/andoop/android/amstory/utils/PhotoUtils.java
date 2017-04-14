package andoop.android.amstory.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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


    private ImageView imageView;

    public void showPhoto(ImageView imageView,String spname){
        this.imageView = imageView;
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
    }
}
