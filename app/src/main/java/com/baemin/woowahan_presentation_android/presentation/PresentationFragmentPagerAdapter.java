package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.PresentationModel;

/**
 * Created by leetaejun on 2016. 2. 15..
 */
public class PresentationFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "VIDEO", "PDF" };
    private int[] imageResId = {
            R.drawable.ic_movie_black_24dp,
            R.drawable.ic_picture_as_pdf_black_24dp
    };
    private Context context;
    private PresentationModel presentationModel;

    public PresentationFragmentPagerAdapter(FragmentManager fragmentManager, Context context, PresentationModel presentationModel) {
        super(fragmentManager);
        this.context = context;
        this.presentationModel = presentationModel;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return PresentationVideoFragment.newInstance(presentationModel);
        } else {
            return PresentationPdfFragment.newInstance(presentationModel);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
