package com.baemin.woowahan_presentation_android.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.ImageModel;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by leetaejun on 2016. 2. 15..
 */
public class PresentationPdfPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ImageModel> imageModelList;
    private PhotoViewAttacher photoViewAttacher;

    public PresentationPdfPagerAdapter(Context context, List<ImageModel> imageModelList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.imageModelList = imageModelList;
    }

    @Override
    public int getCount() {
        return imageModelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= inflater.inflate(R.layout.viewpager_fragment_pdf, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_fragment_pdf_iv);
        Picasso.with(context)
                .load(Constants.API_SERVER_BASE_URL + imageModelList.get(position).getOriginal_url())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        photoViewAttacher = new PhotoViewAttacher(imageView);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, "이미지 로드에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v==obj;
    }


}
