// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Supp_Maxima implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run ( ImageProcessor ip){

		
		int w = ip.getWidth ();
		int h = ip.getHeight ();		
		ImagePlus result3 = NewImage.createByteImage (" Norme ", w, h, 1,NewImage.FILL_BLACK );		
		ImageProcessor ipr3 = result3.getProcessor ();
		int[][] masqueSx = {{1,0,-1},{2,0,-2},{1,0,-1}};
		int[][] masqueSy = {{-1,-2,-1},{0,0,0},{1,2,1}};
		int n = (int)masqueSx.length/2; // taille du demi masque
		float valeur = 0, valeur2 = 0, valeur3 = 0, HH = 0, HG = 0, HD = 0, MG = 0, MD = 0, BG = 0, BB = 0, BD = 0, HH1 = 0, HG1 = 0, HD1 = 0, MG1 = 0, MD1 = 0, BG1 = 0, BB1 = 0, BD1 = 0;
		int direction = 0, direction2 = 0;
		

		for (int y = n; y < h - n; y++){
			for ( int x = n; x < w - n; x++) {
				for(int i = -n ; i <= n ; i++){
					for(int j = -n ; j <= n ; j++){	
							valeur += ip.getPixelValue(y+i,x+j)*masqueSx[j+n][i+n];
							valeur2 += ip.getPixelValue(y+i,x+j)*masqueSy[j+n][i+n];
							HH = ip.getPixelValue(y-1, x);
							HG = ip.getPixelValue(y-1, x-1);
							HD = ip.getPixelValue(y-1, x+1);
							MG = ip.getPixelValue(y, x-1);
							MD = ip.getPixelValue(y, x+1);
							BG = ip.getPixelValue(y+1, x-1);
							BB = ip.getPixelValue(y+1, x);
							BD = ip.getPixelValue(y+1, x+1);
					}
				}
				valeur3 = (float)Math.abs(valeur) + Math.abs(valeur2);
				HH1 = (float)Math.abs(HH);			
				HG1 = (float)Math.abs(HG);
				HD1 = (float)Math.abs(HD);
				MG1 = (float)Math.abs(MG);			
				MD1 = (float)Math.abs(MD);			
				BG1 = (float)Math.abs(BG);
				BB1 = (float)Math.abs(BB);			
				BD1 = (float)Math.abs(BD);
				direction = (int)(180*(Math.atan(valeur2/valeur))/Math.PI)+180;
				direction2 = 45*(Math.round(direction/45));
				//IJ.log(""+direction+"    "+direction2);
				if((direction2 == 90 || direction2 == 270) && (valeur3 < HH || valeur3 < BB)) valeur3 = 0;
				if((direction2 == 0 || direction2 == 180) && (valeur3 < MG || valeur3 < MD)) valeur3 = 0;
				if((direction2 == 45 || direction2 == 225) && (valeur3 < HD || valeur3 < BG)) valeur3 = 0;
				if((direction2 == 135 || direction2 == 315) && (valeur3 < HG || valeur3 < BD)) valeur3 = 0;
				if(valeur3 <0) valeur3 = 0;
				if(valeur3 >255) valeur3 = 255;				
				ipr3.putPixelValue(y,x,(int)valeur3);
				valeur = 0;
				valeur2 = 0;

			}
		}
		result3.show();
		result3.updateAndDraw();
	}
}