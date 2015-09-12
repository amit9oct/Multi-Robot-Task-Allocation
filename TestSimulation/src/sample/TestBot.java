package sample;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import api.Christofides;
import api.ChristofidesManager;
import arena.CreateArena;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import utils.Vector;

//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * TestRobot - a robot by (your name here)
 */
public class TestBot extends AdvancedRobot
{
	/**
	 * run: TestRobot's default behavior
	 */
	private PrintWriter writer = null;
	private Object lock = new Object();
	boolean mflag = false;
	private double offset = 36.0;
	private Queue<Integer> Q = null;
	private Queue<Integer> botQ = null;
	private ArrayList<Boolean> visited = new ArrayList<Boolean>();
	public int indexNumber;
	private Vector curTarget;
	private boolean curTransaction = false;
	private Map<Integer,Integer> pointToBot = new HashMap<Integer,Integer>();
	private Map<Integer,Integer> botToPoint = new HashMap<Integer,Integer>();
	private double[][] costMatrix = null; 
	private double distance = 0.0;
	
	
	public int getIndexNumber(){
		return indexNumber;
	}
	
	
	public boolean isStationaryBot(String nameOfTheBot){
		StringTokenizer tok = new StringTokenizer(nameOfTheBot,"()");
		String a = tok.nextToken();
		if(a.trim().equals("sample.Stationary1"))
			return true;
		else
			return false;
	}
	
	public int getBotIndexNumber(String nameOfTheBot){
		StringTokenizer tok =  new StringTokenizer(nameOfTheBot,"()");
		String a = tok.nextToken();
		String b = null;
		if(tok.hasMoreTokens())
			b = tok.nextToken();
		int index = 0 ;
		if(b!=null)
			index =  Integer.parseInt(b) - 1;
		return index;
	}
	
	public  void moveBot(Vector v){
		//System.out.println("Before transaction X == "+this.getX()+" Y == "+this.getY());
		double x = this.getX();
		double y = this.getY();
		Vector my = new Vector(x, y);
		//System.out.println("Turning to change the cur heading: "+this.getHeading());
		turnLeft(this.getHeading());
		//System.out.println("Turned cur heading: "+this.getHeading());
		double angle = new Vector(0.0,1.0).angleBetween(new Vector(v.getX() - my.getX(),v.getY() - my.getY()));
		//System.out.println("Going to the point "+ "("+v.getX()+","+v.getY()+")");
		//System.out.println("angle = "+ angle + " distance = "+my.distance(v));
		turnRight(angle);
		ahead(my.distance(v));
		curTransaction = true;
		//System.out.println("The target desired"+v.getX()+","+v.getY());
		//System.out.println(""+this.getX()+" , "+this.getY());
		/*int i = 0;
		while(i<100){
			fire(400);
			i++;
		}*/
	}
	
	public void traverseAll(){
		while(!botQ.isEmpty()){
				//System.out.println("X ==  "+this.getX() + " Y == "+this.getY());
				//CreateArena.logs.info("--------------------");
				//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Current queue status");
				//CreateArena.logs.info("--------------------");
				printQueue();
				//CreateArena.logs.info("--------------------");
				//CreateArena.logs.info("--------------------");
				//Scanner in = new Scanner(System.in);
				//String temp = in.next();
				int targetBot = botQ.peek();
				Vector myLoc = new Vector(this.getX(), this.getY());
				curTarget = CreateArena.botToVector.get(targetBot);
				distance += myLoc.distance(curTarget);
				//curTransaction =  false;
				//System.out.println("TargetX == "+v.getX()+" TargetY == "+v.getY());
				//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Current target is the bot number " + (targetBot+1));
				if(!CreateArena.deathAnnounceTable[targetBot]){
					//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Trying to Move to"+(targetBot+1));
					moveBot(curTarget);
				}
				else{
					botQ.poll();
					//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+(targetBot+1) + "is already killed");
					curTransaction = false;
				}
				if(curTransaction){
					int i = 0;
					while(i<75){
						fire(1);
						i++;
					}
					//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Firing to kill "+(targetBot+1));
					if(CreateArena.deathAnnounceTable[targetBot]){
						botQ.poll();
						curTransaction = false;
						//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Killed the bot "+(targetBot+1));
					}
					else{
						curTransaction = false;
						//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Couldn't kill "+(targetBot+1));
					}
				}
				//System.out.println("After visiting the point "+"("+this.getX()+","+this.getY()+")");
				//visited.set(index,true);
				//if(visited.get(index))
					//System.out.println("Marked!! "+index);
		}
		CreateArena.testLog.info("For TestBot("+this.indexNumber+") distance = "+this.distance);
	}
	
	void printQueue(){
		for(Iterator<Integer> itr = botQ.iterator(); itr.hasNext();){
			int i = itr.next();
			//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Bot to be killed is"+(i+1));
			
		}
	}
	
	void printStartQueue(){
		for(Iterator<Integer> itr = Q.iterator(); itr.hasNext();){
			int i = itr.next();
			//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Bot to be killed is"+(i+1));
			
		}
	}
	
//	public void run() {
//		while(true) {
//			turnLeft(90);
//		}
//	}

	public void initQ(){
		assignIndexNumber();
		while(!CreateArena.areAllDestBotSet()){
			doNothing();
		}
		//CreateArena.logs.info("TestBot("+(this.indexNumber+1)+") has been created!!!");
		Q = new ArrayDeque<Integer>(CreateArena.RoboQ.get(CreateArena.botToCluster.get(this.indexNumber)));
		//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"This is queue before TSP");
		//CreateArena.logs.info("------------------------");
		printStartQueue();
		//botQ = new ArrayDeque<Integer>();
		botQ = Q;
		try{
			this.setTSPPoints();
		}catch(Exception e){
			/*while(true)
				turnLeft(90.0);*/
		}
		//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"This is the queue after TSP");
		//CreateArena.logs.info("---------------------------");
		printQueue();
		turnLeft(this.getHeading());
		turnRight(45.0);
		//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Starting at x = "+ this.getX()+" at y= "+this.getY());
		//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Cur Heading:"+ this.getHeading());

	}
	public void run() {
		initQ();	
		while(true) {
			if(!CreateArena.areAllBotsKilled()){
			traverseAll();
			if(!CreateArena.areAllBotsKilled()){
					//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"travesed all points");
					//CreateArena.logs.info("For TestBot("+(this.indexNumber+1)+") "+"Going for reallocation");
					CreateArena.reAlloc(indexNumber, new Vector(this.getX(),this.getY()));
					if(CreateArena.reallocMap.get(CreateArena.botToCluster.get(this.indexNumber))==this.indexNumber)
						initQ();
				}
			}
			doNothing();
			//System.out.close();
		}
		
	}
	
	private void setTSPPoints() {
		// TODO Auto-generated method stub
		int numOfPoints = Q.size();
		for(int i=1;i<=numOfPoints;i++){
			int botId = Q.poll();
			botToPoint.put(botId,i);
			pointToBot.put(i,botId);
		}
		costMatrix = new double[numOfPoints+1][numOfPoints+1];
		Vector myVector = new Vector(this.getX(),this.getY());
		costMatrix[0][0] = 0.0;
		for(int i=1;i<=numOfPoints;i++){
			costMatrix[0][i] = myVector.distance(CreateArena.botToVector.get(pointToBot.get(i)));
			costMatrix[i][0] = costMatrix[0][i];
		}
		for(int i=1;i<=numOfPoints;i++){
			for(int j=i;j<=numOfPoints;j++){
				if(j!=i){
					costMatrix[i][j] = this.getDistanceBetweenBots(pointToBot.get(i),pointToBot.get(j));
					costMatrix[j][i] = costMatrix[i][j];
				}
				else{
					costMatrix[i][j] = 0.0;
				}
			}
		}
		Christofides ch = null;
        try{
                ch = new Christofides(true);
        }
        catch(Exception e){
                e.printStackTrace();
                System.exit(0);
        }
		int[] shortestPath = ch.solve(costMatrix);
		for(int i=1;i<=numOfPoints;i++){
			botQ.add(pointToBot.get(shortestPath[i]));
		}
	}

	private double getDistanceBetweenBots(Integer bot1, Integer bot2) {
		Vector v1 = CreateArena.botToVector.get(bot1);
		Vector v2 = CreateArena.botToVector.get(bot2);
		return v1.distance(v2);
	}


	private void assignIndexNumber(){
		StringTokenizer tok = new StringTokenizer(this.getName(), "()");
		String a = tok.nextToken();
		String b = "1";
		if(tok.hasMoreTokens())
		b = tok.nextToken();
		indexNumber = Integer.parseInt(b)-1;
		System.out.println(this.getIndexNumber());
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//fire(1);
		//System.out.println(e.getName());
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		//back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		
	}
	 public void onHitRobot(HitRobotEvent event) {
	       /*if (event.getBearing() > -90 && event.getBearing() <= 90) {
	           setBack(100);
	       } else {
	           setAhead(100);
	       }*/
		 
		 	//System.out.println("Current Target  is X =="+curTarget.getX()+" Y == "+curTarget.getY());
		 	//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! is found "+event.getName());
		 	//System.out.println("########################Target is" + CreateArena.vectorToBotIndex.get(curTarget));
		 	if(!(isStationaryBot(event.getName()) && CreateArena.vectorToBotIndex.get(curTarget) == getBotIndexNumber(event.getName()))){
		 		//it is an obstacle
		 		if(curTarget!=null){
		 			stop();
		 			back(offset);
		 			Random random = new Random();
		 			int angleDir = random.nextInt(2);
		 			int movementDir = random.nextInt(2); 
		 			if(angleDir==0){
		 				turnLeft(90);
		 				if(movementDir==0)
		 					ahead(offset);
		 				else
		 					ahead(-offset);
		 			}
		 			else{
		 				turnRight(90);
		 				if(movementDir==0)
		 					ahead(offset);
		 				else
		 					ahead(-offset);
		 			}
		 			moveBot(curTarget);
		 		}
		 	}
	   }
}

