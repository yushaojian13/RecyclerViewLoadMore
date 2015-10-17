package com.ysj.recylerviewloadmore;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaojian on 2015 10 17.
 */
public class DataCenter {
    public static final int PAGE_ITEM_COUNT = 20;
    public static final int MIDDLE_TRICK_ITEM_COUNT = 40;
    public static final int MAX_ITEM_COUNT = 60;
    public static final int PLUS_ITEM_COUNT = 10;

    private DataCallback<String> dataCallback;

    private Handler handler = new Handler();

    private int lastCount = 0;
    private boolean firstErrorTricked = false;
    private boolean middleErrorTricked = false;

    public DataCenter(DataCallback<String> dataCallback) {
        this.dataCallback = dataCallback;
    }

    public void reset() {
        lastCount = 0;
    }

    public void loadData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!firstErrorTricked) {
                    firstErrorTricked = true;
                    dataCallback.onResult(ResultCode.ERROR, null);
                } else if (!middleErrorTricked && lastCount == MIDDLE_TRICK_ITEM_COUNT) {
                    middleErrorTricked = true;
                    dataCallback.onResult(ResultCode.ERROR, null);
                } else if (lastCount == MAX_ITEM_COUNT) {
                    dataCallback.onResult(ResultCode.NO_MORE, getData(PLUS_ITEM_COUNT));
                } else {
                    dataCallback.onResult(ResultCode.SUCCESS, getData(PAGE_ITEM_COUNT));
                }
            }
        }, 2000);
    }

    private List<String> getData(int count) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add("Item " + (lastCount + i + 1));
        }

        lastCount += count;

        return data;
    }
}
