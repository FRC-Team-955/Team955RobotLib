package drive;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import config.DriveConfigDefault;
import util.MotionProfileFollower;
import util.MyJoystick;
import util.loops.Loop;

/**
 * Created by Trevor on 5/23/17.
 */
public class Drive {
    protected DriveConfigDefault config;
    
    /**
     * Array containing all talons on the left side of the drivebase.
     * First element is the master talon, all following are slaves
     */
    protected CANTalon[] leftTalons;
    
    /**
     * Array containing all talons on the right side of the drivebase.
     * First element is the master talon, all following are slaves
     */
    protected CANTalon[] rightTalons;

    /**
     * Object used to follow a motion profile for the left side
     */
	private MotionProfileFollower leftFollower;
	
	/**
	* Object used to follow a motion profile for the right side
	*/
	private MotionProfileFollower rightFollower;
	
	private MyJoystick joy;
	
	/**
	 * Used for setting left and right side motors when in the OPEN_LOOP_SET mode
	 */
    private double left, right = 0;

    /** 
     * Enums that account for all control states for the drive system
     * 
     * @author Trevor
     */
    public enum DriveControlStates {
    	/**
    	 * Open loop control using the joystick
    	 */
        OPEN_LOOP_JOY, 
        
        /**
         * Closed loop control mode using motion profile
         */
        MOTION_PROFILE, 
        
        /**
         * Open loop control using variables to independently set left and right sides
         */
        OPEN_LOOP_SET
    }

    /** 
     * Holds the control state that the drive system is currently in
     */
    private DriveControlStates driveControlState;

    /**
     * Initializes the drive subsystem by setting talons, sensors, pid values, and slaves
     * 
     * @param config Config that holds all values, either default or user set, to use
     * @param joy Joystick object that represents physical joystick to use for driving
     */
    public Drive(DriveConfigDefault config, MyJoystick joy) {
        this.config = config;
        this.joy = joy;
        
        leftTalons = new CANTalon[config.numMotorsSide];
        rightTalons = new CANTalon[config.numMotorsSide];

        for(int i = 0; i < config.numMotorsSide; i++) {
            leftTalons[i] = new CANTalon(config.leftId[i]);
            rightTalons[i] = new CANTalon(config.rightId[i]);
        }

        // Start in open loop mode
        leftTalons[0].changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        rightTalons[0].changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        driveControlState = DriveControlStates.OPEN_LOOP_JOY;

        // Set up encoders
        leftTalons[0].setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        rightTalons[0].setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        leftTalons[0].configEncoderCodesPerRev(config.codesPerRev);
        rightTalons[0].configEncoderCodesPerRev(config.codesPerRev);
        leftTalons[0].reverseSensor(config.leftFlipSensor);
        leftTalons[0].reverseOutput(config.leftFlipOutput);
        rightTalons[0].reverseOutput(config.rightFlipOutput);
        rightTalons[0].reverseSensor(config.rightFlipSensor);

        // Set up PID values
        leftTalons[0].setP(config.kPLeft);
        leftTalons[0].setI(config.kILeft);
        leftTalons[0].setD(config.kDLeft);
        leftTalons[0].setF(config.kFLeft);
        rightTalons[0].setP(config.kPRight);
        rightTalons[0].setI(config.kIRight);
        rightTalons[0].setD(config.kDRight);
        rightTalons[0].setF(config.kFRight);

        // Assign Slaves
        for(int i = 1; i < config.numMotorsSide; i++) {
            leftTalons[i].changeControlMode(CANTalon.TalonControlMode.Follower);
            rightTalons[i].changeControlMode(CANTalon.TalonControlMode.Follower);

            leftTalons[i].set(config.leftId[0]);
            rightTalons[i].set(config.rightId[0]);
            
        }

    	leftFollower = new MotionProfileFollower(leftTalons[0], config.pathDt);
    	rightFollower = new MotionProfileFollower(rightTalons[0], config.pathDt);
    }
    
    /**
     * Main control loop, run at periodically from the Looper class
     */
 	private final Loop loop = new Loop() {

 		@Override
 		public void onStart() {

 		}

 		@Override
 		public void onLoop() {
 			switch (driveControlState) {
 				case OPEN_LOOP_JOY:
 					move(joy.getRTheta());
 					break;

 				case MOTION_PROFILE:
 					leftFollower.control();
 					rightFollower.control();

 					leftTalons[0].set(leftFollower.getSetValue().value);
 					rightTalons[0].set(rightFollower.getSetValue().value);
 					break;

 				case OPEN_LOOP_SET:
 					ramp(-left, right);
 					break;
 			}
 		}

 		@Override
 		public void onStop() {
 			openLoopJoyMode();
 			move(0,0);
 		}
 	};
 	
 	/**
 	 * Use for starting loop through Looper class
 	 * @return main control loop
 	 */
 	public Loop getLoop () {
		return loop;
	}
    
 	/**
 	 * Sets left and right drivebase wheels based off of polar coordinates
 	 * 
 	 * @param rTheta 2 element array that contains a magnitude on a scale of -1 to 1 and a heading in radians
 	 */
    public void move(double[] rTheta) {
		move(rTheta[0], rTheta[1]);
	}

    /**
     * Sets left and right drivebase wheels based off of polar coordinates
     * 
     * @param r magnitude on a scale of -1 to 1
     * @param theta heading in radians
     */
	public void move(double r, double theta) {
		double xPos = r * Math.cos(theta);
		double yPos = r * Math.sin(theta);

		double x = xPos * Math.abs(xPos);
		double y = yPos * Math.abs(yPos);

		double left = y + x;
		double right = y - x;

		if (leftTalons[0].getControlMode() != TalonControlMode.MotionProfile) {
			ramp(-left, right);
		}
	}
	
	/**
	 * Ramps the speed of the left and right wheels if needed
	 * @param wantSpeedLeft The wanted speed for the left side
	 * @param wantSpeedRight The wanted speed for the right side
	 */
	public void ramp(double wantSpeedLeft, double wantSpeedRight){
		if(Math.abs(wantSpeedLeft - leftTalons[0].get()) > config.rampRate){
			
			if(wantSpeedLeft > leftTalons[0].get())
				leftTalons[0].set((leftTalons[0].get() +  config.rampRate));
			
			else
				leftTalons[0].set((leftTalons[0].get() - config.rampRate));
			
		}
		
		else {
			leftTalons[0].set(wantSpeedLeft);
		}
		
		if(Math.abs(wantSpeedRight - rightTalons[0].get()) > config.rampRate){
			
			if(wantSpeedRight > rightTalons[0].get())
				rightTalons[0].set(rightTalons[0].get() +  config.rampRate);
			
			else
				rightTalons[0].set(rightTalons[0].get() - config.rampRate);
			
		}
		
		else {
			rightTalons[0].set(wantSpeedRight);
		}
	}
	
	/**
	 * Gets the set value to set the talon to when it is in motion profile mode
	 * @return Set value where 0 - Disable, 1 - Enable, and 2 - Hold
	 */
	public int getSetValue() {
		return leftFollower.getSetValue().value;
	}
	
	/**
	 * Sets the talons to a mode where they can follow a motion profile
	 */
	public void motionProfileMode() {
		driveControlState = DriveControlStates.MOTION_PROFILE;

		leftTalons[0].changeControlMode(TalonControlMode.MotionProfile);
		leftFollower.control();

		rightTalons[0].changeControlMode(TalonControlMode.MotionProfile);
		rightFollower.control();
				
		leftTalons[0].set(leftFollower.getSetValue().value);
		rightTalons[0].set(rightFollower.getSetValue().value);
	}
	
	/**
	 * Sets the talons to a mode where they can be controlled by the joystick
	 */
	public void openLoopJoyMode() {
		driveControlState = DriveControlStates.OPEN_LOOP_JOY;
		resetToOpenLoop();
	}

	/**
	 * Sets the talons to a mode where they can be given a left and a right speed
	 * @param left Speed for left side on a scale of -1 to 1
	 * @param right Speed for right side on a scale of -1 to 1
	 */
	public void openLoopSetMode(double left, double right) {
		this.left = left;
		this.right = right;

		if(driveControlState != DriveControlStates.OPEN_LOOP_SET) {
			resetToOpenLoop();
			driveControlState = DriveControlStates.OPEN_LOOP_SET;
		}
	}

	/**
	 * Resets the talons so that they're buffer is clear and they can accept PercentVbus commands (-1 to 1)
	 */
	private void resetToOpenLoop() {
		leftTalons[0].changeControlMode(TalonControlMode.PercentVbus);
		rightTalons[0].changeControlMode(TalonControlMode.PercentVbus);

		leftFollower.reset();
		rightFollower.reset();
	}
	
	/**
	 * Starts the motion profile for the loaded paths
	 */
	public void startMotionProfile() {
		leftFollower.startMotionProfile();
		rightFollower.startMotionProfile();
	}
	
	/**
	 * Load two paths to be run as a motion profile
	 * @param left left path as a 2d array such that the first dimension is an array of points, and each point is
	 * laid out as such {accumulated position, velocity, dt} where accumulated position is in rotations,
	 * velocity is in rpm, and dt is in ms
	 * @param right right path as a 2d array such that the first dimension is an array of points, and each point is
	 * laid out as such {accumulated position, velocity, dt} where accumulated position is in rotations,
	 * velocity is in rpm, and dt is in ms
	 */
	public void setPaths(double[][]left, double[][]right) {
		leftFollower.setPoints(left);
		rightFollower.setPoints(right);
	}
}
