

import java.awt.Color;
import ij.process.*;
import java.util.*;

public class IntImage {

	/**
	 * La largeur de l'image.
	 */
	private int myWidth;

	/**
	 * La hauteur de l'image.
	 */
	private int myHeight;

	/**
	 * Ce tableau contient les pixels de l'image.
	 */
	private int[] myPixels;

	/**
	 * Renvoie l'indice du pixel de coordonnées (x, y) dans le tableau myPixels.
	 */
	private int indexOf (int x, int y) {
		return y*myWidth + x;
	}

	/**
	 * Construit une image de largeur width et de hauteur height.
	 */
	public IntImage (int width, int height) {
		myWidth = width;
		myHeight = height;
		myPixels = new int[width*height];
	}


	/**
	 * Retourne la valeur minimale de l'image.
	 */ 
	public int getMin() {
		int min = getPixel( 0, 0 );
		for(int y=0 ; y < getHeight() ; ++y)
			for(int x=0 ; x < getWidth() ; ++x) 
				if ( getPixel( x, y ) < min )
					min = getPixel( x, y );
		return min;
	}

	/**
	 * Retourne la valeur maximale de l'image.
	 */ 
	public int getMax() {
		int max = getPixel(0, 0);
		for(int y=0 ; y<getHeight() ; ++y)
			for(int x=0 ; x<getWidth() ; ++x) 
				if ( getPixel(x, y ) > max )
					max = getPixel( x, y );
		return max;
	}

	/**
	 * Renvoie la largeur de l'image.
	 */
	public int getWidth() {
		return myWidth;
	}

	/**
	 * Renvoie la hauteur de l'image.
	 */
	public int getHeight() {
		return myHeight;
	}

	/**
	 * Positionne la valeur du pixel de coordonnées (x, y) à la valeur
	 * value.
	 */
	public void setPixel(int x, int y, int value) {
		if( (x<0) || (getWidth() <= x) || (y<0) || (getHeight() <= y) )
			throw new IllegalArgumentException();

		myPixels[indexOf(x, y)] = value;
	}

	/**
	 * Renvoie la valeur du pixel situé aux coordonnées (x, y).
	 */
	public int getPixel(int x, int y) {
		if(x < 0)
			x = 0;
		else if (getWidth() <= x)
			x = getWidth()-1;

		if(y < 0) 
			y = 0;
		else if (getHeight() <= y)
			y = getHeight() - 1;

		return myPixels[indexOf(x, y)];
	}


	  /**
     * Cree une image couleur representant chaque entier par une
     * couleur différente, les premieres couleurs etant specifiees par
     * [first_colors].
     *
     * @param first_colors un tableau donnant les couleurs des
     * premiers entiers ou 'null'.
     *
     * @return une nouvelle image couleur.
     */
	
	public ColorProcessor createRandomColorImage(Color[] first_colors){
		int w = this.getWidth();
		int h = this.getHeight();
		int min = this.getMin();
		int max = this.getMax();
		System.out.println( " createRandomColorImage min=" + min + " max=" + max );
		Color[] t_color = new Color[max - min + 1];
		int c = 0;
		// Premieres couleurs affectees.
		if (first_colors != null)
			for ( ; c < first_colors.length; c++ )
				t_color[c] = first_colors[c];
		Random r = new Random();
		// Couleurs aleatoires.
		for (; c < t_color.length; c++){
			Color color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
			t_color[c] = color;
		}
		ColorProcessor ip = new ColorProcessor(w, h);
		for ( int x = 0; x < w; x++ )
			for ( int y = 0; y < h; y++ ){
				int v = this.getPixel(x, y);
				Color col = t_color[v-min];
				ip.putPixel(x, y, (col.getRed()<<16)+(col.getGreen()<<8)+col.getBlue());
			}
		return ip;
	}
}

