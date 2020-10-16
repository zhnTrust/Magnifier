package com.example.digitalclock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

public class CustomTimeView extends TextView {

    private final CustomTimeView textView;
    private String time;
    private TimeHandler mTimeHandler = new TimeHandler();
    public CustomTimeView(Context context) {
        this(context, null);
    }

    public CustomTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.textView = this;
        init(context);
    }

    private void init(Context context) {
        try {
            //初始化textview显示时间
            updateClock();

            //更新进程开始
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mTimeHandler.startScheduleUpdate();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //更新Handler通过handler的延时发送消息来更新时间
    private class TimeHandler extends Handler {
        private boolean mStopped;

        private void post() {
            //每隔1秒发送一次消息
            sendMessageDelayed(obtainMessage(0), 1000);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!mStopped) {
                updateClock();
                //实现实时更新
                post();
            }
        }

        public void startScheduleUpdate() //开始更新
        {
            mStopped = false;
            post();
        }


        public void stopScheduleUpdate() //停止更新
        {
            mStopped = true;
            removeMessages(0);
        }

    }

    public String getTime() //返回当前的时间，并结束handler的信息发送
    {
        //停止发送消息
        mTimeHandler.stopScheduleUpdate();
        return time;
    }

    private void updateClock() {
        //获取当前的时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String s = "";
        String m = "";
        String h = "";
        if (hour < 10) {
            h = "0" + hour;
        }
        else {
            h = hour + "";
        }
        if (minute < 10) {
            m = "0" + minute;
        }
        else {
            m = minute + "";
        }
        if (second < 10) {
            s = "0" + second;
        }
        else {
            s = second + "";
        }
        time = h + ":" + m + ":" + s;
        textView.setText(time);
    }


}
