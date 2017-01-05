package com.xoptimal.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xoptimal.photopicker.view.frag.ExImagePagerFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * Created by Freddie on 2016/12/30 0030 .
 * Description:
 */
public class ExPhotoPagerAdapter extends PagerAdapter {

    private List<String> paths = new ArrayList<>();
    private RequestManager                          mGlide;
    private ExImagePagerFragment.ImagePagerListener mPagerListener;

    public ExPhotoPagerAdapter(RequestManager glide, List<String> paths) {
        this.paths = paths;
        this.mGlide = glide;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(me.iwf.photopicker.R.layout.__picker_picker_item_pager, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_pager);

        final String path = paths.get(position);
        final Uri    uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }

        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

        if (canLoadImage) {
            mGlide.load(uri)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .dontTransform()
                    .override(800, 800)
                    .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                    .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPagerListener != null) {
                    mPagerListener.onOClickPhoto();
                } else {
                    if (context instanceof Activity) {
                        if (!((Activity) context).isFinishing()) {
                            ((Activity) context).onBackPressed();
                        }
                    }
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }

    public void setPagerListener(ExImagePagerFragment.ImagePagerListener pagerListener) {
        mPagerListener = pagerListener;
    }

    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
