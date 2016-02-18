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

    private List<List<String>> thumbsImageUrlList;

    public MainCategoriesAdapter(Context context, List<CategoryModel> list) {
        super(context, list);
//        thumbsImageUrlList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            thumbsImageUrlList.add(new ArrayList<String>());
//            for (int j = 0; j < list.get(i).getPresentations().size(); j++) {
//                if (list.get(i).getPresentations().get(j).getVideo() != null) {
//                    thumbsImageUrlList.get(i).add(list.get(i).getPresentations().get(j).getVideo().getThumb_url());
//                } else {
//                    thumbsImageUrlList.get(i).add(Constants.DEFAULT_THUMB_URL);
//                }
//            }
//        }
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

//        MainCategoriesPagerAdapter adapter = new MainCategoriesPagerAdapter(context, new CategoryModel());
//        holder.viewPagerFixed.setAdapter(adapter);
//
//        if (list.get(position).getPresentations().size() > 0) {
//            adapter = new MainCategoriesPagerAdapter(context, list.get(position));
//            holder.viewPagerFixed.setId(position + getCount());
//            holder.viewPagerFixed.setAdapter(adapter);
//            holder.viewPagerFixed.setCurrentItem(0);
//        }
        holder.viewPagerFixed.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);


        return convertView;
    }

    public static class sCategoriesViewHolder {
        @Bind(R.id.row_categories_title_tv)
        TextView titleTextView;

        @Bind(R.id.row_categories_thumb_vp)
        ViewPagerFixed viewPagerFixed;

        public sCategoriesViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
