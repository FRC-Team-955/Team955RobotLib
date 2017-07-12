![logo](https://raw.githubusercontent.com/FRC-Team-955/Team955RobotLib/master/docs/logo.png)
=====
Team 955's robot library containing all general code

[Documentation can be found here](https://frc-team-955.github.io/Team955RobotLib/)

[Example project can be found here](https://github.com/FRC-Team-955/RobotLibExample)

### Recommended Use

- Add project to eclipse, then add to the build path of your robot project.
- Create your own version of each of the main classes, and extend from their counterpart in the library (Drive, Config files, etc.)
- For each of the config classes, override the setConfig() method, and change any values you don't want to use the default for from there
- Use the singleton architecture for each main part (Drive, Joystick, etc.). This allows you to have one object that corresponds with the one physical object. You can also call the getInstance() method from anywhere in your code and have access to that object without multiple layers of messy dependency injections

```Java
// Singleton
private static Drive instance = new Drive();

public static Drive getInstance() {
	return instance;
}
```