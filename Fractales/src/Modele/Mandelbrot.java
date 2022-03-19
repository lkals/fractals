package Modele;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Mandelbrot extends Julia {

	protected Mandelbrot(Builder builder) {
		super(builder);
	}
	
	@Override
	public int indexDivergence(BiFunction<Complexe, Complexe, Complexe> fonction, Complexe zn) {
        int i = 0;
        Complexe z = zn;
        while (i < getIterMax() - 1 && z.getModule() <= 2) {
            z = fonction.apply(z,zn);
            i++;
        }
        return i;
    }

	@Override
	protected void computeRectangle (int id, int debut, int fin) {
		ArrayList <Pixel> ret = new ArrayList<>();
        for (int i=debut; i<=fin; i++) {
            for (int j=0; j<getNbPointsLongueur(); j++) {
            	double x0 = getConstante().getReal() - getLongueur()/2 + getLongueur()*i/getNbPointsLongueur();
            	double y0 = getConstante().getIm() - getHauteur()/2 + getHauteur()*j/getNbPointsHauteur();
                int ind = indexDivergence((z, c) -> z.carre().somme(c) , new Complexe(x0,y0));
                if (ind<getIterMax()-1) {
                    ret.add(new Pixel(getIterMax(),ind,i,j));
                } else {
                    ret.add(new Pixel(getIterMax(),ind,i,j));
                }
            }
        }
        getPixels().put(id,ret);
	}
}
