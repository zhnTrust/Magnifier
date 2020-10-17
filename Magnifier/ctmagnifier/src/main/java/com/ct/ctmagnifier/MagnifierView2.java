package com.ct.ctmagnifier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MagnifierView2 extends View {
    private float locationX = 0, locationY = 0;   //记录当前的原始位置。
    private float downX = 0, downY = 0;           //记录开始按下的位置。
    private Bitmap bm;                            //获得当前activity的截图或需要放大的图片
    private Activity activity;
    private ViewTreeObserver viewTreeObserver = null;
    private BitmapShader bitmapShader;
    private Paint mPaintBg;
    private Paint mPaint;
    private Paint mPaintShade;
    private Matrix mMatrix;

    //属性配置
    private int mLeft, mTop;               //初始位置，相对于父控件的位置
    private int mSize, mWidth, mHeight;                    //控件宽高
    private float scaleX, scaleY;                //x,y的放大倍数
    private String mColor;               //放大镜颜色
    private int mAlpha;                  //放大镜透明度
    private ViewGroup mParent;
    private View mTarget;


    private float mTranslateX = 0.0f, mTranslateY = 0.0f;


    public MagnifierView2(MagnifierView2.Builder builder, Context context) {//对象初始化一次就行了
        super(context);
        activity = (Activity) context;
        this.mParent = builder.parent;
        if (mParent == null) {
            mParent = (ViewGroup) (activity.findViewById(android.R.id.content));
        }
        this.mTarget = builder.target;
        if (mTarget == null) {
            mTarget = mParent;
        }
        this.mSize = builder.size;
        this.mWidth = builder.size;
        this.mHeight = builder.size;
        this.scaleX = builder.scaleX;
        this.scaleY = builder.scaleY;
        this.mColor = builder.color;
        this.mAlpha = builder.alpha;
        this.mLeft = builder.left;
        this.mTop = builder.top;

        mPaintBg = new Paint();
        mPaint = new Paint();
        mPaintShade = new Paint();
        mMatrix = new Matrix();


        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mWidth, mHeight);
        this.setLayoutParams(lp);


    }

    public MagnifierView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagnifierView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 把当前view设置到根布局中
     */
    public void attachToParent() {
        if (activity != null && mParent != null && this.getParent() == null)//getParent()防止当前控件没有加入别的父
        {
            mParent.addView(this);
            //view加载完成调用,防止直接在activity create方法里调用无法使用,因为create里，绘制还没有完成
            viewTreeObserver = mParent.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (bm == null) {
                        //初始化控件位置(右上角)
                        View parent = (View) getParent();
                        parent.getMeasuredWidth();
                        MagnifierView2.this.setX(parent.getMeasuredWidth() - mSize);
                        MagnifierView2.this.setY(0);
                        bm = getScreenBm(mTarget);//获得指定布局的截图
                        bitmapShader = new BitmapShader(bm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);//利用BitmapShader画圆,模式可以查询用法
                        invalidate();
                    }
                }
            });
        }
    }

    /**
     * 移除放大镜
     */
    public void detachFromParent() {
        if (mParent != null && this.getParent() != null) {
            mParent.removeView(this);
        }
    }

    /**
     * 放大镜位置复位
     */
    public void resetXY() {
        this.setX(mLeft);
        this.setY(mTop);
        invalidate();
    }

    /**
     * 创建建造者，用于构建当前对象。多用于复杂构建
     */
    public static class Builder {
        private Context context;
        private int left = 0, top = 0;                  //初始坐标(左上角相对于父控件的位置)
        private int size = 300, width = 300, height = 300;                         //控件宽高(默认300像素)
        private float scaleX = 0.0f, scaleY = 0.0f;     //x,y的放大倍数(默认不放大)
        private String color = "#ff0000";      //放大镜颜色(默认红色)
        private int alpha = 50;                //放大镜透明度(默认半透明)

        private ViewGroup parent;   //父控件
        private View target;        //放大目标控件

        public Builder(Context context) {
            this.context = context;
        }

        public MagnifierView2.Builder coordinate(int left, int top) //坐标
        {
            if (left > 0)
                this.left = left;
            if (top > 0)
                this.top = top;
            return this;
        }

        public Builder size(int size) //宽高
        {
            this.size = size;
            this.width = size;
            this.height = size;
            return this;
        }

        public Builder parent(ViewGroup parent) //父容器
        {
            this.parent = parent;
            return this;
        }

        public Builder scale(float scale) //放大镜放大倍数
        {
            this.scaleX = scale;
            this.scaleY = scale;
            return this;
        }

        public Builder color(String color) //颜色
        {
            this.color = color;
            return this;
        }

        public Builder alpha(int alpha) //透明度
        {
            if (alpha >= 200) {
                this.alpha = 200;
            }
            else if (alpha < 0) {
                this.alpha = 0;
            }
            else {
                this.alpha = alpha;
            }
            return this;
        }

        public Builder target(View target) //放大目标
        {
            this.target = target;
            return this;
        }

        public MagnifierView2 build() {
            return new MagnifierView2(this, context);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bm != null) {

            mPaint.setAntiAlias(true);           //抗锯齿
            mPaint.setShader(bitmapShader);      //bitmapShader画圆形图片

            //创建矩阵，缩放平移图片
            mMatrix.setScale(scaleX, scaleY);
            //为了放大效果是取放大镜中心开始放大的效果

            //负号表示相反反向移动(如手指右移，则图片需左移)
            mMatrix.postTranslate(-mTranslateX, -mTranslateY);
//            mMatrix.postTranslate(-(mTranslateX -mSize / 2), -(mTranslateY - mSize / 2));
            bitmapShader.setLocalMatrix(mMatrix);                    //利用bitmapShader画圆形图片
            canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2, mPaint);

        }
    }

    public void reFresh(float x, float y) {
        mTranslateX = x;
        mTranslateY = y;
        invalidate();
    }


    private Bitmap getScreenBm(View contentView) {
        Bitmap bm;
        contentView.setDrawingCacheEnabled(true);
        contentView.buildDrawingCache();
        bm = contentView.getDrawingCache();//指向的当前view的显示对象的缓存bm，如果view里控件增加等改变，bm相应改变
        //contentView.setDrawingCacheEnabled(false);
        //contentView.destroyDrawingCache();//释放缓存
        return bm;
    }
}
