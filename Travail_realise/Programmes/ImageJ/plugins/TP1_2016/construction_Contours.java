import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 

public class construction_Contours implements PlugInFilter {
	
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}
	
	
	public void run(ImageProcessor ip) {
		ImagePlus imgDiffA = new ImagePlus("C:/Users/Thibault/Documents/M2-Info/TIVO/TP1/ImagesTest/xa7_1.pgm");
		ImagePlus imgXa1_1 = new ImagePlus("C:/Users/Thibault/Documents/M2-Info/TIVO/TP1/ImagesTest/xa1_1.pgm");
		ImagePlus imgXa0_1 = new ImagePlus("C:/Users/Thibault/Documents/M2-Info/TIVO/TP1/ImagesTest/xa0_1.pgm");
		ImagePlus imgXa0_2 = new ImagePlus("C:/Users/Thibault/Documents/M2-Info/TIVO/TP1/ImagesTest/xa0_2.pgm");
		ImagePlus imgXa7_2 = new ImagePlus("C:/Users/Thibault/Documents/M2-Info/TIVO/TP1/ImagesTest/xa7_2.pgm");
		ImagePlus imgXa0_3 = new ImagePlus("C:/Users/Thibault/Documents/M2-Info/TIVO/TP1/ImagesTest/xa0_3.pgm");
		
		ImageProcessor ipDiffA = imgDiffA.getProcessor();
		int wA = ipDiffA.getWidth();
		int hA = ipDiffA.getHeight();
		int[][] pixelsA = ipDiffA.getIntArray();
		
		
		
		int[][] pixels = ip.getIntArray();
		String freeman1 = contours(ip, pixels);
		IJ.log("C = {"+freeman1+"}");
		
		String freeman2 = contours(ipDiffA, pixelsA);
		IJ.log("C = {"+freeman2+"}");
		//afficherFreeman(freeman);
		
		int res = wagnerFischer(freeman1,freeman2);
		
		//division par le max des deux codes comparés
		int tmp = Math.max(freeman1.length(), freeman2.length());
		double toto = (double) res / tmp;
		IJ.log("Distance d'edition : "+toto);
		
		
		ipDiffA = imgXa1_1.getProcessor();
		wA = ipDiffA.getWidth();
		hA = ipDiffA.getHeight();
		pixelsA = ipDiffA.getIntArray();
		freeman2 = contours(ipDiffA, pixelsA);
		IJ.log("C = {"+freeman1+"}");
		IJ.log("C = {"+freeman2+"}");
		res = wagnerFischer(freeman1,freeman2);
		tmp = Math.max(freeman1.length(), freeman2.length());
		toto = (double) res / tmp;
		IJ.log("Distance d'edition : "+toto);
		
		ipDiffA = imgXa0_1.getProcessor();
		wA = ipDiffA.getWidth();
		hA = ipDiffA.getHeight();
		pixelsA = ipDiffA.getIntArray();
		freeman2 = contours(ipDiffA, pixelsA);
		IJ.log("C = {"+freeman1+"}");
		IJ.log("C = {"+freeman2+"}");
		res = wagnerFischer(freeman1,freeman2);
		tmp = Math.max(freeman1.length(), freeman2.length());
		toto = (double) res / tmp;
		IJ.log("Distance d'edition : "+toto);
		
		ipDiffA = imgXa0_2.getProcessor();
		wA = ipDiffA.getWidth();
		hA = ipDiffA.getHeight();
		pixelsA = ipDiffA.getIntArray();
		freeman2 = contours(ipDiffA, pixelsA);
		IJ.log("C = {"+freeman1+"}");
		IJ.log("C = {"+freeman2+"}");
		res = wagnerFischer(freeman1,freeman2);
		tmp = Math.max(freeman1.length(), freeman2.length());
		toto = (double) res / tmp;
		IJ.log("Distance d'edition : "+toto);
		
		ipDiffA = imgXa7_2.getProcessor();
		wA = ipDiffA.getWidth();
		hA = ipDiffA.getHeight();
		pixelsA = ipDiffA.getIntArray();
		freeman2 = contours(ipDiffA, pixelsA);
		IJ.log("C = {"+freeman1+"}");
		IJ.log("C = {"+freeman2+"}");
		res = wagnerFischer(freeman1,freeman2);
		tmp = Math.max(freeman1.length(), freeman2.length());
		toto = (double) res / tmp;
		IJ.log("Distance d'edition : "+toto);
		
		ipDiffA = imgXa0_3.getProcessor();
		wA = ipDiffA.getWidth();
		hA = ipDiffA.getHeight();
		pixelsA = ipDiffA.getIntArray();
		freeman2 = contours(ipDiffA, pixelsA);
		IJ.log("C = {"+freeman1+"}");
		IJ.log("C = {"+freeman2+"}");
		res = wagnerFischer(freeman1,freeman2);
		tmp = Math.max(freeman1.length(), freeman2.length());
		toto = (double) res / tmp;
		IJ.log("Distance d'edition : "+toto);
		
	}
	
	
	
	public int wagnerFischer(String f1, String f2){
		int size1 = f1.length();
		int size2 = f2.length();
		
		int[][] tmp = new int[size1+1][size2+1];
		
		for(int i = 0; i <= size1; i++) {
			tmp[i][0] = i;
		}
		for(int j = 0; j <= size2; j++) {
			tmp[0][j] = j;
		}
		
		for(int i = 1; i <= size1; i++) {
			for (int j = 1; j <= size2; j++) {
				int m = (f1.charAt(i - 1) == f2.charAt(j - 1)) ? 0:1;
				tmp[i][j] = Math.min(Math.min(tmp[i - 1][j] + 1, tmp[i][j - 1] + 1), tmp[i - 1][j - 1] + m);
			}
		}
		
		
		return tmp[size1][size2];
	}
	
	public String contours(ImageProcessor ip, int[][] pixels){
		
		//int[] res = new int[ip.getWidth()*ip.getHeight()];
		StringBuilder st = new StringBuilder();
		int initialX=0;
		int initialY=0;
		int pereX;
		int pereY;
		int curX;
		int curY;
		
		
		
		//calcul de intialX et initialY (le point de départ du contour)
		//IJ.log("w: "+ip.getWidth()+" h: "+ip.getHeight());
		//IJ.log("w: "+ip.getWidth()+" h: "+ip.getHeight()+"i"+pixels[201][371]);
		for(int i = 0 ; i < ip.getWidth() ; i++){
			for(int j = 0 ; j < ip.getHeight() ; j++){
				if(pixels[i][j]==0){
					initialX = i;
					initialY = j;
					i = ip.getWidth();
					j = ip.getHeight();
				}
			}
		}
		
		//calcul du contour à partir du point de départ du contour
		pereX = initialX;
		pereY = initialY;
		curX = initialX;
		curY = initialY;
		int cmpt = 0;
		int fils = 0;
		//IJ.log("curX: "+curX+" curY: "+curY);
		//IJ.log("C = {");
		while(!(curX == initialX && curY == initialY) || cmpt == 0){
			
			cmpt++;
			fils = getFils(curX, curY, pereX, pereY, pixels);
			st.append(fils+"");
			//res[cmpt] = fils;
			//IJ.log(fils+",");
			pereX = curX;
			pereY = curY;
			switch(fils){
			case 0:
				curX++;
				break;
			case 1:
				curX++;
				curY--;
				break;
			case 2:
				curY--;
				break;
			case 3:
				curX--;
				curY--;
				break;
			case 4:
				curX--;
				break;
			case 5:
				curX--;
				curY++;
				break;
			case 6:
				curY++;
				break;
			case 7:
				curX++;
				curY++;
				break;
			default:
				curX = pereX;
				curY = pereY;
				break;
			}
			//IJ.log("x: "+curX+" y: "+curY);
			ip.putPixel(curX, curY, 125); 
			
		}
		//IJ.log(" }");
		//effacerPixelsSolos(pixels, ip);
		//if(eixsteEncoreDuContour(pixels, ip))run(ip);
		return st.toString();
	}
	
	public int getFils(int x, int y, int pereX, int pereY, int[][] pixels){
		
		//calcul de la direction pour retourner au père
		
		int directionPourPere = 4; //sens de rotation initial choisi --> anti-horraire par le bas.
		if((pereX == x) && (pereY == y-1)) directionPourPere = 2;
		if((pereX == x) && (pereY == y+1)) directionPourPere = 6;
		if((pereX == x+1) && (pereY == y-1)) directionPourPere = 1;
		if((pereX == x-1) && (pereY == y+1)) directionPourPere = 5;
		if((pereX == x-1) && (pereY == y-1)) directionPourPere = 3;
		if((pereX == x+1) && (pereY == y+1)) directionPourPere = 7;
		if((pereX == x+1) && (pereY == y)) directionPourPere = 0;
		if((pereX == x-1) && (pereY == y)) directionPourPere = 4;
		//IJ.log(directionPourPere+"");
		
		//calcul de la nouvelle direction
		for (int i = 0; i < 8; i++) {
			int start = ((i+directionPourPere+1)%8);
			switch(start){
			case 0:
				if((x < pixels.length-1) && (pixels[x+1][y]==0))return 0;
				break;
			case 1:
				if((x < pixels.length-1) && (y > 0) &&(pixels[x+1][y-1]==0))return 1;
				break;
			case 2:
				if((y>0)&& (pixels[x][y-1]==0))return 2;
				break;
			case 3:
				if((x>0) && (y>0) && (pixels[x-1][y-1]==0))return 3;
				break;
			case 4:
				if((x>0) && (pixels[x-1][y]==0))return 4;
				break;
			case 5:
				if((x>0) && (y<pixels[0].length-1) && (pixels[x-1][y+1]==0))return 5;
				break;
			case 6:
				if((y<pixels[0].length-1) && (pixels[x][y+1]==0))return 6;
				break;
			case 7:
				if((x<pixels.length-1) && (y<pixels[0].length-1) && (pixels[x+1][y+1]==0))return 7;
				break;
			default:
				return directionPourPere;
			}
		}
		return -1;
	}
	
	
	public void effacerPixelsSolos(int[][] pixels, ImageProcessor ip ){
		for(int i = 1 ; i < pixels.length-1 ; i++){
			for(int j =  1; j < pixels[0].length-1 ; j++){
				if(ip.getPixelValue(i,j)==125){
					if(ip.getPixelValue(i+1,j+1)==0)ip.putPixel(i+1, j+1, 255); 
					if(ip.getPixelValue(i-1,j-1)==0)ip.putPixel(i-1, j-1, 255); 
					if(ip.getPixelValue(i+1,j-1)==0)ip.putPixel(i+1, j-1, 255); 
					if(ip.getPixelValue(i-1,j+1)==0)ip.putPixel(i-1, j+1, 255); 
					if(ip.getPixelValue(i,j+1)==0)ip.putPixel(i, j+1, 255); 
					if(ip.getPixelValue(i,j-1)==0)ip.putPixel(i, j-1, 255); 
					if(ip.getPixelValue(i+1,j)==0)ip.putPixel(i+1, j, 255); 
					if(ip.getPixelValue(i-1,j)==0)ip.putPixel(i-1, j, 255); 
					
				}
			}
		}
	}
	
	
	public boolean eixsteEncoreDuContour(int[][] pixels, ImageProcessor ip){
		for(int i = 1 ; i < pixels.length-1 ; i++){
			for(int j =  1; j < pixels[0].length-1 ; j++){
				if(ip.getPixelValue(i,j)==0){
					return true;
				}
			}
		}
		return false;
	}
	
	public void afficherFreeman(int[] tab){
		IJ.log("C = {");
		for(int i=0; i<tab.length; i++){
			IJ.log(tab[i]+"");
		}
		IJ.log(" }");
	}
}