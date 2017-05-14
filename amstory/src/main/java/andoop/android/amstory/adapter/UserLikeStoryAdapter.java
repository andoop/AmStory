package andoop.android.amstory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import andoop.android.amstory.R;

/**
 * Created by Administrator on 2017/5/14/014.
 */

public class UserLikeStoryAdapter extends RecyclerView.Adapter <UserLikeStoryAdapter.MyViewHolder> {
    private Context context;
    private int type; //1是我的作品，2是喜欢的作品

    public UserLikeStoryAdapter(Context context,int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_like, parent, false));
    }

    @Override
    public void onBindViewHolder(final UserLikeStoryAdapter.MyViewHolder holder, int position) {
        
        if(type == 1) {
            holder.mUserName.setVisibility(View.INVISIBLE);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onItemClick(v,holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mUserName; //录制者
        public TextView mUploadTime; //上传时间
        public ImageView mIcon; //作品图片
        public TextView mAuthorName; //作品名
        public TextView mAuthorTime; //时长
        public TextView mAuthorHearNumber; //收听数
        public TextView mAuthorLikeNumber; //喜欢数
        public TextView mAuthorTag1; //tag1
        public TextView mAuthorTag2; //tag2
        public View rootView;

        public MyViewHolder(View view) {
            super(view);
            rootView = view;
            mUserName = (TextView) view.findViewById(R.id.user_like_name);
            mUploadTime = (TextView) view.findViewById(R.id.user_like_time);
            mIcon = (ImageView) view.findViewById(R.id.user_like_icon);
            mAuthorName = (TextView) view.findViewById(R.id.user_like_author);
            mAuthorTime = (TextView) view.findViewById(R.id.user_author_time);
            mAuthorHearNumber = (TextView) view.findViewById(R.id.user_like_listenin);
            mAuthorLikeNumber = (TextView) view.findViewById(R.id.user_like_number);
            mAuthorTag1 = (TextView) view.findViewById(R.id.user_like_tag1);
            mAuthorTag2 = (TextView) view.findViewById(R.id.user_like_tag2);
        }
    }

    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener = listener;
    }

    public interface ItemClickListener{

        void onItemClick(View view,int position);
    }
}
