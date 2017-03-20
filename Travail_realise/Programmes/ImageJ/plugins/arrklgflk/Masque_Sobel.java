// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Masque_Sobel implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run ( ImageProcessor ip){
		int w = ip.getWidth ();
		int h = ip.getHeight ();
		ImagePlus result = NewImage.createByteImage (" Sobel SX ", w, h, 1,NewImage.FILL_BLACK );
		ImagePlus result2 = NewImage.createByteImage (" Sobel SY ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor ();
		ImageProcessor ipr2 = result2.getProcessor ();
		int[][] masqueSx = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masqueSy = {{-1,-2,-1},{0,0,0},{1,2,1}};
		int n = (int)masqueSx.length/2; // taille du demi masque
		float valeur = 0, valeur2 = 0;
		

		for (int y = n; y < w - n; y++){
			for ( int x = n; x < h - n; x++) {
				for(int i = -n ; i <= n ; i++){
					for(int j = -n ; j <= n ; j++){	
							valeur += ip.getPixelValue(y+i,x+j)*masqueSx[j+n][i+n];
							valeur2 += ip.getPixelValue(y+i,x+j)*masqueSy[j+n][i+n];
					}
				}
				if(valeur < 0) valeur = 0;
				if(valeur > 255) valeur = 255;				
				if(valeur2 < 0) valeur2 = 0;	
				if(valeur2 > 255) valeur2 = 255;
					
				ipr.putPixelValue(y,x,(int)valeur);	
				ipr2.putPixelValue(y,x,(int)valeur2);
				valeur = 0;
				valeur2 = 0;

			}
		}
		result.show();
		result.updateAndDraw();
		result2.show();
		result2.updateAndDraw();
	}
}
