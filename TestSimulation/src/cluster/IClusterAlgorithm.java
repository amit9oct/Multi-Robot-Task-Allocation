package cluster;

import java.util.HashMap;

public interface IClusterAlgorithm {
	/**
	 * For pre-processing input and converting HashMap to desired input for algorithm 
	 */
	public void preProcessing(String fileName);
	
	/**
	 * Run clustering algorithm
	 */
	public void algorithm();
	
	/**
	 * Post-processing to return HashMap after clustering
	 */
	public HashMap<Integer, Integer> postProcessing();
	
}
