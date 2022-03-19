package Modele;

import java.lang.Math;

/**
 * class immuable : au lieu de retourner l'objet ; retourner copie
 */

public class Complexe {
 private final double real;
 private final double im;
 public static final Complexe ZERO = new Complexe(0,0);
 public static final Complexe UN = new Complexe(1,0);
 public static final Complexe I = new Complexe(0,1);

 public Complexe (double r, double im) {
     this.real=r;
     this.im=im;
 }

 public double getReal() {
	 double r = real;
     return r;
 }

 public double getIm() {
	 double i = im;
     return i;
 }

 public Complexe getConjugue() {
     return new Complexe(real,-im);
 }

 public double getModule() {
     return Math.sqrt(real*real+im*im);
 }

 public double getArg() {
     return Math.atan2(im,real);
 }

 public String toString() {
     return real+" + i "+im;
 }

 public Complexe carre () {
     return new Complexe((real*real)-(im*im), 2*real*im);
 }

 public Complexe somme (Complexe c) {
     return new Complexe (this.real + c.real,this.im+c.im);
 }

 public Complexe soustraction (Complexe c) {
     return new Complexe(this.real - c.real, this.im - c.im);
 }

 public Complexe multiplication (Complexe c) {
     return new Complexe(real*c.real - im*c.im, real*c.im + im*c.real);
 }

 public Complexe division (Complexe diviseur) throws Exception {
     double denom = (diviseur.real * diviseur.real) + (diviseur.im *diviseur.im);
     if (denom != 0) {
         return new Complexe(((real * diviseur.real)+(im * diviseur.im))/denom , (((im*diviseur.real)-(real*diviseur.im))/denom));
     } else {
         throw new Exception("division par zéro");
     }
 }

 public boolean equals(Object other) {
     if (this==other) return true;
     if (other instanceof Complexe) {
         Complexe c = (Complexe) other;
         return (c.real==this.real && c.im == this.im);
     }
     return false;
 }

 // fabrique statique :
 public static Complexe fromPolarCoordinates(double rho, double theta) {
     return new Complexe(rho*Math.cos (theta),rho*Math.sin(theta));
 }

}