package Modele;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * classe abstraite Fractale
 */

public abstract class Fractale {

    public abstract int indexDivergence(BiFunction<Complexe, Complexe, Complexe> fonction, Complexe zn);
    protected abstract void computeRectangle(int id,int debut, int fin);
    public abstract Map<Integer, List<Pixel>> getPixels();
    public abstract int getNbPointsHauteur();
    public abstract int getNbPointsLongueur();
    public abstract Pixel [][] getArrayPixels();
    public abstract int getIterMax();
    public abstract Point getA();
    public abstract Point getB();
    public abstract double getPas();
	public abstract String getPathFile();
	public abstract void downloadImage(int color);
	public abstract void setPathfile(String string);

    /**
     * classe static Builder
     */
    public static class Builder {
    	String pathFile = "MyFile.png";
        double pas = 0;
        int iterMax = 0;
        Complexe constante = Complexe.ZERO;
        int nbPointsHauteur=0;
        int nbPointsLongueur=0;
        Point a=new Point(-1,-1);
        Point b=new Point(1,1);

        public Builder () {}

        public Builder pathFile(String path) {
        	pathFile = path;
        	return this;
        }
        public Builder pas (double val) {
            pas = val;
            return this;
        }

        public Builder iterMax (int val) {
            iterMax = val;
            return this;
        }

        public Builder constante (double real, double im) {
            constante = new Complexe(real,im);
            return this;
        }

        public Builder nbPointsHauteur(int h) {
            nbPointsHauteur=h;
            return this;
        }

        public Builder nbPointsLongueur(int l) {
            nbPointsLongueur=l;
            double longueur = Math.abs(b.getX() - a.getX());
            pas = longueur/(nbPointsLongueur-1);
            System.out.println("pas builder :"+pas);
            return this;
        }

        
        public Builder rectangle (Modele.Point a, Modele.Point b) {
            /*a est le point en bas à gauche dans le plan
        	et b est le point en haut à droit*/
            this.a = a;
            this.b = b;
        	double longueur = Math.abs(b.getX() - a.getX());
            double hauteur = Math.abs(b.getY() - a.getY());
        	nbPointsLongueur = (int) (longueur / pas) + 1;
        	nbPointsHauteur = (int) (hauteur / pas) +1;
        	return this;
        }

        public Julia buildJulia() {
            if (Math.abs(b.getX() - a.getX()) == Math.abs(b.getY() - a.getY()) && nbPointsLongueur!=nbPointsHauteur) {
                throw new IllegalArgumentException("On se place dans un carré complexe : hauteur=largeur!");
            }return new Julia(this);
        }
        
        public Mandelbrot buildMandelbrot() {
            if (Math.abs(b.getX() - a.getX()) == Math.abs(b.getY() - a.getY()) && nbPointsLongueur!=nbPointsHauteur) {
                throw new IllegalArgumentException("On se place dans un carré complexe : hauteur=largeur!");
            }return new Mandelbrot(this);
        }
    }
}
