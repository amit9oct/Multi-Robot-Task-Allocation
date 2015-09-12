package sample;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;


public class Stationary extends Robot {
	public static Color COLOR = Color.green;
	public void colorSet(Color color){
		this.setAllColors(Color.green);
		
	}
	public void run(){
		//This Bot does nothing;
		//doNothing();
		this.colorSet(COLOR);
		this.turnLeft(90);
		this.turnLeft(90);
		//turnLeft(this.getHeading());
		while(true){
			doNothing();
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//fire(1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
