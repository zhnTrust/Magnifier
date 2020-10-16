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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MyMagnifier extends View {
    private float locationX = 0, locationY = 0;   //记录当前的原始位置。
    private float downX = 0, downY = 0;           //记录开始按下的位置。
    private Bitmap bm;                            //获得当前activity的截图或需要放大的图片
    private Activity activity;
    private ViewTreeObserver viewTreeObserver = null;
    private BitmapShader bitmapShader;
    private ViewGroup mParent;
    private View mTarget;
    //属性配置
    private int initLeft, initTop;               //初始位置，相对于父控件的位置
    private int viewW, viewH;                    //控件宽高
    private float scaleX, scaleY;                //x,y的放大倍数
    private String magnifierColor;               //放大镜颜色
    private int magnifierAlpha;                  //放大镜透明度
    private float magnifierLen;                  //放大镜正方形边长
    private Paint mPaintBg;
    private Paint mPaint;
    private Paint mPaintShade;
    private Matrix mMatrix;

    private int mCenterX, mCenterY;

    public MyMagnifier(MyMagnifier.Builder builder, Context context) {//对象初始化一次就行了
        super(context);
        activity = (Activity) context;
        this.mParent = builder.rootVg;
        if (mParent == null) {
            mParent = (ViewGroup) (activity.findViewById(android.R.id.content));
        }
        ;
        this.mTarget = builder.target;
        if (mTarget == null) {
            mTarget = mParent;
        }
        this.viewH = builder.viewH;
        this.viewW = builder.viewW;
        this.scaleX = builder.scaleX;
        this.scaleY = builder.scaleY;
        this.magnifierColor = builder.magnifierColor;
        this.magnifierAlpha = builder.magnifierAlpha;
        this.initLeft = builder.initLeft;
        this.initTop = builder.initTop;

        mPaintBg = new Paint();
        mPaint = new Paint();
        mPaintShade = new Paint();
        mMatrix = new Matrix();


        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(viewW, viewW);
        this.setLayoutParams(lp);

        magnifierLen = viewH > viewW ? viewW : viewH;
    }

    public MyMagnifier(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMagnifier(Context context, AttributeSet attrs, int defStyleAttr) {
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
                        MyMagnifier.this.setX(parent.getMeasuredWidth() - magnifierLen);
                        MyMagnifier.this.setY(0);
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
        this.setX(initLeft);
        this.setY(initTop);
        invalidate();
    }

    /**
     * 创建建造者，用于构建当前对象。多用于复杂构建
     */
    public static class Builder {
        private Context context;
        private int initLeft = 0, initTop = 0;          //初始位置，相对于父控件的位置
        private int viewW = 300, viewH = 300;           //控件宽高
        private float scaleX = 1.5f, scaleY = 1.5f;     //x,y的放大倍数
        private String magnifierColor = "#ff0000";      //放大镜颜色
        private int magnifierAlpha = 50;                //放大镜透明度

        private ViewGroup rootVg;
        private View target;

        public Builder(Context context) {
            this.context = context;
        }

        public MyMagnifier.Builder intiLT(int initLeft, int initTop) {
            if (initLeft > 0)
                this.initLeft = initLeft;
            if (initTop > 0)
                this.initTop = initTop;
            return this;
        }

        public Builder viewWH(int viewW, int viewH) {
            this.viewW = viewW;
            this.viewH = viewH;
            return this;
        }

        public Builder rootVg(ViewGroup rootVg) {
            this.rootVg = rootVg;
            return this;
        }

        public Builder scale(float scale)       //放大镜放大倍数
        {
            this.scaleX = scale;
            this.scaleY = scale;
            return this;
        }

        public Builder color(String color) {
            this.magnifierColor = color;
            return this;
        }

        public Builder alpha(int alpha) {
            if (alpha >= 200) {
                this.magnifierAlpha = 200;
            } else if (alpha < 0) {
                this.magnifierAlpha = 0;
            } else {
                this.magnifierAlpha = alpha;
            }
            return this;
        }

        public Builder target(View target) {
            this.target = target;
            return this;
        }

        public MyMagnifier build() {
            return new MyMagnifier(this, context);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bm != null) {
            //背景防止加载自带透明的图片时，放大图片后面能看到原来的图片

            mPaintBg.setAntiAlias(true);         //抗锯齿
            mPaintBg.setColor(Color.parseColor("#ff0000"));
            //canvas.drawCircle(magnifierLen / 2, magnifierLen / 2, magnifierLen / 2, mPaintBg);

            mPaint.setAntiAlias(true);           //抗锯齿
            mPaint.setShader(bitmapShader);      //bitmapShader画圆形图片

            //创建矩阵，缩放平移图片
            //mMatrix.setScale(scaleX, scaleY);
            //为了放大效果是取放大镜中心开始放大的效果
            //mMatrix.postTranslate(
            //        -(scaleX * getX() + (scaleX - 1) * magnifierLen / 2),
            //        -(scaleY * getY() + (scaleY - 1) * magnifierLen / 2));
            mMatrix.postTranslate(-mTarget.getMeasuredWidth() / 2, 0);
            bitmapShader.setLocalMatrix(mMatrix);                    //利用bitmapShader画圆形图片
            canvas.drawCircle(magnifierLen / 2, magnifierLen / 2, magnifierLen / 2, mPaint);

            //外层遮罩
            mPaintShade.setAntiAlias(true);                          //抗锯齿
            mPaintShade.setColor(Color.parseColor(magnifierColor));  //设置边框
            mPaintShade.setAlpha(magnifierAlpha);
            //canvas.drawCircle(magnifierLen / 2, magnifierLen / 2, magnifierLen / 2, paintShade);
        }
    }

    public void reFresh(int x, int y) {

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
