package config;
/**
 * Class used to pass into Drive as a config.
 * Create a new class that extends from this one and
 * use the abstract setConfig() method to set all variables 
 * that you need to change. Any not set in that method will 
 * use the default value set here.
 * 
 * @author Trevor
 */
public abstract class DriveConfigDefault {
	
	/**
	 * boolean used to determine if the sensor on the left talon
	 * should be flipped (multiplied by -1)
	 */
    public boolean leftFlipSensor = false;
    
    /**
	 * boolean used to determine if the sensor on the right talon
	 * should be flipped (multiplied by -1).
	 */
    public boolean rightFlipSensor = false;
    
    /** 
     * boolean used to determine if the output from the control loop
	 * on the left talon should be flipped (multiplied by -1).
     */
    public boolean leftFlipOutput = false;
    
    /** 
     * boolean used to determine if the output from the control loop
	 * on the right talon should be flipped (multiplied by -1).
     */
    public boolean rightFlipOutput = false;
    
    /**
     * The number of motors that are used on each side of the robot.
     */
    public int numMotorsSide = 2;
    
    /**
     * Array containing all id's for talons on the left side. Should be the 
     * same length as the numMotorsSide. First id is used as master, and should
     * be the talon with the sensor connected to it.
     */
    public int[] leftId = {0,1};
    
    /**
     * Array containing all id's for talons on the right side. Should be the 
     * same length as the numMotorsSide. First id is used as master, and should
     * be the talon with the sensor connected to it.
     */
    public int[] rightId = {2,3};
    
    /**
     * Number of encoder counts used to scale encoder to real world revolutions. 
     * Multiplyies by the number of encoder counts to get the current number of revolutions.
     */
    public int codesPerRev = 19;

    /**
     * proportional constant used in PID for left side of drivebase
     */
    public double kPLeft = 0.12;
    
    /**
     * integral constant used in PID for left side of drivebase
     */
    public double kILeft = 0;
    
    /**
     * derivative constant used in PID for left side of drivebase
     */
    public double kDLeft = 0;
    
    /**
     * feed forward constant used in PID for left side of drivebase
     */
    public double kFLeft = .8;
    
    /**
     * proportional constant used in PID for right side of drivebase
     */
    public double kPRight = 0.12;
    
    /**
     * integral constant used in PID for left side of drivebase
     */
    public double kIRight = 0;
    
    /**
     * derivative constant used in PID for left side of drivebase
     */
    public double kDRight = 0;
    
    /**
     * feed forward constant used in PID for left side of drivebase
     */
    public double kFRight = .7;

    /**
     * The maximum speed that the drive talons are allowed to change each cycle
     */
    public double rampRate = 0.5;
    
    /**
     * The wanted time between points on motion profile paths to follow
     */
    public double pathDt = 0.2;
    
    /**
     * Basic constructor that calls setConfig() so that user changed values are updated
     */
    public DriveConfigDefault() {
        setConfig();
    }

    /**
     * Method to be written that should set any values to what is wanted. Any value that
     * is not set will use the default value
     */
    public abstract void setConfig();

}
