package com.xoptimal.photopicker.view.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xoptimal.photopicker.R;
import com.xoptimal.photopicker.components.TopNavigationBar;
import com.xoptimal.photopicker.view.frag.ExPhotoPickerFragment;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_GRID_COLUMN;
import static me.iwf.photopicker.PhotoPicker.EXTRA_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_CAMERA;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;


/**
 * Created by Freddie on 2016/12/28 0028 .
 * Description: 图片浏览界面
 */
public class PhotoPickerActivity extends ToolbarActivity implements View.OnClickListener {

    private ExPhotoPickerFragment pickerFragment;
    private int maxCount = DEFAULT_MAX_COUNT;
    private TextView         tv_preview;
    private Button           btn_done;
    private TopNavigationBar mTopNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_act_photo);
        initView();
        showFragment();
    }

    private void showFragment() {

        boolean           showCamera     = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        boolean           showGif        = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        boolean           previewEnabled = getIntent().getBooleanExtra(EXTRA_PREVIEW_ENABLED, true);
        int               columnNumber   = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);
        ArrayList<String> originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);

        pickerFragment = ExPhotoPickerFragment
                .newInstance(showCamera, showGif, previewEnabled, columnNumber, maxCount, isShowSingle, originalPhotos);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, pickerFragment, "tag").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private boolean isShowSingle;

    private void initView() {

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);

        isShowSingle = getIntent().getBooleanExtra(EXTRA_SINGLE, false);

        if (isShowSingle) {
            findViewById(R.id.rl_bottom).setVisibility(View.GONE);
        } else {
            tv_preview = (TextView) findViewById(R.id.tv_preview);
            tv_preview.setOnClickListener(this);
            btn_done = (Button) findViewById(R.id.btn_done);
            btn_done.setOnClickListener(this);
        }

        mTopNavigationBar = (TopNavigationBar) findViewById(R.id.top_bar);
        mTopNavigationBar.setImageResource(R.id.iv_left, R.drawable.x_icon_return)
                .setText(R.id.tv_left, getString(R.string.text_picture))
                .setText(R.id.tv_center, TextUtils.isEmpty(title) ? getString(R.string.text_picture_all) : title)
                .setText(R.id.tv_right, getString(R.string.text_cancel))
                .setOnClickListener(R.id.iv_left, this)
                .setOnClickListener(R.id.tv_left, this)
                .setOnClickListener(R.id.tv_right, this);
    }

    public void checkSelectState() {

        if (isShowSingle) return;

        int size = pickerFragment.getPhotoGridAdapter().getSelectedPhotos().size();
        tv_preview.setEnabled(size > 0);
        btn_done.setEnabled(size > 0);

        if (size > 0) {
            btn_done.setEnabled(true);
            btn_done.setText(String.format(getString(R.string.btn_done_count), size));
        } else {
            btn_done.setEnabled(false);
            btn_done.setText(getString(R.string.btn_done));
        }
    }

    public void submit() {
        Intent intent = new Intent();
        intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, pickerFragment.getSelectedPhotoPaths());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_preview) {
            PhotoExPreviewActivity.start(PhotoPickerActivity.this, maxCount, pickerFragment.getSelectedPhotoPaths());

        } else if (i == R.id.btn_done) {
            submit();

        } else if (i == R.id.tv_left || i == R.id.iv_left) {
            PhotoPickerListActivity.start(this);

        } else if (i == R.id.tv_right) {
            onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PhotoExPreviewActivity.REQUEST_CODE:
                    ArrayList<String> selPaths = data.getStringArrayListExtra(PhotoExPreviewActivity.EXTRA_PHOTOS_SEL);
                    if (data.getBooleanExtra(PhotoExPreviewActivity.EXTRA_COMPLETE, false)) {
                        submit();
                    } else {
                        pickerFragment.getPhotoGridAdapter().setSelectPath(selPaths);
                    }
                    break;

                case PhotoPickerListActivity.REQUEST_CODE:
                    mTopNavigationBar.setText(R.id.tv_center, data.getStringExtra(EXTRA_TITLE));
                    pickerFragment.setCurrentDirectoryIndex(data.getIntExtra(EXTRA_DIR_INDEX, 0));
                    break;
            }
        }
    }


    public static final String EXTRA_TITLE     = "EXTRA_TITLE";
    public static final String EXTRA_SINGLE    = "EXTRA_SINGLE";
    public static final String EXTRA_DIR_INDEX = "EXTRA_DIR_INDEX";


    public static void start(Context context, String title, int maxCount, ArrayList<String> originalPhotos, boolean isShowCamera, boolean isShowGif, boolean isShowPreview, boolean isShowSingle) {
        Intent intent = new Intent(context, PhotoPickerActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MAX_COUNT, maxCount);
        intent.putExtra(EXTRA_SINGLE, isShowSingle);
        intent.putExtra(EXTRA_ORIGINAL_PHOTOS, originalPhotos);
        intent.putExtra(EXTRA_SHOW_CAMERA, isShowCamera);
        intent.putExtra(EXTRA_SHOW_GIF, isShowGif);
        intent.putExtra(EXTRA_PREVIEW_ENABLED, isShowPreview);
        context.startActivity(intent);
    }
}
