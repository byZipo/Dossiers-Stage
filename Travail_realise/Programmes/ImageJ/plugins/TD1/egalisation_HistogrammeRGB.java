import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class egalisation_HistogrammeRGB implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		int[][] pixels = ip.getIntArray();
		int[][] I = new int[ip.getWidth()][ip.getHeight()];
		int[] Hi = new int[256];
		double[] Pi = new double[256];
		double[] Ri = new double[256];
		
		// remplissage de I
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int r = (pixels[i][j] & 0xFF0000)>>16; // rouge
				int g = (pixels[i][j] & 0x00FF00)>>8; // vert
				int b = (pixels[i][j] & 0x0000FF); // bleu
				double val = (double)0.3*r + 0.59*g + 0.11*b;
				I[i][j] = (int)val;
			}
		}
		
		// remplissage de Hi(n)
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int n = I[i][j];
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
		
		
		
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int r = (pixels[i][j] & 0xFF0000)>>16; // rouge
				int g = (pixels[i][j] & 0x00FF00)>>8; // vert
				int b = (pixels[i][j] & 0x0000FF); // bleu
				
				/*int intensity = I[i][j];
				double valR = r*Ri[intensity];
				double valG = g*Ri[intensity];
				double valB = b*Ri[intensity];*/
				
				
				double valR = 255*Ri[r];
				double valG = 255*Ri[g];
				double valB = 255*Ri[b];
				
				
				double valFinale = 0;
				valFinale -= 16777216;
				valFinale += (((int)valR)<<16);
				valFinale += (((int)valG)<<8);
				valFinale += ((int)valB);
				
				ip.putPixel(i,j,(int)valFinale);
			}
		}
		
		
		
		
		
	}
	
	
	
	
}