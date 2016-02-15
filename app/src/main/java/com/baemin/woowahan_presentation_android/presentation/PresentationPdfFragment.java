package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;

public class PresentationPdfFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PDF = "PDF";

    private PresentationModel presentationModel;
    private ViewPager pdfViewPager;
    private PresentationPdfPagerAdapter pdfPagerAdapter;
    private RelativeLayout leftChevronLayout;
    private RelativeLayout rightChevronLayout;
    private TextView titleTextView;


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

        leftChevronLayout = (RelativeLayout) view.findViewById(R.id.fragment_presentation_pdf_chevronleft_rl);
        rightChevronLayout = (RelativeLayout) view.findViewById(R.id.fragment_presentation_pdf_chevronright_rl);
        leftChevronLayout.setOnClickListener(this);
        rightChevronLayout.setOnClickListener(this);
        titleTextView = (TextView) view.findViewById(R.id.fragment_presentation_pdf_title_tv);
        pdfViewPager = (ViewPager) view.findViewById(R.id.fragment_presentation_pdf_vp);
        pdfPagerAdapter = new PresentationPdfPagerAdapter(getActivity(), presentationModel.getImages());
        pdfViewPager.setAdapter(pdfPagerAdapter);
        pdfViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleTextView.setText("" + (position + 1) + "/" + presentationModel.getImages().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        titleTextView.setText("총 " + presentationModel.getImages().size() + "pages");


        return view;
    }

    @Override
    public void onClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.fragment_presentation_pdf_chevronleft_rl:
                if (pdfViewPager.getCurrentItem() > 0) {
                    position = pdfViewPager.getCurrentItem();
                    titleTextView.setText("" + (position - 1) + "/" + presentationModel.getImages().size());
                    pdfViewPager.setCurrentItem(position - 1, true);
                }
                break;

            case R.id.fragment_presentation_pdf_chevronright_rl:
                if (pdfViewPager.getCurrentItem() < presentationModel.getImages().size()) {
                    position = pdfViewPager.getCurrentItem();
                    titleTextView.setText("" + (position + 1) + "/" + presentationModel.getImages().size());
                    pdfViewPager.setCurrentItem(position + 1, true);
                }
                break;

            default:
                break;
        }
    }
}
