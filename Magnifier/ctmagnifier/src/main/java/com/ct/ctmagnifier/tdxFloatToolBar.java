package com.ct.ctmagnifier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class tdxFloatToolBar extends ViewGroup {

    private static final String TAG = "D==>FloatTools2";

    private Context mContext;
    private int mPostionX, mPostionY;
    private int mWidth, mHeight;
    private int mChildWidth, mChildHeight;
    private int mVSpace;
    private float mPreEventRawX, mPreEventRawY;    //上次触摸点的绝对坐标
    private float mPrePositionX, mPrePositionY;    //上次触摸时自己的相对坐标
    private View.OnClickListener mOnClickListener;
    private View.OnTouchListener mChildOnTouchListener;
    private boolean mTouchResult = true;

    public tdxFloatToolBar(Context context, Builder builder) {
        super(context);

//        this.mWidth = builder.width;
//        this.mHeight = builder.height;
        this.mContext = context;
        this.mVSpace = builder.vSpace;


        mChildHeight = builder.width;
        mChildWidth = builder.height;
        mWidth = mChildWidth;
        mHeight = mChildHeight * 3 + mVSpace * 2;

        this.setBackgroundColor(0x20ff0000);
        for (int i = 0; i < 3; i++) {
            View child = new View(context);
            child.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_hz));
            this.addView(child);
        }
    }


    private int locatePosition(float x, float y) {
        int index = -1;
        if (0 <= y && y <= mChildHeight) {
            index = 0;
        }

        else if (mChildHeight + mVSpace <= y && y <= mChildHeight * 2 + mVSpace) {
            index = 1;
        }
        else if ((mChildHeight + mVSpace) * 2 <= y && y <= mHeight) {
            index = 2;
        }

        return index;

    }

    private void handleClick(int index) {
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        Toast.makeText(mContext, "浮动按钮" + index, Toast.LENGTH_SHORT).show();
    }

    public tdxFloatToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public tdxFloatToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
//            this.measureChild(child,widthMeasureSpec,heightMeasureSpec);
            LayoutParams cLayoutParams = (LayoutParams) child.getLayoutParams();
            cLayoutParams.left = 0;
            cLayoutParams.top = (mChildHeight + mVSpace) * i;
        }
        this.setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            child.layout(
                    layoutParams.left,
                    layoutParams.top,
                    layoutParams.left + mChildWidth,
                    layoutParams.top + mChildHeight
            );

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchResult = false;
                mPrePositionX = this.getX();
                mPrePositionY = this.getY();
                mPreEventRawX = event.getRawX();
                mPreEventRawY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = event.getRawX() - mPreEventRawX;
                float offsetY = event.getRawY() - mPreEventRawY;

                int newPosX = (int) (mPrePositionX + offsetX);
                int newPosY = (int) (mPrePositionY + offsetX);
                //会重新测量、布局、绘制
                this.setX(mPrePositionX + offsetX);
                this.setY(mPrePositionY + offsetY);
//                this.layout(newPosX, newPosY, newPosX + mWidth, newPosY + mHeight);
//                this.requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                float curX = event.getRawX();
                float curY = event.getRawY();
                //防止点击时候稍微有点移动就被消费了
                if (Math.abs(curX - mPreEventRawX) < 5 || Math.abs(curY - mPrePositionY) < 5) {
                    //todo 点击事件
                    mTouchResult = true;
                    handleClick(locatePosition(event.getX(), event.getY()));
                }
                break;
        }
        return true;
    }


    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int left = 0;
        public int top = 0;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public static class Builder {
        private Context context;
        private int width = 150, height = 150;
        private int vSpace = 20;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder vSpace(int vSpace) {
            this.vSpace = vSpace;
            return this;
        }

        public tdxFloatToolBar build() {
            return new tdxFloatToolBar(context, this);
        }

    }
}
