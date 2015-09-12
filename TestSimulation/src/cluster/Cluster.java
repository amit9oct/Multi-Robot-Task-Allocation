package cluster;

import java.util.ArrayList;

import api.Christofides;
import arena.CreateArena;

import utils.Vector;

public class Cluster {
	private int clusterId;
	private Vector centroid = null;
	private double tspDistance;
	private ArrayList<Integer> clusterPoints;
	private double[][] distance = null;
	private int clusterSize;
	
	public Cluster(int clusterId,ArrayList<Integer> clusterArrayList){
		this.clusterId = clusterId;
		this.clusterPoints = new ArrayList<Integer>(clusterArrayList);
		this.clusterSize = clusterPoints.size();
		this.calculateCentroid();
		this.calculateTspDistance();
	}

	private void calculateTspDistance() {
		// TODO Auto-generated method stub
		int len  = this.clusterSize;
		if(len==1){
			this.tspDistance = 0.0;
			return;
		}
		distance = new double[len][len];
		for(int i=0;i<len;i++){
			distance[i][i]  = 0.0;
			for(int j=i+1;j<len;j++){
				distance[i][j] = CreateArena.destinations[clusterPoints.get(i)].getVector().distance(CreateArena.destinations[clusterPoints.get(j)].getVector());
				distance[j][i] = distance[i][j];
			}
		}
		/*System.out.println("Distance Matrix for Cluster ="+ this.getClusterId());
		for(int i=0;i<len;i++){
			for(int j=0;j<len;j++){
				System.out.print(distance[i][j]+" ");
			}
			System.out.println();
		}*/
		Christofides ch = null;
        try{
                ch = new Christofides(true);
        }
        catch(Exception e){
                e.printStackTrace();
                System.exit(0);
        }
		int[] shortestPath = ch.solve(distance);
		tspDistance = 0.0;
		for(int i=1;i<len;i++){
			tspDistance += distance[shortestPath[i]][shortestPath[i-1]];
		}
	}

	private void calculateCentroid() {
		// TODO Auto-generated method stub
		int len = this.clusterSize;
		double x = 0.0, y = 0.0;
		Vector v = null;
		for(int i=0;i<len;i++){
			v = CreateArena.destinations[clusterPoints.get(i)].getVector();
			x += v.getX();
			y += v.getY();
		}
		x /= len;
		y /= len;
		centroid = new Vector(x, y);
	}
	
	public int getClusterSize(){
		return this.clusterSize;
	}
	
	public int getClusterId(){
		return this.clusterId;
	}
	
	public Vector getCentroid(){
		return this.centroid;
	}
	
	public double getTspDistance(){
		return this.tspDistance;
	}
	
	public ArrayList<Integer> getClusterPoints(){
		ArrayList<Integer> clusterP = new ArrayList<Integer>(clusterPoints);
		return clusterP;
	}
}
