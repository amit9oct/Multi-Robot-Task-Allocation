package cluster;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import arena.CreateArena;

import utils.Vector;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;


public class KMeansAlgo implements IClusterAlgorithm{

	private Dataset data = null;
	private Dataset[] clusters = null;
	private int clusterCount;
	private HashMap<Integer, Integer> clusterMap = null; 
	
	public Vector instanceToVector(Instance instance) throws Exception{
		Iterator<Double> itr = instance.iterator();
		double[] attributes = new double[2];
		int i=0;
		while(itr.hasNext()){
			if(i>=2)
				throw new Exception("More than 2 attributes encountered in KMeans!");
			attributes[i] = itr.next();
			i++;
		}
		return new Vector(attributes[0], attributes[1]);
		
	}
	
	@Override
	public void preProcessing(String filename) {
		// TODO Auto-generated method stub
		try {
			data = FileHandler.loadDataset(new File(filename),",");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void algorithm() {
		// TODO Auto-generated method stub
		Clusterer km = new KMeans(10);
		this.clusters = km.cluster(this.data);
		this.clusterCount = this.clusters.length;
	}

	@Override
	public HashMap<Integer, Integer> postProcessing() {
		clusterMap = new HashMap<Integer,Integer>();
		for(int i=0;i<clusterCount;i++){
			Iterator<Instance> itr = clusters[i].iterator();
			if(itr.hasNext()){
				Instance exemplar = itr.next();
				Vector ex = null;
				try {
					ex = instanceToVector(exemplar);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int exIndex = CreateArena.coordinatesToIndex.get(ex);
				clusterMap.put(exIndex,exIndex);
				Instance ins = null;
				int insIndex;
				while(itr.hasNext()){
					ins = itr.next();
					Vector v = null;
					try {
						v = instanceToVector(ins);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					insIndex = CreateArena.coordinatesToIndex.get(v);
					clusterMap.put(insIndex, exIndex);
				}
				
			}
			
		}
		return clusterMap;
	}

}
