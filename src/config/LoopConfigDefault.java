package config;
/**
 * Class used to pass into Looper as a config.
 * Create a new class that extends from this one and
 * use the abstract setConfig() method to set all variables 
 * that you need to change. Any not set in that method will 
 * use the default value set here.
 * 
 * @author Trevor
 */
public abstract class LoopConfigDefault {
	
	/**
	 * The time in seconds between each run of the main control loop
	 */
	public double loopDt = 0.01;
    
    /**
     * Basic constructor that calls setConfig() so that user changed values are updated
     */
    public LoopConfigDefault() {
        setConfig();
    }

    /**
     * Method to be written that should set any values to what is wanted. Any value that
     * is not set will use the default value
     */
    public abstract void setConfig();

}
