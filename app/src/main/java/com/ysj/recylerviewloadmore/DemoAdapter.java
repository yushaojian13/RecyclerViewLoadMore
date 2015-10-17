package com.ysj.recylerviewloadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ysj.log.L;

import java.util.List;

/**
 * Created by Yu Shaojian on 2015 10 08.
 */
public class DemoAdapter extends RecyclerView.Adapter {
    public static final int VIEW_ITEM = 0;
    public static final int VIEW_PROGRESS = 1;
    public static final int VIEW_LOAD_MORE = 2;

    private int lastPositionViewType = VIEW_ITEM;

    private List<String> data;

    private OnLoadMoreClickListener loadMoreClickListener;

    public void setOnLoadMoreClickListener(OnLoadMoreClickListener loadMoreClickListener) {
        this.loadMoreClickListener = loadMoreClickListener;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setLastPositionViewType(int type) {
        lastPositionViewType = type;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_ITEM : lastPositionViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item, parent, false);
            vh = new ItemViewHolder(v);
        } else if (viewType == VIEW_LOAD_MORE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_load_more, parent, false);
            vh = new LoadMoreViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_progress, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.itemTV.setText(data.get(position));
        } else if (holder instanceof LoadMoreViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadMoreClickListener != null) {
                        loadMoreClickListener.onClick();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV;

        public ItemViewHolder(View v) {
            super(v);
            itemTV = (TextView) v.findViewById(R.id.tv_item);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }

    public static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public LoadMoreViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.d("getAdapterPosition = " + getAdapterPosition());
                    L.d("getLayoutPosition = " + getLayoutPosition());
                }
            });
        }
    }

    public interface OnLoadMoreClickListener {
        void onClick();
    }
}
