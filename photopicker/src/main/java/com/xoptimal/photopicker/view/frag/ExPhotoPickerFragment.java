package com.xoptimal.photopicker.view.frag;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xoptimal.photopicker.R;
import com.xoptimal.photopicker.adapter.ExPhotoGridAdapter;
import com.xoptimal.photopicker.utils.ToastUtils;
import com.xoptimal.photopicker.view.act.PhotoExPreviewActivity;
import com.xoptimal.photopicker.view.act.PhotoPickerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import me.iwf.photopicker.utils.ImageCaptureManager;
import me.iwf.photopicker.utils.MediaStoreHelper;
import me.iwf.photopicker.utils.PermissionsConstant;
import me.iwf.photopicker.utils.PermissionsUtils;

import static android.app.Activity.RESULT_OK;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;
import static me.iwf.photopicker.utils.MediaStoreHelper.INDEX_ALL_PHOTOS;

/**
 * Created by Freddie on 2016/12/28 0028 .
 * Description:
 */
public class ExPhotoPickerFragment extends Fragment {

    private ImageCaptureManager captureManager;
    private ExPhotoGridAdapter  photoGridAdapter;

    private PopupDirectoryListAdapter listAdapter;
    //所有photos的路径
    private List<PhotoDirectory>      directories;
    //传入的已选照片
    private ArrayList<String>         originalPhotos;

    private int SCROLL_THRESHOLD = 30;
    int column;
    //目录弹出框的一次最多显示的目录数目
    private final static String EXTRA_CAMERA = "camera";
    private final static String EXTRA_COLUMN = "column";
    private final static String EXTRA_COUNT  = "count";
    private final static String EXTRA_GIF    = "gif";
    private final static String EXTRA_ORIGIN = "origin";
    private final static String EXTRA_SINGLE = "single";
    private RequestManager mGlideRequestManager;
    private int            mMaxCount;

    public static ExPhotoPickerFragment newInstance(boolean showCamera, boolean showGif,
                                                    boolean previewEnable, int column, int maxCount, boolean showSingle, ArrayList<String> originalPhotos) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_CAMERA, showCamera);
        args.putBoolean(EXTRA_GIF, showGif);
        args.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnable);
        args.putBoolean(EXTRA_SINGLE, showSingle);
        args.putInt(EXTRA_COLUMN, column);
        args.putInt(EXTRA_COUNT, maxCount);
        args.putStringArrayList(EXTRA_ORIGIN, originalPhotos);
        ExPhotoPickerFragment fragment = new ExPhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean mShowSingle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mGlideRequestManager = Glide.with(this);

        directories = new ArrayList<>();
        originalPhotos = getArguments().getStringArrayList(EXTRA_ORIGIN);
        mShowSingle = getArguments().getBoolean(EXTRA_SINGLE, false);
        column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        boolean showCamera    = getArguments().getBoolean(EXTRA_CAMERA, true);
        boolean previewEnable = getArguments().getBoolean(EXTRA_PREVIEW_ENABLED, true);
        mMaxCount = getArguments().getInt(EXTRA_COUNT, 1);
        photoGridAdapter = new ExPhotoGridAdapter(getActivity(), mGlideRequestManager, directories, originalPhotos, column, mShowSingle);
        photoGridAdapter.setOnItemChangListener(mItemClickListener);
        photoGridAdapter.setShowCamera(showCamera);
        photoGridAdapter.setPreviewEnable(previewEnable);

        Bundle mediaStoreArgs = new Bundle();

        boolean showGif = getArguments().getBoolean(EXTRA_GIF);
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, showGif);
        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                    }
                });

        captureManager = new ImageCaptureManager(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listAdapter = new PopupDirectoryListAdapter(mGlideRequestManager, directories);
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setBackgroundResource(R.color.color_f1f1f1);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                int index = showCamera ? position - 1 : position;

                List<String> photos    = photoGridAdapter.getCurrentPhotoPaths();
                List<String> selPhotos = photoGridAdapter.getSelectedPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);

                PhotoExPreviewActivity.start(getActivity(), index, mMaxCount, new ArrayList<>(photos), new ArrayList<>(selPhotos));
            }
        });

        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo photo, final int selectedItemCount) {

                if (mMaxCount <= 1) {
                    List<String> photos = photoGridAdapter.getSelectedPhotos();
                    if (!photos.contains(photo.getPath())) {
                        photos.clear();
                        photoGridAdapter.notifyDataSetChanged();
                    }
                    return true;
                }
                if (selectedItemCount > mMaxCount) {
                    ToastUtils.makeText(getActivity(), getString(R.string.__picker_over_max_count_tips, mMaxCount));
                    return false;
                }
                return true;
            }
        });

        photoGridAdapter.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissionsUtils.checkCameraPermission(ExPhotoPickerFragment.this)) return;
                if (!PermissionsUtils.checkWriteStoragePermission(ExPhotoPickerFragment.this))
                    return;
                openCamera();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    resumeRequestsIfNotDestroyed();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resumeRequestsIfNotDestroyed();
                }
            }
        });
        return recyclerView;
    }

    private void openCamera() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentDirectoryIndex(int position) {
        photoGridAdapter.setCurrentDirectoryIndex(position);
        photoGridAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            if (captureManager == null) {
                FragmentActivity activity = getActivity();
                captureManager = new ImageCaptureManager(activity);
            }

            captureManager.galleryAddPic();
            if (directories.size() > 0) {
                String         path      = captureManager.getCurrentPhotoPath();
                PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
                directory.getPhotos().add(INDEX_ALL_PHOTOS, new Photo(path.hashCode(), path));
                directory.setCoverPath(path);
                photoGridAdapter.notifyDataSetChanged();

                if (mShowSingle) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(path);
                    submit(list);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PermissionsConstant.REQUEST_CAMERA:
                case PermissionsConstant.REQUEST_EXTERNAL_WRITE:
                    if (PermissionsUtils.checkWriteStoragePermission(this) &&
                            PermissionsUtils.checkCameraPermission(this)) {
                        openCamera();
                    }
                    break;
            }
        }
    }

    public ExPhotoGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        return photoGridAdapter.getSelectedPhotoPaths();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (directories == null) {
            return;
        }

        for (PhotoDirectory directory : directories) {
            directory.getPhotoPaths().clear();
            directory.getPhotos().clear();
            directory.setPhotos(null);
        }
        directories.clear();
        directories = null;
    }

    private void resumeRequestsIfNotDestroyed() {
        if (!AndroidLifecycleUtils.canLoadImage(this)) {
            return;
        }
        mGlideRequestManager.resumeRequests();
    }

    private void submit(ArrayList<String> list) {
        Intent intent = new Intent();
        intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, list);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onChangeItem() {
            ((PhotoPickerActivity) getActivity()).checkSelectState();
        }

        @Override
        public void onClickPhoto(String photoPath) {

            ArrayList<String> list = new ArrayList();
            list.add(photoPath);
            submit(list);
        }
    };


    public interface OnItemClickListener {
        void onChangeItem();

        void onClickPhoto(String photoPath);
    }

}
