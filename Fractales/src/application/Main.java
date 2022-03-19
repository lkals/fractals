package application;

import Modele.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main extends Application {

	private Parent root;
	private Stage primaryStage;

	@SuppressWarnings("exports")
	@Override
    public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
    	root = FXMLLoader.load(getClass().getResource("/Vue/home.fxml"));
    	Scene scene = new Scene (root);
    	primaryStage.setScene(scene);
    	primaryStage.setTitle("Let's make some fractals !!!!!");
    	//primaryStage.getIcons().add(new Image("/image/icons/glacier.png"));
    	primaryStage.show();
	}

	/**
	 *
	 * @throws Exception
	 */
	public void quitMainScreen() throws Exception{
		this.primaryStage.close();
	}

	/**
	 * fonction servant uniquement pour le premier test, non utilisée pour le rendu final
	 * @param z0
	 * @param iterconst
	 * @return
	 */
    public static int divergenceIndex (Complexe z0, int iterconst) {
        int ite=0;
        Complexe zn=z0;
        Complexe constante = new Complexe (-0.7269,0.1889);
        // sortie de boucle si divergence
        while (ite < iterconst-1 && zn.getModule() <= 2) {
            zn = zn.carre().somme(constante);

            ite++;
        }return ite;
    }
    
    public static void test () throws IOException {
    	 double xmin   = -4.0;
         double ymin   = -2.0;
         double width  =  8.0;
         double height =  8.0;
         int n = 801;
         int ITERS  = 2000;
         BufferedImage img=new BufferedImage(n, n, BufferedImage.TYPE_INT_RGB);
         for (int col = 0; col < n; col++){
             for (int row = 0; row < n; row++){
                 double x = xmin + col * width / n;
                 double y = ymin + row * height / n;
                 Complexe z = new Complexe(x, y);
                 int t = divergenceIndex(z, ITERS);
                 int r=t+5;
                 int g =t+130;
                 int b =t+50;
                 int coul = (r << 16) | (g << 8) | b;
                 img.setRGB(col,row,coul);
             }
         }File f = new File("MyFile.png");
         ImageIO.write(img, "PNG", f);
		 System.exit(0);
	}
    
    public static double castD(String s) {
    	try {
    		return Double.parseDouble(s);
    	}catch (NumberFormatException e) {
    		System.out.println("Ceci n'est pas un double : " + s);
    		return -1;
    	}
    }
    
    public static int castI(String s) {
    	try {
    		return Integer.parseInt(s);
    	}catch (NumberFormatException e) {
    		System.out.println("Ceci n'est pas un int : " + s);
    		return -1;
    	}
    }
    
    public static void terminal(String [] args) throws IOException {
    	double real = -0.7269;
    	double im = 0.1889;
    	Point a = new Point(-1,-1);
    	Point b = new Point(1,1);
        double pas = 0.01;
        int largeur = 201;
        int hauteur = 201;
        int iterMax = 1000;
    	String pathFile = "Default.png";
    	String ensemble = "Julia";
    	int color = 1;

    	for (int i = 1; i < args.length; i++) {
    		switch (args[i]) {
    		case "-c" :
    			real = castD(args[i+1]);
    			im = castD(args[i+2]);
    			i += 2;
    			break;
    		case "-e" :
    			ensemble = args[i+1];
    			i++;
    			break;
    		case "-m" :
    			largeur = castI(args[i+1]);
    			hauteur = castI(args[i+1]);
    			i++;
    			break;
    		case "-i" :
    			iterMax = castI(args[i+1]);
    			i++;
    			break;
    		case "-color" :
    			color = castI(args[i+1]);
    		case "-file" :
    			pathFile = args[i+1];
    			i++;
    			break;
    		default : usage(); break;
    		}
    	}

        int matrixSize = largeur*hauteur;
    	System.out.println("hauteur  : "+hauteur);
        Fractale fractale = null;

		if (ensemble.equals("Julia")) {
			try {
				fractale = new Julia.Builder().constante(real, im).iterMax(iterMax).nbPointsLongueur(largeur).nbPointsHauteur(hauteur).pathFile(pathFile).buildJulia();
			} catch (IllegalArgumentException e) {
				System.out.println("attention :"+e.getMessage());
			}
		}else if (ensemble.equals("Mandelbrot")) {
			try {
				fractale = new Mandelbrot.Builder().constante(real, im).iterMax(iterMax).nbPointsLongueur(largeur).nbPointsHauteur(hauteur).pathFile(pathFile).buildMandelbrot();
			} catch (IllegalArgumentException e) {
				System.out.println("attention :"+e.getMessage());
			}
		}try {
			FractalRenderer.calcule(fractale, 4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}fractale.downloadImage(color);
    }
    
    public static String usage() {
    	return "Voici les usages de \"Let's make some fractals !!!!!\" :\n"
    			+ "-test : permet de lancer le 1e test effectué\n"
    			+ "-interface : permet de lancer l'interface graphique\n"
    			+ "-terminal : permet de lancer la version en ligne de commande avec les valeurs par défauts (équivalent à -test). Pour modifier ces valeurs, il faut ajouter :\n"
    			+ "  -c real im : la constante c = real + i * im dans f(z) = z + c\n"
    			+ "  -e ens : définission de l'ensemble de fractale voulu : Julia ou Mandelbrot\n"
    			+ "  -m width hight : définit la longueur d'un côté de la matrice\n"
    			+ "  -i iterMax : définit le nombre maximum d'itération du calcul de divergence\n"
    			+ "  -color methodColor : 1:coloriage multicolore, 2:monochrome, autre:noir et blanc\n"
    			+ "  -file pathFile : définit le fichier dans lequel vous voulez enregistrer votre image.";
    }

    public static void main(String[] args) throws IOException {
		if (args.length > 0) {
			if (args[0].equals("-test")) test();
			else if (args[0].equals("-interface")) launch(args);
			else if (args[0].equals("-terminal")) terminal(args);
			else System.out.println(usage());
		}else System.out.println(usage());
		System.exit(0);
	}
}