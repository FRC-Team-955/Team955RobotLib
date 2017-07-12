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
    protected CANTalon[] leftTalons;
    protected CANTalon[] rightTalons;

	private MotionProfileFollower leftFollower;
	private MotionProfileFollower rightFollower;
	
	private MyJoystick joy;
	
    private double left, right = 0;

    public enum DriveControlStates {
        OPEN_LOOP_JOY, MOTION_PROFILE, OPEN_LOOP_SET
    }

    private DriveControlStates driveControlState;

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
    
    // Main control loop, run at 100hz
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
 	
 	public Loop getLoop () {
		return loop;
	}
    
    public void move(double[] rTheta) {
		move(rTheta[0], rTheta[1]);
	}

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
	
	public int getSetValue() {
		return leftFollower.getSetValue().value;
	}
	
	public void motionProfileMode() {
		driveControlState = DriveControlStates.MOTION_PROFILE;

		leftTalons[0].changeControlMode(TalonControlMode.MotionProfile);
		leftFollower.control();

		rightTalons[0].changeControlMode(TalonControlMode.MotionProfile);
		rightFollower.control();
				
		leftTalons[0].set(leftFollower.getSetValue().value);
		rightTalons[0].set(rightFollower.getSetValue().value);
	}
	
	public void openLoopJoyMode() {
		driveControlState = DriveControlStates.OPEN_LOOP_JOY;
		resetToOpenLoop();
	}

	public void openLoopSetMode(double left, double right) {
		this.left = left;
		this.right = right;

		if(driveControlState != DriveControlStates.OPEN_LOOP_SET) {
			resetToOpenLoop();
			driveControlState = DriveControlStates.OPEN_LOOP_SET;
		}
	}

	private void resetToOpenLoop() {
		leftTalons[0].changeControlMode(TalonControlMode.PercentVbus);
		rightTalons[0].changeControlMode(TalonControlMode.PercentVbus);

		leftFollower.reset();
		rightFollower.reset();
	}
	
	public void startMotionProfile() {
		leftFollower.startMotionProfile();
		rightFollower.startMotionProfile();
	}
	
	public void setPaths(double[][]left, double[][]right) {
		leftFollower.setPoints(left);
		rightFollower.setPoints(right);
	}
}
