/**
 * Created by Trevor on 5/23/17.
 */
public abstract class DriveConfig {
    public boolean leftFlipSensor = false;
    public boolean rightFlipSensor = false;
    public boolean leftFlipOutput = false;
    public boolean rightFlipOutput = false;
    public int numMotorsSide = 2;
    public int[] leftId = {0,1};
    public int[] rightId = {2,3};
    public int codesPerRev = 19;

    public double kPLeft = 0.12;
    public double kILeft = 0;
    public double kDLeft = 0;
    public double kFLeft = .8; //.99
    public double kPRight = 0.12;
    public double kIRight = 0;
    public double kDRight = 0;
    public double kFRight = .7;

    public DriveConfig() {
        setConfig();
    }

    abstract void setConfig();

}
