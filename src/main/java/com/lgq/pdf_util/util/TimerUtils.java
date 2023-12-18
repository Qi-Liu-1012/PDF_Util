package com.lgq.pdf_util.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author liuguoqiang
 * @Date 2023/8/21
 * @Version 1.0
 */
public class TimerUtils {

    public static Timer timerDelayDo(TimerTask task, long delayTime, TimeUnit timeUnit) {
        Timer timer = new Timer();
        long millTime = timeUnit.convert(delayTime, TimeUnit.MILLISECONDS);
        timer.schedule(task, millTime);
        return timer;
    }
}
