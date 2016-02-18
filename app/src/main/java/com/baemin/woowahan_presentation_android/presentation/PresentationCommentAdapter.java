package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.WPBaseAdapter;
import com.baemin.woowahan_presentation_android.model.CommentModel;
import com.baemin.woowahan_presentation_android.model.CommentsModel;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.DateConvertor;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 17..
 */
public class PresentationCommentAdapter extends WPBaseAdapter<CommentModel> {

    public PresentationCommentAdapter(Context context, List<CommentModel> list) {
        super(context, list);
    }

    public boolean addComment(CommentsModel commentsModel) {
        list.addAll(commentsModel.getRows());
        notifyDataSetChanged();

        return true;
    }

    public int getLastCommentId() {
        if (list.size() > 0) {
            return list.get(list.size() - 1).getId();
        } else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sPresentationCommentViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_comments, parent, false);
            holder = new sPresentationCommentViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (sPresentationCommentViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load(Constants.API_SERVER_BASE_URL + list.get(position).getUser().getImage().getThumb_url())
                .into(holder.userImageView);
        holder.userTextView.setText(list.get(position).getUser().getFullname());

        String createdAt = "";
        try {
            createdAt = DateConvertor.convertToDate(list.get(position).getCreated_at());
            createdAt = DateConvertor.utcToLocaltime(createdAt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.createdatTextView.setText(createdAt);
        holder.teamTextView.setText(list.get(position).getUser().getTeam_name());
        holder.contentTextView.setText(list.get(position).getContent());

        return convertView;
    }

    public static class sPresentationCommentViewHolder {

        @Bind(R.id.row_comment_user_iv)
        RoundedImageView userImageView;

        @Bind(R.id.row_comment_user_tv)
        TextView userTextView;

        @Bind(R.id.row_comment_user_createdat_tv)
        TextView createdatTextView;

        @Bind(R.id.row_comment_user_team_tv)
        TextView teamTextView;

        @Bind(R.id.row_comment_user_content_tv)
        TextView contentTextView;

        public sPresentationCommentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
