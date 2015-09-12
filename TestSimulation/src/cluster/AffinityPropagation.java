package cluster;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import utils.Vector;


import arena.CreateArena;

import fr.lri.tao.apro.data.points.Point;
import fr.lri.tao.apro.data.points.Points;
import fr.lri.tao.apro.data.points.PointsProvider;
import fr.lri.tao.apro.hiap.HiAP;

public class AffinityPropagation implements IClusterAlgorithm {

	private Points pointList = new Points();
	private int numberOfPoints = 0;
	private int height, width, clusterSize;
	private static final int WORKERS = 1, THREADS_PER_WORKER = 1, WORKER_ITERS = 20000, WAP_ITERS = 20000;
	private HashMap<Integer,Integer> pointCluster = null;
	private static final double DAMP_FACTOR = 0.25;
	private static final int NUM_OF_BOTS = CreateArena.NUM_ROBOTS;
	
	public AffinityPropagation(int height,int width,int clusterSize){
		this.height = height;
		this.width = width;
		this.clusterSize = clusterSize;
	}
	
	@Override
	public void preProcessing(String fileName) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		String currentLine = null;
		numberOfPoints = 0;
		try {
			int i = 0;
			while((currentLine = br.readLine()) != null){
				//double[] inputArray = new double[2];
				double[] inputArray = new double[2+NUM_OF_BOTS];
				inputArray[0] = Double.parseDouble(currentLine.split(",")[0]);
				inputArray[1] = Double.parseDouble(currentLine.split(",")[1]);
				Vector v = new Vector(inputArray[0],inputArray[1]);
				for(int j=0;j<NUM_OF_BOTS;j++){
					inputArray[2+j] = v.distance(CreateArena.RoboDestination[j].getVector());
				}
				Point temp = new Point((long)i,inputArray);
				pointList.add(temp);
				i++;
			}
			numberOfPoints = i;
			br.close();
		} catch (NumberFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		CreateArena.logs.info("****AP PointList****");
		CreateArena.logs.info(pointList.toString());
	}

	@Override
	public void algorithm() {

		PointsProvider pointsPro = new PointsProvider(pointList);
		HiAP hiap = new HiAP(pointsPro, clusterSize , WORKERS,THREADS_PER_WORKER,WORKER_ITERS,WAP_ITERS);
		pointCluster = new HashMap<Integer,Integer>();
		hiap.setDamping(DAMP_FACTOR);
		try {
			hiap.run();
		} catch (Exception e) {

			//e.printStackTrace();
		}
		for(int i=0;i<this.numberOfPoints;i++){
			try{
				pointCluster.put((int)pointList.get(i).getId(),(int)hiap.getExemplar((int)pointList.get(i).getId()).getId());
			}catch(Exception e){
				break;
			}
		}
	}

	@Override
	public HashMap<Integer, Integer> postProcessing() {
		return this.pointCluster;
	}

}
