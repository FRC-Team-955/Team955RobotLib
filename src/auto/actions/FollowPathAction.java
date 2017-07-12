package auto.actions;

import drive.Drive;

/**
 * Action to follow a given motion profile
 * 
 * @author Trevor
 */
public class FollowPathAction implements Action {

    private final double[][] leftPath, rightPath;
    private final Drive drive;

    public FollowPathAction(double[][] leftPath, double[][] rightPath, Drive drive) {
        this.leftPath = leftPath;
        this.rightPath = rightPath;
        this.drive = drive;
    }

    @Override
    public boolean isFinished() {
        if(drive.getSetValue() == 2) { // 2 is value when profile is finished executing
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
        drive.openLoopJoyMode();
    }

    @Override
    public void start() {
        drive.setPaths(leftPath, rightPath);
        drive.motionProfileMode();
        drive.startMotionProfile();
    }
}
