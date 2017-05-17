package andoop.android.amstory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andoop.android.amstory.R;
import andoop.android.amstory.adapter.InteractAdapter;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/5/13/013.
 */

public class FansFragment extends Fragment {

    @InjectView(R.id.interact_recycler)
    RecyclerView mRecycler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.interact_vp_fragment, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        InteractAdapter adapter = new InteractAdapter(getActivity());
        mRecycler.setAdapter(adapter);
        adapter.setItemClickListener(new InteractAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showToast("点击了 item"+position);
            }
        });
    }
}
