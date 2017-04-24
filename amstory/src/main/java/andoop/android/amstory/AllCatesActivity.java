package andoop.android.amstory;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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


    private String names1[] = {"0-3个月","3-6个月","6-12个月","12-18个月"
            ,"18-24个月","2-3岁","3-4岁","4-5岁","5-6岁"};
    private String names2[] = {"奇幻冒险","科普故事","诗歌辞赋","神话传说","可爱卡通","儿歌歌谣"
            ,"童话经典","古诗文言","国学经典"};
    private String names3[] = {"温馨伴眠","玩乐助兴","亲子时光","欢乐出行"
            ,"吃饭喝水","运动练习","宝宝早安","洗澡戏水"};
    private String names4[]={"文明礼仪学习","爱的教育","社交能力培养","情绪管理学习","自我认知训练",
            "想象力培养","表达能力培养","生活常识普及","生活习惯养成","健康知识教育","入园前教育","文学素养熏陶","亲近自然"};
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
                ImageView fenlei = (ImageView) inflate.findViewById(R.id.fenlei);
                FlowLayout flowLayout= (FlowLayout) inflate.findViewById(R.id.fl_item_allcates);
                tvcate.setText(cates.cat);
                List<String> names= new ArrayList<>();
                if (position==0){
                    names.clear();
                    names.addAll(Arrays.asList(names1));
                    fenlei.setImageResource(R.drawable.feileinianling);
                }
                if (position==1){
                    names.clear();
                    names.addAll(Arrays.asList(names2));
                    fenlei.setImageResource(R.drawable.fenleileixing);
                }
                if (position==2){
                    names.clear();
                    names.addAll(Arrays.asList(names3));
                    fenlei.setImageResource(R.drawable.fenleisucai);
                }
                if (position==3){
                    names.clear();
                    names.addAll(Arrays.asList(names4));
                    fenlei.setImageResource(R.drawable.fenleign);
                }
                for (int i = 0; i <names.size() ; i++) {
                    TextView textView = new TextView(AllCatesActivity.this);
                    textView.setBackgroundResource(R.drawable.cate_bg_selector);
                    final String text=names.get(i);
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
                            Intent data = new Intent();
                            Bundle extra = new Bundle();
                            extra.putString("cat",text);
                            data.putExtras(extra);
                            AllCatesActivity.this.setResult(100,data);
                            AllCatesActivity.this.finish();
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
