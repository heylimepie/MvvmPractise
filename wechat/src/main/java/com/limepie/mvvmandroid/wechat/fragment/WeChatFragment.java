package com.limepie.mvvmandroid.wechat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.limepie.mvvmandroid.base.BaseFragment;
import com.limepie.mvvmandroid.wechat.R;
import com.limepie.mvvmandroid.wechat.model.WeChat;
import com.limepie.mvvmandroid.wechat.viewmodel.WeChatViewModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

@Route(path = "/wechat/wechatfragment")
public class WeChatFragment extends BaseFragment {

    WeChatViewModel weChatViewModel;
    List<BaseFragment> data = new ArrayList<>();

    private CustomAdapter adapter;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, container, false);

        tabLayout = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.vp);

        initData();

        return view;
    }

    private void initData() {

        weChatViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(WeChatViewModel.class);

        weChatViewModel.getWeChats().observe(getActivity(), new Observer<List<WeChat>>() {
            @Override
            public void onChanged(List<WeChat> weChats) {
                initTab(weChats);
                initFragment(weChats);
            }
        });
    }

    private void initFragment(List<WeChat> weChats) {
        for (int i = 0; i < weChats.size(); i++) {
            SubArticleFragment subArticleFragment = SubArticleFragment.newInstance(weChats.get(i).getId());
            data.add(subArticleFragment);
        }

        adapter = new CustomAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, data);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTab(List<WeChat> weChats) {
        for (int i = 0; i < weChats.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(weChats.get(i).getName()));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    class CustomAdapter extends FragmentPagerAdapter {

        List<BaseFragment> data;

        public CustomAdapter(FragmentManager fragmentManager, int behavior, List<BaseFragment> data) {
            super(fragmentManager, behavior);
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }
}
