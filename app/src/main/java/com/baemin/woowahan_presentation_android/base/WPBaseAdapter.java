package com.baemin.woowahan_presentation_android.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public abstract class WPBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> list;

    public WPBaseAdapter(Context context, List<T> tList) {
        if (tList == null) throw new IllegalArgumentException("Data must not be null");
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = tList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
