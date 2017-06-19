// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Seuillage_hysteresis implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		//ICI ON PEUT REGLER LES SEUILS
		int Sb = 40; //seuil haut
		int Sh = 80; // seuil bas
		int w = ip.getWidth();
		int h = ip.getHeight();
		byte [] pixels = ( byte []) ip.getPixels();
		ImagePlus result = NewImage.createByteImage("Seuillage hysteresis ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor();
		
		
		int[][] pixelsr = new int[w][h];
		
		int[][] masquex = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masquey = {{-1,-2,-1},{0,0,0},{1,2,1}};
		
		
		
		/* Sx */
		for (int x = 1; x < w-1; x++){
			for ( int y = 1; y < h-1; y++) {
				float toto = 		(float)((float)(ip.getPixelValue(x-1,y-1)*masquex[0][0]) +
									(float)(ip.getPixelValue(x,y-1)*masquex[1][0]) +
									(float)(ip.getPixelValue(x+1,y-1)*masquex[2][0]) +
									(float)(ip.getPixelValue(x-1,y)*masquex[0][1]) +
									(float)(ip.getPixelValue(x,y)*masquex[1][1]) +
									(float)(ip.getPixelValue(x+1,y)*masquex[2][1]) +
									(float)(ip.getPixelValue(x-1,y+1)*masquex[0][2]) +
									(float)(ip.getPixelValue(x,y+1)*masquex[1][2]) +
									(float)(ip.getPixelValue(x+1,y+1)*masquex[2][2]));
				
				
				
				float toto2 = 		(float)((float)(ip.getPixelValue(x-1,y-1)*masquey[0][0]) +
									(float)(ip.getPixelValue(x,y-1)*masquey[1][0]) +
									(float)(ip.getPixelValue(x+1,y-1)*masquey[2][0]) +
									(float)(ip.getPixelValue(x-1,y)*masquey[0][1]) +
									(float)(ip.getPixelValue(x,y)*masquey[1][1]) +
									(float)(ip.getPixelValue(x+1,y)*masquey[2][1]) +
									(float)(ip.getPixelValue(x-1,y+1)*masquey[0][2]) +
									(float)(ip.getPixelValue(x,y+1)*masquey[1][2]) +
									(float)(ip.getPixelValue(x+1,y+1)*masquey[2][2]));
				
				
				//GRADIENT
				float tmp = (float)toto*toto;
				float tmp2 = (float)toto2*toto2;
				float tmp3 = (float)tmp+tmp2;
				float finalVal = (float)Math.sqrt(tmp3);
				int finalRound = Math.round(finalVal);
				
				
				
				//DESSIN
				pixelsr[x][y] = (int)finalRound;
				ipr.putPixel(x,y,(int)finalRound);
				
			}
		}
		
		for(int i = 1; i<w-1; i++){
			for(int j=1 ;j<h-1; j++){
				int val = pixelsr[i][j];
				if(val<Sb)ipr.putPixel(i,j,0); //Si norme(i,j) < Sb --> pixel noir
				else if(val>Sh)ipr.putPixel(i,j,255); //Si norme(i,j) > Sh --> contour
				else{ //Si norme(i,j) between Sb and Sh --> contour si un voisin est un contour 
					if(pixelsr[i][j-1]>Sh || pixelsr[i-1][j]>Sh || pixelsr[i+1][j]>Sh || pixelsr[i][j+1]>Sh || pixelsr[i+1][j+1]>Sh || pixelsr[i-1][j-1]>Sh || pixelsr[i+1][j-1]>Sh || pixelsr[i-1][j+1]>Sh)ipr.putPixel(i,j,255);
					else ipr.putPixel(i,j,0);
				}
			}
		}
		
		
		
		
		result.show();
		result.updateAndDraw();
	}
}
