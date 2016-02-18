package com.baemin.woowahan_presentation_android.search;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.User;
import com.baemin.woowahan_presentation_android.presentation.PresentationsActivity;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 18..
 */
public class UserItemView extends RelativeLayout {

    private Context context;

    private View view;

    @Bind(R.id.row_search_user_image_iv)
    RoundedImageView userImageView;
    @Bind(R.id.row_search_user_tv)
    TextView userNameTextView;
    @Bind(R.id.row_search_user_team_tv)
    TextView userTeamTextView;

    public UserItemView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    private void init(Context context) {
        view = inflate(context, R.layout.row_search_users, this);
        ButterKnife.bind(this);
    }

    public void bind(final User user) {
        Picasso.with(context)
                .load(Constants.API_SERVER_BASE_URL + user.getImage())
                .into(userImageView);
        userNameTextView.setText(user.getFullname());
        userTeamTextView.setText(user.getTeam_name());

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("id", "" + user.getId());
                Intent intent = new Intent(context.getApplicationContext(), PresentationsActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID, user.getId());
                intent.putExtra(Constants.EXTRA_USER_NAME, user.getFullname());
                intent.putExtra(Constants.EXTRA_PRESENTATION_COME_IN, "user_id_presentations");
                context.startActivity(intent);
            }
        });
    }


}
