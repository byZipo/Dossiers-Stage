import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class mesure_Contraste implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		byte[] pixels=(byte[]) ip.getPixels();
		double moyenne = 0;
		int sommePix = 0;
		for (int i=0; i<pixels.length; i++){
			int grey=pixels[i] & 0xFF; 
			sommePix += grey;
		}
		moyenne = (double)sommePix/pixels.length;
		double invLargHaut = (double)1/(ip.getWidth()*ip.getHeight());
		double tmp = 0;
		for (int j=0; j<pixels.length; j++){
			tmp += (pixels[j] - moyenne)*(pixels[j] - moyenne); 
		}
		
		tmp *= invLargHaut;
		
		double contraste = Math.sqrt(tmp);
		IJ.log("Moyenne = "+moyenne);
		IJ.log("Contraste = "+contraste);
	}
	
	
	
	
}