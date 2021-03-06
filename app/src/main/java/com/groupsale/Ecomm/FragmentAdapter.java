package com.groupsale.Ecomm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.groupsale.Ecomm.fragment.ExploreFragment;
import com.groupsale.Ecomm.fragment.JoinFragment;
import com.groupsale.Ecomm.fragment.StoreFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private final int Tabcount;

    public FragmentAdapter(FragmentManager fragmentManager, int CountTabs) {
        super(fragmentManager);
        this.Tabcount = CountTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new JoinFragment();
            case 1:
                return new ExploreFragment();

            case 2:
                return new StoreFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Tabcount;
    }
}
