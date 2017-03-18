package andoop.android.amstory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.customview.FlowLayout;
import andoop.android.amstory.data.DataManager;
import andoop.android.amstory.module.Cates;
import andoop.android.amstory.utils.DensityUtil;
import butterknife.ButterKnife;
import butterknife.InjectView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/14
* explain：全部分类页面
* * * * * * * * * * * * * * * * * * */

public class AllCatesActivity extends AppCompatActivity {
    @InjectView(R.id.rv_activity_allcates)
    ListView recyclerView;
    List<Cates> mData;
    @InjectView(R.id.tv_ct_title03)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cates);
        ButterKnife.inject(this);
        tv_title.setText("全部分类");
        findViewById(R.id.iv_back_ct_title03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        mData=new ArrayList<>();
        recyclerView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Object getItem(int position) {
                return mData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View inflate = View.inflate(AllCatesActivity.this, R.layout.item_allcates, null);
                final Cates cates = mData.get(position);
                TextView tvcate= (TextView) inflate.findViewById(R.id.tv_item_allcates);
                FlowLayout flowLayout= (FlowLayout) inflate.findViewById(R.id.fl_item_allcates);
                tvcate.setText(cates.cat);
                for (int i = 0; i <cates.list.length ; i++) {
                    TextView textView = new TextView(AllCatesActivity.this);
                    textView.setBackgroundResource(R.drawable.cate_bg_selector);
                    final String text=cates.list[i];
                    textView.setText(text);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(DensityUtil.dip2px(AllCatesActivity.this,7)
                            ,DensityUtil.dip2px(AllCatesActivity.this,4)
                            ,DensityUtil.dip2px(AllCatesActivity.this,7)
                            ,DensityUtil.dip2px(AllCatesActivity.this,4));

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    flowLayout.addView(textView,layoutParams);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(AllCatesActivity.this, "选择分类："+text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return inflate;
            }
        });


        //获取分类数据
        DataManager.newInstance(this).getCates(new DataManager.StoryDataListener<List<Cates>>() {
            @Override
            public void onSuccess(List<Cates> data, int page) {

                if(data!=null){
                    mData.clear();
                    mData.addAll(data);
                  BaseAdapter adapter= (BaseAdapter) recyclerView.getAdapter();
                    adapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(AllCatesActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(AllCatesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
