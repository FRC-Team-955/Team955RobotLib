package auto.modes;

import auto.AutoModeEndedException;
import auto.actions.Action;

/**
 * An abstract class that is the basis of the robot's autonomous routines. This
 * is implemented in auto.modes (which are routines that do actions).
 * 
 * Inspired by 254 The Cheesy Poofs
 * 
 * @author Trevor
 */
public abstract class AutoModeBase {
    protected double updateRate = 1.0 / 50.0;
    protected boolean active = false;

    protected abstract void routine() throws AutoModeEndedException;

    public void run() {
        active = true;
        try {
            routine();
        } catch (AutoModeEndedException e) {
            System.out.println("AutoModeExecutor mode done, ended early");
            return;
        }
        done();
        System.out.println("AutoModeExecutor mode done");
    }

    public void done() {
    }

    public void stop() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isActiveWithThrow() throws AutoModeEndedException {
        if (!isActive()) {
            throw new AutoModeEndedException();
        }
        return isActive();
    }

    public void runAction(Action action) throws AutoModeEndedException {
        isActiveWithThrow();
        action.start();
        while (isActiveWithThrow() && !action.isFinished()) {
            action.update();
            long waitTime = (long) (updateRate * 1000.0);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        action.done();
    }

}