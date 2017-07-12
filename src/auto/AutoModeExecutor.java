package auto;

import auto.modes.AutoModeBase;

/**
 * This class spawns a new thread and runs an AutoBaseMode from it
 *
 * Inspired by 254 The Cheesy Poofs
 * 
 * @author Trevor
 */
public class AutoModeExecutor {
	private AutoModeBase autoMode;
	private Thread thread = null;
	
	/**
	 * Sets the mode to run to the given mode
	 * @param mode New instance of an auto mode class that extends AutoModeBase
	 */
	public void setMode(AutoModeBase mode) {
		autoMode = mode;
	}

	/**
	 * Spawns a new thread and runs auto using autoMode
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (autoMode != null) {
						autoMode.run();
					}
				}
			});
			thread.start();
		}

	}

	/**
	 * Stops the autoMode
	 */
	public void stop() {
		if (autoMode != null) {
			autoMode.stop();
		}
		thread = null;
	}
}
