package com.ct.magnifierdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.ct.ctmagnifier.FloatTools;
import com.ct.ctmagnifier.tdxFloatToolBar;
import com.ct.ctmagnifier.MagnifierView;
import com.ct.ctmagnifier.tdxMagnifier;
import com.ct.ctmagnifier.tdxBottomScrollToolBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG==>MainActivity";
    private Switch switchBtn1, switchBtn2, switchBtn3;
    private LinearLayout ll_contents;
    private RelativeLayout rl_image;

    private Button mBtnReset;

    private MagnifierView mMagnifierView;
    //private MagnifierView1 mMagnifierView1;
    //private MagnifierView2 mMagnifierView2;

    private FloatTools mFloatTools;

    private tdxMagnifier tdxMagnifier1;
    private tdxMagnifier tdxMagnifier2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        RelativeLayout contentView = findViewById(R.id.activity_main);
        ll_contents = findViewById(R.id.ll_contents);
        rl_image = findViewById(R.id.rl_image);

        switchBtn1 = findViewById(R.id.switchBtn1);
        switchBtn2 = findViewById(R.id.switchBtn2);
        switchBtn3 = findViewById(R.id.switchBtn3);
        mBtnReset = findViewById(R.id.btn_restore);

//        mFloatTools = new FloatTools.Builder(this)
//                .setParent(contentView)
//                .setSize(400, 800)
//                .setPostion(200, 400)
//                .build();
//        mFloatTools.attachToViewTree();

        tdxFloatToolBar tdxFloatToolBar = new tdxFloatToolBar.Builder(this)
                .size(150, 150)
                .vSpace(10)
                .build();
        contentView.addView(tdxFloatToolBar);


        //mMagnifierView1 = new MagnifierView1.Builder(MainActivity.this)
        //        //.rootVg(mRelativeLayout)
        //        .viewWH(320, 320)
        //        .scale(1.0f)
        //        .alpha(16)
        //        .color("#ff00ff")
        //        .build();
        //
        //
        mMagnifierView = new MagnifierView.Builder(MainActivity.this)
                .rootVg(ll_contents)
                .scale(1.5f)    //原图@
                .viewWH(200, 200)
                .build();

        tdxMagnifier1 = new tdxMagnifier.Builder(this)
                .size(280)
                .scale(1.5f)
                .parent(contentView)
                .singleRlRule(RelativeLayout.CENTER_HORIZONTAL)
                .target(rl_image)
                .center(140, 140)
                .build();
        tdxMagnifier2 = new tdxMagnifier.Builder(this)
                .size(300)
                .scale(1.5f)
                .parent(contentView)
                .singleRlRule(RelativeLayout.ALIGN_PARENT_END)
                .center(83, 25)
                .target(ll_contents)
                .build();
        tdxMagnifier1.attachToParent();
        tdxMagnifier2.attachToParent();
//        mMagnifierView.startViewToRoot();

        tdxBottomScrollToolBar tdxBottomScrollToolBar = new tdxBottomScrollToolBar.Builder(this)
                .parent(contentView)
                .size(900,150)
                .childCount(10)
                .build();
        tdxBottomScrollToolBar.attachToParent();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        switchBtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tdxMagnifier1.attachToParent();
                }
                else {
                    tdxMagnifier1.detachFromParent();
                }
            }
        });


        switchBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tdxMagnifier2.attachToParent();
                }
                else {
                    tdxMagnifier2.detachFromParent();
                }
            }
        });
        switchBtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                if (tdxMagnifier1 != null && tdxMagnifier1.getParent() != null) {
                    tdxMagnifier1.restore();
                }
                if (tdxMagnifier2 != null && tdxMagnifier2.getParent() != null) {
                    tdxMagnifier2.restore();
                }
            }
        });

        rl_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE: {
                        tdxMagnifier1.refresh(event.getX(), event.getY());
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return false;
            }
        });
        rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick(): ");
            }
        });

        ll_contents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        tdxMagnifier2.refresh(event.getX(), event.getY());
                        break;
                }
                return false;
            }
        });
        ll_contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick(): click");
            }
        });
    }

}
