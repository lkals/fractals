package Modele;
import Vue.HomeController;

import java.util.ArrayList;
import java.util.List;

/**
 * classe permettant de calculer une fractale en utilisant plusieurs Threads
 */
public class FractalRenderer {
    /**
     *
     * @param fractale
     * @param nbThreads
     * @return
     * @throws InterruptedException
     */
    public static Fractale calcule(Fractale fractale, int nbThreads) throws InterruptedException {
        int hauteur = fractale.getNbPointsHauteur();

        double size = hauteur/nbThreads;
        int borne = (int) Math.floor(size);
        int reste = (hauteur%nbThreads)-1;
        int d=0;
        int f=borne;
        List<Thread> list = new ArrayList<>();
        for(int i = 0; i < nbThreads; i++) {
                if (i == nbThreads - 1) {
                    Thread thread = new Thread(new MyRunnable(i, fractale, d, f + reste));
                    list.add(thread);
                    thread.start();
                } else {
                    Thread thread = new Thread(new MyRunnable(i, fractale, d, f));
                    list.add(thread);
                    thread.start();
                    d = f + 1;
                    f = borne * (i + 2);
                }
        }for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }return fractale;
    }

    static class MyRunnable implements Runnable {
        int id;
        Fractale fractal;
        int debut;
        int fin;
        
        MyRunnable( int id, Fractale fractal, int debut, int fin ) {
            this.id = id;
            this.fractal = fractal;
            this.debut=debut;
            this.fin=fin;
        }

        @Override
        public void run() {
            fractal.computeRectangle(id,debut,fin);
        }
    }
}
