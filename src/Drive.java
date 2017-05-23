import com.ctre.CANTalon;

/**
 * Created by Trevor on 5/23/17.
 */
public class Drive {
    private DriveConfig config;
    private CANTalon[] leftTalons;
    private CANTalon[] rightTalons;

    public enum DriveControlStates {
        OPEN_LOOP_JOY, MOTION_PROFILE, OPEN_LOOP_SET
    }

    private DriveControlStates driveControlState;

    public Drive(DriveConfig config) {
        this.config = config;

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
    }

}
