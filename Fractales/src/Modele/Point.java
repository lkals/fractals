package Modele;

/**
 * classe représentant un nombre réel dans le plan
 */
public class Point{
	private double x;
	private double y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX() {return this.x;}
	public double getY() {return this.y;}

	public void setX(double x) {
		this.x=x;
	}

	public void setY(double y) {
		this.y=y;
	}

	public void moveX(double decalage) {
		x=x+decalage;
	}

	public void moveY(double decalage) {
		y=y+decalage;
	}

	public void mult(double facteur) {
		x=x*facteur;
		y=y*facteur;
	}
}