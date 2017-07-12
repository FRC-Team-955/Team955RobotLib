package auto.actions;

import drive.Drive;
import edu.wpi.first.wpilibj.Timer;

/**
 * Action to run the left and right side at a given percentage (-1 to 1) for a given amount of time
 * 
 * @author Trevor
 */
public class TimedMoveAction implements Action {

    private final Drive drive;
    private final Timer timer = new Timer();
    double left, right, time;

    public TimedMoveAction(double left, double right, double time, Drive drive) {
        this.left = left;
        this.right = right;
        this.time = time;
        this.drive = drive;
    }

    @Override
    public boolean isFinished() {
        if(timer.get() > time) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        drive.openLoopSetMode(0,0);
    }

    @Override
    public void start() {
        timer.start();
        drive.openLoopSetMode(left, right);
    }
}
