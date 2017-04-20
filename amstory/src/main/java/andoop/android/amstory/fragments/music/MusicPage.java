package andoop.android.amstory.fragments.music;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.R;
import andoop.android.amstory.module.LocalMusicModule;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by domob on 2017/4/16.
 * 背景和音效分类页面
 */

public class MusicPage extends Fragment {
    @InjectView(R.id.tv_mp_title)
    TextView tv_title;
    @InjectView(R.id.rv_mp)
    RecyclerView recylerview;

    private String mPath;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying=false;

    private List<LocalMusicModule> mData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_music_page, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_title.setVisibility(View.GONE);
        Bundle arguments = getArguments();
        if(arguments!=null){
            String path = arguments.getString("path");
            tv_title.setText(path);
            initData(path);
        }

        mediaPlayer=new MediaPlayer();

    }

    //初始化数据
    private void initData(String path) {
        mData=new ArrayList<>();
        //从本地文件中加载数据
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }


        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".wav")||name.endsWith(".mp3");
            }
        });

        if(files==null||files.length<=0){
            showEmpty();
            return;
        }
        List<LocalMusicModule> datas=new ArrayList<>();
        for(File f:files){
            LocalMusicModule localMusicModule = new LocalMusicModule();
            localMusicModule.name=f.getName();
            localMusicModule.path=f.getAbsolutePath();
            datas.add(localMusicModule);
        }

        mData.clear();
        mData.addAll(datas);

        recylerview.setLayoutManager(new GridLayoutManager(getActivity(),4));
        recylerview.setAdapter(new MBgChooseAdapter());
        recylerview.getAdapter().notifyDataSetChanged();
        //recylerview.scrollToPosition();

    }
    @Override
    public void onDestroy() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
    //播放音乐
    private void play(String path) throws IOException {
        Log.e("----->" + "MusicPage", "play:"+path);
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }else {
                mediaPlayer.release();
                mediaPlayer=null;
            }
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying=false;
                    recylerview.getAdapter().notifyDataSetChanged();
                    recylerview.scrollToPosition(mPos);
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    isPlaying=true;
                }
            });



    }
    //暂停试听
    private void pause(){
        Log.e("----->" + "MusicPage", "pause:");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPlaying=false;
        }
    }
    int mPos=0;
    //选择某个选项
    private void select(int pos){
        mPos=pos;
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPlaying=false;
        }
        for (int i = 0; i < mData.size(); i++) {
            if(i==pos){
                mData.get(i).selected=true;
            }else {
                mData.get(i).selected=false;
            }
        }

        recylerview.getAdapter().notifyDataSetChanged();
        recylerview.scrollToPosition(pos);
    }

    private void showEmpty() {
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("没有数据");
    }

        private class MBgChooseAdapter extends RecyclerView.Adapter<MusicPage.MBgChooseViewHolder>{
        @Override
        public MusicPage.MBgChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MusicPage.MBgChooseViewHolder(View.inflate(MusicPage.this.getActivity(),R.layout.item_list_choosebg,null));
        }

        @Override
        public void onBindViewHolder(MusicPage.MBgChooseViewHolder holder, int position) {
            LocalMusicModule localMusicModule = mData.get(position);
            holder.play.setTag(localMusicModule.path);
            holder.rootView.setTag(position);
            holder.name.setText(localMusicModule.name.replace(".wav",""));
            if(localMusicModule.selected){
                holder.rl.setBackgroundColor(Color.parseColor("#ff0000"));
                holder.play.setVisibility(View.VISIBLE);
            }else {
                holder.rl.setBackgroundColor(Color.parseColor("#00ff0000"));
                holder.play.setVisibility(View.GONE);
            }

            holder.play.setImageResource(R.drawable.ic_play);


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
    public class MBgChooseViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.cv_listchoose_mr)
        RelativeLayout rootView;
        @InjectView(R.id.iv_mc_play)
        ImageView play;
        @InjectView(R.id.rl_listchoose_bg)
        RelativeLayout rl;
        @InjectView(R.id.tv_list_choose_name)
        TextView name;
        public MBgChooseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(MusicPage.this.getActivity(), StoryMakeActivity.class);
//                    intent.putExtra("path",v.getTag().toString());
//                    MusicPage.this.getActivity().setResult(100,intent);
//                    MusicPage.this.getActivity().finish();
                    //显示播放按钮
                    //play.setVisibility(View.VISIBLE);
                    //发送广播,由父activity接收
                    Intent intent=new Intent("music_choose_data");
                    Bundle extras=new Bundle();
                    extras.putString("path",mData.get(Integer.parseInt(v.getTag().toString())).path);
                    intent.putExtras(extras);
                    LocalBroadcastManager.getInstance(MusicPage.this.getActivity()).sendBroadcast(intent);
                    select(Integer.parseInt(v.getTag().toString()));

                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPlaying){
                        //暂停播放
                        pause();
                        play.setImageResource(R.drawable.ic_play);
                    }else {
                        //播放
                        try {
                            play(v.getTag().toString());
                            play.setImageResource(R.drawable.ic_pause);
                        } catch (IOException e) {
                            e.printStackTrace();
                            isPlaying=false;
                            play.setImageResource(R.drawable.ic_play);
                        }
                    }
                }
            });
        }
    }
}
