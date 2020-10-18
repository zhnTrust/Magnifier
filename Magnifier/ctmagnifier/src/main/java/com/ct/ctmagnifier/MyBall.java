package com.ct.ctmagnifier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class MyBall extends View {

    private int mSize;
    private String mColor;
    private int mAlpha;
    private float mScale;


    private ViewGroup mParent;
    private View mTarget;
    private float mDownX;
    private float mDownY;
    private float mPreLocationX;
    private float mPreLocationY;
    private Paint mPaintFrame;
    private Paint mPaintBg;
    private Paint mPaintPic;
    private Paint mPaintShader;
    private BitmapShader mTargetBitmapShader;
    private Matrix mMatrix;

    private int mSingleRlRule;

    private float centerX, centerY;
    private Builder mBuilder;


    public MyBall(Context context, Builder builder) {
        super(context);
        mBuilder = builder;

        //build变量
        this.mSize = builder.size;
        this.mColor = builder.color;
        this.mAlpha = builder.alpha;
        this.mScale = builder.scale;
        this.mParent = builder.parent;
        if (mParent == null) {
            mParent = ((Activity) context).findViewById(android.R.id.content);
        }
        this.mSingleRlRule = builder.singleRlRule;
        this.mTarget = builder.target;
        this.centerX = builder.centerX;
        this.centerY = builder.centerY;


        //初始化变量
        mPaintFrame = new Paint();
        mPaintBg = new Paint();
        mPaintPic = new Paint();
        mPaintShader = new Paint();
        mMatrix = new Matrix();

    }

    public MyBall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //图层0 矩形边框层
        mPaintFrame.setColor(Color.parseColor("#ff0000"));  //red
        mPaintFrame.setAlpha(50);
        canvas.drawRect(0, 0, mSize * 2, mSize, mPaintFrame);

        //图层1 底色(防透明)
        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(Color.parseColor("#00ff00")); //green
        mPaintBg.setAlpha(50);
        canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2, mPaintBg);

        //图层2 图像层
        if (mTargetBitmapShader != null) {
            mMatrix.setScale(mScale, mScale);
            mMatrix.postTranslate(-(centerX * mScale - mSize / 2), -(centerY * mScale - mSize / 2));
            mTargetBitmapShader.setLocalMatrix(mMatrix);
            mPaintPic.setAntiAlias(true);
            mPaintPic.setShader(mTargetBitmapShader);
            canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2, mPaintPic);
        }

        //图层3 (滤镜层)
        //mPaintShader.setAntiAlias(true);
        //mPaintShader.setColor(Color.parseColor("#ff0000")); //red
        //mPaintShader.setAlpha(32);
        //canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2, mPaintShader);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPreLocationX = getX();
                mPreLocationY = getY();
                mDownX = event.getRawX();
                mDownY = event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float curX = event.getRawX();
                float curY = event.getRawY();
                //移动
                setX(mPreLocationX + (curX - mDownX));
                setY(mPreLocationY + (curY - mDownY));
                invalidate();
                break;
            }
            default: {
                break;
            }
        }
        //return super.onTouchEvent(event);
        return true;
    }

    public void attachToParent() {
        if (mParent != null && getParent() == null) {
            //添加到view树
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mSize, mSize);
            layoutParams.addRule(mSingleRlRule);
            this.setLayoutParams(layoutParams);
            mParent.addView(this);
            //获取view的截图
            ViewTreeObserver viewTreeObserver = mParent.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Bitmap targetShot = getViewShot(mTarget);
                    if (targetShot != null) {
                        mTargetBitmapShader = new BitmapShader(targetShot, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    }
                }
            });


        }
    }

    public void detachFromParent() {
        if (mParent != null && this.getParent() != null) {
            mParent.removeView(this);
        }
    }


    public void refresh(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        invalidate();
    }

    public void restore() {
        this.centerX = mBuilder.centerX;
        this.centerY = mBuilder.centerY;
        invalidate();
    }

    private Bitmap getViewShot(final View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap shot = view.getDrawingCache();
        //view.getContentDescription();
        //view.setDrawingCacheEnabled(false);
        return shot;
    }

    public static class Builder {
        private Context context;
        private ViewGroup parent;
        private View target;
        private int size = 300;
        private String color = "#ff0000";
        private float scale = 1.0f;
        private int alpha = 32;
        private int singleRlRule = RelativeLayout.ALIGN_PARENT_START;

        private float centerX = 0, centerY = 0;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder parent(ViewGroup parent) {
            this.parent = parent;
            return this;
        }

        public Builder target(View target) {
            this.target = target;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder alpha(int alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder scale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder singleRlRule(int rule) {
            this.singleRlRule = rule;
            return this;
        }

        public Builder center(float centerX, float centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
            return this;
        }

        public MyBall build() {
            return new MyBall(context, this);
        }
    }

}
