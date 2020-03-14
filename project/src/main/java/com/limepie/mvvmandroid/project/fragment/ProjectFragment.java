package com.limepie.mvvmandroid.project.fragment;

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
import com.limepie.mvvmandroid.project.R;
import com.limepie.mvvmandroid.project.model.Project;
import com.limepie.mvvmandroid.project.viewmodel.ProjectViewModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

@Route(path = "/project/projectfragment")
public class ProjectFragment extends BaseFragment {

    ProjectViewModel projectViewModel;
    List<BaseFragment> data = new ArrayList<>();

    private CustomAdapter adapter;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        tabLayout = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.vp);

        initData();
        return view;
    }

    private void initData() {

        projectViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ProjectViewModel.class);

        projectViewModel.getProjects().observe(getActivity(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                initTab(projects);
                initFragment(projects);
            }
        });
    }

    private void initFragment(List<Project> projects) {
        for (int i = 0; i < projects.size(); i++) {
            SubProjectFragment subArticleFragment = SubProjectFragment.newInstance(projects.get(i).getId());
            data.add(subArticleFragment);
        }

        adapter = new CustomAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, data);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTab(List<Project> weChats) {
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
