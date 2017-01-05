package com.xoptimal.photopicker.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xoptimal.photopicker.R;


/**
 * Created by Freddie on 2016/12/28 0028 .
 * Description:
 */
public class TopNavigationBar extends RelativeLayout {

    public TopNavigationBar(Context context) {
        this(context, null);
    }

    public TopNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.x_view_navigation_top, this);
    }

    private SparseArray<View> childViews = new SparseArray<>();

    public <T extends View> T findById(int id) {
        View childView = childViews.get(id);
        if (childView == null) {
            childView = findViewById(id);
            if (childView != null)
                childViews.put(id, childView);
        }
        if (childView.getVisibility() == View.GONE) {
            childView.setVisibility(VISIBLE);
        }
        return (T) childView;
    }

    public TopNavigationBar setImageResource(int id, int resId) {
        ImageView imageView = findById(id);
        imageView.setImageResource(resId);
        return this;
    }

    public TopNavigationBar setBackgroundResource(int id, int resId) {
        ImageView imageView = findById(id);
        imageView.setBackgroundResource(resId);
        return this;
    }

    public TopNavigationBar setTextColor(int id, int color) {
        TextView textView = findById(id);
        textView.setTextColor(color);
        return this;
    }

    public TopNavigationBar setText(int id, String text) {
        TextView textView = findById(id);
        textView.setText(text);
        return this;
    }

    public TopNavigationBar setOnClickListener(int id, OnClickListener listener) {
        findById(id).setOnClickListener(listener);
        return this;
    }
}
