package sample;

import java.awt.Color;
import java.util.Map;
import java.util.StringTokenizer;


import arena.CreateArena;

import robocode.DeathEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import utils.Vector;

public class Stationary1 extends Robot {
	public static int CLUSTER_COMPLETED = 0;
	public static Color COLOR = Color.red;
	public int indexNumber;
	public void colorSet(){
		System.out.println("Got in "+this.indexNumber);
		System.out.println(CreateArena.getClusterNumber(this.indexNumber));
		switch(CreateArena.getClusterNumber(this.indexNumber)){
			case 0: this.setAllColors(Color.green);
					break;
			case 1: this.setAllColors(Color.red);
					break;
			case 2: this.setAllColors(Color.gray);
					break;
			case 3: this.setAllColors(Color.MAGENTA);
					break;
			case 4: this.setAllColors(Color.cyan);
			        break;
			case 5: this.setAllColors(Color.darkGray);
			        break;
			case 6: this.setAllColors(Color.LIGHT_GRAY);
	        		break;
			case 7: this.setAllColors(Color.white);
	        		break;
			case 8: this.setAllColors(Color.getHSBColor(23, 34, 15));
	        		break;
			case 9: this.setAllColors(Color.getHSBColor(23, 34, 150));
	        		break;
			case 10: this.setAllColors(Color.getHSBColor(23, 340, 15));
	        		break;
			case 11: this.setAllColors(Color.getHSBColor(230, 34, 15));
	        		break;
			case 12: this.setAllColors(Color.getHSBColor(230, 340, 15));
	        		break;
			case 13: this.setAllColors(Color.getHSBColor(203, 340, 150));
	        		break;
			default: this.setAllColors(Color.black);
		}
		
	}
	
	public int getIndexNumber(){
		return indexNumber;
	}
	
	public void run(){
		//This Bot does nothing;
		//doNothing();
		assignIndexNumber();
		this.colorSet();
		this.turnLeft(90);
		this.turnLeft(90);
		System.out.println("I am init!!");
		int clusterNumber = CreateArena.getClusterNumber(this.getIndexNumber());
		/*CreateArena.RoboQArray.get(clusterNumber).remove(CreateArena.destinations[this.getIndexNumber()].getVector());
		//CreateArena.vectorToBotIndex.remove(CreateArena.destinations[this.getIndexNumber()].getVector());
		System.out.println("Given location = "+CreateArena.destinations[this.getIndexNumber()].getVector());
		CreateArena.RoboQArray.get(clusterNumber).add(new Vector(this.getX(), this.getY()));
		System.out.println("Actual location = "+(new Vector(this.getX(),this.getY())));*/
		CreateArena.vectorToBotIndex.put(new Vector(this.getX(), this.getY()),this.getIndexNumber());
		CreateArena.botToVector.put(this.getIndexNumber(),new Vector(this.getX(),this.getY()));
		for (Map.Entry<Vector, Integer> entry : CreateArena.vectorToBotIndex.entrySet()){
        	
        	System.out.println("Key = "+entry.getKey()+" Value = "+entry.getValue());
        	
        }
		CreateArena.destReady[this.getIndexNumber()] = true;
		
		//CreateArena.lock.notifyAll();
		//turnLeft(this.getHeading());
		while(true){
			doNothing();
			//System.out.println("I am alive!!! "+this.getName());
		}
	}
	
	private void assignIndexNumber(){
		StringTokenizer tok = new StringTokenizer(this.getName(), "()");
		String a = tok.nextToken();
		String b = tok.nextToken();
		indexNumber = Integer.parseInt(b)-1;
		System.out.println(this.getIndexNumber());
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//fire(1);
	}
	public void onDeath(DeathEvent event){
		CreateArena.deathAnnounceTable[this.indexNumber] = true;
	}
}
