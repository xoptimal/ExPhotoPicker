package com.xoptimal.photopicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.xoptimal.photopicker.view.act.PhotoPickerActivity;

import java.util.ArrayList;

import static me.iwf.photopicker.PhotoPicker.EXTRA_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_CAMERA;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * Created by Freddie on 2017/1/3 0003 .
 * Description:
 */
public class ExPhotoPicker {

    private ArrayList<String> mPhotos;
    private int               mMaxCount;
    private boolean           mShowCamera;
    private boolean           mShowPreview;
    private boolean           mShowGif;
    private boolean           mShowSingleModel;

    private ExPhotoPicker(ArrayList<String> photos, int maxCount, boolean showCamera,
                         boolean showPreview, boolean showGif, boolean showSingleModel) {
        mPhotos = photos;
        mMaxCount = maxCount;
        mShowCamera = showCamera;
        mShowPreview = showPreview;
        mShowGif = showGif;
        mShowSingleModel = showSingleModel;
    }

    public void start(Activity activity, int code) {
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        activity.startActivityForResult(initIntent(intent), code);
    }

    public void start(Fragment fragment, int code) {
        Intent intent = new Intent(fragment.getActivity(), PhotoPickerActivity.class);
        fragment.startActivityForResult(initIntent(intent), code);
    }

    private Intent initIntent(Intent intent) {
        intent.putExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(EXTRA_SHOW_GIF, mShowGif);
        intent.putExtra(EXTRA_PREVIEW_ENABLED, mShowPreview);
        intent.putExtra(EXTRA_MAX_COUNT, mMaxCount);
        intent.putExtra(PhotoPickerActivity.EXTRA_SINGLE, mShowSingleModel);
        intent.putStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS, mPhotos);
        return intent;
    }


    public static class Builder {

        private ArrayList<String> mPhotos;
        private int               mMaxCount;
        private boolean           mShowCamera;
        private boolean           mShowPreview;
        private boolean           mShowGif;
        private boolean           mShowSingleModel;

        public Builder setPhotos(ArrayList<String> photos) {
            mPhotos = photos;
            return this;
        }

        public Builder setMaxCount(int maxCount) {
            mMaxCount = maxCount;
            return this;
        }


        public Builder showSingleModel(boolean showSingleModel) {
            mShowSingleModel = showSingleModel;
            return this;
        }

        public Builder showGif(boolean showGif) {
            mShowGif = showGif;
            return this;
        }

        public Builder showCamera(boolean showCamera) {
            mShowCamera = showCamera;
            return this;
        }

        public Builder showPreview(boolean showPreview) {
            mShowPreview = showPreview;
            return this;
        }

        public ExPhotoPicker build() {
            return new ExPhotoPicker(mPhotos, mMaxCount, mShowCamera, mShowPreview, mShowGif, mShowSingleModel);
        }
    }
}
