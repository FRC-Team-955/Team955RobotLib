package util.loops;

/**
 * Created by Trevor on 5/12/2017.
 *
 * Inspired by 254 The Cheesy Poofs
 *
 * Interface for loops, which are routine that run periodically in the robot
 * code (such as periodic gyroscope calibration, etc.)
 */
public interface Loop {
    public void onStart();

    public void onLoop();

    public void onStop();
}

