package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.WPBaseAdapter;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.DateConvertor;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 12..
 */
public class PresentationsAdapter extends WPBaseAdapter<PresentationModel> {

    public PresentationsAdapter(Context context, List<PresentationModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sPresentationsViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_presentations, parent, false);
            holder = new sPresentationsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (sPresentationsViewHolder) convertView.getTag();
        }

        // initialize
        holder.isVideoTextView.setVisibility(View.GONE);
        holder.isPdfTextView.setVisibility(View.GONE);

        // has video?
        if (list.get(position).getVideo() != null) {
            Picasso.with(context)
                    .load(Constants.API_SERVER_BASE_URL + list.get(position).getVideo().getThumb_url())
                    .resize(120, 120)
                    .into(holder.thumbImageView);
            holder.isVideoTextView.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(context)
                    .load(Constants.DEFAULT_THUMB_URL)
                    .resize(120, 120)
                    .into(holder.thumbImageView);
        }

        // has pdf?
        if (list.get(position).getPdf() != null) {
            holder.isPdfTextView.setVisibility(View.VISIBLE);
        }

        holder.titleTextView.setText(list.get(position).getTitle());

        String createdAt = "";
        String updatedAt = "";
        try {
            createdAt = DateConvertor.convertToDate(list.get(position).getCreated_at());
            createdAt = DateConvertor.utcToLocaltime(createdAt);
            updatedAt = DateConvertor.convertToDate(list.get(position).getUpdated_at());
            updatedAt = DateConvertor.utcToLocaltime(updatedAt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.createdTextView.setText(createdAt);
        holder.updatedTextView.setText(updatedAt);

        holder.subtitleTextView.setText(list.get(position).getSubtitle());

        Picasso.with(context)
                .load(Constants.API_SERVER_BASE_URL + list.get(position).getUser().getImage().getThumb_url())
                .into(holder.userImageView);
        holder.userTextView.setText(list.get(position).getUser().getFullname());
        holder.teamnameTextView.setText(list.get(position).getUser().getTeam_name());

        return convertView;
    }

    public static class sPresentationsViewHolder {
        @Bind(R.id.row_presentations_presentation_thumb_iv)
        ImageView thumbImageView;

        @Bind(R.id.row_presentations_presentation_title_tv)
        TextView titleTextView;

        @Bind(R.id.row_presentations_presentation_created_at_tv)
        TextView createdTextView;

        @Bind(R.id.row_presentations_presentation_updated_at_tv)
        TextView updatedTextView;

        @Bind(R.id.row_presentations_presentation_subtitle_tv)
        TextView subtitleTextView;

        @Bind(R.id.row_presentations_isvideo_tv)
        TextView isVideoTextView;

        @Bind(R.id.row_presentations_ispdf_tv)
        TextView isPdfTextView;

        @Bind(R.id.row_presentations_user_image_iv)
        ImageView userImageView;

        @Bind(R.id.row_presentations_user_name_tv)
        TextView userTextView;

        @Bind(R.id.row_presentations_user_teamname_tv)
        TextView teamnameTextView;

        public sPresentationsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
