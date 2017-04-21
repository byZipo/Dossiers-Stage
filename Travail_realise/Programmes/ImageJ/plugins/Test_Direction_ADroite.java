//package RegionGrow.main;

import java.awt.Point;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Test_Direction_ADroite implements PlugInFilter{
	
	byte[] pixels;
	int h,w;
	ImageProcessor ipr;

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G ;
	}

	@Override
	public void run(ImageProcessor ip) {
		h=ip.getHeight();
		w=ip.getWidth();	
		pixels=(byte[])ip.getPixelsCopy();
		
		ImageProcessor ipDT= new ByteProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("aDroiteDe", ipDT);
		ipr = imageDT.getProcessor();
		byte[] pixelsDT= (byte[])ipDT.getPixels();
		int[][] pixelsA = ip.getIntArray();
		
		Point ref = new Point(w/2, h/2);
	
		aDroiteDe(ref);
	
		imageDT.show();
		imageDT.updateAndDraw();
		
		
	}
	
	
	public void aDroiteDe(Point ref){
		for (int i = 0; i<w; i++) {
			for(int j = 0; j<h; j++){
				double tetha = Math.atan2(ref.getY()-j,i-ref.getX());
				double angle = 180*tetha/Math.PI;
				if(angle<0) angle += 360;
				
				//affectation couleur
				//fonction : 
				//255   \				    /
				//	     \				   /
				//127.5	  \				  /
				//0	       \_____________/
				//      0  90    180    270 360
				
				
				if(angle==0) ipr.putPixel(i, j, 255);
				if(angle>=90 && angle <=270)ipr.putPixel(i, j, 0);
				if(angle<90 && angle >0){
					double pourcentageAngle = 100- (angle*100/90);
					double couleur = 255*pourcentageAngle/100;
					ipr.putPixel(i, j, (int)couleur);
				}
				if(angle>270 && angle<=360){
					angle -= 270;
					double pourcentageAngle = angle*100/90;
					double couleur = 255*pourcentageAngle/100;
					ipr.putPixel(i, j, (int)couleur);
				}
			}
		}
	}
	
	

}
