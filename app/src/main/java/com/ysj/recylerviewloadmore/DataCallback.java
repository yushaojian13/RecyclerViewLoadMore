package com.ysj.recylerviewloadmore;

import java.util.List;

/**
 * Created by yushaojian on 2015 10 17.
 */
public interface DataCallback<T> {
    void onResult(ResultCode resultCode, List<T> result);
}
