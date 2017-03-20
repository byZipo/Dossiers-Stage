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
		int[][] pixels = ip.getIntArray();
		
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
		IJ.log("C = {");
		while(!(curX == initialX && curY == initialY) || cmpt == 0){
			
			cmpt++;
			fils = getFils(curX, curY, pereX, pereY, pixels);
			IJ.log(fils+",");
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
		IJ.log(" }");
		effacerPixelsSolos(pixels, ip);
		if(eixsteEncoreDuContour(pixels, ip))run(ip);
	}
	
	public int getFils(int x, int y, int pereX, int pereY, int[][] pixels){
		
		//calcul de la direction pour retourner au père
		
		int directionPourPere = 0;
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
}