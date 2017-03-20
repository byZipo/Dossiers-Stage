import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class image_Filp implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		int[][] pixels = ip.getIntArray();
		int w = ip.getWidth();
		if(w%2==0){ //si c'est pair on a un pas de moins à réaliser (il n'ya a pas de milieu)
            w--;		
		}
		for(int x=0; x<=w/2; x++){
			for(int y=0; y<ip.getHeight(); y++){
				int tmp = pixels[x][y];
				pixels[x][y] = pixels[w-x-1][y];
				pixels[w-x-1][y] = tmp;
				ip.putPixel(x, y, pixels[x][y]);
				ip.putPixel((w-x-1), y, pixels[w-x-1][y]); 
			}
		}
	}
	
	
	
	
}