package task;

import java.util.HashMap;

public interface ITaskAllocation {
	/**
	 * 
	 * @param numRobots
	 * @param numClusters
	 * Calculates cost matrix for given set of Robots and Task Clusters
	 */
	void preProcessing(int numRobots,int numClusters);
	
	void algorithm();
	
	/**
	 * Returns a hashMap for every Bot being mapped to its appropriate cluster
	 */
	HashMap<Integer, Integer> postProcessing();
}
