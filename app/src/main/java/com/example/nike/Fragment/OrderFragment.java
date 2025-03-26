package com.example.nike.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nike.Adapter.VPOrderAdapter;
import com.example.nike.R;
import com.google.android.material.tabs.TabLayout;


public class OrderFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout= view.findViewById(R.id.tabOrders);
        viewPager=view.findViewById(R.id.viewPagerOrder);

        tabLayout.setupWithViewPager(viewPager);
        VPOrderAdapter vpOrderAdapter=new VPOrderAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpOrderAdapter.addFragment(new ChoXacNhanFragment(),"CHỜ XÁC NHẬN");
        vpOrderAdapter.addFragment(new DangGiaoFragment(),"ĐANG GIAO");
        vpOrderAdapter.addFragment(new DaGiaoFragment(),"ĐÃ GIAO");
        vpOrderAdapter.addFragment(new DaHuyFragment(),"ĐÃ HỦY");
        viewPager.setAdapter(vpOrderAdapter);
        return view;
    }
}