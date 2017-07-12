package util;

import config.JoystickConfigDefaults;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Adds functions to assist in joystick use
 * 
 * @author Trevor
 *
 */
public class MyJoystick extends Joystick {

	private boolean[] lastButtonState;
	private boolean[] buttonState;
	JoystickConfigDefaults config;

	/**
	 * Creates joystick with given configuration
	 * 
	 * @param config Config object that config values are pulled from
	 */
	public MyJoystick(JoystickConfigDefaults config) {
		super(config.portNumber);

		this.config = config;
		lastButtonState = new boolean[config.maxButtons];
		buttonState = new boolean[config.maxButtons];

		for (int i = 0; i < config.maxButtons; i++) {
			lastButtonState[i] = false;
			buttonState[i] = false;
		}
	}

	/**
	 * Updates the button values for the controller
	 */
	public void update() {
		for (int i = 0; i < config.maxButtons; i++) {
			buttonState[i] = !lastButtonState[i] && super.getRawButton(i + 1);
			lastButtonState[i] = super.getRawButton(i + 1);
		}
	}

	/**
	 * Gives the location of the joystick in the coordinate system (r, theta) where r is the distance from the 
 	 * center and theta is the angle the joystick is pointing in radians
	 * 
	 * @return magnitude and angle of joystick
	 */
	public double[] getRTheta() {
		double x = this.getRawRightX()*-1;
		double y = this.getRawLeftY();
		
//		System.out.println("Right X: " + x + "\tLeftY: " + y);
		
		double r = Math.sqrt((x * x) + (y * y));
		double theta = Math.atan2(y, x);

		double[] rTheta = { r, theta };

//		System.out.println("Y: " + y + "\t\tX: " + x);
		
		return rTheta;
		
	}

	/**
	 * Gives button value at given id
	 * 
	 * @param button the button number on the controller. Use joy.cpl to determine physical button id's
	 * @return button true if button has been pressed once, accounts for debounce
	 */
	public boolean getButton(int button) {
		return buttonState[button- 1];
	}

	/**
	 * Gets the x value of the left joystick
	 * 
	 * @return the x value of the left joystick
	 */
	public double getRawLeftX() {
		return super.getRawAxis(config.chnLeftX);
	}

	/**
	 * Gets the y value of the left joystick
	 * 
	 * @return the y value of the left joystick
	 */
	public double getRawLeftY() {
		return super.getRawAxis(config.chnLeftY);
	}

	/**
	 * Gets the x value of the right joystick
	 * 
	 * @return the x value of the right joystick
	 */
	public double getRawRightX() {
		return super.getRawAxis(config.chnRightX);
	}

	/**
	 * Gets the y value of the right joystick
	 * 
	 * @return the y value of the right joystick
	 */
	public double getRawRightY() {
		return super.getRawAxis(config.chnRightY);
	}
	
	/**
     * Checks if the dpad is pressed in the up direction 
     * @return is the dpad pressed
     */
	public boolean getDpadUp() {
    	return super.getPOV(0) == 0;
    }
    
    /**
     * Checks if the dpad is pressed in the right direction 
     * @return is the dpad pressed
     */
    public boolean getDpadRight() {
    	return super.getPOV(0) == 90;
    }

    /**
     * Checks if the dpad is pressed in the down direction 
     * @return is the dpad pressed
     */
    public boolean getDpadDown(){
    	return super.getPOV(0) == 180;
    }
    
    /**
     * Checks if the dpad is pressed in the left direction 
     * @return is the dpad pressed
     */
    public boolean getDpadLeft(){
    	return super.getPOV(0) == 270;
    }
}