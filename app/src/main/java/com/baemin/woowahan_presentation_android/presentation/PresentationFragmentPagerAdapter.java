package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.baemin.woowahan_presentation_android.model.PresentationModel;

/**
 * Created by leetaejun on 2016. 2. 15..
 */
public class PresentationFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "VIDEO", "PDF" };
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
        return tabTitles[position];
    }
}
