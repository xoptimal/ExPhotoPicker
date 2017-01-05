package com.xoptimal.photopicker;

import android.content.Context;
import android.content.Intent;

import com.xoptimal.photopicker.view.act.PhotoPreviewActivity;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Freddie on 2017/1/3 0003 .
 * Description:
 */
public class ExPhotoPreView {

    private ArrayList<String> mPhotos;
    private int               mPosition;

    public ExPhotoPreView(ArrayList<String> photos, int position) {
        mPhotos = photos;
        mPosition = position;
    }

    public void start(Context context) {
        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        intent.putStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS, mPhotos);
        intent.putExtra(PhotoPreview.EXTRA_CURRENT_ITEM, mPosition);
        context.startActivity(intent);
    }

    public static class Builder {

        private ArrayList<String> mPhotos;
        private int               mPosition;

        public Builder setPhotos(ArrayList<String> photos) {
            mPhotos = photos;
            return this;
        }

        public Builder setCurrentItem(int position) {
            mPosition = position;
            return this;
        }

        public ExPhotoPreView build() {
            return new ExPhotoPreView(mPhotos, mPosition);
        }
    }
}
