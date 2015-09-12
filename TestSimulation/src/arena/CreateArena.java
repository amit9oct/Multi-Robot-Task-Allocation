package arena;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cluster.AffinityPropagation;
import cluster.Cluster;
import cluster.IClusterAlgorithm;
import cluster.KMeansAlgo;



import pointGenerator.RandomPointGenerator;

import bincopy.CopyFile;


import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.*;
import sample.Stationary1;
import task.HungarianSolver;
import task.ITaskAllocation;
import utils.Vector;

public class CreateArena {
	public static final int NUM_DESTINATION_POINTS = 30;
	public static int NUM_ROBOTS = 5;
	public static final int GAME_HEIGHT = 5000;
	public static final int GAME_WIDTH = 5000;
	public static BattlefieldSpecification battlefield = new BattlefieldSpecification(GAME_WIDTH,GAME_HEIGHT);
    public static BattleSpecification battleSpec = new BattleSpecification(battlefield);
    public static Destination[] destinations = new Destination[NUM_DESTINATION_POINTS];
    public static double[][] coordinates = null;
	public static ArrayList<ArrayList<Vector>> RoboQArray = new ArrayList<ArrayList<Vector> >();
	public static Destination[] RoboDestination = null;
	public static boolean[] isExemplar = new boolean[NUM_DESTINATION_POINTS+1];
	public static boolean[][] pointsInExemplar =  new boolean[NUM_DESTINATION_POINTS+1][NUM_DESTINATION_POINTS+1];
	public static int NUM_CLUSTERS = 2;
	public static int cluster_count = 0;
	public static Map<Integer,Integer> pointClusterMap = null;
	public static int NUM_CLUSTERS_MADE = 0;
	public static Map<Integer, Integer> clusterMap = null;
	public static IClusterAlgorithm clusterAlgo = null; 
	public static String FILENAME = "inputPoints.txt";
	public static final String[] ALGONAMES = setAlgoNames();
	public static boolean READ_FILE = false;
	private static final String TEST_BOT_FILENAME = "testBotPoints.txt";
	
	public static HashMap<Vector,Integer> coordinatesToIndex = new HashMap<Vector,Integer>();
	public static int CLUSTER_SIZE = 1;

	public static RobocodeEngine engine = new RobocodeEngine();
	public static RobotSpecification clusterSpec = null;
	public static Map<Vector,Integer> vectorToBotIndex = null;
	public static boolean[] deathAnnounceTable = null;
	public static boolean[] destReady = new boolean[NUM_DESTINATION_POINTS];
	public static ArrayList<ArrayList<Integer>> RoboQ = new ArrayList<ArrayList<Integer> >();
	//public static Object lock;
	public static HashMap<Integer,Vector> botToVector = new HashMap<Integer,Vector>();
	public static Cluster[] clusterCollection = null;
	public static ITaskAllocation taskAlloc = null;
	public static HashMap<Integer, Integer> botToCluster = null;
	private static int algoNumber = 0;
	public static Logger logs = null;
	public static Logger testLog = null;
	private static double[][] roboCoordinates;
	public static boolean[] realloc = new boolean[10000];
	public static HashMap<Integer, Integer> reallocMap = new HashMap<Integer,Integer>();
	
	
	public static int getClusterNumber(int pointNumber){
		int exemplar = 0;
		if(pointClusterMap.get(pointNumber)==null)
			exemplar = 4;
		else
			exemplar = (int)(pointClusterMap.get(pointNumber)+0);
		System.out.println("Exemplar = " + exemplar);
		return clusterMap.get(exemplar);
	}
	
	private static String[] setAlgoNames() {
		String[] algos = new String[2];
		algos[0] = "AP";
		algos[1] = "KMeans";
		return algos;
	}

	public static void setDestinationPoints(String fileName,boolean generateNew){
		RandomPointGenerator randomGen = new RandomPointGenerator(NUM_DESTINATION_POINTS, fileName, GAME_HEIGHT, GAME_WIDTH);
		BufferedReader br = null;
		if(generateNew)
			coordinates = randomGen.generate();
		try {
			if(!generateNew){	
				br = new BufferedReader(new FileReader(fileName));
				String currentLine = null;
				int i = 0;
				coordinates = new double[NUM_DESTINATION_POINTS][2];
				while((currentLine = br.readLine()) != null){
					coordinates[i][0] = Double.parseDouble(currentLine.split(",")[0]);
					coordinates[i][1] = Double.parseDouble(currentLine.split(",")[1]);
					i++;
				}
				br.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
        for(int i=0;i<NUM_DESTINATION_POINTS;i++){ 
        	//System.out.println(new Vector(coordinates[i][0],coordinates[i][1]));
        	destinations[i] = new Destination(coordinates[i][0],coordinates[i][1]);
        	coordinatesToIndex.put(new Vector(coordinates[i][0],coordinates[i][1]),i);
        	//System.out.println(destinations[i].getVector());
        	destinations[i].setDestinationRobot(new Stationary1());
        }
	}
	
	
	public static void clusterAlgorithm(String algoName,String fileName){
		if(algoName.equals("AP")){
			clusterAlgo = new AffinityPropagation(GAME_HEIGHT, GAME_WIDTH, CLUSTER_SIZE);
		}
		else if(algoName.equals("KMeans")){
			clusterAlgo = new KMeansAlgo();
		}
		clusterAlgo.preProcessing(fileName);
		clusterAlgo.algorithm();
        pointClusterMap = clusterAlgo.postProcessing();
	}
	
	public static void exemplarToClusterIndex(){
        clusterMap = new HashMap<Integer,Integer>();
        for (Map.Entry<Integer, Integer> entry : pointClusterMap.entrySet()){
           int value = (int)(entry.getValue()+0);
           int key = (int)(entry.getKey()+0);
           if(!isExemplar[value]){
        	   isExemplar[value] = true;
        	   clusterMap.put(value, NUM_CLUSTERS_MADE);
        	   NUM_CLUSTERS_MADE++;
           }
           //System.out.println(key+"/"+value);
        }
	}
	
	public static void setStationaryPoints(){
        	clusterSpec = engine.getLocalRepository("sample.Stationary1")[0];
        	vectorToBotIndex = new HashMap<Vector,Integer>();
        	deathAnnounceTable = new boolean[NUM_DESTINATION_POINTS];
        	for(int i=0;i<NUM_DESTINATION_POINTS;i++)
            	try{
            		vectorToBotIndex.put(destinations[i].getVector(),i);
            		deathAnnounceTable[i] = false;
            		//System.out.println("i = "+vectorToBotIndex.get(new Vector(destinations[i].getPixelX(),destinations[i].getPixelY())));
            		battleSpec.addRobotToBattleField(clusterSpec, destinations[i].getInitSetup());
            	}catch(Exception e){
            		e.printStackTrace();
            	}
	}
	
	public static void assignStationaryBots(){
		for (Map.Entry<Integer,Integer> entry : pointClusterMap.entrySet()){
        	int value = (int)(entry.getValue()+0);
        	int key = (int)(entry.getKey()+0);
        	
        	//System.out.println("Key = "+key+" Value = "+value);
        	pointsInExemplar[value][key] = true;
        	
        }
        for(int i=0;i<NUM_CLUSTERS_MADE;i++){
        	RoboQArray.add(new ArrayList<Vector>());
        	RoboQ.add(new ArrayList<Integer>());
        }
        
        //logs.info("Reading the points in the cluster\n");
        for(int i=0;i<NUM_DESTINATION_POINTS;i++){
        	if(isExemplar[i]){
        		//logs.info("Cluster = "+(cluster_count + 1));
        		for(int j=0;j<NUM_DESTINATION_POINTS;j++){
        			if(pointsInExemplar[i][j]){
                		try {
                			if(cluster_count<NUM_CLUSTERS_MADE){
                				RoboQArray.get(cluster_count).add(new Vector(destinations[j].getPixelX(),destinations[j].getPixelY()));
                				RoboQ.get(cluster_count).add(j);
                			}
                			//logs.info("\t\t   ---->"+ destinations[j].getVector()+" is stationary bot number"+(j+1));
                			//System.out.println("Cluster = "+(cluster_count+1)+" assigned -->  "+destinations[j].getVector()+" stationary bot number "+(j+1));
                		} catch (Exception e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}	
        			}
        		}
        		cluster_count++;
        		System.out.println("Cluster = "+Stationary1.CLUSTER_COMPLETED);
        	}
        }
	}
	
	private static void setTestBotPoints(String fileName,boolean generateNew) {
		RandomPointGenerator randomGen = new RandomPointGenerator(NUM_ROBOTS, fileName, GAME_HEIGHT, GAME_WIDTH);
		BufferedReader br = null;
		if(generateNew)
			roboCoordinates = randomGen.generate();
		try {
			if(!generateNew){	
				br = new BufferedReader(new FileReader(fileName));
				String currentLine = null;
				int i = 0;
				roboCoordinates = new double[NUM_ROBOTS][2];
				while((currentLine = br.readLine()) != null){
					roboCoordinates[i][0] = Double.parseDouble(currentLine.split(",")[0]);
					roboCoordinates[i][1] = Double.parseDouble(currentLine.split(",")[1]);
					i++;
				}
				br.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		RoboDestination =  new Destination[NUM_ROBOTS];
		//logs.info("Robot Locations\n");
        for(int i=0;i<NUM_ROBOTS;i++){ 
        	//logs.info("\t  ----> TestBot("+(i+1)+")"+new Vector(roboCoordinates[i][0],roboCoordinates[i][1]));
        	RoboDestination[i] = new Destination(roboCoordinates[i][0],roboCoordinates[i][1]);
        }
		
	}
	
	
	
	public static void assignTestBots(){

        RobotSpecification[] testSpec = engine.getLocalRepository("sample.TestBot");
        for(int i=0;i<NUM_ROBOTS;i++){
        	try {
    			battleSpec.addRobotToBattleField(testSpec[0], RoboDestination[i].getInitSetup());
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
	}
	
	public static void makeClusters(){
		clusterCollection = new Cluster[NUM_CLUSTERS_MADE];
		int exemplarIndex;
		for(int i=0;i<NUM_DESTINATION_POINTS;i++){
			if(isExemplar[i]){
				exemplarIndex = clusterMap.get(i);
				ArrayList<Integer> clusterArrayList = new ArrayList<Integer>();
				for(int j=0;j<NUM_DESTINATION_POINTS;j++){
					if(pointsInExemplar[i][j]){
						clusterArrayList.add(j);
					}
				}
				clusterCollection[exemplarIndex] = new Cluster(exemplarIndex,clusterArrayList);
			}
		}
	}
	
	private static void assignClusters() {
		// TODO Auto-generated method stub
        taskAlloc = new HungarianSolver();
        taskAlloc.preProcessing(NUM_ROBOTS, NUM_CLUSTERS_MADE);
        taskAlloc.algorithm();
        botToCluster = taskAlloc.postProcessing();
        for(Entry<Integer,Integer> e : botToCluster.entrySet()){
        	if(e.getKey()<NUM_ROBOTS)
        		realloc[e.getValue()] = true;
        }
	}
	
	public static void main(String[] args) {
		logs = Logger.getLogger(CreateArena.class.getName());
		testLog = Logger.getLogger(CreateArena.class.getName());
		PropertyConfigurator.configure("log4j.properties");
		int numOfTestCases = 40;
		for(int i=0;i<numOfTestCases;i++){
			FILENAME = "inputPoints"+Integer.toString(i)+".txt";
			READ_FILE = false;
			//Reading or changing the destination points on the map
			/*if(!READ_FILE)
				logs.info("Changeing the input file "+FILENAME);
			else
				logs.info("Reading the input file "+FILENAME);*/
			setDestinationPoints(FILENAME,!READ_FILE);
			for(int algoNum=0;algoNum<2;algoNum++){
				testLog.info("Test Run "+Integer.toString(i)+" AlgoUsed "+ALGONAMES[algoNum]);
				//logs.info("Copying Binaries...");
				CopyFile.copyFile("TestBot.class", "TestBot.class");
				CopyFile.copyFile("Stationary.class", "Stationary.class");
				CopyFile.copyFile("Stationary1.class", "Stationary1.class");
				CopyFile.copyFile("Crazy.class", "Crazy.class");
				// Disable log messages from Robocode
				//logs.info("Binaries copied!! Starting robocode engine");
				
				//Setting up the Robocode engine
				RobocodeEngine.setLogMessagesEnabled(true); 
				engine.addBattleListener(new BattleObserver());
				engine.setVisible(true);
				//logs.info("engine setup complete!");
				
				
				
				//Setting up the Test bots from input file
				setTestBotPoints(TEST_BOT_FILENAME,!READ_FILE);
				
				
				//Applying the clustering algorithm
				// 1 - KMeans
				// 0 - AP
				clusterAlgorithm(ALGONAMES[algoNum],FILENAME);
				
				//Setting the Exemplar of each cluster
				exemplarToClusterIndex();
				
				//Setting up the stationary points or destination points
				setStationaryPoints();
				
				//setting up the robots and clusters
				assignTestBots();
				assignStationaryBots();
				makeClusters();
				assignClusters();
				
				battleSpec.setAllBots();
				// Run our specified battle and let it run till it is over
				engine.runBattle(battleSpec, true); // waits till the battle finishes
				// Cleanup our RobocodeEngine
				engine.close();
			}
        }
        // Make sure that the Java VM is shut down properly
        System.exit(0);
    }
	public static boolean areAllBotsKilled(){
		boolean answer =  true;
		for(int i=0;i<NUM_DESTINATION_POINTS;i++){
			answer = answer && CreateArena.deathAnnounceTable[i];
		}
		return answer;
	}
	public static boolean areAllDestBotSet() {
		// TODO Auto-generated method stub
		boolean answer =  true;
		for(int i=0;i<NUM_DESTINATION_POINTS;i++){
			answer = answer && CreateArena.destReady[i];
		}
		return answer;
	}
	
	public static int getNearestBotCluster(Vector v){
		Vector u;
		int minBotIndex = 0;
		int index = 0;
		double minBotDistance = Double.MAX_VALUE;
		for(Entry<Vector,Integer> e:vectorToBotIndex.entrySet()){
			u = e.getKey();
			index = e.getValue();
			if(v.distance(u)<minBotDistance && !deathAnnounceTable[index] && !realloc[getClusterNumber(index)]){
				minBotDistance = v.distance(u);
				minBotIndex = index;
			}
		}
		return minBotIndex;
	}
	
	public static void reAlloc(int robotIndex,Vector robotVector){
		//Greedy or Hungarian assignment
		//Better to do Greedy Case by case. Hungarian not possible when just one bot is left.
		synchronized (realloc) {
			//logs.warn("Bot number"+(robotIndex+1)+" entered into critical section!!!");
			int nearestBotIndex = getNearestBotCluster(robotVector);
			botToCluster.remove(robotIndex);
			botToCluster.put(robotIndex,getClusterNumber(nearestBotIndex));
			realloc[getClusterNumber(nearestBotIndex)] = true;
			reallocMap.put(getClusterNumber(nearestBotIndex),robotIndex);
			//logs.warn("Bot number"+(robotIndex+1)+" assigned the cluster = "+(getClusterNumber(nearestBotIndex)+1));
		}
	}
}

class BattleObserver extends BattleAdaptor {
	 
    // Called when the battle is completed successfully with battle results
    public void onBattleCompleted(BattleCompletedEvent e) {
        System.out.println("-- Battle has completed --");
        
        // Print out the sorted results with the robot names
        System.out.println("Battle results:");
        for (robocode.BattleResults result : e.getSortedResults()) {
            System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
        }
    }

    // Called when the game sends out an information message during the battle
    public void onBattleMessage(BattleMessageEvent e) {
        System.out.println("Msg> " + e.getMessage());
    }

    // Called when the game sends out an error message during the battle
    public void onBattleError(BattleErrorEvent e) {
        System.out.println("Err> " + e.getError());
    }
}
