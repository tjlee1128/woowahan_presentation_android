package com.baemin.woowahan_presentation_android.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.WPBaseAdapter;
import com.baemin.woowahan_presentation_android.model.TeamModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 18..
 */
public class SearchTeamsAdapter extends WPBaseAdapter<TeamModel> {

    public SearchTeamsAdapter(Context context, List<TeamModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sSearchTeamsViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_search_teams, parent, false);
            holder = new sSearchTeamsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (sSearchTeamsViewHolder) convertView.getTag();
        }

        holder.teamTextView.setText(list.get(position).getName());

        return convertView;
    }

    public static class sSearchTeamsViewHolder {
        @Bind(R.id.row_search_teams_tv)
        TextView teamTextView;

        public sSearchTeamsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
