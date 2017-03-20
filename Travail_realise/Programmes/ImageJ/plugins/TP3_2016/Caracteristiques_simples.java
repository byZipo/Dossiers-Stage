import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . filter .*;

public class Caracteristiques_simples implements PlugInFilter {
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
	for (int i = 0; i<wA; i++) {
		for(int j = 0; j<hA; j++){

			if(pixelsA[i][j]==255)pixelsA[i][j]=0;
			else {
				pixelsA[i][j] = 500;
			}
		}
	}

	int[][] DM = pixelsA;
	

	//calcul de l'aire de la région
	int m00 = 0;
	for(int y=0; y<hA; y++){
		for(int x=0; x<wA; x++){
			int f = 0;
			if(DM[x][y]>0)f=1;
			else f=0;
			m00 += Math.pow(x,0)*Math.pow(y,0)*f;
			//IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}



	IJ.log("AIRE : "+m00);

	//vérification formule aire 
	/*int aire = 0;
	for(int y=0; y<hA; y++){
		for(int x=0; x<wA; x++){
			if(DM[x][y]>0)aire++;
		}
	}

	IJ.log("AIRE vérification :"+aire);*/


	//calcul du centre de gravité
	int xGrav = 0;
	int yGrav = 0;
	int m10 = 0;
	int m01 = 0;
	for(int y=0; y<hA; y++){
		for(int x=0; x<wA; x++){
			int f = 0;
			if(DM[x][y]>0)f=1;
			else f=0;
			m10 += Math.pow(x,1)*Math.pow(y,0)*f;
			m01 += Math.pow(x,0)*Math.pow(y,1)*f;
		}
	}

	xGrav = m10/m00;
	yGrav = m01/m00;

	IJ.log("CENTRE DE GRAVITE : ("+xGrav+","+yGrav+")");

	//calcul largeur et hauteur de la boite englobante
	int xMin = 500000;
	int yMin = 500000;
	int xMax = -1;
	int yMax = -1;
	for(int y=0; y<hA; y++){
		for(int x=0; x<wA; x++){
			if(DM[x][y]>0 && x<xMin)xMin=x;
			if(DM[x][y]>0 && y<yMin)yMin=y;
			if(DM[x][y]>0 && x>xMax)xMax=x;
			if(DM[x][y]>0 && y>yMax)yMax=y;
		}
	}

	int largeur = xMax-xMin;
	int hauteur = yMax-yMin;

	IJ.log("LARGEUR BOITE ENGLOBANTE :"+largeur);
	IJ.log("HAUTEUR BOITE ENGLOBANTE :"+hauteur);



	//calcul du diamètre

	int diametre = 0;
	//premier point
	for(int y=0; y<hA; y++){
		for(int x=0; x<wA; x++){
			//il doit appartenir à la région
			if(DM[x][y]>0){
				//deuxième point
				for(int b=0;b<hA;b++){
					for(int a=0;a<wA;a++){
						//il doit aussi appartenir à la région
						if(DM[a][b]>0){
							int distance = Math.abs(x-a)+Math.abs(y-b);
							if(distance>diametre)diametre=distance;
						}
					}
				}
			}

		}
	}

	IJ.log("DIAMETRE"+diametre);



	//calcul du périmètre nécessaire pour le rapport isoperimetrique


	//on calcule DT4 qui va nous permettre de calculer les pixels du contour de la région en 8-connexité
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


	int perimetre = 0;
	// les pixels avec une distance de 1 sont ceux du contour, on les somme pour obtenir le perimetre
	for(int y=0; y<hA; y++){
		for(int x=0; x<wA; x++){
			if(DM[x][y]==1)perimetre++;
		}
	}

	IJ.log("PERIMETRE : "+perimetre);

	double rapportIsoperimetrique = (double)((double)perimetre*perimetre)/((double)4.0*Math.PI*m00);

	IJ.log("RAPPORT ISOPERIMETRIQUE : "+rapportIsoperimetrique);

	double elongation =(double)1-((double)1.0/rapportIsoperimetrique);
		
	IJ.log("ELONGATION : "+elongation);
	//imageDT.show();
	//imageDT.updateAndDraw();
 }

 public int setup ( String arg , ImagePlus imp){
	return DOES_8G ;
 }

}