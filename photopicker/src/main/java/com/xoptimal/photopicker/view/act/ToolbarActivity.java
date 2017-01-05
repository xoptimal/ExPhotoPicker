package com.xoptimal.photopicker.view.act;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xoptimal.photopicker.R;


/**
 * Created by Freddie on 2016/12/28 0028 .
 * Description: 具备TopBar导航界面
 */
public class ToolbarActivity extends AppCompatActivity {

    private LinearLayout ll_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.x_act_toolbar);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
    }


    /* 重写 setContentView */

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getLayoutInflater().inflate(layoutResID, ll_root);
    }

    @Override
    public void setContentView(View view) {
        ll_root.addView(view, new LinearLayout.LayoutParams(-1, -1));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        ll_root.addView(view, params);
    }
}
