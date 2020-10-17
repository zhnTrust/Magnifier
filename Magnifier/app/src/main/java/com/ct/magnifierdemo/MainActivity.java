package com.ct.magnifierdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.ct.ctmagnifier.MagnifierView1;
import com.ct.ctmagnifier.MagnifierView;
import com.ct.ctmagnifier.MagnifierView2;

public class MainActivity extends AppCompatActivity {
    private Switch switchBtn, switchBtn2;
    private RelativeLayout mRelativeLayout;
    private ImageView iv_center;
    private ImageView iv_bottom;

    private View mBtnReset;

    private MagnifierView mMagnifierView;
    private MagnifierView1 mMagnifierView1;
    private MagnifierView2 mMagnifierView2;

    private float mPreX = 0.0f, mPreY = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        mRelativeLayout = findViewById(R.id.rl);
        iv_bottom = findViewById(R.id.iv_bottom);
        iv_center = findViewById(R.id.iv_center);



        switchBtn = (Switch) findViewById(R.id.switchBtn);
        switchBtn2 = (Switch) findViewById(R.id.switchBtn2);
        mBtnReset = findViewById(R.id.btn);

//        mMagnifierView2 = new MagnifierView2.Builder(this)
//                .target(iv_center)
//                .scale(1.0f)
//                .build();
//        mMagnifierView2.attachToParent();

        mMagnifierView1 = new MagnifierView1.Builder(MainActivity.this)
                .rootVg(mRelativeLayout)
                .viewWH(320, 320)
                .scale(1.0f)
                .alpha(16)
                .color("#ff00ff")
                .build();


        mMagnifierView = new MagnifierView.Builder(MainActivity.this)
                .rootVg(mRelativeLayout)
                .scale(1.5f)    //原图
                .viewWH(200, 200)
                .build();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int measuredHeight = iv_center.getMeasuredHeight();
                int measuredWidth = iv_center.getMeasuredWidth();
                int width = iv_center.getWidth();
                int height = iv_center.getHeight();
                if (isChecked) {
                    mMagnifierView1.startViewToRoot();
                }
                else {
                    mMagnifierView1.closeViewToRoot();
                }
            }
        });


        switchBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMagnifierView.startViewToRoot();
                }
                else {
                    mMagnifierView.closeViewToRoot();
                }
            }
        });


        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMagnifierView1.resetXY();
                mMagnifierView.resetXY();
            }
        });

//        mRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//
//                        //重置
////                        myMagnifier.reFresh(event.getX() - mPreX, event.getY() - mPreY);
////
////                        //记录
////                        mPreX = event.getX();
////                        mPreY = event.getY();
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:   //随手移动，getRawX()与getX()有区别
//                        //移动
//                        mMagnifierView2.reFresh(event.getX() - mPreY, event.getY() - mPreY);
//
//                        //记录
//                        mPreX = event.getX();
//                        mPreY = event.getY();
//                        break;
//                }
//                return false;
//            }
//        });

        //mRelativeLayout.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //开启缓存并创建
        //        mRelativeLayout.setDrawingCacheEnabled(true);
        //        mRelativeLayout.buildDrawingCache();
        //        Bitmap viewShot = mRelativeLayout.getDrawingCache();
        //        iv_bottom.setImageDrawable(new BitmapDrawable(viewShot));
        //        iv_bottom.invalidate();
        //        //释放缓存并关闭
        //        mRelativeLayout.destroyDrawingCache();
        //        mRelativeLayout.setDrawingCacheEnabled(false);
        //
        //
        //    }
        //});
    }
}
