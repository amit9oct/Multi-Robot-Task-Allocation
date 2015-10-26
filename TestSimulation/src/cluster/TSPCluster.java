package cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import api.Christofides;
import arena.CreateArena;

import utils.Vector;


class TSPData{
	ArrayList<Integer> orderedTraversal = new ArrayList<Integer>();
	double length = 0.0;
	double getLength(){
		if(this.length == 0.0){
			int len = orderedTraversal.size();
			for(int i=0;i<len-1;i++){
				this.length += CreateArena.getDistanceBetween(orderedTraversal.get(i), orderedTraversal.get(i+1));
			}
		}
		return this.length;
	}
	public TSPData(ArrayList<Integer> orderedTraversal){
		this.orderedTraversal = orderedTraversal;
		this.length = 0.0;
	}
}

public class TSPCluster implements IClusterAlgorithm{
	
	ArrayList<Vector> pointList = new ArrayList<Vector>();
	ArrayList<TSPData> tspData = new ArrayList<TSPData>();
	ArrayList<ArrayList<Integer>> clusterList = new ArrayList<ArrayList<Integer>>();
	TSPData minTSP = null;
	int numOfClusterMade;
	@Override
	public void preProcessing(String fileName) {
		//Take the input from file and then move ahead.
		BufferedReader br = null;
		String strRead =  null;
		try {
			br =  new BufferedReader(new FileReader(fileName));
			while((strRead=br.readLine())!=null){
				String[] strReadArr = strRead.split(",");
				double x = Double.parseDouble(strReadArr[0]);
				double y = Double.parseDouble(strReadArr[1]);
				pointList.add(new Vector(x, y));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Compute the TSP using chritofides
		int numOfPoints = pointList.size();
		double[][] costMatrix = new double[numOfPoints][numOfPoints];
		for(int start = 0;start<numOfPoints;start++){
			for(int j=0;j<numOfPoints;j++){
				costMatrix[start][j] = CreateArena.getDistanceBetween(start, j);
				costMatrix[j][start] = costMatrix[start][j];
			}
			for(int i=0;i<numOfPoints;i++){
				if(i!=start){
					for(int j=i;j<numOfPoints;j++){
						if(j!=i){
							costMatrix[i][j] = CreateArena.botToVector.get(i).distance(CreateArena.botToVector.get(j));
							costMatrix[j][i] = costMatrix[i][j];
						}
						else{
							costMatrix[i][j] = 0.0;
						}
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
			ArrayList<Integer> orderedTraversal = new ArrayList<Integer>();
			orderedTraversal.add(start);
			for(int i=1;i<numOfPoints;i++){
				orderedTraversal.add(shortestPath[i]);
			}
			tspData.add(new TSPData(orderedTraversal));
		}
		for(int i=0;i<numOfPoints;i++){
			if(minTSP==null){
				minTSP = (TSPData)tspData.get(i);
			}
			else if(minTSP.getLength() > tspData.get(i).length){
				minTSP = tspData.get(i);
			}
		}
	}

	@Override
	public void algorithm(int clusterSize) {
		// TODO Auto-generated method stub
		int len = minTSP.orderedTraversal.size();
		double totalLen = minTSP.getLength();
		double fragmentLen = totalLen/(double)clusterSize;
		double coveredDis = 0.0;
		for(int i=0;i<len-1;){
			coveredDis = 0.0;
			ArrayList<Integer> clusterSet = new ArrayList<Integer>();
			while(coveredDis<=fragmentLen){
				clusterSet.add(minTSP.orderedTraversal.get(i));
				coveredDis += CreateArena.getDistanceBetween(minTSP.orderedTraversal.get(i), minTSP.orderedTraversal.get(i+1));
				i += 1;
			}
			clusterList.add(clusterSet);
		}
	}

	@Override
	public HashMap<Integer, Integer> postProcessing() {
		// TODO Auto-generated method stub
		return null;
	}

}
