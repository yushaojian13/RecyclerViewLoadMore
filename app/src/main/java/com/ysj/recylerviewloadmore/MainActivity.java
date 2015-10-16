package com.ysj.recylerviewloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private DemoAdapter demoAdapter;
    private EndlessScrollListener endlessScrollListener;

    private List<String> data = new ArrayList<>();
    private Handler handler = new Handler();
    private boolean hasMore = true;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        demoAdapter = new DemoAdapter();
        demoAdapter.setData(data);
        recyclerView.setAdapter(demoAdapter);

        setListeners(layoutManager);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 2000);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setListeners(final LinearLayoutManager layoutManager) {
        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                data.add(null);
                demoAdapter.notifyItemInserted(data.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.remove(data.size() - 1);
                        demoAdapter.notifyItemRemoved(data.size());

                        loadData();
                        endlessScrollListener.setLoaded();
                    }
                }, 2000);
            }

            @Override
            public void onBottomReached() {
                Toast.makeText(MainActivity.this, "No more data", Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.addOnScrollListener(endlessScrollListener);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                demoAdapter.notifyDataSetChanged();

                hasMore = true;
                endlessScrollListener.setHasMore(true);
                loadData();
            }
        });
    }

    private void loadData() {
        int lastCount = data.size();

        if (hasMore) {
            for (int i = lastCount + 1; i <= lastCount + 20; i++) {
                data.add("Item " + i);
            }

            demoAdapter.notifyItemRangeInserted(lastCount, 20);

            int magic = random.nextInt(20);

            hasMore = magic > 5;
            endlessScrollListener.setHasMore(hasMore);
        }

        refreshLayout.setRefreshing(false);
    }

}
