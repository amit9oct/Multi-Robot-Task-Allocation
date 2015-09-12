
package testApro;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.lri.tao.apro.data.points.Point;
import fr.lri.tao.apro.data.points.Points;
import fr.lri.tao.apro.data.points.PointsProvider;
import fr.lri.tao.apro.hiap.Exemplars;
import fr.lri.tao.apro.hiap.HiAP;

public class AproTestClass {
	public double[][] coordinates = null;
	public static Points pointList = new Points();
	public int numberOfPoints = 0;
	public AproTestClass(int height, int width,int numberOfPoints,double[][] array){
		coordinates = new double[numberOfPoints][2];
		this.numberOfPoints = numberOfPoints;
		for(int i=0 ; i<numberOfPoints ;i++){
			coordinates[i][0] = (double)array[i][0];
			coordinates[i][1] = (double)array[i][1];
			pointList.add(new Point((long)i,coordinates[i]));
		}
		System.out.println(pointList.toString());
	}
	
	public Map<Long,Long> algo(int clustersSize){
		PointsProvider pointsPro = new PointsProvider(pointList);
		HiAP hiap = new HiAP(pointsPro, clustersSize , 1 , 1, 20000, 20000);
		Map<Long,Long> pointCluster = new HashMap<Long,Long>();
		hiap.setDamping(0.25);
		try {
			hiap.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		for(int i=0;i<this.numberOfPoints;i++){
			try{
				pointCluster.put(pointList.get(i).getId(),hiap.getExemplar((int)pointList.get(i).getId()).getId());
			}catch(Exception e){
				break;
			}
		}
		return pointCluster;
	}
}
