// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Seuillage_hysteresis implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}




	public void run ( ImageProcessor ip){


		GenericDialog gd = new GenericDialog ( " SEUIL SB ",IJ.getInstance () );
	
		gd.addSlider ( " facteur ", 0.0 , 255.0 , 1.0 );
		gd.showDialog ();
		if ( gd.wasCanceled () ) {
			IJ.error ( " PlugIn cancelled " );
		return ;
		}

		double seuilB = ( double ) gd.getNextNumber ();
	
		
		GenericDialog gd2 = new GenericDialog ( " SEUIL SH ",IJ.getInstance () );
	
		gd2.addSlider ( " facteur ", 0.0 , 255.0 , 1.0 );
		gd2.showDialog ();
		if ( gd.wasCanceled () ) {
			IJ.error ( " PlugIn cancelled " );
		return ;
		}
	
		
		double seuilH = ( double ) gd2.getNextNumber ();

		
		int w = ip.getWidth ();
		int h = ip.getHeight ();		
		ImagePlus result3 = NewImage.createByteImage (" Seuillage par hysteresis ", w, h, 1,NewImage.FILL_BLACK );		
		ImageProcessor ipr3 = result3.getProcessor ();
		int[][] masqueSx = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masqueSy = {{-1,-2,-1},{0,0,0},{1,2,1}};
		int n = (int)masqueSx.length/2; // taille du demi masque
		float valeur = 0, valeur2 = 0, valeur3 = 0;
		

		for (int y = n; y < w - n; y++){
			for ( int x = n; x < h - n; x++) {
				for(int i = -n ; i <= n ; i++){
					for(int j = -n ; j <= n ; j++){	
							valeur += ip.getPixelValue(y+i,x+j)*masqueSx[j+n][i+n];
							valeur2 += ip.getPixelValue(y+i,x+j)*masqueSy[j+n][i+n];
					}
				}
				valeur3 = (float)Math.abs(valeur) + Math.abs(valeur2);
				if(valeur3 < seuilB){
					ipr3.putPixelValue(y,x,0);
				}
				else{
					if(valeur3 > seuilH){
						ipr3.putPixelValue(y,x,255);
					}
					else{
						if(ip.getPixelValue(y-1,x-1)==255 || ip.getPixelValue(y-1,x)==255 || ip.getPixelValue(y-1,x+1)==255 || ip.getPixelValue(y,x-1)==255){
							ipr3.putPixelValue(y,x,255);
						}
						else{
							ipr3.putPixelValue(y,x,0);
						}
					}
				}
				valeur = 0;
				valeur2 = 0;

			}
		}
		result3.show();
		result3.updateAndDraw();
	}
}