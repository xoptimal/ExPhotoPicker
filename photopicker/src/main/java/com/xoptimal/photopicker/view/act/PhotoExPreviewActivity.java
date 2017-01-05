package com.xoptimal.photopicker.view.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xoptimal.photopicker.R;
import com.xoptimal.photopicker.components.TopNavigationBar;
import com.xoptimal.photopicker.entity.ExPhoto;
import com.xoptimal.photopicker.utils.ToastUtils;
import com.xoptimal.photopicker.view.frag.ExImagePagerFragment;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Freddie on 2016/11/2 0002 .
 * Description:图片预览界面-增加版
 */
public class PhotoExPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private ExImagePagerFragment mExImagePagerFragment;
    private TopNavigationBar     top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_preview_ex);
        top_bar = (TopNavigationBar) findViewById(R.id.top_bar);
        initEXPreview();
    }

    private RelativeLayout rl_bottom;
    private Button         btn_done;

    private List<ExPhoto> mExPhotos = new ArrayList<>();
    private int mMaxCount;

    private void initEXPreview() {

        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);
        int currentItem = getIntent().getIntExtra(PhotoPreview.EXTRA_CURRENT_ITEM, 0);

        List<String> paths    = getIntent().getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);
        List<String> selPaths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS_SEL);
        mMaxCount = getIntent().getIntExtra(EXTRA_COUNT, -1);

        if (selPaths == null) {
            for (String path : paths) {
                mExPhotos.add(new ExPhoto(path, true));
            }

        } else {
            for (String path : paths) {
                ExPhoto exPhoto = new ExPhoto(path);
                if (selPaths.contains(path)) {
                    exPhoto.setSelected(true);
                }
                mExPhotos.add(exPhoto);
            }
        }

        mExImagePagerFragment = ExImagePagerFragment.newInstance(paths, currentItem);
        getFragmentManager().beginTransaction().add(R.id.fl_container, mExImagePagerFragment).commit();

        top_bar.setText(R.id.tv_left, getString(R.string.text_picture))
                .setImageResource(R.id.iv_right, R.drawable.icon_checkbox)
                .setBackgroundResource(R.id.iv_right, mExPhotos.get(currentItem).isSelected() ? R.drawable.shape_indicator_selected : R.drawable.shape_indicator_normal)
                .setImageResource(R.id.iv_left, R.drawable.icon_return)
                .setOnClickListener(R.id.tv_left, this)
                .setOnClickListener(R.id.iv_left, this)
                .setOnClickListener(R.id.iv_right, this);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        top_bar.setText(R.id.tv_center, String.format("%d/%d", mExImagePagerFragment.getCurrentItem() + 1, mExPhotos.size()));
        selCount();
        mExImagePagerFragment.setPagerListener(new ExImagePagerFragment.ImagePagerListener() {
            @Override
            public void onPageSelected() {
                top_bar.setText(R.id.tv_center, String.format("%d/%d", mExImagePagerFragment.getCurrentItem() + 1, mExPhotos.size()));
                int currentItem = mExImagePagerFragment.getCurrentItem();
                top_bar.setBackgroundResource(R.id.iv_right, mExPhotos.get(currentItem).isSelected() ? R.drawable.shape_indicator_selected : R.drawable.shape_indicator_normal);
            }

            @Override
            public void onOClickPhoto() {
                int visibility = rl_bottom.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                rl_bottom.setVisibility(visibility);
                top_bar.setVisibility(visibility);
            }
        });
    }

    public static final String EXTRA_COUNT      = "EXTRA_COUNT";
    public static final int    REQUEST_CODE     = 20;
    public static final String EXTRA_PHOTOS_SEL = "EXTRA_PHOTOS_SEL";

    public static void start(Activity activity, int count, ArrayList<String> paths) {
        start(activity, 0, count, paths, null);
    }

    public static void start(Activity activity, int position, int count, ArrayList<String> paths, ArrayList<String> selPaths) {

        Intent intent = new Intent(activity, PhotoExPreviewActivity.class);
        intent.putExtra(EXTRA_COUNT, count);
        intent.putExtra(PhotoPreview.EXTRA_CURRENT_ITEM, position);
        intent.putExtra(PhotoPreview.EXTRA_PHOTOS, paths);

        if (selPaths != null)
            intent.putExtra(EXTRA_PHOTOS_SEL, selPaths);

        activity.startActivityForResult(intent, REQUEST_CODE);
    }


    private void selCount() {
        int count = getSelPaths().size();
        if (count > 0) {
            btn_done.setEnabled(true);
            btn_done.setText(String.format(getString(R.string.btn_done_count), count));
        } else {
            btn_done.setEnabled(false);
            btn_done.setText(getString(R.string.btn_done));
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_left || i == R.id.iv_left) {
            onBackPressed();

        } else if (i == R.id.iv_right) {
            boolean selected = mExPhotos.get(mExImagePagerFragment.getCurrentItem()).isSelected();

            if (getSelPaths().size() >= mMaxCount && !selected) {
                ToastUtils.makeText(PhotoExPreviewActivity.this, getString(R.string.__picker_over_max_count_tips, mMaxCount));
            } else {
                mExPhotos.get(mExImagePagerFragment.getCurrentItem()).setSelected(!selected);
                top_bar.setBackgroundResource(R.id.iv_right, !selected ? R.drawable.shape_indicator_selected : R.drawable.shape_indicator_normal);
                selCount();
            }

        } else if (i == R.id.btn_done) {
            complete = true;
            onBackPressed();
        }
    }

    private boolean complete;
    public static final String EXTRA_COMPLETE = "EXTRA_COMPLETE";

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_PHOTOS_SEL, getSelPaths());
        intent.putExtra(EXTRA_COMPLETE, complete);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private ArrayList<String> getSelPaths() {
        ArrayList<String> paths = new ArrayList();
        for (ExPhoto exPhoto : mExPhotos) {
            if (exPhoto.isSelected()) {
                paths.add(exPhoto.getPath());
            }
        }
        return paths;
    }
}
