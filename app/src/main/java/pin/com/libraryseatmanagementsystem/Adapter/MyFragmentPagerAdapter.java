package pin.com.libraryseatmanagementsystem.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import pin.com.libraryseatmanagementsystem.Fragment.BaseFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> list;
    private FragmentManager fragmentManager;

    public MyFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        fragmentManager = fm;
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
