package sample;
import /home/amit9oct/Amitayush/SWARM/jars/libs/robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * TestRobot - a robot by (your name here)
 */
public class TestRobot extends Robot
{
	/**
	 * run: TestRobot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		double des_x = 100,des_y = 200;
		double src_x = this.getX();
		double src_y = this.getY();
		double angle = this.getHeading();
		turnLeft(angle);
		turnRight(90);
		ahead(des_x - src_x);
		turnLeft(90);
		ahead(des_y - src_y);
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			//fire(1);
			//ahead(100);
			//turnGunRight(360);
			//back(100);
			//turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		
	}	
}
