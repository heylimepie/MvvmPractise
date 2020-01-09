package com.limepie.mvvmwanandroid.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.limepie.mvvmwanandroid.BaseFragment;
import com.limepie.mvvmwanandroid.R;
import com.limepie.mvvmwanandroid.model.Article;
import com.limepie.mvvmwanandroid.viewmodel.ArticleViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements OnRefreshListener,OnLoadMoreListener {


    private RecyclerView recyclerView;
    ArticleAdapter articleAdapter;

    ArticleViewModel articleViewModel;

    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView = view.findViewById(R.id.recycleView);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(this);

        initData();


        return view;
    }

    private void initData() {
        articleViewModel = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(ArticleViewModel.class);
        articleAdapter = new ArticleAdapter(articleViewModel.getArticles().getValue());
        recyclerView.setAdapter(articleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleViewModel.getArticles().observe(getActivity(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                refreshLayout.finishLoadMore();
                articleAdapter.addData(articles.subList(articleAdapter.getData().size(),articles.size()));
//                articleAdapter.setNewData(articles);
            }
        });
    }



    public static HomeFragment newInstance() {
        HomeFragment instance= new HomeFragment();
        return instance;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        articleViewModel.loadMore();
        Log.i("limepie","loadmore ...");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }


    class ArticleAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

        public ArticleAdapter(List<Article> data) {
            super(R.layout.item_article,data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, @org.jetbrains.annotations.Nullable Article article) {
            baseViewHolder.setText(R.id.tv_author,article.getAuthor());
            baseViewHolder.setText(R.id.tv_describe,article.getChapterName()+"/"+article.getSuperChapterName());
            baseViewHolder.setText(R.id.tv_content,article.getTitle());
        }
    }

}
