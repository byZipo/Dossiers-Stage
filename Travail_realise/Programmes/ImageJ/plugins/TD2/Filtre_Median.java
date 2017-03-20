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

public class Filtre_Median implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;
	}
	
	public void run( ImageProcessor ip){
	
	int w = ip.getWidth();
	int h = ip.getHeight();
	int[][] pixels = ip.getIntArray();
	
	
	ImagePlus result = NewImage.createByteImage(" Retailler " ,w,h, 1, NewImage.FILL_BLACK );
	ImageProcessor ipr = result.getProcessor();
	byte[] pixelsr = (byte[]) ipr.getPixels();
	
	
	
	int N = 5; //taille du masque
	int[] masque = new int[N*N]; 
	int prof = N/2; //profondeur du masque (Si N=3 --> 1 Si N=5 --> 2, etc)
	for(int i=0;i<w;i++){
		for(int j=0;j<h;j++)
			ipr.putPixel(i,j,pixels[i][j]);
	}
	
	for(int i=prof; i<w-prof-1; i++){
		for(int j=prof; j<h-prof-1; j++){
			
			int x = -prof;
			int y = -prof;
			for(int a=0;a<(N*N);a++){
				masque[a] = pixels[x+i][y+j];
				if(a%N==0 && a>0){ //x s'incremente tous les N tours, y oscille entre -prof et prof
					x++; 
					y=-prof; //reset 
				}else y++;      
			}
			/*masque[0] = pixels[i-prof][j-prof];
			masque[1] = pixels[i][j-prof];
			masque[2] = pixels[i-prof][j];
			masque[3] = pixels[i][j];
			masque[4] = pixels[i+prof][j];
			masque[5] = pixels[i][j+prof];
			masque[6] = pixels[i+prof][j+prof];
			masque[7] = pixels[i+prof][j-prof];
			masque[8] = pixels[i-prof][j+prof];*/
			int val = mediane(masque);
			ipr.putPixel(i,j,val);
			
		}
	}
	
	
	
	
	result.show();
	result.updateAndDraw();
	}
	
	
	
	
	static int mediane (int a[]){
		int [] effectifs = new int [256]; // tableau des effectifs
		for (int i=0 ; i < a.length ; i++) {
		effectifs[a[i ]]++;
		}
		int somme = 0;
		for (int i=0 ; i <= 255 ; i++) {
		somme = somme + effectifs[i] ;
		if (2* somme >= a.length )
		return i;
		}
		return -1;
		}

}
