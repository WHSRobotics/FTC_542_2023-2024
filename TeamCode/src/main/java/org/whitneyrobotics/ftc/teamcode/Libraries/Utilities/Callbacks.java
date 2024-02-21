package org.whitneyrobotics.ftc.teamcode.Libraries.Utilities;

import com.qualcomm.robotcore.util.RobotLog;

public class Callbacks {

    private static int GLOBAL_ID = 0;
    private static int breakId = -1;
    public synchronized static void setTimeout(Runnable callback, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                callback.run();
            } catch (InterruptedException e) {
                RobotLog.e(e.toString());
            }
        }).start();
    }

    public synchronized static int setInterval(Runnable callback, int delay) {
        int breakId = GLOBAL_ID++;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(delay);
                    callback.run();
                    if(Callbacks.breakId == breakId) {
                        Callbacks.breakId = -1;
                        break;
                    }
                } catch (InterruptedException e) {
                    RobotLog.ee("Problem with Interval with PID: " + breakId,e.toString());
                }
            }
        }).start();
        return breakId;
    }

    public static void clearInterval(int id) {
        Callbacks.breakId = id;
    }

}
