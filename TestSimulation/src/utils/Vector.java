package utils;


public class Vector {
	
	private static final int X = 0;
	private static final int Y = 1;
	private static final int DIMENSION = 2;
	private double[] array = new double[DIMENSION];
	
	public Vector(double x,double y){
		this.array[X] = x;
		this.array[Y] = y;
	}
	
	public double dotProduct(Vector other){
		return this.array[X]*other.array[X] + this.array[Y]*other.array[Y];
	}
	public double det(Vector other){
		return this.array[X]*other.array[Y] - this.array[Y]*other.array[X];
	}
	public double magnitude(){
		return Math.sqrt(this.array[X]*this.array[X] + this.array[Y]*this.array[Y]);
	}
	/**
	 * Return angle between the two vector this and other in degrees*/
	public double angleBetween(Vector other){
		double dot = this.dotProduct(other);
		double det = this.det(other);
		//System.out.println("dot = "+dot);
		//System.out.println("Magnitudes are "+other.magnitude()+" "+this.magnitude());
		//System.out.println(Math.atan2(det, dot)/Math.PI*180);
		if(this.magnitude()!=0 && other.magnitude()!=0){
			double angle = (Math.atan2(det, dot)/Math.PI)*180;
			if(angle<0){
				angle = -angle;
			}
			else{
				angle = 360 -angle;
			}
			return angle;
		}
		else
			return (this.magnitude()!=0)? this.angleBetween(new Vector(0.0,1.0)):other.angleBetween(new Vector(0.0,1.0));
	}
	
	public double distance(Vector other){
		return Math.sqrt((this.array[X]-other.array[X])*(this.array[X]-other.array[X])+(this.array[Y]-other.array[Y])*(this.array[Y]-other.array[Y]));
	}
	
	public double getX(){
		return this.array[X];
	}
	
	public double getY(){
		return this.array[Y];
	}
	
	public void setX(double x){
		this.array[X] = x;
	}
	
	public void setY(double y){
		this.array[Y] = y;
	}
	@Override
	public boolean equals(Object other){
		Vector temp = (Vector)other;
		if(temp.getX() == this.getX() && temp.getY() == this.getY())
			return true;
		else
			return false;
	} 
	@Override
	public int hashCode(){
		//System.out.println("In hashCode");
		String temp_str = Double.toString(this.array[X])+','+Double.toString(this.array[Y]);
		//System.out.println(temp_str+" "+temp_str.hashCode());
		return temp_str.hashCode();
	}
	@Override
	public String toString(){
		return "("+Double.toString(this.getX())+","+Double.toString(this.getY())+")";
	}
}
