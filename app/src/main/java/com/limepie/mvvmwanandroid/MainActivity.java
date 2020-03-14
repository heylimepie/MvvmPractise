package com.limepie.mvvmwanandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.limepie.mvvmandroid.article.fragment.ArticleFragment;
import com.limepie.mvvmandroid.base.BaseFragment;
import com.limepie.mvvmandroid.project.fragment.ProjectFragment;
import com.limepie.mvvmandroid.wechat.fragment.WeChatFragment;
import com.limepie.mvvmwanandroid.model.BannerVo;
import com.limepie.mvvmwanandroid.util.GlideImageLoader;
import com.limepie.mvvmwanandroid.viewmodel.BannerViewModel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

@Route(path = "/app/mainActivity")
public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CustomAdapter adapter;
    private TabLayout tabLayout;
    private Banner banner;

    private BannerViewModel bannerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ARouter.getInstance().inject(this);
        viewPager = findViewById(R.id.vp);
        tabLayout = findViewById(R.id.tb);
        initTabLayout();
        bannerViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(BannerViewModel.class);
        banner = findViewById(R.id.banner);
        initBanner();
        setData();
    }

    void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.home).setIcon(R.drawable.ic_home_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.wechat).setIcon(R.drawable.ic_wechat));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.project).setIcon(R.drawable.ic_project));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.navigation).setIcon(R.drawable.ic_navigation));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.knowledge_tree).setIcon(R.drawable.ic_dashboard_black_24dp));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private void initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        bannerViewModel.getBanners().observe(this, new Observer<List<BannerVo>>() {
            @Override
            public void onChanged(List<BannerVo> bannerVos) {
                ArrayList<String> title = new ArrayList<>(bannerVos.size());
                ArrayList<String> imgPath = new ArrayList<>(bannerVos.size());
                for (BannerVo banner : bannerVos) {
                    title.add(banner.getTitle());
                    imgPath.add(banner.getImagePath());
                }
                banner.setImages(imgPath);
                banner.setBannerTitles(title);
                banner.start();
            }
        });
    }

    private void setData() {
        List<BaseFragment> data = new ArrayList<>();
        ArticleFragment articleFragment = (ArticleFragment) ARouter.getInstance().build("/article/articlefragment").navigation();
        WeChatFragment weChatFragment = (WeChatFragment) ARouter.getInstance().build("/wechat/wechatfragment").navigation();
        ProjectFragment projectFragment =(ProjectFragment) ARouter.getInstance().build("/project/projectfragment").navigation();
        data.add(articleFragment);
        data.add(weChatFragment);
        data.add(projectFragment);
        adapter = new CustomAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, data);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
