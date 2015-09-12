package sample;


import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.control.RobotSetup;

//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * TestRobot - a robot by (your name here)
 */
public class Dest extends Robot
{
	private double pixelX,pixelY;
	private RobotSetup robotSetup = null;
	public Dest(double xCoord,double yCoord){
		this.pixelX = xCoord;
		this.pixelY = yCoord;
		this.robotSetup =  new RobotSetup(this.pixelX,this.pixelY,0.0);
		//this.setAllColors(Color.green);
	}
	public double getPixelX(){
		return this.pixelX;
	}
	public double getPixelY(){
		return this.pixelY;
	}
	public RobotSetup getInitSetup(){
		return this.robotSetup;
	}
	public void run() {

		this.setAllColors(Color.green);
		while(true) {
			doNothing();
		}
	}	
}
