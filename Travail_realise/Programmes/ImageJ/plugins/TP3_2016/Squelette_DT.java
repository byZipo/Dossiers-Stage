import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . filter .*;

public class Squelette_DT implements PlugInFilter {
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
	/*for (int i = 0; i<wA; i++) {
		for(int j = 0; j<hA; j++){
			if(pixelsA[i][j]==255)pixelsA[i][j]=0;
			else {
				//cmptNoirs++;
				pixelsA[i][j] = 500;
			}
		}
	}*/

	//IJ.log("Nombre de pixles de la forme : "+cmptNoirs);


	for (int i = 0; i<wA; i++) {
		for(int j = 0; j<hA; j++){
			if(pixelsA[i][j]==255)pixelsA[i][j]=0;
			else {
				pixelsA[i][j] = 500;
			}
		}
	}

	//IJ.log("Nombre de pixles de la forme : "+cmptNoirs);
	int[][] DM = pixelsA;
	//demi-masque avant
	//int[][] masqueAvant = {{1,1,1},{1,0,5000}};
	


int maxDistance = 0;

//DT4
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
			if(min>maxDistance)maxDistance=min;
			//IJ.log("DM["+x+"]["+y+"] : "+DM[x][y]);
		}
	}

	


	/*

	for (int iteration = 0 ; iteration < NBITERATIONS ; iteration++) {

		for(int x=1; x<wA-1; x++){
			for(int y=1; y<hA-1; y++){
				if (DM[x-1][y]==0   && DM[x+1][y]==0    && DM[x][y+1]==0   && DM[x][y-1]>0) DM[x][y]=255;
       			if (DM[x-1][y]>0 && DM[x+1][y]==0    && DM[x][y+1]==0   && DM[x][y-1]==0)   DM[x][y]=255;
        		if (DM[x-1][y]==0   && DM[x+1][y]==0    && DM[x][y+1]>0 && DM[x][y-1]==0)   DM[x][y]=255;
        		if (DM[x-1][y]==0   && DM[x+1][y]>0  && DM[x][y+1]==0   && DM[x][y-1]==0)   DM[x][y]=255;
 
        		if (DM[x-1][y+1]>0 && DM[x-1][y]>0 && DM[x-1][y-1]>0 && DM[x][y-1]>0   && DM[x+1][y-1]==0 && DM[x+1][y]==0     && DM[x][y+1]>0) DM[x][y]=255;
        		if (DM[x-1][y+1]>0 && DM[x-1][y]>0 && DM[x-1][y-1]>0 && DM[x][y-1]>0   && DM[x+1][y]==0   && DM[x+1][y+1]==0   && DM[x][y+1]>0) DM[x][y]=255;
        		if (DM[x-1][y+1]==0   && DM[x-1][y]==0   && DM[x][y-1]>0   && DM[x+1][y-1]>0 && DM[x+1][y]>0 && DM[x+1][y+1]>0 && DM[x][y+1]>0) DM[x][y]=255;
		        if (DM[x-1][y]==0     && DM[x-1][y-1]==0 && DM[x][y-1]>0   && DM[x+1][y-1]>0 && DM[x+1][y]>0 && DM[x+1][y+1]>0 && DM[x][y+1]>0) DM[x][y]=255;
 
		        if (DM[x-1][y+1]>0 && DM[x-1][y]>0   && DM[x][y-1]==0     && DM[x+1][y-1]==0   && DM[x+1][y]>0   && DM[x+1][y+1]>0 && DM[x][y+1]>0) DM[x][y]=255;
		        if (DM[x-1][y+1]>0 && DM[x-1][y]>0   && DM[x-1][y-1]==0   && DM[x][y-1]==0     && DM[x+1][y]>0   && DM[x+1][y+1]>0 && DM[x][y+1]>0) DM[x][y]=255;
        		if (DM[x-1][y]>0   && DM[x-1][y-1]>0 && DM[x][y-1]>0   && DM[x+1][y-1]>0 && DM[x+1][y]>0   && DM[x+1][y+1]==0   && DM[x][y+1]==0)   DM[x][y]=255;
        		if (DM[x-1][y+1]==0   && DM[x-1][y]>0   && DM[x-1][y-1]>0 && DM[x][y-1]>0   && DM[x+1][y-1]>0 && DM[x+1][y]>0   && DM[x][y+1]==0)   DM[x][y]=255;
 
		        if (DM[x-1][y]>0   && DM[x-1][y-1]>0 && DM[x][y-1]>0  && DM[x+1][y]==0     && DM[x][y+1]==0)   DM[x][y]=255;
		        if (DM[x-1][y+1]>0 && DM[x-1][y]>0   && DM[x][y-1]==0     && DM[x+1][y]==0     && DM[x][y+1]>0) DM[x][y]=255;
		        if (DM[x-1][y]==0     && DM[x][y-1]==0     && DM[x+1][y]>0   && DM[x+1][y+1]>0 && DM[x][y+1]>0) DM[x][y]=255;
		        if (DM[x-1][y]==0     && DM[x][y-1]>0   && DM[x+1][y-1]>0 && DM[x+1][y]>0   && DM[x][y+1]==0)   DM[x][y]=255;
		        ipr.putPixel(x,y,DM[x][y]);
			}
		}

	}*/

	//calcul du squelette
	boolean fini = false;
	int priorite = 1;
	while(!fini){
		for(int y=1; y<hA-1; y++){
			for(int x=1; x<wA-1; x++){
				if(DM[x][y]>0 && estSimple8(x,y,DM) && DM[x][y]==priorite && !estMediane(x,y,DM))DM[x][y]=0;
			}
		}
		if(priorite > maxDistance)fini = true;
		priorite++;
	}


	


	//tentative d'amélioration du squelette (peut être commenté)
	int iter = 0;
	while(iter<10){
		//affinage
		for(int y=1; y<hA-1; y++){
			for(int x=1; x<wA-1; x++){
					if(DM[x][y]>0 && DM[x][y+1]>0  && DM[x+1][y]>0  && DM[x-1][y]>0 &&  DM[x][y-1]==0 )DM[x][y+1]=0;
					if(DM[x][y]>0 && DM[x][y-1]>0  && DM[x+1][y]>0  && DM[x-1][y]>0 &&  DM[x][y+1]==0 )DM[x][y-1]=0;
					if(DM[x][y]>0 && DM[x+1][y]>0  && DM[x][y+1]>0  && DM[x][y-1]>0 &&  DM[x-1][y]==0 )DM[x+1][y]=0;
					if(DM[x][y]>0 && DM[x-1][y]>0  && DM[x][y+1]>0  && DM[x][y-1]>0 &&  DM[x+1][y]==0 )DM[x-1][y]=0;
			}
		}

		//comblage des trous dus à l'affinage
		for(int y=1; y<hA-1; y++){
			for(int x=1; x<wA-1; x++){
				if(DM[x][y]==0 && DM[x-1][y]>0 && DM[x+1][y]>0 && DM[x][y-1]==0 && DM[x][y+1]==0)DM[x][y]=255;
				if(DM[x][y]==0 && DM[x][y-1]>0 && DM[x][y+1]>0 && DM[x+1][y]==0 && DM[x-1][y]==0)DM[x][y]=255;
			}
		}
		iter++;
	}

	//dessin
	for (int i = 0; i<wA; i++) {
		for (int j = 0; j<hA; j++) {
			int value = DM[i][j];
			if(value>0)ipr.putPixel(i,j,255); 
			else ipr.putPixel(i,j,0);
		}
	}
	
	imageDT.show();
	imageDT.updateAndDraw();
 }



 public boolean estSimple8(int x,int y, int[][] DM){
				if (DM[x-1][y]==0   && DM[x+1][y]==0    && DM[x][y-1]==0   && DM[x][y+1]>0) return true;
       			if (DM[x-1][y]>0 && DM[x+1][y]==0    && DM[x][y-1]==0   && DM[x][y+1]==0)   return true;
        		if (DM[x-1][y]==0   && DM[x+1][y]==0    && DM[x][y-1]>0 && DM[x][y+1]==0)   return true;
        		if (DM[x-1][y]==0   && DM[x+1][y]>0  && DM[x][y-1]==0   && DM[x][y+1]==0)   return true;
 
        		if (DM[x-1][y-1]>0 && DM[x-1][y]>0 && DM[x-1][y+1]>0 && DM[x][y+1]>0   && DM[x+1][y+1]==0 && DM[x+1][y]==0     && DM[x][y-1]>0) return true;
        		if (DM[x-1][y-1]>0 && DM[x-1][y]>0 && DM[x-1][y+1]>0 && DM[x][y+1]>0   && DM[x+1][y]==0   && DM[x+1][y-1]==0   && DM[x][y-1]>0) return true;
        		if (DM[x-1][y-1]==0   && DM[x-1][y]==0   && DM[x][y+1]>0   && DM[x+1][y+1]>0 && DM[x+1][y]>0 && DM[x+1][y-1]>0 && DM[x][y-1]>0) return true;
		        if (DM[x-1][y]==0     && DM[x-1][y+1]==0 && DM[x][y+1]>0   && DM[x+1][y+1]>0 && DM[x+1][y]>0 && DM[x+1][y-1]>0 && DM[x][y-1]>0) return true;
 
		        if (DM[x-1][y-1]>0 && DM[x-1][y]>0   && DM[x][y+1]==0     && DM[x+1][y+1]==0   && DM[x+1][y]>0   && DM[x+1][y-1]>0 && DM[x][y-1]>0) return true;
		        if (DM[x-1][y-1]>0 && DM[x-1][y]>0   && DM[x-1][y+1]==0   && DM[x][y+1]==0     && DM[x+1][y]>0   && DM[x+1][y-1]>0 && DM[x][y-1]>0) return true;
        		if (DM[x-1][y]>0   && DM[x-1][y+1]>0 && DM[x][y+1]>0   && DM[x+1][y+1]>0 && DM[x+1][y]>0   && DM[x+1][y-1]==0   && DM[x][y-1]==0)   return true;
        		if (DM[x-1][y-1]==0   && DM[x-1][y]>0   && DM[x-1][y+1]>0 && DM[x][y+1]>0   && DM[x+1][y+1]>0 && DM[x+1][y]>0   && DM[x][y-1]==0)   return true;
 
		        if (DM[x-1][y]>0   && DM[x-1][y+1]>0 && DM[x][y+1]>0  && DM[x+1][y]==0     && DM[x][y-1]==0)   return true;
		        if (DM[x-1][y-1]>0 && DM[x-1][y]>0   && DM[x][y+1]==0     && DM[x+1][y]==0     && DM[x][y-1]>0) return true;
		        if (DM[x-1][y]==0     && DM[x][y+1]==0     && DM[x+1][y]>0   && DM[x+1][y-1]>0 && DM[x][y-1]>0) return true;
		        if (DM[x-1][y]==0     && DM[x][y+1]>0   && DM[x+1][y+1]>0 && DM[x+1][y]>0   && DM[x][y-1]==0)   return true;
		        return false;
 }


 public boolean estMediane(int i,int j,int[][] DM){
 	if(DM[i][j]>0){
		boolean isMax = true;
		if(DM[i-1][j-1]>0 && DM[i][j]<DM[i-1][j-1]) isMax = false;
		if(DM[i-1][j]>0   && DM[i][j]<DM[i-1][j])   isMax = false;
		if(DM[i-1][j+1]>0 && DM[i][j]<DM[i-1][j+1]) isMax = false;
		if(DM[i][j-1]>0   && DM[i][j]<DM[i][j-1])   isMax = false;
		if(DM[i][j+1]>0   && DM[i][j]<DM[i][j+1])   isMax = false;
		if(DM[i+1][j-1]>0 && DM[i][j]<DM[i+1][j-1]) isMax = false;
		if(DM[i+1][j]>0   && DM[i][j]<DM[i+1][j])   isMax = false;
		if(DM[i+1][j+1]>0 && DM[i][j]<DM[i+1][j+1]) isMax = false;

		return isMax; 
	}
	return false;
 }

 public int setup ( String arg , ImagePlus imp){
	return DOES_8G ;
 }

}