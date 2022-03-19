package Modele;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.function.BiFunction;

import javax.imageio.ImageIO;

/**
 * classe représentant un ensemble Julia
 */
public class Julia extends Fractale {
	private final double pas;
    private final int iterMax;
    private final Point a;
    private final Point b;
    private final int nbPointsHauteur;
    private final int nbPointsLongueur;
    private String pathFile;
    private final Complexe constante;
    private Map<Integer,List<Pixel>> pixels= new HashMap<>();


    protected Julia (Builder builder) {
        pas = builder.pas;
        iterMax = builder.iterMax;
        constante = builder.constante;
        pathFile = builder.pathFile;
        a = builder.a;
        b = builder.b;
        nbPointsLongueur = builder.nbPointsLongueur;
        nbPointsHauteur = builder.nbPointsHauteur;
    }
    
    public int indexDivergence(BiFunction<Complexe, Complexe, Complexe> fonction, Complexe zn) {
        int i = 0;
        while (i < iterMax - 1 && zn.getModule() <= 2) {
            zn = fonction.apply(zn,constante);
            i++;
        }return i;
    }
    
    protected void computeRectangle(int id,int debut, int fin) {
        ArrayList <Pixel> ret = new ArrayList<>();
        for (int i=debut; i<=fin; i++) {
            for (int j=0; j<nbPointsLongueur; j++) {
                int ind = indexDivergence((z, c) -> z.carre().somme(c), coordToComplex(i,j));
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

    public Pixel [][] getArrayPixels() {
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
    }

    public static void printArray(Pixel [][] arr) {
        System.out.println("[");
        for (int i=0; i<arr.length;i++){
            System.out.print("[");
            for (int j=0; j<arr[i].length;j++) {
                if (j==arr[i].length-1) {
                    System.out.print("["+arr[i][j]+"]");
                } else {
                    System.out.print("["+arr[i][j]+"],");
                }
            }System.out.println("]");
        }System.out.println("]");
    }
    
    public void setPathfile(String s) {
    	pathFile = s;
    }

    public double getPas() {return pas;}
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

    public Point getA() {return a;}
    public Point getB() {return b;}
    public Complexe getConstante() {return constante;}

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