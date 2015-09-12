package pointGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomPointGenerator {
	private static Random random = new Random();
	private int numberOfPoints = 0;
	private String filename = null;
	private int height, width;
	public RandomPointGenerator(int numberOfPoints,String filename,int height,int width){
		this.numberOfPoints = numberOfPoints;
		this.filename = filename;
		this.height = height;
		this.width = width;
	}
	public double[][] generate(){
		double[][] answer = new double[this.numberOfPoints][2];
		try
		{	
			File file = new File(this.filename);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i=0;i<numberOfPoints;i++){
				double x = ((double)random.nextInt(this.width));
				double y = ((double)random.nextInt(this.height));
				answer[i][0] = x;
				answer[i][1] = y;
				bw.write(x+","+y+"\n");
			}
			bw.close();
			System.out.println("All the random points written in the file "+this.filename+" Done!!!");

		}catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}
}
