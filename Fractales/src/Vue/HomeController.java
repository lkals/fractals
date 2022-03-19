package Vue;

import Modele.*;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static javafx.scene.paint.Color.WHITE;


/**
 * classe HomeController permettant d'implémenter l'interface graphique
 */
public class HomeController {

    private Fractale fractale;
    private Canvas canvas;
    private GraphicsContext gc;
    private PixelWriter pw;
    private final List<Complexe> constantes;

    @FXML private SplitPane splitPane;
    @FXML private StackPane fractalPane;
    @FXML private Pane anchorBelow;
    @FXML private Label messageLabel;
    @FXML private Button computeButtonThreads;
    @FXML private Button computeButtonParallel;

    @FXML private TextField longueurTextField;
    @FXML private TextField hauteurTextField;
    @FXML private TextField sizeTextField;
    @FXML private TextField iterMaxTextField;
    @FXML private CheckBox defaultCheckBox;

    //TODO: exception: launch sans valeur sélectionnée
    @FXML private ChoiceBox<String> constanteBox;
    @FXML private TextField realTextField;
    @FXML private TextField imTextField;
    @FXML private RadioButton monoradioButton;
    @FXML private RadioButton defaultradioButton;
    @FXML private RadioButton bwradioButton;
    
   /* @FXML private Button downloadButton;
    @FXML private TextField downloadTextField;
    */

    @FXML private RadioButton juliaRadioButton;
    @FXML private RadioButton mandelRadioButton;

    @FXML private Button leftButton;
    @FXML private Button rightButton;
    @FXML private Button upButton;
    @FXML private Button downButton;

    private ToggleGroup groupe;
    private ToggleGroup typeFractale;
    private RadioButton selected;

    public HomeController() {
        constantes = Julia.getConstantes();
    }


    @FXML
    public void initialize() {
        groupe = new ToggleGroup();
        typeFractale = new ToggleGroup();
        mandelRadioButton.setToggleGroup(typeFractale);
        juliaRadioButton.setToggleGroup(typeFractale);
        juliaRadioButton.setSelected(true);

        monoradioButton.setToggleGroup(groupe);
        defaultradioButton.setToggleGroup(groupe);
        bwradioButton.setToggleGroup(groupe);
        defaultradioButton.setSelected(true);

        canvas = new Canvas();
        fractalPane.getChildren().add(canvas);
        canvas.widthProperty().bind(fractalPane.widthProperty());
        canvas.heightProperty().bind(fractalPane.heightProperty());

        gc = canvas.getGraphicsContext2D();
        pw = gc.getPixelWriter();

        /*Lorsque le textfield est vide, le bouton compute est désactivé*/
        computeButtonThreads.disableProperty().bind(
                ( Bindings.createBooleanBinding( () ->
                iterMaxTextField.getText().trim().isEmpty(), iterMaxTextField.textProperty()
                ))
                        .or( Bindings.createBooleanBinding( () ->
                                sizeTextField.getText().trim().isEmpty(), sizeTextField.textProperty()
                        ))
        );
        
        for (Complexe c : constantes) {
            constanteBox.getItems().add(c.toString());
        }
    }

    public void handleDeplacementVertical (double decalage) {
        double real=getConstante().getReal();
        double im=getConstante().getIm();
        int iterMax = 0;
        iterMax = Integer.parseInt(iterMaxTextField.getText());
        Point a = new Point(fractale.getA().getX(),fractale.getA().getY());
        Point b = new Point(fractale.getB().getX(),fractale.getB().getY());

        a.moveY(decalage);
        b.moveY(decalage);
        double pas = fractale.getPas();

        selected = (RadioButton) typeFractale.getSelectedToggle();
        if (selected.getText().equals("Mandelbrot")) {
	        try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildMandelbrot();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }else {
        	try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildJulia();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }initFractalParallel();
    }

    private void afficheWarning(String w) {
        messageLabel.setText("ATTENTION : "+w);
        messageLabel.setTextFill(Color.RED);
    }
    
    public void handleDeplacementLateral (double decalage) {
        double real=getConstante().getReal();
        double im=getConstante().getIm();
        int iterMax = 0;
        iterMax = Integer.parseInt(iterMaxTextField.getText());
        Point a = new Point(fractale.getA().getX(),fractale.getA().getY());
        Point b = new Point(fractale.getB().getX(),fractale.getB().getY());

        a.moveX(decalage);
        b.moveX(decalage);
        double pas = fractale.getPas();

        selected = (RadioButton) typeFractale.getSelectedToggle();
        if (selected.getText().equals("Mandelbrot")) {
	        try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildMandelbrot();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }else {
        	try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildJulia();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }initFractalParallel();
    }

    @FXML
    public void handleUp(ActionEvent actionEvent) {
        handleDeplacementVertical(-0.2);
    }

    @FXML
    public void handleRight(ActionEvent actionEvent) {
        handleDeplacementLateral(0.2);
    }

    @FXML
    public void handleLeft(ActionEvent actionEvent) {
        handleDeplacementLateral(-0.2);
    }

    @FXML
    public void handleDown(ActionEvent actionEvent) {
        handleDeplacementVertical(0.2);
    }

    public void handleZoomAvant(ActionEvent actionEvent) {
        double real=getConstante().getReal();
        double im=getConstante().getIm();
        int iterMax = 0;
        iterMax = Integer.parseInt(iterMaxTextField.getText());
        Point a = new Point(fractale.getA().getX(),fractale.getA().getY());
        Point b = new Point(fractale.getB().getX(),fractale.getB().getY());
        a.mult(0.8);
        b.mult(0.8);
        double pas = fractale.getPas();
        System.out.println("pas :"+pas);
        pas *= 0.8;
        System.out.println("new pas :"+pas);
        selected = (RadioButton) typeFractale.getSelectedToggle();
        if (selected.getText().equals("Mandelbrot")) {
	        try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildMandelbrot();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }else {
        	try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildJulia();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }initFractalParallel();
    }

    public void handleZoomArriere(ActionEvent actionEvent) {
        double real=getConstante().getReal();
        double im=getConstante().getIm();
        int iterMax = 0;
        iterMax = Integer.parseInt(iterMaxTextField.getText());
        Point a = new Point(fractale.getA().getX(),fractale.getA().getY());
        Point b = new Point(fractale.getB().getX(),fractale.getB().getY());
        a.mult(1.0/0.8);
        b.mult(1.0/0.8);
        double pas = fractale.getPas();
        System.out.println("pas :"+pas);
        pas /= 0.8;
        System.out.println("new pas :"+pas);   
        selected = (RadioButton) typeFractale.getSelectedToggle();
        if (selected.getText().equals("Mandelbrot")) {
	        try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildMandelbrot();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }else {
        	try {
	            fractale = new Fractale.Builder().constante(real, im).pas(pas).iterMax(iterMax).rectangle(a,b).buildJulia();
	        } catch (IllegalArgumentException e1) {
	            System.out.println("erreur fract");
	            afficheWarning(e1.getMessage());
	            return;
	        }
        }initFractalParallel();
    }

    public Complexe getConstante() {
        for (Complexe c:constantes) {
            if (constanteBox.getValue().toString().equals(c.toString())) {
                return new Complexe(c.getReal(),c.getIm());
            }
        }return null;
    }

    /**
     * Cette methode est appelée lorsque l'on clique sur le bouton compute. Ce comportement est défini dans le fichier FXML
     */
    @FXML
    private void handleComputeButtonThreadsClick() {
        if (gatherFractalInfos()) {
            initFractalThreads();
        } else {
            return;
        }
    }
    @FXML
    public void handleComputeButtonParallelClick() {
        if (gatherFractalInfos()) {
            initFractalParallel();
        } else {
            return;
        }
    }
    
   /* @FXML
    private void handleDownloadButtonClick() {
        selected = (RadioButton) groupe.getSelectedToggle();
    	if (gatherFractalInfos()) {
    		fractale.setPathfile(downloadTextField.toString());
    		switch (selected.getText()) {
    			case "par défaut":
    				fractale.downloadImage(1);
    				break;
    			case "black & white" :
    				fractale.downloadImage(2);
    				break;
    			case "monochrome" :
    				fractale.downloadImage(0);
    				break;
    		}
    	}else return;
    }*/

    @FXML
    private void UpdateDefaultValues() {
        System.out.println("default");
        iterMaxTextField.setText("1000");
        sizeTextField.setText("1000");
        constanteBox.setValue(constantes.get(0).toString());
    }

    private void clear() {
        System.out.println("appel clear");
        Pixel[][] rectangle = fractale.getArrayPixels();
        for (int i = 0; i < rectangle.length; i++) {
            for (int j = 0; j < rectangle[i].length; j++) {
                Color c = WHITE;
                pw.setColor(i,j,c);
            }
        }
    }

    /**
     * draw
     */
    private void draw () {

        Pixel[][] rectangle = fractale.getArrayPixels();
        selected = (RadioButton) groupe.getSelectedToggle();
        for (int i = 0; i < rectangle.length; i++) {
            for (int j = 0; j < rectangle[i].length; j++) {
                Pixel p = rectangle[i][j];
                switch(selected.getText()) {
                    case "par défaut":
                        p.setColor((max,ind) -> new java.awt.Color(((ind + 5) << 16) | ((ind + 50) << 8) | (ind + 130)));
                        break;
                    case "black & white":
                        p.setColor((max,ind) -> {
                            if (ind<(max)-1) {
                                return new java.awt.Color(0,0,0);
                            } else {
                                return new java.awt.Color(255,255,255);
                            }
                        });
                        break;
                    case "monochrome":
                        p.setColor((max,ind) -> new java.awt.Color((255*ind)/max,0,0));
                        break;
                }
                // TODO: suivant config de couleur utilisée, utiliser streams
                int r = rectangle[i][j].getCouleur().getRed();
                int g = rectangle[i][j].getCouleur().getGreen();
                int b = rectangle[i][j].getCouleur().getBlue();
                Color c = Color.rgb(r,g,b);
                // Conversion de Color de awt à Color de javafx
                pw.setColor(i,j,c);
            }
        }
    }

    private boolean gatherFractalInfos() {
        double real = 0;
        double im = 0;
        if (realTextField.getText() != "" && imTextField.getText() != "") {
        	real = Double.parseDouble(realTextField.getText());
        	im = Double.parseDouble(imTextField.getText());
        }else {
		    for (Complexe c:constantes) {
		    	if (constanteBox.getValue().toString().equals(c.toString())) {
		    		real = c.getReal();
		    		im = c.getIm();
		    	}
		    }
        }String pathFile = "Default.png";
        int iterMax = 0;
        iterMax = Integer.parseInt(iterMaxTextField.getText());
        if (fractale!=null) {
            clear();
        }
        Fractale f=null;
        RadioButton selected = (RadioButton) typeFractale.getSelectedToggle();
        if (selected.getText().equals("Mandelbrot")) {
            try {
                f = new Mandelbrot.Builder().constante(real, im).iterMax(iterMax).nbPointsLongueur(Integer.parseInt(sizeTextField.getText())).nbPointsHauteur(Integer.parseInt(sizeTextField.getText())).pathFile(pathFile).buildMandelbrot();
            } catch (IllegalArgumentException e1) {
                System.out.println("exception");
                afficheWarning(e1.getMessage());
                return false;
            }
        } else {
            try {
                f = new Julia.Builder().constante(real, im).iterMax(iterMax).nbPointsLongueur(Integer.parseInt(sizeTextField.getText())).nbPointsHauteur(Integer.parseInt(sizeTextField.getText())).pathFile(pathFile).buildJulia();
            } catch (IllegalArgumentException e1) {
                System.out.println("exception");
                afficheWarning(e1.getMessage());
                return false;
            }
        }
        fractale=f;
        return true;
    }

    /**
     * Calcul d'une fractale, en utilisant la classe FractaleRenderer et la méthode static
     * et 4 threads (fixé)
     * On peut après change de couleur en instantané avec les boutons
     * monoradioButton, defaultradioButton, et bwradioButton
     */
    private void initFractalThreads() {
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        try {
            FractalRenderer.calcule(fractale,4);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }monoradioButton.setOnAction( event -> draw());
        defaultradioButton.setOnAction( e -> draw());
        bwradioButton.setOnAction(e -> draw());

        // redessine quand on change la taille du pane
        //canvas.widthProperty().addListener(event -> draw(canvas));
       //canvas.heightProperty().addListener(event -> draw(canvas));
        draw();
        Calendar calendar2 = Calendar.getInstance();

        Date endDate = calendar2.getTime();
        long sumDate = endDate.getTime() - startDate.getTime();
        System.out.println("time :" + sumDate + " ms");
    }

    private void initFractalParallel() {
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        try {
            fractale.computeParallel();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        monoradioButton.setOnAction( event -> draw());
        defaultradioButton.setOnAction( e -> draw());
        bwradioButton.setOnAction(e -> draw());

        draw();
        Calendar calendar2 = Calendar.getInstance();

        Date endDate = calendar2.getTime();
        long sumDate = endDate.getTime() - startDate.getTime();
        System.out.println("time :" + sumDate + " ms");
    }




}