// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Masque_Sobel implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		byte [] pixels = ( byte []) ip.getPixels();
		ImagePlus result = NewImage.createByteImage(" Filtrage Sx", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor();
		
		ImagePlus result2 = NewImage.createByteImage(" Filtrage Sy", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr2 = result2.getProcessor();
		
		byte[] pixelsr = ( byte []) ipr.getPixels();
		byte[] pixelsr2 = ( byte []) ipr2.getPixels();
		
		int[][] masquex = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masquey = {{-1,-2,-1},{0,0,0},{1,2,1}};
		int n = 1; // taille du demi - masque
		
		
		/* Sx */
		for (int y = 1; y < w-1; y++){
			for ( int x = 1; x < h-1; x++) {
				float toto = 		(float)((float)(ip.getPixelValue(x-1,y-1)*masquex[0][0]) +
									(float)(ip.getPixelValue(x,y-1)*masquex[0][1]) +
									(float)(ip.getPixelValue(x+1,y-1)*masquex[0][2]) +
									(float)(ip.getPixelValue(x-1,y)*masquex[1][0]) +
									(float)(ip.getPixelValue(x,y)*masquex[1][1]) +
									(float)(ip.getPixelValue(x+1,y)*masquex[1][2]) +
									(float)(ip.getPixelValue(x-1,y+1)*masquex[2][0]) +
									(float)(ip.getPixelValue(x,y+1)*masquex[2][1]) +
									(float)(ip.getPixelValue(x+1,y+1)*masquex[2][2]));
				
				//écrêtage
				if(toto<0)toto = 0;
				if(toto>255)toto = 255;
				ipr.putPixel(x,y,(int)toto);
			}
		}
		result.show();
		result.updateAndDraw();
		
		
		/* Sy */
		for (int y = 1; y < w-1; y++){
			for ( int x = 1; x < h-1; x++) {
				float toto = 		(float)((float)(ip.getPixelValue(x-1,y-1)*masquey[0][0]) +
									(float)(ip.getPixelValue(x,y-1)*masquey[0][1]) +
									(float)(ip.getPixelValue(x+1,y-1)*masquey[0][2]) +
									(float)(ip.getPixelValue(x-1,y)*masquey[1][0]) +
									(float)(ip.getPixelValue(x,y)*masquey[1][1]) +
									(float)(ip.getPixelValue(x+1,y)*masquey[1][2]) +
									(float)(ip.getPixelValue(x-1,y+1)*masquey[2][0]) +
									(float)(ip.getPixelValue(x,y+1)*masquey[2][1]) +
									(float)(ip.getPixelValue(x+1,y+1)*masquey[2][2]));
				
				//gestion des bortds grâce à l'écretage
				if(toto<0)toto = 0;
				if(toto>255)toto = 255;
				ipr2.putPixel(x,y,(int)toto);
			}
		}
		result2.show();
		result2.updateAndDraw();
		
		
		
		
	}
}
