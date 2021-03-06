package util.loops;

import java.util.ArrayList;
import java.util.List;

import config.LoopConfigDefault;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

/**
 * This code runs all of the robot's loops. Loop objects are stored in a List
 * object. They should be started when the robot powers up and stopped when 
 * the match ends in the robot init methods in Robot.java
 * 
 * Inspired by 254 The Cheesy Poofs
 * 
 * @author Trevor
 */
public class Looper {
	
	/**
	 * Tells whether the loops are running or not
	 */
    private boolean running;
    
    private final LoopConfigDefault config;
    
    /**
     * Notifier which is used to schedule the loops with a given dt between each loop
     */
    private final Notifier notifier;
    
    /**
     * List containing all of the loops to run
     */
    private final List<Loop> loops;
    
    /**
     * Object used to make sure that threads are synchronized
     */
    private final Object taskRunningLock = new Object();
    private double timestamp = 0;
    private double dt = 0;
    
    /**
     * runnable that is used to run all of the loops
     */
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

    /**
     * Initializes looper and gets ready to run
     * @param config Config object which contains values to be pulled in
     */
    public Looper(LoopConfigDefault config) {
    	this.config = config;
        notifier = new Notifier(runnable);
        running = false;
        loops = new ArrayList<>();
    }

    /**
     * Adds a given loop the the list to be run periodically
     * @param loop Loop to be added
     */
    public synchronized void register(Loop loop) {
        synchronized (taskRunningLock) {
            loops.add(loop);
        }
    }

    /**
     * Starts the looper at the rate given in the config
     */
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

    /**
     * Stops the looper and runs onStop() method for all loops running
     */
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
