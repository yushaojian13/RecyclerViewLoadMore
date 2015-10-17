package com.ysj.recylerviewloadmore;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView loadMoreTV;

    private DemoAdapter demoAdapter;
    private EndlessScrollListener endlessScrollListener;

    private List<String> data = new ArrayList<>();

    private DataCallback<String> dataCallback;
    private DataCenter dataCenter;

    private boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        loadMoreTV = (TextView) findViewById(R.id.tv_load_more);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        demoAdapter = new DemoAdapter();
        demoAdapter.setData(data);
        recyclerView.setAdapter(demoAdapter);

        registerDataCallback();
        setListeners(layoutManager);

        setRefreshing();
        dataCenter.loadData();
    }

    private void setRefreshing() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void registerDataCallback() {
        dataCallback = new DataCallback<String>() {
            @Override
            public void onResult(ResultCode resultCode, List<String> result) {
                removeRefreshView();
                removeLoadingView();

                hasMore = true;

                switch (resultCode) {
                    case NO_MORE:
                        hasMore = false;
                    case SUCCESS:
                        int lastCount = data.size();
                        int resultCount = result == null ? 0 : result.size();

                        if (resultCount > 0) {
                            data.addAll(result);
                            demoAdapter.setLastPositionViewType(DemoAdapter.VIEW_ITEM);
                            demoAdapter.notifyItemRangeInserted(lastCount, lastCount + resultCount);
                        }

                        endlessScrollListener.setLoaded();
                        break;
                    case ERROR:
                        if (data.size() != 0) {
                            data.add(null);
                            demoAdapter.setLastPositionViewType(DemoAdapter.VIEW_LOAD_MORE);
                            demoAdapter.notifyItemChanged(data.size() - 1);
                        } else {
                            loadMoreTV.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        };

        dataCenter = new DataCenter(dataCallback);
    }

    private void setListeners(final LinearLayoutManager layoutManager) {
        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (hasMore) {
                    addLoadingView();
                    dataCenter.loadData();
                }
            }

            @Override
            public void onBottomReached() {
                if (!hasMore) {
                    Toast.makeText(MainActivity.this, "No more data", Toast.LENGTH_SHORT).show();
                }
            }
        };

        recyclerView.addOnScrollListener(endlessScrollListener);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataCenter.reset();
                dataCenter.loadData();
            }
        });

        demoAdapter.setOnLoadMoreClickListener(new DemoAdapter.OnLoadMoreClickListener() {
            @Override
            public void onClick() {
                demoAdapter.setLastPositionViewType(DemoAdapter.VIEW_PROGRESS);
                demoAdapter.notifyItemChanged(data.size());
                dataCenter.loadData();
            }
        });

        loadMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreTV.setVisibility(View.GONE);
                setRefreshing();
                dataCenter.loadData();
            }
        });
    }

    private void addLoadingView() {
        data.add(null);
        demoAdapter.setLastPositionViewType(DemoAdapter.VIEW_PROGRESS);
        demoAdapter.notifyItemInserted(data.size() - 1);
    }

    private void removeRefreshView() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            data.clear();
            demoAdapter.notifyDataSetChanged();
        }
    }

    private void removeLoadingView() {
        if (isLoading()) {
            data.remove(data.size() - 1);
            demoAdapter.notifyItemRemoved(data.size());
        }
    }

    private boolean isLoading() {
        return data.size() > 0 && data.get(data.size() - 1) == null;
    }
}
