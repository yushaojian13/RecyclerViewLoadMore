package com.ysj.recylerviewloadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 5;
    private int lastVisibleItem, lastCompletedItem, totalItemCount;

    private boolean loading = false;

    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        lastCompletedItem = layoutManager.findLastCompletelyVisibleItemPosition();
        totalItemCount = layoutManager.getItemCount();

        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            loading = true;

            onLoadMore();
        }

        if (lastCompletedItem == totalItemCount - 1) {
            onBottomReached();
        }
    }

    public abstract void onLoadMore();
    public abstract void onBottomReached();
}