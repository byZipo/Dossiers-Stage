import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class egalisation_Histogramme implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		int[][] pixels = ip.getIntArray();
		int[] Hi = new int[256];
		double[] Pi = new double[256];
		double[] Ri = new double[256];
		
		// remplissage de Hi(n)
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int n = pixels[i][j];
				Hi[n]++; 
			}
		}
		
		// remplissage de Pi(n)
		for(int i = 0 ; i < Pi.length ; i++){
			Pi[i] = (double)Hi[i]/(ip.getWidth()*ip.getHeight());
		}
		
		
		// remplissage de Ri(n)
		for(int i = 0 ; i < Ri.length ; i++){
			double tmp = 0;
			for(int j = 0 ; j <= i ; j++){
				tmp += Pi[j];
			}
			Ri[i] = tmp;
		}
		
		
		for(int i = 0; i< ip.getWidth(); i++){
			for(int j = 0; j< ip.getHeight();j++){
				double val = 255*Ri[pixels[i][j]];
				ip.putPixel(i, j, (int)val); 
			}
		}
		
		
		
		
		
		
		
	}
	
	
	
	
}