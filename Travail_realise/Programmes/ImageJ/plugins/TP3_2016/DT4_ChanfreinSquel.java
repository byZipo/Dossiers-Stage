import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . filter .*;

public class DT4_ChanfreinSquel implements PlugInFilter {
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
	//demi-masque avant
	//int[][] masqueAvant = {{1,1,1},{1,0,5000}};
	




	//premier balayage (1ère version fausse)
	/*for(int x=0; x<wA-2; x++){
		for(int y=0; y<hA-3; y++){
			int min = 2000;
			int val = 0;
			for(int i=0; i<2; i++){
				for(int j=0; j<3; j++){
					val =  DM[x+i][y+j] + masqueAvant[i][j];
					if(val<min)min = val;
				}
			}
			DM[x][y] = min;
			//IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}*/

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
			IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}


	//demi-masque arrière
	//int[][] masqueArriere = {{5000,0,1},{1,1,1}};
	
	//deuxième balayage (2ème version fausse)
	/*IJ.log("Resultat de l'algorithme SEQ");
	for(int x=wA-1; x>2; x--){
		for(int y=hA-1; y>3; y--){
			int min = 2000;
			int val = 0;
			for(int i=0; i<2; i++){
				for(int j=0; j<3; j++){
					val =  DM[x-i][y-j] + masqueArriere[i][j];
					if(val<min)min = val;
				}
			}
			DM[x][y] = min;
			IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}*/


	//affichage de l'image avec les distances de SEQ en nuances de gris
	for (int i = 0; i<wA; i++) {
		for (int j = 0; j<hA; j++) {
			int value = DM[i][j];
			ipr.putPixel(i,j,value*15); //on applique un poids pour mieux différencier les couleurs en fonction des distances
		}
	}
		
	imageDT.show();
	imageDT.updateAndDraw();
 }

 public int setup ( String arg , ImagePlus imp){
	return DOES_8G ;
 }

}