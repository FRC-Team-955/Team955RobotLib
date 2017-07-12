package util.loops;

import java.util.ArrayList;
import java.util.List;

import config.LoopConfigDefault;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

/**
 * Created by Trevor on 5/12/2017.
 *
 * Inspired by 254 The Cheesy Poofs
 *
 * This code runs all of the robot's loops. Loop objects are stored in a List
 * object. They are started when the robot powers up and stopped after the
 * match.
 */
public class Looper {
	
    private boolean running;

    private final LoopConfigDefault config;
    private final Notifier notifier;
    private final List<Loop> loops;
    private final Object taskRunningLock = new Object();
    private double timestamp = 0;
    private double dt = 0;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            synchronized (taskRunningLock) {
                if (running) {
                    double now = Timer.getFPGATimestamp();
                    for (Loop loop : loops) {
                        loop.onLoop();
                    }
                    dt = now - timestamp;
                    timestamp = now;
                }
            }
        }
    };

    public Looper(LoopConfigDefault config) {
    	this.config = config;
        notifier = new Notifier(runnable);
        running = false;
        loops = new ArrayList<>();
    }

    public synchronized void register(Loop loop) {
        synchronized (taskRunningLock) {
            loops.add(loop);
        }
    }

    public synchronized void start() {
        if (!running) {
            System.out.println("Starting loops");
            synchronized (taskRunningLock) {
                timestamp = Timer.getFPGATimestamp();
                for (Loop loop : loops) {
                    loop.onStart();
                }
                running = true;
            }
            notifier.startPeriodic(config.loopDt);
        }
    }

    public synchronized void stop() {
        if (running) {
            System.out.println("Stopping loops");
            notifier.stop();
            synchronized (taskRunningLock) {
                running = false;
                for (Loop loop : loops) {
                    System.out.println("Stopping " + loop);
                    loop.onStop();
                }
            }
        }
    }
}
