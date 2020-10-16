package com.ct.magnifierdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.ct.ctmagnifier.FixedMagnifierView;
import com.ct.ctmagnifier.MagnifierView;
import com.ct.ctmagnifier.MyMagnifier;

public class MainActivity extends AppCompatActivity {
    private Switch switchBtn, switchBtn2;
    private RelativeLayout rl;
    private ImageView iv_center;
    private ImageView iv_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (RelativeLayout) findViewById(R.id.rl);
        iv_bottom = findViewById(R.id.iv_bottom);
        iv_center = findViewById(R.id.iv_center);

        new MyMagnifier.Builder(this).target(iv_center).build().attachToParent();


        switchBtn = (Switch) findViewById(R.id.switchBtn);
        final FixedMagnifierView mv = new FixedMagnifierView.Builder(MainActivity.this)
                .intiLT(100, 200)
                .viewWH(320, 320)
                .scale(2f)
                .alpha(16)
                .color("#ff00ff")
                .build();
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mv.startViewToRoot();
                } else {
                    mv.closeViewToRoot();
                }
            }
        });

        switchBtn2 = (Switch) findViewById(R.id.switchBtn2);
        final MagnifierView mv2 = new MagnifierView.Builder(MainActivity.this)
                .rootVg(rl)
                .viewWH(200, 200)
                .build();
        switchBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mv2.startViewToRoot();
                } else {
                    mv2.closeViewToRoot();
                }
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.resetXY();
                mv2.resetXY();
            }
        });

        //rl.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //开启缓存并创建
        //        rl.setDrawingCacheEnabled(true);
        //        rl.buildDrawingCache();
        //        Bitmap viewShot = rl.getDrawingCache();
        //        iv_bottom.setImageDrawable(new BitmapDrawable(viewShot));
        //        iv_bottom.invalidate();
        //        //释放缓存并关闭
        //        rl.destroyDrawingCache();
        //        rl.setDrawingCacheEnabled(false);
        //
        //
        //    }
        //});


    }
}
