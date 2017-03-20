import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.frame.*;

public class Diff_Im extends PlugInFrame {
	public Diff_Im(){
		super( "Soustraction entre images ");
	}

	public void run(String arg) {
		ImagePlus imgDiffA = new ImagePlus("C:/Users/Thibault/Documents/TIVO_RENESSON/Exemples_TI_S2/diff1.jpg");
		/*int[] dimensions = imgDiffA.getDimensions();
		int width = dimensions[0];
		int height = dimensions[1];
		IJ.log("Taille imgDiffA = "+width+" x "+height);*/
		ImageProcessor ipDiffA = imgDiffA.getProcessor();
		int wA = ipDiffA.getWidth();
		int hA = ipDiffA.getHeight();
		IJ.log("Taille imgDiffA = "+wA+" x "+hA);
		byte[] pixelsDiffA = (byte []) ipDiffA.getPixels();
	
		ImagePlus imgDiffB = new ImagePlus("C:/Users/Thibault/Documents/TIVO_RENESSON/Exemples_TI_S2/diff2.jpg");
		ImageProcessor ipDiffB = imgDiffB.getProcessor();
		int wB = ipDiffB.getWidth();
		int hB = ipDiffB.getHeight();	
		IJ.log("Taille imgDiffB = "+wB+" x "+hB);
		byte[] pixelsDiffB = (byte []) ipDiffB.getPixels();

		ImageProcessor ipRes = new ByteProcessor(wA,hA);
		ImagePlus imgRes = new ImagePlus(" Soustraction ",ipRes);
		
		byte[] pixelsRes = (byte []) ipRes.getPixels();
		
		// Question 7
		for(int i = 0 ; i < wA*hA ; i++){
				int pixelA = pixelsDiffA[i] & 0xFF;
				int pixelB = pixelsDiffB[i] & 0xFF;
				int max = Math.max((pixelA-pixelB),0);
				int pix = 0;
				if(pixelA == 0)pix = 255;
				ipRes.putPixel(i%wA,i/wA,pix);
				if(pixelA > pixelB) {
					ipRes.putPixel(i%wA,i/wA,125);
				}else if(pixelA < pixelB){
					ipRes.putPixel(i%wA,i/wA,180);
				}
		}
		
		// Question 6
		/*for(int i = 0 ; i < wA*hA ; i++){
			int pixelA = pixelsDiffA[i] & 0xFF;
			int pixelB = pixelsDiffB[i] & 0xFF;
			int max = Math.max((pixelA-pixelB),0);
			ipRes.putPixel(i%wA,i/wA,max);
	}*/
		imgRes.show();
		imgRes.updateAndDraw();
	}	
}	
		
		