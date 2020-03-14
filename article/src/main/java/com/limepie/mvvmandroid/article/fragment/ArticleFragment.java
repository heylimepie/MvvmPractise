package com.limepie.mvvmandroid.article.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.limepie.mvvmandroid.article.R;
import com.limepie.mvvmandroid.article.model.Article;
import com.limepie.mvvmandroid.article.viewmodel.ArticleViewModel;
import com.limepie.mvvmandroid.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

@Route(path = "/article/articlefragment")
public class ArticleFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private RecyclerView recyclerView;
    ArticleAdapter articleAdapter;

    ArticleViewModel articleViewModel;

    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article, container, false);

        recyclerView = view.findViewById(R.id.recycleView);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(this);

        initData();


        return view;
    }

    private void initData() {
        articleViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(ArticleViewModel.class);
        articleAdapter = new ArticleAdapter(articleViewModel.getArticles().getValue());
        recyclerView.setAdapter(articleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleViewModel.getArticles().observe(getActivity(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                refreshLayout.finishLoadMore();
                List<Article> temp = articles.subList(articleAdapter.getData().size(), articles.size());
                if (temp.size() > 0) {
                    articleAdapter.addData(temp);
                }

            }
        });
    }


    public static ArticleFragment newInstance() {
        ArticleFragment instance = new ArticleFragment();
        return instance;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        articleViewModel.loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }


    class ArticleAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

        public ArticleAdapter(List<Article> data) {
            super(R.layout.item_article_card, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, @org.jetbrains.annotations.Nullable Article article) {
            baseViewHolder.setText(R.id.tv_author, article.getAuthor());
            baseViewHolder.setText(R.id.date, article.getNiceDate());
            baseViewHolder.setGone(R.id.tv_desc, TextUtils.isEmpty(article.getDesc()));
            baseViewHolder.setText(R.id.tv_desc, article.getDesc());
            baseViewHolder.setText(R.id.tv_chapter, article.getChapterName() + "/" + article.getSuperChapterName());
            if (!TextUtils.isEmpty(article.getEnvelopePic())) {
                baseViewHolder.setGone(R.id.thumbnail, false);
                Glide.with(ArticleFragment.this.getContext()).load(article.getEnvelopePic()).into((ImageView) baseViewHolder.getView(R.id.thumbnail));
            } else {
                baseViewHolder.setGone(R.id.thumbnail, true);
            }
            baseViewHolder.setText(R.id.tv_title, HtmlCompat.fromHtml(article.getTitle(), FROM_HTML_MODE_LEGACY).toString());
        }
    }

}
