// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Filtre_Moyenneur implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		byte [] pixels = ( byte []) ip.getPixels();
		ImagePlus result = NewImage.createByteImage(" Filtrage ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor();
		byte[] pixelsr = ( byte []) ipr.getPixels();
		int[][] masque = {{1 , 1, 1}, {1, 1, 1}, {1, 1, 1}};
		int n = 1; // taille du demi - masque
		
		for (int y = 1; y < w-1; y++){
			for ( int x = 1; x < h-1; x++) {
				float toto = 		(float)ip.getPixelValue(x-1,y-1)*masque[0][0] +
									ip.getPixelValue(x,y-1)*masque[0][1] +
									ip.getPixelValue(x+1,y-1)*masque[0][2] +
									ip.getPixelValue(x-1,y)*masque[1][0] +
									ip.getPixelValue(x,y)*masque[1][1] +
									ip.getPixelValue(x+1,y)*masque[1][2] +
									ip.getPixelValue(x-1,y+1)*masque[2][0] +
									ip.getPixelValue(x,y+1)*masque[2][1] +
									ip.getPixelValue(x+1,y+1*masque[2][2]);
				
				toto = (float)toto/9;
				ipr.putPixel(x,y,(int)toto);
			}
		}
		result.show();
		result.updateAndDraw();
	}
}
