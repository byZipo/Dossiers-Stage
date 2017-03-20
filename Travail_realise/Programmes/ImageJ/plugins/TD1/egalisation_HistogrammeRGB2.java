import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class egalisation_HistogrammeRGB2 implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		int[][] pixels = ip.getIntArray();
		int[][][] I = new int[ip.getWidth()][ip.getHeight()][3];
		int[][] Hi = new int[256][3];
		double[][] Pi = new double[256][3];
		double[][] Ri = new double[256][3];
		
		// remplissage de I
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int r = (pixels[i][j] & 0xFF0000)>>16; // rouge
				int g = (pixels[i][j] & 0x00FF00)>>8; // vert
				int b = (pixels[i][j] & 0x0000FF); // bleu
				
				I[i][j][0] = r;
				I[i][j][1] = g;
				I[i][j][2] = b;
			}
		}
		
		// remplissage de Hi(n)
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int nR = I[i][j][0];
				int nG = I[i][j][1];
				int nB = I[i][j][2];
				Hi[nR][0]++;
				Hi[nG][1]++;
				Hi[nB][2]++;
			}
		}
				
		// remplissage de Pi(n)
		for(int i = 0 ; i < Pi.length ; i++){
			Pi[i][0] = (double)Hi[i][0]/(ip.getWidth()*ip.getHeight());
			Pi[i][1] = (double)Hi[i][1]/(ip.getWidth()*ip.getHeight());
			Pi[i][2] = (double)Hi[i][2]/(ip.getWidth()*ip.getHeight());
		}
				
				
		// remplissage de Ri(n)
		for(int i = 0 ; i < Ri.length ; i++){
			double tmpR = 0;
			double tmpG = 0;
			double tmpB = 0;
			for(int j = 0 ; j <= i ; j++){
				tmpR += Pi[j][0];
				tmpG += Pi[j][1];
				tmpB += Pi[j][2];
				
			}
			Ri[i][0] = tmpR;
			Ri[i][1] = tmpG;
			Ri[i][2] = tmpB;
		}
		
		
		
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				int r = (pixels[i][j] & 0xFF0000)>>16; // rouge
				int g = (pixels[i][j] & 0x00FF00)>>8; // vert
				int b = (pixels[i][j] & 0x0000FF); // bleu
				
				//int intensity = I[i][j];
				//double valR = r*Ri[intensity];
				//double valG = g*Ri[intensity];
				//double valB = b*Ri[intensity];
				
				
				double valR = 255*Ri[r][0];
				double valG = 255*Ri[g][1];
				double valB = 255*Ri[b][2];
				
				
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