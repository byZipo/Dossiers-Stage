import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . filter .*;

public class AxeMedian_DT4 implements PlugInFilter {
byte[] pixels;
int h,w;


 public void run (ImageProcessor ip){
	h=ip.getHeight();
	w=ip.getWidth();	
	pixels=(byte[])ip.getPixelsCopy();
	//Phase 1
	//Phase 2
	//Cr�ation de l'image de la DT
	ImageProcessor ipDT= new ByteProcessor(w,h);
	ImagePlus imageDT= new ImagePlus("DT8 Chanfrein", ipDT);
	ImageProcessor ipr = imageDT.getProcessor();
	byte[] pixelsDT= (byte[])ipDT.getPixels();

	int[][] pixelsA = ip.getIntArray();
	int wA = ip.getWidth();
	int hA = ip.getHeight();


	//les pixels blanc deviennent les pixels verts, donc les pixels à 0
	//les autres pixels prennent un poids imitant +infini, ici 500
	//int cmptNoirs = 0;
	for (int i = 0; i<wA; i++) {
		for(int j = 0; j<hA; j++){
			if(pixelsA[i][j]==255)pixelsA[i][j]=0;
			else {
				//cmptNoirs++;
				pixelsA[i][j] = 500;
			}
		}
	}

	//IJ.log("Nombre de pixles de la forme : "+cmptNoirs);
	int[][] DM = pixelsA;
	

	for(int y=1; y<hA-3; y++){
		for(int x=1; x<wA-2; x++){
			int min = 2000;
			int val = 0;
			val =  DM[x][y-1] + 1;  //les y sont inversés par rapport à la formule du cours car l'axe y est vers le bas
			if(val<min)min = val;
			val = DM[x-1][y] + 1;
			if(val<min)min = val;
			val = DM[x][y] + 0;
			if(val<min)min = val;
			DM[x][y] = min;
			//IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}

	for(int y=hA-2; y>3; y--){
		for(int x=wA-2; x>2; x--){
			int min = 2000;
			int val = 0;
			val =  DM[x][y] + 0;
			if(val<min)min = val;
			val = DM[x+1][y] + 1;
			if(val<min)min = val;
			val = DM[x][y+1] + 1;
			if(val<min)min = val;
			DM[x][y] = min;
			//IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}


	//calcul des médianes
	for (int i = 1; i<wA-1; i++) {
		for (int j = 1; j<hA-1; j++) {
			int value = DM[i][j];
			
			//si le point appartient à une région
			if(value>0){
				boolean isMax = true;
				//si il a une distance > à celle de ses voisins de la région en d4, alors c'est un point de la médiane
				if(DM[i-1][j]>0   && value<DM[i-1][j])   isMax = false;
				if(DM[i][j-1]>0   && value<DM[i][j-1])   isMax = false;
				if(DM[i][j+1]>0   && value<DM[i][j+1])   isMax = false;
				if(DM[i+1][j]>0   && value<DM[i+1][j])   isMax = false;

				if(isMax)ipr.putPixel(i,j,255);
				else ipr.putPixel(i,j,125); //on affiche tout de même le contour de base pour une meilleure visibilité
			}
		}
	}




	



		
	imageDT.show();
	imageDT.updateAndDraw();
 }

 public int setup ( String arg , ImagePlus imp){
	return DOES_8G ;
 }

}