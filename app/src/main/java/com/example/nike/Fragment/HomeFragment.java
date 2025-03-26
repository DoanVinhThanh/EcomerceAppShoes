package com.example.nike.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.nike.R;

public class HomeFragment extends Fragment {

    private TextView tablayout1, tablayout2, tablayout3;
    private int selectedTabNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Anhxa(view);

        // Mặc định hiển thị MenFragment khi vào màn hình
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MenFragment())
                .commit();

        // Xử lý sự kiện click cho từng tab
        tablayout1.setOnClickListener(v -> selectTab(1));
        tablayout2.setOnClickListener(v -> selectTab(2));
        tablayout3.setOnClickListener(v -> selectTab(3));

        return view;
    }

    private void selectTab(int tabNumber) {
        TextView selectedTextView, nonSelectedTextView1, nonSelectedTextView2;
        Fragment selectedFragment;

        switch (tabNumber) {
            case 1:
                selectedTextView = tablayout1;
                nonSelectedTextView1 = tablayout2;
                nonSelectedTextView2 = tablayout3;
                selectedFragment = new MenFragment();
                break;
            case 2:
                selectedTextView = tablayout2;
                nonSelectedTextView1 = tablayout1;
                nonSelectedTextView2 = tablayout3;
                selectedFragment = new WomenFragment();
                break;
            case 3:
                selectedTextView = tablayout3;
                nonSelectedTextView1 = tablayout1;
                nonSelectedTextView2 = tablayout2;
                selectedFragment = new KidFragment();
                break;
            default:
                return;
        }

        // Cập nhật FragmentContainer với fragment tương ứng
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment)
                .commit();

        // Cập nhật UI của tab được chọn (không có hiệu ứng)
        selectedTextView.setBackgroundResource(R.drawable.round_back_white_100);
        selectedTextView.setTypeface(null, Typeface.BOLD);
        selectedTextView.setTextColor(Color.BLACK);

        nonSelectedTextView1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        nonSelectedTextView1.setTextColor(Color.parseColor("#C0C0C0"));
        nonSelectedTextView1.setTypeface(null, Typeface.NORMAL);

        nonSelectedTextView2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        nonSelectedTextView2.setTextColor(Color.parseColor("#C0C0C0"));
        nonSelectedTextView2.setTypeface(null, Typeface.NORMAL);

        selectedTabNumber = tabNumber;
    }

    private void Anhxa(View view) {
        tablayout1 = view.findViewById(R.id.tabItem1);
        tablayout2 = view.findViewById(R.id.tabItem2);
        tablayout3 = view.findViewById(R.id.tabItem3);
    }
}
