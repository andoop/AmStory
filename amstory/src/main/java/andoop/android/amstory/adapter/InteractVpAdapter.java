package andoop.android.amstory.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import andoop.android.amstory.fragments.AttentionFragment;
import andoop.android.amstory.fragments.FansFragment;

/**
 * Created by Administrator on 2017/5/13/013.
 */

public class InteractVpAdapter extends FragmentPagerAdapter{
    private Context context;

    public InteractVpAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {

        Fragment ft = null;
        switch (position) {
            case 0:
                ft = new AttentionFragment();
                break;

            case 1:
                ft = new FansFragment();

                break;
        }
        return ft;
    }
}
