package config;
/**
 * Class used to pass into MyJoystick as a config.
 * Create a new class that extends from this one and
 * use the abstract setConfig() method to set all variables 
 * that you need to change. Any not set in that method will 
 * use the default value set here.
 * 
 * @author Trevor
 */
public abstract class JoystickConfigDefaults {
	
	/**
	 * Maximum number of buttons on the physical controller
	 */
	public int maxButtons = 12;
	
	/**
	 * Port number that this controller is plugged into
	 */
	public int portNumber = 0;
	
	/**
	 * Axis channel of the left x axis
	 */
	public int chnLeftX = 0;
	
	/**
	 * Axis channel of the left y axis
	 */
	public int chnLeftY = 1;
	
	/**
	 * Axis channel of the right x axis
	 */
	public int chnRightX = 2;
	
	/**
	 * Axis channel of the right y axis
	 */
	public int chnRightY = 3;
	
    
    /**
     * Basic constructor that calls setConfig() so that user changed values are updated
     */
    public JoystickConfigDefaults() {
        setConfig();
    }

    /**
     * Method to be written that should set any values to what is wanted. Any value that
     * is not set will use the default value
     */
    public abstract void setConfig();

}
