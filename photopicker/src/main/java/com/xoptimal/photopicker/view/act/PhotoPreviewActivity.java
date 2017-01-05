package com.xoptimal.photopicker.view.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.xoptimal.photopicker.R;
import com.xoptimal.photopicker.view.frag.ExImagePagerFragment;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Freddie on 2016/11/2 0002 .
 * Description:图片预览界面(普通版)
 */
public class PhotoPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.x_act_preview);
        showFragment();
    }


    private ExImagePagerFragment mFragment;

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mFragment.getViewPager());
    }


    private void showFragment() {

        int               currentItem = getIntent().getIntExtra(PhotoPreview.EXTRA_CURRENT_ITEM, 0);
        ArrayList<String> list        = getIntent().getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);

        mFragment = ExImagePagerFragment.newInstance(list, currentItem);
        getFragmentManager().beginTransaction().add(R.id.fl_container, mFragment).commit();
    }


    public static void start(Context context, int currentItem, ArrayList<String> list) {

        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        intent.putStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS, list);
        intent.putExtra(PhotoPreview.EXTRA_CURRENT_ITEM, currentItem);
        context.startActivity(intent);
    }
}
