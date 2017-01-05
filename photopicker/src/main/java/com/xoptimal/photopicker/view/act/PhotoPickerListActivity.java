package com.xoptimal.photopicker.view.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xoptimal.photopicker.R;
import com.xoptimal.photopicker.adapter.ExPhotoListAdapter;
import com.xoptimal.photopicker.components.TopNavigationBar;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * Created by Freddie on 2016/12/28 0028 .
 * Description: 图片集界面
 */
public class PhotoPickerListActivity extends ToolbarActivity {

    public final static int REQUEST_CODE = 10;

    private ListView             mListView;
    private List<PhotoDirectory> directories;
    private ExPhotoListAdapter   mAdapter;
    private TopNavigationBar     mTopNavigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mListView = new ListView(this));
        mListView.setDivider(null);
        mListView.setBackgroundResource(android.R.color.white);
        initTopBar();

        RequestManager mGlideRequestManager = Glide.with(this);
        directories = new ArrayList<>();
        Bundle  mediaStoreArgs = new Bundle();
        boolean showGif        = false;
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, showGif);
        MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        mAdapter.notifyDataSetChanged();
                    }
                });

        mListView.setAdapter(mAdapter = new ExPhotoListAdapter(mGlideRequestManager, directories));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                PhotoDirectory directory = directories.get(position);

                Intent intent = new Intent();
                intent.putExtra(PhotoPickerActivity.EXTRA_TITLE, directory.getName());
                intent.putExtra(PhotoPickerActivity.EXTRA_TITLE, directory.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initTopBar() {
        mTopNavigationBar = (TopNavigationBar) findViewById(R.id.top_bar);
        mTopNavigationBar.setText(R.id.tv_center, getString(R.string.text_picture))
                .setText(R.id.tv_right, getString(R.string.text_cancel))
                .setOnClickListener(R.id.tv_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PhotoPickerListActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }
}
