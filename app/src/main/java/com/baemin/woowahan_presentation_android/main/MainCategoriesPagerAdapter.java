package com.baemin.woowahan_presentation_android.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.CategoryModel;
import com.baemin.woowahan_presentation_android.model.ImageModel;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by leetaejun on 2016. 2. 18..
 */
public class MainCategoriesPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private CategoryModel imageUrlList;
    private PhotoViewAttacher photoViewAttacher;

    public MainCategoriesPagerAdapter(Context context, CategoryModel imageUrlList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.imageUrlList = imageUrlList;
    }

    @Override
    public int getCount() {
        if (imageUrlList.getPresentations() == null)
            return 0;
        else
            return imageUrlList.getPresentations().size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= inflater.inflate(R.layout.viewpager_categories_thumbs, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_categories_thumbs_iv);

        if (imageUrlList.getPresentations().get(position).getVideo() != null) {
            Picasso.with(context)
                    .load(Constants.API_SERVER_BASE_URL + imageUrlList.getPresentations().get(position).getVideo().getThumb_url())
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
        } else {
            Picasso.with(context)
                    .load(Constants.API_SERVER_BASE_URL + imageUrlList.getPresentations().get(position).getImages().get(0).getOriginal_url())
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
        }
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
