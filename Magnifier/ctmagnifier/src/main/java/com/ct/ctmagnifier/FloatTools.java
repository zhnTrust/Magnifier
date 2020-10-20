package com.ct.ctmagnifier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatTools extends LinearLayout {
    private static final String TAG = "DEBUG==>FloatTools";

    private ViewGroup mParent;
    private int mWidth, mHeight;
    private float mPositonX, mPositonY;
    private Paint paintBg;
    private float mDownRawX;
    private float mDownRowY;

    private float initPositionX, initPositionY;


    public FloatTools(Context context, Builder builder) {
        super(context);
        this.mParent = builder.parent;
        this.mWidth = builder.width;
        this.mHeight = builder.height;
        this.mPositonX = builder.positionX;
        this.mPositonY = builder.positionY;

        //自身宽高，位置
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mWidth, mWidth);
        this.setX(mPositonX);
        this.setY(mPositonY);
        this.setLayoutParams(layoutParams);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.parseColor("#ff0000"));
        //添加子按钮
        TextView tv1 = new TextView(context);
        LinearLayout.LayoutParams lp_tv1 = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        tv1.setLayoutParams(lp_tv1);
        tv1.setText("按钮一");

        TextView tv2 = new TextView(context);
        LinearLayout.LayoutParams lp_tv2 = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tv2.setLayoutParams(lp_tv2);
        tv2.setText("按钮二");

        TextView tv3 = new TextView(context);
        LinearLayout.LayoutParams lp_tv3 = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tv3.setLayoutParams(lp_tv3);
        tv3.setText("按钮三");

        this.addView(tv1);
        this.addView(tv2);
        this.addView(tv3);

        tv1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: tv1");
            }
        });

//        tv2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: tv2");
//            }
//        });
//
//        tv3.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: tv3");
//            }
//        });

//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: this");
//            }
//        });


        paintBg = new Paint();
    }

    public FloatTools(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatTools(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

//                initPositionX = getX();
//                initPositionY = getY();
                mPositonX = getX();
                mPositonY = getY();
                mDownRawX = event.getRawX();
                mDownRowY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                float offsetX = rawX - mDownRawX;
                float offsetY = rawY - mDownRowY;
                setX(mPositonX + offsetX);
                setY(mPositonY + offsetY);


                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        paintBg.setColor(Color.parseColor("#ff0000"));
//        paintBg.setAlpha(32);
//        canvas.drawRect(0, 0, mWidth, mHeight, paintBg);

        super.onDraw(canvas);

    }

    public void attachToViewTree() {
//        if (mParent == null) {
//            throw new Exception("parent is null,call Builder.setParent() to set parent");
//        }
        if (mParent != null) {
            mParent.addView(this);
        }

    }

    public void detachFromViewTree() {
        if (mParent != null && this.getParent() != null) {
            mParent.removeView(this);
        }
    }


    public static class Builder {
        private Context context;
        private ViewGroup parent;
        private int width = 100, height = 400;
        private int positionX = 0, positionY = 0;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setParent(ViewGroup parent) {
            this.parent = parent;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setPostion(int x, int y) {
            this.positionX = x;
            this.positionY = y;
            return this;
        }

        public FloatTools build() {
            return new FloatTools(context, this);
        }

    }


}
