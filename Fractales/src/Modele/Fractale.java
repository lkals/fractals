package Modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * classe abstraite Fractale
 */

public abstract class Fractale {

    protected final int nbPointsHauteur;
    protected final int nbPointsLongueur;
    protected final double pas;
    protected final int iterMax;
    protected final Point a;
    protected final Point b;
    protected String pathFile;
    protected final Complexe constante;
    protected Pixel[][] indexes;
    protected Map<Integer,List<Pixel>> pixels= new HashMap<>();

    public Fractale(int nbPointsLongueur, int nbPointsHauteur, double pas,int iterMax, Point a,Point b,Complexe cte) {
        this.nbPointsHauteur=nbPointsHauteur;
        this.nbPointsLongueur=nbPointsLongueur;
        this.pas=pas;
        this.iterMax=iterMax;
        this.a=a;
        this.b=b;
        constante=cte;
        indexes = new Pixel[nbPointsHauteur][nbPointsLongueur];
    }


    /**
     * Computes the divergence index of a number
     * @param fonction, a function which returns a complex number
     * @param zn a complex number
     * @return
     */
    public abstract int indexDivergence(BiFunction<Complexe, Complexe, Complexe> fonction, Complexe zn);

    /**
     * Computes the divergence indexes for a certain range
     * @param id
     * @param debut
     * @param fin
     */
    protected abstract void computeRectangle(int id,int debut, int fin);

    public abstract void computeParallel();

	public abstract void downloadImage(int color);

    public static List<Complexe> getConstantes() {
        List<Complexe> constantes = new ArrayList<>();
        constantes.add(new Complexe(-0.7269,0.1889));
        constantes.add(new Complexe(0.3,0.5));
        constantes.add(new Complexe(0.285 , 0.01 ));
        constantes.add(new Complexe(0.285 , 0.013 ));
        constantes.add(new Complexe(-1.417022285618 , 0.0099534));
        constantes.add(new Complexe(-0.038088 , 0.9754633 ));
        constantes.add(new Complexe(-1.476,0));
        constantes.add(new Complexe(-0.4 ,0.6 ));
        constantes.add(new Complexe(-0.8 , 0.156 ));
        return constantes;
    }

    public abstract int getNbPointsHauteur();

   

    public Pixel[][] getArrayPixels() {
        return indexes;
    }

    public abstract Point getA();

    public abstract Point getB();

    public abstract double getPas();

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
            }return new Julia(nbPointsLongueur,nbPointsHauteur,pas,iterMax,a,b,constante);
        }
        
        public Mandelbrot buildMandelbrot() {
            if (Math.abs(b.getX() - a.getX()) == Math.abs(b.getY() - a.getY()) && nbPointsLongueur!=nbPointsHauteur) {
                throw new IllegalArgumentException("On se place dans un carré complexe : hauteur=largeur!");
            }return new Mandelbrot(nbPointsLongueur,nbPointsHauteur,pas,iterMax,a,b,constante);
        }
    }
}
