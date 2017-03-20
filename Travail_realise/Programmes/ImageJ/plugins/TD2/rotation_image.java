import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class rotation_image implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	//Rotation de 90 degrés
	public void run(ImageProcessor ip) {
		int[][] pixels = ip.getIntArray();
		int w = ip.getWidth();
		int h = ip.getHeight();
		ImageProcessor ipRes = new ByteProcessor(h,w);
		ImagePlus imgRes = new ImagePlus(" Rotation 90 ",ipRes);
		for(int x=0; x<w; x++){
			for(int y=0; y<h; y++){
				int xprim = w/2 + (y-(h/2));
				int yprim = h/2 - (x-(w/2));
				xprim -= (w-h)/2;
				yprim += (w-h)/2;
				ipRes.putPixel(xprim, yprim, pixels[x][y]);
				//IJ.log("xprim ="+xprim);
				//IJ.log("yprim ="+yprim);
			}
		}
		imgRes.show();
		imgRes.updateAndDraw();
	}
	
	
	
	
}