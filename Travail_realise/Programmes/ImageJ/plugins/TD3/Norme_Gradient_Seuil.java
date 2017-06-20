	// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Norme_Gradient_Seuil implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		
		int S = 40; //ICI ON PEUT REGLER LE SEUILLAGE
		/* AVEC normalisation
		 * Pour barbara : S = 20 donne de bons résultats
		 * Pour micors : idem S = 20
		 * Pour photo : S = 40
		 * Pour souris : S = 20
		 * Pour synth : S = 40
		 */
		int w = ip.getWidth();
		int h = ip.getHeight();
		byte [] pixels = ( byte []) ip.getPixels();
		ImagePlus result = NewImage.createByteImage(" Gradient avec ou sans seuillage ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor();
		
		
		byte[] pixelsr = ( byte []) ipr.getPixels();
		
		int[][] masquex = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masquey = {{-1,-2,-1},{0,0,0},{1,2,1}};
		int n = 1; // taille du demi - masque
		
		
		/* Sx */
		for (int x = 1; x < w-1; x++){
			for ( int y = 1; y < h-1; y++) {
				float toto = 		(float)((float)(ip.getPixelValue(x-1,y-1)*masquex[0][0]) +
									(float)(ip.getPixelValue(x,y-1)*masquex[0][1]) +
									(float)(ip.getPixelValue(x+1,y-1)*masquex[0][2]) +
									(float)(ip.getPixelValue(x-1,y)*masquex[1][0]) +
									(float)(ip.getPixelValue(x,y)*masquex[1][1]) +
									(float)(ip.getPixelValue(x+1,y)*masquex[1][2]) +
									(float)(ip.getPixelValue(x-1,y+1)*masquex[2][0]) +
									(float)(ip.getPixelValue(x,y+1)*masquex[2][1]) +
									(float)(ip.getPixelValue(x+1,y+1)*masquex[2][2]));
				
				
				
				float toto2 = 		(float)((float)(ip.getPixelValue(x-1,y-1)*masquey[0][0]) +
									(float)(ip.getPixelValue(x,y-1)*masquey[0][1]) +
									(float)(ip.getPixelValue(x+1,y-1)*masquey[0][2]) +
									(float)(ip.getPixelValue(x-1,y)*masquey[1][0]) +
									(float)(ip.getPixelValue(x,y)*masquey[1][1]) +
									(float)(ip.getPixelValue(x+1,y)*masquey[1][2]) +
									(float)(ip.getPixelValue(x-1,y+1)*masquey[2][0]) +
									(float)(ip.getPixelValue(x,y+1)*masquey[2][1]) +
									(float)(ip.getPixelValue(x+1,y+1)*masquey[2][2]));
				
				
				//GRADIENT
				float tmp = (float)toto*toto;
				float tmp2 = (float)toto2*toto2;
				float tmp3 = (float)tmp+tmp2;
				float finalVal = (float)Math.sqrt(tmp3);
				int finalRound = Math.round(finalVal);
				
				/*-----------------*/
				
				// PLUSIEURS CHOIX : ECRETAGE OU NORMALISATION, SEUILLAGE OU NON
				/* Sur l'image "photo" pour obtenir les mêmes iamges que dans le cours,
				 * On fait soit gradient,ecretage, sans seuillage --> Sobel sans seuillage
				 * soit gradient,normalisation et seuillage (on peut modifier S) --> Seuillage
				 */
				
				
				//ecretage 
				/*
				if(finalRound>255)finalRound = 255;
				if(finalRound<0)finalRound = 0;
				*/
				
				
				//normalisation
				
				finalRound /= 4;
				finalRound = Math.abs(finalRound);
				
				/*--------------------*/
				
				//SEUILLAGE
				
				if(finalRound > S) finalRound = 255;
				if(finalRound < S) finalRound = 0;
				
				/*-------------------*/
				
				//DESSIN
				
				ipr.putPixel(x,y,(int)finalRound);
				
			}
		}
		result.show();
		result.updateAndDraw();
	}
}
