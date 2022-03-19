package Modele;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

/**
 * classe représentant un ensemble Julia
 */
public class Julia extends Fractale {


     Julia (int nbPointsLongueur, int nbPointsHauteur, double pas,int iterMax, Point a,Point b,Complexe cte) {
        super(nbPointsLongueur,nbPointsHauteur,pas,iterMax,a,b,cte);
    }
    
    public int indexDivergence(BiFunction<Complexe, Complexe, Complexe> fonction, Complexe zn) {
        int i = 0;
        while (i < iterMax - 1 && zn.getModule() <= 2) {
            zn = fonction.apply(zn,constante);
            i++;
        }return i;
    }

    public void computeParallel() {
        //todo: error Caused by: java.lang.ArrayIndexOutOfBoundsException: Index 1999 out of bounds for length 1999
        // pb de zoom je pense...
        IntStream.range(0,nbPointsHauteur).parallel().forEach(x-> {
            IntStream.range(0,nbPointsHauteur).parallel().forEach( y-> {
                int ind = indexDivergence((z, c) -> z.carre().somme(c),coordToComplex(x,y));
                indexes[x][y]=new Pixel(iterMax,ind,x,y);
            });
        });
    }
    
    protected void computeRectangle(int id,int debut, int fin) {
        ArrayList <Pixel> ret = new ArrayList<>();
        for (int i=debut; i<=fin; i++) {
            for (int j=0; j<nbPointsLongueur; j++) {
                int ind = indexDivergence((z, c) -> z.carre().somme(c), coordToComplex(i,j));
                indexes[i][j]=new Pixel(iterMax,ind,i,j);
                if (ind<iterMax-1) {
                    ret.add(new Pixel(iterMax,ind,i,j));
                } else {
                    ret.add(new Pixel(iterMax,ind,i,j));
                }
            }
        }
        pixels.put(id,ret);
    }

    public Map<Integer,List<Pixel>> getPixels() {
        return pixels;
    }

    /*public Pixel [][] getArrayPixels() {
        Pixel [][] ret = new Pixel[nbPointsHauteur][nbPointsLongueur];
        int ctr=0;
        for (var entry : pixels.entrySet()) {
            if (entry.getKey()==ctr) {
                for (Pixel p : entry.getValue()) {
                    int i=p.getPosI();
                    int j=p.getPosJ();
                    ret[i][j]=p;
                }
            }ctr++;
        }return ret;
    }*/

    @Override
    public Point getA() {
        return a;
    }

    @Override
    public Point getB() {
        return b;
    }

    @Override
    public double getPas() {
        return pas;
    }

    public int getIterMax() {return iterMax;}
    public String getPathFile() {return pathFile;}
    public int getNbPointsHauteur() {
        return nbPointsHauteur;
    }


    public int getNbPointsLongueur() {
        return nbPointsLongueur;
    }

    public Complexe coordToComplex(int i, int j) {
        return new Complexe(a.getX() + i * (getHauteur() / nbPointsHauteur), a.getY() + j * (getLongueur() / nbPointsLongueur));
    }

    public double getHauteur() {
        return Math.abs(b.getY() - a.getY());
    }
    public double getLongueur() {
        return Math.abs(b.getX() - a.getX());
    }
    public Complexe getConstante() {return constante;}
    
    public void downloadImage (int color) {
    	Pixel[][] rectangle = getArrayPixels();
    	BufferedImage img=new BufferedImage(rectangle.length, rectangle[0].length, BufferedImage.TYPE_INT_RGB);
    	for (int i = 0; i < rectangle.length; i++) {
    		for (int j = 0; j < rectangle[i].length; j++) {
    			Pixel p = rectangle[i][j];
				switch(color) {
					case 1:
						p.setColor((max,ind) -> 
						new java.awt.Color(((ind + 5) << 16) | ((ind + 50) << 8) | (ind + 130)));
						break;
					case 2:
						p.setColor((max,ind) -> new java.awt.Color((255*ind)/max,0,0));
						break;
					default:
						p.setColor((max,ind) -> {
							if (ind<(max)-1) {
								return new java.awt.Color(0,0,0);
							} else {
								return new java.awt.Color(255,255,255);
							}
						});
						break;
				}img.setRGB(i,j,p.getCouleur().getRGB());
    		}
    	}File file = new File(getPathFile());
        try {
			ImageIO.write(img, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}