package andoop.android.amstory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import andoop.android.amstory.R;
import andoop.android.amstory.utils.ToastUtils;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/5/13/013.
 */

public class InteractAdapter extends RecyclerView.Adapter <InteractAdapter.MyViewHolder>{

    private Context context;

    public InteractAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_interact, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final InteractAdapter.MyViewHolder holder, int position) {

        holder.mInteract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context,"关注");
            }
        });

        Picasso.with(context)
                .load(R.drawable.header)
                .transform(new CropCircleTransformation())
                .into(holder.mIcon);


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

        ImageView mIcon;
        TextView mUserName;
        TextView mGuanzhu;
        TextView mFans;
        TextView mInteract;
        View rootView;

        public MyViewHolder(View view) {
            super(view);
            rootView = view;
            mIcon = (ImageView) view.findViewById(R.id.itme_interact_icon);
            mUserName = (TextView) view.findViewById(R.id.itme_interact_user_name);
            mGuanzhu = (TextView) view.findViewById(R.id.itme_interact_user_guanzhu);
            mFans = (TextView) view.findViewById(R.id.itme_interact_user_fans);
            mInteract = (TextView) view.findViewById(R.id.itme_interact_interact);
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
