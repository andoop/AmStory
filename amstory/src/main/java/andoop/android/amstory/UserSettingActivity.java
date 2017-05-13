package andoop.android.amstory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import andoop.android.amstory.utils.PhotoUtils;
import andoop.android.amstory.utils.SpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserSettingActivity extends AppCompatActivity {

    @InjectView(R.id.iv_back_ct_title02)
    ImageView ivBackCtTitle02;
    @InjectView(R.id.iv_play_ct_title02)
    ImageView ivPlayCtTitle02;
    @InjectView(R.id.iv_pf_head)
    ImageView ivPfHead;
    @InjectView(R.id.activity_person_setting)
    LinearLayout activityPersonSetting;
//    @InjectView(R.id.child_iv)
//    ImageView childIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);
        ButterKnife.inject(this);


        initData();
    }


    private void initData() {
        PhotoUtils.getInstance().init(this)
                .showPhoto(ivPfHead, SpUtils.HEAD_IMAGE);
        ivBackCtTitle02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        PhotoUtils.getInstance().init(this)
//                .showPhoto(childIv, SpUtils.CHILD_IMAGE);
//        initListener();
    }

    private void initListener() {
        ivPfHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pname = SpUtils.HEAD_IMAGE;
                showDialog2(ivPfHead);
            }
        });

//        childIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pname = SpUtils.CHILD_IMAGE;
//                showDialog2(childIv);
//            }
//        });
        ivPlayCtTitle02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String names[] = {"相册","相机"};
    private void showDialog2(final ImageView imageView) {
        new AlertDialog.Builder(UserSettingActivity.this)
                .setTitle("请选择方式")
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                           getXiangce(imageView);
                        } else {
                            getXiangJi(imageView);
                        }
                    }
                })
                .show();
    }

    private String pname;
    //相机
    public void getXiangJi(final ImageView imageView) {
        GalleryFinal.openCamera(1, new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (reqeustCode == 0 || reqeustCode == 1){
                                Picasso.with(UserSettingActivity.this)
                                        .load(new File(resultList.get(0).getPhotoPath()))
                                        .transform(new CropCircleTransformation())
                                        .into(imageView);
                                SpUtils.getInstace().save(pname,resultList.get(0).getPhotoPath());
                            }
                        }
                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {
                            Picasso.with(UserSettingActivity.this)
                                    .load(R.drawable.my_user_default)
                                    .transform(new CropCircleTransformation())
                                    .into(imageView);
                        }
                    });
    }
    //相册
    public void getXiangce(final ImageView imageView) {
        GalleryFinal
                .openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (reqeustCode == 0 || reqeustCode == 1){
                            Picasso.with(UserSettingActivity.this)
                                    .load(new File(resultList.get(0).getPhotoPath()))
                                    .transform(new CropCircleTransformation())
                                    .into(imageView);
                            SpUtils.getInstace().save(pname,resultList.get(0).getPhotoPath());
                        }
                    }
                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        Picasso.with(UserSettingActivity.this)
                                .load(R.drawable.my_user_default)
                                .transform(new CropCircleTransformation())
                                .into(imageView);
                    }
                });
    }


}
