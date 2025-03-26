package com.example.nike.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPOrderAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentArrayAdapter=new ArrayList<>();
    private final ArrayList<String> fragmentTitle= new ArrayList <>();

    public VPOrderAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayAdapter.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayAdapter.size();
    }
    public void addFragment(Fragment fragment ,String title ){
        fragmentArrayAdapter.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
