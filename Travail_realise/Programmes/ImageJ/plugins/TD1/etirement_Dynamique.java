import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class etirement_Dynamique implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		int[][] pixels = ip.getIntArray();
		int[] LUT = new int[256];
		
		
		int max = 0;
		int min = 10000;
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				if(pixels[i][j] > max) max = pixels[i][j];
				if(pixels[i][j] < min) min = pixels[i][j];
			}
		}
		IJ.log("AVANT traitement : min = "+min+" max = "+max);
		for(int ng = 0; ng < 256; ng++){
			LUT[ng] = 255*(ng-min)/(max-min);
		}
		/*for(int i = 0; i< ip.getWidth(); i++){
			for(int j = 0; j< ip.getHeight();j++){
				pixels[i][j] = LUT[pixels[i][j]];
				ip.putPixel(i, j, pixels[i][j]); 
			}
		}*/
		ip.applyTable(LUT);
	}
	
	
	
	
}