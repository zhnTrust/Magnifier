package com.ct.ctmagnifier;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class tdxBottomScrollToolBar extends HorizontalScrollView implements View.OnClickListener {

    private Context mContext;
    private ViewGroup mParent;
    private int mWidth, mHeight;            //大小宽高
    private int mPositionX, mPositionY;     //相对于父布局的初始位置
    private int mMarginBottom;
    private int mChildCount;                //子view的数量
    private LinearLayout mHost;

    public tdxBottomScrollToolBar(Context context, Builder builder) {

        super(context);
        this.mContext = context;
        this.mParent = builder.parent;
        this.mWidth = builder.width;
        this.mHeight = builder.height;
        this.mChildCount = builder.childCount;
        this.mMarginBottom = builder.marginBottom;

        initView();
    }

    private void initView() {

        RelativeLayout.LayoutParams layoutparamScrollView = new RelativeLayout.LayoutParams(mWidth, mHeight);
        layoutparamScrollView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutparamScrollView.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutparamScrollView.bottomMargin = mMarginBottom;
        this.setLayoutParams(layoutparamScrollView);
        this.setBackgroundColor(0x20ff0000);


        mHost = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParamsHost = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParamsHost.gravity = Gravity.CENTER;
        mHost.setLayoutParams(layoutParamsHost);
        for (int i = 0; i < mChildCount; i++) {
            View child = new View(mContext);
            LinearLayout.LayoutParams layoutParamsChild = new LinearLayout.LayoutParams(
                    80, 80);
            layoutParamsChild.gravity = Gravity.CENTER_VERTICAL;
            layoutParamsChild.setMargins(10, 0, 10, 0);
            child.setLayoutParams(layoutParamsChild);
            child.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_hz));
            child.setOnClickListener(this);
            mHost.addView(child);
        }
        this.addView(mHost);

    }

    public tdxBottomScrollToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public tdxBottomScrollToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void attachToParent() {
        if (mParent != null && this.getParent() == null) {
            mParent.addView(this);
        }
    }

    @Override
    public void onClick(View v) {
        int index = mHost.indexOfChild(v);
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
        Toast.makeText(mContext, "底部按钮" + index, Toast.LENGTH_SHORT).show();
    }


    public static class Builder {
        private Context context;
        private ViewGroup parent;
        private int width = 500, height = 200;
        private int childCount = 6;
        private int marginBottom = 20;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder parent(ViewGroup parent) {
            this.parent = parent;
            return this;

        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder childCount(int cout) {
            this.childCount = cout;
            return this;
        }

        public Builder marginBottom(int marginBottom) {
            this.marginBottom = marginBottom;
            return this;
        }

        public tdxBottomScrollToolBar build() {
            return new tdxBottomScrollToolBar(context, this);
        }
    }


}
