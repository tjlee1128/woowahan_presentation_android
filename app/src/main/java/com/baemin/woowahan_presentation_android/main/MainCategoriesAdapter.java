package com.baemin.woowahan_presentation_android.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.ViewPagerFixed;
import com.baemin.woowahan_presentation_android.base.WPBaseAdapter;
import com.baemin.woowahan_presentation_android.model.CategoryModel;
import com.baemin.woowahan_presentation_android.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class MainCategoriesAdapter extends WPBaseAdapter<CategoryModel> {

    public MainCategoriesAdapter(Context context, List<CategoryModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sCategoriesViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_categories, parent, false);
            holder = new sCategoriesViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (sCategoriesViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(list.get(position).getName());


        return convertView;
    }

    public static class sCategoriesViewHolder {
        @Bind(R.id.row_categories_title_tv)
        TextView titleTextView;

        public sCategoriesViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
