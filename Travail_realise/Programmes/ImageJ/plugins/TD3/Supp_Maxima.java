// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Supp_Maxima implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		
		int S = 200; //ICI ON PEUT REGLER LE SEUILLAGE
		int w = ip.getWidth();
		int h = ip.getHeight();
		byte [] pixels = ( byte []) ip.getPixels();
		ImagePlus result = NewImage.createByteImage(" Suppression des non maximas ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor();
		
		
		int[][] pixelsr = new int[w][h];
		
		int[][] masquex = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masquey = {{-1,-2,-1},{0,0,0},{1,2,1}};
		
		int[][] tabAngles = new int[w][h];
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
				//int finalRound = Math.round(finalVal);
				
				float finalRound = (float)Math.abs(toto2)+Math.abs(toto);
				
				
				//CALCUL DES DIRECTIONS DU GRADIENT DANS L'IMAGE
				
				double angle = (double)(Math.atan(toto2/toto));
				angle = (double)(angle*180/Math.PI);
				
				if(angle<0){
					angle = (double)(angle+360);
				}
				
				/*if((angle>=0 && angle<22.5)|| (angle>=337.5 && angle<361)) angle = 0;
				else if(angle>=22.5 && angle<67.5) angle = 45;
				else if(angle>=67.5 && angle<112.5) angle = 90;
				else if(angle>=112.5 && angle<157.5) angle = 135;
				else if(angle>=157.5 && angle<202.5) angle = 180;
				else if(angle>=202.5 && angle<247.5) angle = 225;
				else if(angle>=247.5 && angle<292.5) angle = 270;
				else if(angle>=292.5 && angle<337.5) angle = 315;
				*/
				
				angle = 45*(Math.round(angle/45));
				
				//IJ.log(""+angle);
				tabAngles[x][y] = (int)angle;
				
				
				/*-----------------*/
				
				// PLUSIEURS CHOIX : ECRETAGE OU NORMALISATION, SEUILLAGE OU NON
				/* Sur l'image "photo" pour obtenir les mêmes iamges que dans le cours,
				 * On fait soit gradient,ecretage, sans seuillage --> Sobel sans seuillage
				 * soit gradient,normalisation et seuillage (on peut modifier S) --> Seuillage
				 */
				
				
				//ecretage 
				
				
				if(finalRound>255)finalRound = 255;
				if(finalRound<0)finalRound = 0;
				
				
				
				//normalisation
				//finalRound = (finalRound/8);
				//finalRound = finalRound+128;
				
				//finalRound /= 4;
				//finalRound = Math.abs(finalRound);
				
				
				/*--------------------*/
				
				//SEUILLAGE
				
				
				//if(finalRound > S) finalRound = 255;
				//if(finalRound < S) finalRound = 0;
				
				
				/*-------------------*/
				
				//DESSIN
				pixelsr[x][y] = (int)finalRound;
				ipr.putPixel(x,y,(int)finalRound);
				
			}
		}
		
		for(int i = 1; i<w-1; i++){
			for(int j=1 ;j<h-1; j++){
				int angle = tabAngles[i][j];
				int val = pixelsr[i][j];
				switch(angle){
				case 0:
					if((val < pixelsr[i+1][j]) || (val < pixelsr[i-1][j]))ipr.putPixel(i,j,0);
					break;
				case 45:
					if((val < pixelsr[i+1][j-1]) || (val < pixelsr[i-1][j+1]))ipr.putPixel(i,j,0);
					break;
				case 90:
					if((val < pixelsr[i][j-1]) || (val < pixelsr[i][j+1]))ipr.putPixel(i,j,0);
					break;
				case 135:
					if((val < pixelsr[i-1][j-1]) || (val < pixelsr[i+1][j+1]))ipr.putPixel(i,j,0);
					break;
				case 180:
					if((val < pixelsr[i-1][j]) || (val < pixelsr[i+1][j]))ipr.putPixel(i,j,0);
					break;
				case 225:
					if((val < pixelsr[i-1][j+1])|| (val < pixelsr[i+1][j-1]))ipr.putPixel(i,j,0);
					break;
				case 270:
					if((val < pixelsr[i][j-1]) || (val < pixelsr[i][j+1]))ipr.putPixel(i,j,0);
					break;
				case 315:
					if((val < pixelsr[i-1][j-1]) || (val < pixelsr[i+1][j+1]))ipr.putPixel(i,j,0);
					break;
				case 360 : 
					if((val < pixelsr[i+1][j]) || (val < pixelsr[i-1][j]))ipr.putPixel(i,j,0);
					break;
				default:
					IJ.log("ERREUR");
					break;
				}
			}
		}
		
		/*
		 * Du fait de la suppression des non maximas, les lignes des contours sont fines puisque l'on ne garde que le contour et pas le voisons des contours
		 */
		
		
		result.show();
		result.updateAndDraw();
	}
}
