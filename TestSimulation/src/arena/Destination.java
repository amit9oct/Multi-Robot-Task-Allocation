package arena;

import robocode.Robot;
import robocode.control.RobotSetup;
import utils.Vector;

public class Destination {
		public static final boolean isRobot = false;
		private Robot robot = null;
		private double pixelX,pixelY;
		private Vector vector = null;
		private RobotSetup robotSetup = null;
		public Destination(double xCoord,double yCoord){
			this.pixelX = xCoord;
			this.pixelY = yCoord;
			this.robotSetup =  new RobotSetup(this.pixelX,this.pixelY,0.0);
			this.vector = new Vector(xCoord,yCoord);
			//this.setAllColors(Color.green);
		}
		public void setDestinationRobot(Robot robot){
			this.robot = robot;
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
		public Vector getVector(){
			return this.vector;
		}
}
