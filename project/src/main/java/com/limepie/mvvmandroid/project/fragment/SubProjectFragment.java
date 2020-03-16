package com.limepie.mvvmandroid.project.fragment;

import android.os.Bundle;
import android.text.TextUtils;
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

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.limepie.mvvmandroid.base.BaseFragment;
import com.limepie.mvvmandroid.project.R;
import com.limepie.mvvmandroid.project.model.Article;
import com.limepie.mvvmandroid.project.util.ImageLoader;
import com.limepie.mvvmandroid.project.viewmodel.SubArticleViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class SubProjectFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    SubArticleViewModel subArticleViewModel;
    ArticleAdapter articleAdapter;
    RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);

        recyclerView = view.findViewById(R.id.recycleView);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);

        initData();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            id = getArguments().getInt("id");
        }
    }

    public static SubProjectFragment newInstance(int authorId) {
        SubProjectFragment subArticleFragment = new SubProjectFragment();
        Bundle args = new Bundle();
        args.putInt("id",authorId);
        subArticleFragment.setArguments(args);
        return subArticleFragment;
    }

    private void initData() {
        subArticleViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(SubArticleViewModel.class);
        subArticleViewModel.setId(id);
        articleAdapter = new ArticleAdapter(subArticleViewModel.getArticles().getValue());
        articleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance().build("/webengine/webactivity")
                        .withString("title",((Article)adapter.getData().get(position)).getTitle())
                        .withString("url",((Article)adapter.getData().get(position)).getLink())
                        .navigation();
            }
        });
        recyclerView.setAdapter(articleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subArticleViewModel.getArticles().observe(getActivity(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                refreshLayout.finishLoadMore();
                List<Article> temp = articles.subList(articleAdapter.getData().size(),articles.size());
                if (temp.size()>0) {
                    articleAdapter.addData(temp);
                }
            }
        });
    }



    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        subArticleViewModel.loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    class ArticleAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

        public ArticleAdapter(List<Article> data) {
            super(R.layout.item_article_card,data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, @org.jetbrains.annotations.Nullable Article article) {
            baseViewHolder.setText(R.id.tv_author,article.getAuthor());
            baseViewHolder.setText(R.id.date,article.getNiceDate());
            baseViewHolder.setGone(R.id.tv_desc, TextUtils.isEmpty(article.getDesc()));
            baseViewHolder.setText(R.id.tv_desc,article.getDesc());
            baseViewHolder.setText(R.id.tv_chapter,article.getChapterName()+"/"+article.getSuperChapterName());
            if(!TextUtils.isEmpty(article.getEnvelopePic())) {
                baseViewHolder.setGone(R.id.thumbnail,false);
                ImageLoader.load(SubProjectFragment.this.getContext().getApplicationContext(),article.getEnvelopePic(),(ImageView) baseViewHolder.getView(R.id.thumbnail));
            } else {
                baseViewHolder.setGone(R.id.thumbnail,true);
            }
            baseViewHolder.setText(R.id.tv_title, HtmlCompat.fromHtml(article.getTitle(), FROM_HTML_MODE_LEGACY).toString());
        }
    }
}
