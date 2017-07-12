package util.loops;

/**
 * Interface for loops, which are routine that run periodically in the robot
 * code (such as drive code, etc.)
 * 
 * Inspired by 254 The Cheesy Poofs
 * 
 * @author Trevor
 */
public interface Loop {
	/**
	 * Code here will be run when the loop first starts
	 */
    public void onStart();

    /**
     * Code here will be run periodically at a rate given in the looper
     */
    public void onLoop();

    /**
     * Code here will be run when the loop is stopped
     */
    public void onStop();
}

