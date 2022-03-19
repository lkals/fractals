package Modele;
import java.awt.*;
import java.util.function.BiFunction;

/**
 * classe représentant un Pixel
 */
public class Pixel {

    private final int itermax;
    private final int index;
    private Color col;
    private final int posI;
    private final int posJ;

    /**
     *
     * @param itermax
     * @param ind
     * @param i
     * @param j
     */
    public Pixel(int itermax, int ind, int i, int j) {
            this.itermax=itermax;
            index=ind;
            col = new Color(((ind+5) << 16) | ((ind+50) << 8) | (ind+130));
            posI=i;
            posJ=j;
    }

    /**
     *
     * @param choix
     */
    @SuppressWarnings("exports")
	public void setColor(BiFunction<Integer,Integer,Color> choix) {
        col = choix.apply(itermax, index);
    }

    public void setColor(int r,int g, int b) {
        col=new Color(r,g,b);
    }

    public Color getCouleur() {
                return col;
        }

    public int getPosI() {
        return posI;
    }

    public String toString() {
            return "i:"+posI+", j:"+posJ+", index:"+index;
    }

    public int getPosJ() {
        return posJ;
    }


}
