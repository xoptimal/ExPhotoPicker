package com.xoptimal.exphotopicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xoptimal.photopicker.ExPhotoPicker;
import com.xoptimal.photopicker.ExPhotoPreView;
import com.xoptimal.photopicker.components.TopNavigationBar;
import com.xoptimal.photopicker.view.act.ToolbarActivity;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends ToolbarActivity {

    private ImageView iv_icon;
    private TextView  tv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTopBar();
        initView();
    }

    private TopNavigationBar top_bar;

    private void initTopBar() {
        top_bar = (TopNavigationBar) findViewById(R.id.top_bar);
        top_bar.setText(R.id.tv_left, "图片选择器");
    }

    private void initView() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_hint.setText("请选择图片!");
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoPaths.size() > 0) {
                    new ExPhotoPreView.Builder()
                            .setPhotos(mPhotoPaths)
                            .build().start(MainActivity.this);
                }
            }
        });
    }

    private ArrayList<String> mPhotoPaths = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                mPhotoPaths.clear();
                mPhotoPaths.addAll(photos);
                if (mPhotoPaths != null && mPhotoPaths.size() > 0)
                    showPhoto(mPhotoPaths.get(0));
            }
        }
    }

    private void showPhoto(String photoPath) {
        Glide.with(this).load(new File(photoPath)).into(iv_icon);
        tv_hint.setText("点击图片预览");
    }

    public void querySingle(View view) {
        new ExPhotoPicker.Builder()
                .showSingleModel(true)
                .build().start(this, 10);
    }

    public void queryMore(View view) {
        new ExPhotoPicker.Builder()
                .setMaxCount(3)
                .showPreview(true)
                .build().start(this, 10);
    }
}
