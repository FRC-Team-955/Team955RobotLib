package util.navx;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * Controls the NavX sensor
 * @author Trevor
 *
 */
public class NavX extends IMUAdvanced
{
	
	public NavX(int baudRate)
	{
		super(new SerialPort(baudRate, SerialPort.Port.kMXP));
	}

	/**
	 * 	@return angle in degrees from -180 to 180
	 */
	public double getAngleDeg()
	{
		return super.getYaw();
	}
	
	/**
	 * 	@return angle in degrees from -pi to pi
	 */
	public double getAngleRad()
	{
		return (super.getYaw() * ((float) Math.PI/180));
	}
}