package task;

import java.util.HashMap;

import taskAllocation.HungarianAlgorithm;

import arena.CreateArena;

public class HungarianSolver implements ITaskAllocation{

	private double[][] costMatrix = null;
	private int sizeOfCostMatrix;
	int[] assignment = null;
	
	@Override
	public void preProcessing(int numRobots, int numClusters) {
		// TODO Auto-generated method stub
		sizeOfCostMatrix = Math.max(numRobots, numClusters);
		int len = sizeOfCostMatrix;
		costMatrix = new double[len][len];
		for(int i=0;i<len;i++){
			for(int j=0;j<len;j++){
				if(i<numRobots && j<numClusters){
					costMatrix[i][j] = CreateArena.RoboDestination[i].getVector().distance(CreateArena.clusterCollection[j].getCentroid()) + CreateArena.clusterCollection[j].getTspDistance();
				}
				else{
					costMatrix[i][j] = 0.0;
				}
			}
		}
		
	}

	@Override
	public void algorithm() {
		// TODO Auto-generated method stub
		HungarianAlgorithm hungarianAlgo = new HungarianAlgorithm(costMatrix);
		assignment = hungarianAlgo.execute();
	}

	@Override
	public HashMap<Integer, Integer> postProcessing() {
		// TODO Auto-generated method stub
		HashMap<Integer, Integer> botToClusterAssigned = new HashMap<Integer,Integer>();
		for(int i=0;i<sizeOfCostMatrix;i++){
			botToClusterAssigned.put(i,assignment[i]);
		}
		printAssignment();
		return botToClusterAssigned;
	}
	
	public void printAssignment(){
		int len = this.assignment.length;
		for(int i=0;i<len;i++){
			CreateArena.logs.info("Robot"+ (i+1) +" assigned to cluster number" +(this.assignment[i]+1));
		}
	}
}
