package com.baemin.woowahan_presentation_android.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.base.WPMessageDialog;
import com.baemin.woowahan_presentation_android.model.PresentationModel;
import com.baemin.woowahan_presentation_android.presentation.PresentationActivity;
import com.baemin.woowahan_presentation_android.presentation.PresentationOnlyPdfActivity;
import com.baemin.woowahan_presentation_android.presentation.PresentationOnlyVideoActivity;
import com.baemin.woowahan_presentation_android.presentation.PresentationsActivity;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 19..
 */
public class MainFavoritePagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<PresentationModel> presentationModelList;
//    private PhotoViewAttacher photoViewAttacher;

    public MainFavoritePagerAdapter(Context context, List<PresentationModel> presentationModelList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.presentationModelList = presentationModelList;
    }

    @Override
    public int getCount() {
        return presentationModelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.viewpager_main_header_item, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_main_header_item_iv);

        if (presentationModelList.get(position).getVideo() != null) {
            Picasso.with(context)
                    .load(Constants.API_SERVER_BASE_URL + presentationModelList.get(position).getVideo().getThumb_url())
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(Constants.API_SERVER_BASE_URL + presentationModelList.get(position).getImages().get(0).getOriginal_url())
                    .into(imageView);
        }
        ((TextView) view.findViewById(R.id.viewpager_main_header_item_title_tv)).setText(presentationModelList.get(position).getTitle());
        ((TextView) view.findViewById(R.id.viewpager_main_header_item_thumbs_tv)).setText("" + presentationModelList.get(position).getThumbs_count());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (presentationModelList.get(position).getVideo() == null) {
                    intent = new Intent(context, PresentationOnlyPdfActivity.class);
                }

                if (presentationModelList.get(position).getPdf() == null) {
                    intent = new Intent(context, PresentationOnlyVideoActivity.class);
                }

                if (presentationModelList.get(position).getVideo() != null && presentationModelList.get(position).getPdf() != null) {
                    intent = new Intent(context, PresentationActivity.class);
                } else {
                }

                intent.putExtra(Constants.EXTRA_PRESENTATION_ID, presentationModelList.get(position).getId());
                intent.putExtra(Constants.EXTRA_PRESENTATION_NAME, presentationModelList.get(position).getTitle());
                context.startActivity(intent);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }
}