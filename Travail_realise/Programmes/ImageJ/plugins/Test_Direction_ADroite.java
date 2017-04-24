//package RegionGrow.main;

import java.awt.Point;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class Test_Direction_ADroite implements PlugInFilter{
	
	byte[] pixels;
	int[][] pixelsA;
	int h,w;
	ImageProcessor ipr;

	@Override
	public int setup(String arg, ImagePlus imp) {
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToGray8();
		imp.updateAndDraw();
		return DOES_8G ;
	}

	@Override
	public void run(ImageProcessor ip) {
		h=ip.getHeight();
		w=ip.getWidth();	
		pixels=(byte[])ip.getPixelsCopy();
		
		ImageProcessor ipDT= new ColorProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("aDroiteDe", ipDT);
		ipr = imageDT.getProcessor();
		pixelsA = new int[w][h];
		
		/*Point ref = new Point(w/2, h/2);
		aDroiteDe(ref);
		
		Point ref2 = new Point((w/2),(h/2)+1);
		aDroiteDe(ref2);
		
		Point ref3 = new Point((w/2)+1,(h/2));
		aDroiteDe(ref3);*/
		Point p;
		
		//calcul
		//pour chaque point de la forme de référence (A OPTIMISER en "pour chaque point du contour de la forme")
		for(int i = w/2 ; i < (w/2)+20 ; i++){
			for(int j = h/2 ; j < (h/2)+20 ; j++){
				pixelsA[i][j]=256;
				p = new Point (i,j);
				aDroiteDe(p);
			}
		}
		
		
		//dessin
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int[] tab = {pixelsA[i][j],pixelsA[i][j],pixelsA[i][j]}; //l'image est rgb donc need 3 canaux 
				if(pixelsA[i][j]!=256)ipr.putPixel(i,j,tab);
				else ipr.putPixel(i, j, 255);
			}
		}
	
		imageDT.show();
		imageDT.updateAndDraw();
		
		
	}
	
	
	public void aDroiteDe(Point ref){
		
		double tetha;
		double angle;
		double couleur;
		double pourcentageAngle;
		
		for (int i = (int)ref.getX(); i<w; i++) { //on peut "couper" la partie de gauche de l'image par rapport au point de ref, on sait que ce sera tout noir
			for(int j = 0; j<h; j++){
				if(pixelsA[i][j]<255){//optimisation de temps (un pixel blanc ne peut pas être amélioré)
					tetha = Math.atan2(ref.getY()-j,i-ref.getX());
					angle = 180*tetha/Math.PI;
					if(angle<0) angle += 360;
					
					//affectation couleur
					//fonction : 
					//255   \				    /
					//	     \				   /
					//127.5	  \				  /
					//0	       \_____________/
					//      0  90    180    270 360
					
					couleur = -1;
					
					//cas mu = 1
					if(angle==0){
						pixelsA[i][j] = 255;
						//ipr.putPixel(i, j, 255);
					}
					
					//cas mu = 0
					else if(pixelsA[i][j]<=0 && angle>=90 && angle <=270){
						pixelsA[i][j] = 0;
						//ipr.putPixel(i, j, 0);
					}
					
					//cas mu flou quartan supérieur
					else if(angle<90 && angle >0){
						pourcentageAngle = 100- (angle*100/90);
						couleur = 255*pourcentageAngle/100;
						
						//on modifie la valeur si elle est meilleure
						if(couleur > pixelsA[i][j]){
							pixelsA[i][j] = (int)couleur;
							//ipr.putPixel(i, j, (int)couleur);
						}
					}
					
					//cas mu flou quartan inférieur
					else if(angle>270 && angle<=360){
						angle -= 270;
						pourcentageAngle = angle*100/90;
						couleur = 255*pourcentageAngle/100;
						if(couleur > pixelsA[i][j]){
							pixelsA[i][j] = (int)couleur;
							//ipr.putPixel(i, j, (int)couleur);
						}
					}
				}
			}
		}
	}
	
	

}
