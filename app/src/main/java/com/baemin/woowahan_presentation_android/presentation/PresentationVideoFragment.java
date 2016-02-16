package com.baemin.woowahan_presentation_android.presentation;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.DateConvertor;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class PresentationVideoFragment extends Fragment {
    private static final String ARG_VIDEO = "VIDEO";

    private PresentationModel presentationModel;

    // video view
    private VideoView videoView;
    private View placeholder;
    private MediaController mediaController;

    public PresentationVideoFragment() {
        // Required empty public constructor
    }

    public static PresentationVideoFragment newInstance(PresentationModel presentationModel) {
        PresentationVideoFragment fragment = new PresentationVideoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_VIDEO, presentationModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            presentationModel = (PresentationModel) getArguments().getSerializable(ARG_VIDEO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presentation_video, container, false);

        placeholder = view.findViewById(R.id.fragment_presentation_video_holder);
        videoView = (VideoView) view.findViewById(R.id.fragment_presentation_video_vv);
//        videoView.setZOrderOnTop(false);
        Uri uri= Uri.parse(Constants.API_SERVER_BASE_URL + presentationModel.getVideo().getUrl());
        videoView.setVideoURI(uri);
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                placeholder.setVisibility(View.GONE);
            }
        });


        ((TextView) view.findViewById(R.id.fragment_presentation_title_tv)).setText(presentationModel.getTitle());
        ((TextView) view.findViewById(R.id.fragment_presentation_subtitle_tv)).setText(presentationModel.getSubtitle());

        String updatedAt = "";
        try {
            updatedAt = DateConvertor.convertToDate(presentationModel.getUpdated_at());
            updatedAt = DateConvertor.utcToLocaltime(updatedAt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((TextView) view.findViewById(R.id.fragment_presentation_date_updatedat_tv)).setText("modified on " + updatedAt);
        ((TextView) view.findViewById(R.id.fragment_presentation_content_tv)).setText(presentationModel.getContent());

        Picasso.with(getActivity())
                .load(Constants.API_SERVER_BASE_URL + presentationModel.getUser().getImage().getThumb_url())
                .into((RoundedImageView) view.findViewById(R.id.fragment_presentation_user_image_iv));
        ((TextView) view.findViewById(R.id.fragment_presentation_user_tv)).setText(presentationModel.getUser().getFullname());
        ((TextView) view.findViewById(R.id.fragment_presentation_team_name_tv)).setText(presentationModel.getUser().getTeam_name());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        placeholder.setVisibility(View.VISIBLE);
    }
}
