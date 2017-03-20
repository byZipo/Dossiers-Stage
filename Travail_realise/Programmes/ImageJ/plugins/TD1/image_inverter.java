import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class image_inverter implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		byte[] pixels=(byte[]) ip.getPixels();
		float moyenne = 0;
		int sommePix = 0;
		for (int i=0; i<pixels.length; i++){
			int grey=pixels[i] & 0xFF; 
			sommePix += grey;
		}
		moyenne = sommePix/pixels.length;
		IJ.log("Moyenne = "+moyenne);
	}
	
	
	
	
}