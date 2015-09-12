package sample;

import robocode.Robot;

public class BabyBot extends Robot{
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		double des_x = 0,des_y = 0;
		double src_x = this.getX();
		double src_y = this.getY();
		double angle = this.getHeading();
		turnLeft(angle);
		System.out.println("Turning Right By 90 degrees");
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


}
