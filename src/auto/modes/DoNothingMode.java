package auto.modes;

import auto.AutoModeEndedException;

/**
 * Auto mode example which does nothing
 * 
 * @author Trevor
 */
public class DoNothingMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Do nothing mode completed");
    }
}
