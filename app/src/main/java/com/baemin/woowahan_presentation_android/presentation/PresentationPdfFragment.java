package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;

public class PresentationPdfFragment extends Fragment {
    private static final String ARG_PDF = "PDF";

    private PresentationModel presentationModel;


    public PresentationPdfFragment() {
        // Required empty public constructor
    }

    public static PresentationPdfFragment newInstance(PresentationModel presentationModel) {
        PresentationPdfFragment fragment = new PresentationPdfFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PDF, presentationModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            presentationModel = (PresentationModel) getArguments().getSerializable(ARG_PDF);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presentation_pdf, container, false);

        return view;
    }
}
