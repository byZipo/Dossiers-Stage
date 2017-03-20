// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 
import java.awt.*;
import ij.plugin.filter.PlugInFilterRunner;
import ij.util.Tools;
import ij.macro.*;
import ij.plugin.frame.Recorder;
import ij.plugin.ScreenGrabber;
import ij.gui.*;
// pour classe ImageProcessor

public class Change_Echelle implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;
	}
	
	public void run( ImageProcessor ip){
	// dialogue permettant de fixer la valeur du facteur d’echelle
	GenericDialog gd = new GenericDialog( " Facteur d’echelle ",IJ.getInstance() );
	
	gd.addSlider( " facteur ", 1.0 , 10.0 , 2.0 );
	gd.showDialog();
	if ( gd.wasCanceled() ) {
		IJ.error( " PlugIn cancelled " );
	return;
	}
	
	double ratio = ( double ) gd.getNextNumber();
	int w = ip.getWidth();
	int h = ip.getHeight();
	int[][] pixels = ip.getIntArray();
	
	IJ.log("Ratio = "+ratio);
	
	int wprim = (int)ratio*w;
	int hprim = (int)ratio*h;
	
	
	ImagePlus result = NewImage.createByteImage(" Retailler " ,wprim,hprim, 1, NewImage.FILL_BLACK );
	ImageProcessor ipr = result.getProcessor();
	byte[] pixelsr = (byte[]) ipr.getPixels();
	
	/* Question 3*/
	/*for (int x = 0; x < wprim; x++) {
		for (int y = 0; y < hprim; y++) {
			double a = (double)x/ratio;
			double b = (double)y/ratio;
			ipr.putPixel(x, y, pixels[(int)a][(int)b]);
		}
	}*/
	
	/* Question 4 */
	for (int x = 0; x < wprim-ratio; x++) {
		for (int y = 0; y < hprim-ratio; y++) {
			double a1 = (double)x/ratio;
			double b1 = (double)y/ratio;
			int a = (int)a1;
			int b = (int)b1;
			
			double ip1 = (double)(pixels[a][b] + (double)((b1-b)*(pixels[a][b+1]-pixels[a][b])));
			double ip2 = (double)(pixels[a+1][b] + (double)((b1-b)*(pixels[a+1][b+1]-pixels[a+1][b])));
			
			int Ip = (int) (ip1+(a1-a)*(ip2-ip1));
			//IJ.log("ip1= "+ip1+" ip2= "+ip2+" Ip= "+Ip);
			
			ipr.putPixel(x,y,Ip);
		}
	}
	
	
	result.show();
	result.updateAndDraw();
	}
}
