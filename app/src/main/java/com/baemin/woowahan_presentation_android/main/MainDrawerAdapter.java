package com.baemin.woowahan_presentation_android.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.WPBaseAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 16..
 */
public class MainDrawerAdapter extends WPBaseAdapter<String> {

    public MainDrawerAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sMainDrawerViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_main_drawer, parent, false);
            holder = new sMainDrawerViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (sMainDrawerViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(list.get(position).toString());

        return convertView;
    }

    public static class sMainDrawerViewHolder {

        @Bind(R.id.row_main_drawer_tv)
        TextView titleTextView;

        public sMainDrawerViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
