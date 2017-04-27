//package RegionGrow.main;

import java.awt.Point;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
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
		ImagePlus imageDT= new ImagePlus("Fusion de relations spatiales", ipDT);
		ipr = imageDT.getProcessor();
		pixelsA = new int[w][h];
		int[][] pixelsCopy = ip.getIntArray();
		
		
		
		/*Point ref2 = new Point((w/2),(h/2)+1);
		aDroiteDe(ref2);
		
		Point ref3 = new Point((w/2)+1,(h/2));
		aDroiteDe(ref3);*/
		
		
		//calcul
		//pour chaque point de la forme de référence (A OPTIMISER en "pour chaque point du contour de la forme")
		// EN FAIT PAS BESOIN, LE CENTRE DE LA FORME SUFFIT, POUR LES TESTS ON PREND DONC UN SEUL POINT
		
		
		//calcul du centre de gravité de la colonne vertébrale (point de référence)
		Point ref = new Point(391, 374);
		pixelsA[(int)ref.getX()][(int)ref.getY()]=-50;
		int[][] haut = enHautDe(ref);
		int[][] droite = aDroiteDe(ref);
		int[][] proche = moyennementProcheDe(ref);
		int[][] gauche = aGaucheDe(ref);
		
		int[][] fusion = fusion(gauche,haut);
		int[][] fusion2 = fusion(fusion, proche);
		
		
		//dessin
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int[] tab = {fusion2[i][j],fusion2[i][j],fusion2[i][j]}; //l'image est rgb donc need 3 canaux 
				if(pixelsA[i][j]!=-50)ipr.putPixel(i, j, tab);
				else ipr.putPixel(i, j, 255); //dessin du point de référence
				if(fusion2[i][j]==0){
					int[] cpy = {pixelsCopy[i][j],pixelsCopy[i][j],pixelsCopy[i][j]};
					ipr.putPixel(i,j,cpy);
				}
			}
		}
		imageDT.show();
		imageDT.updateAndDraw();
	}
	
	
	//fusion par t-norme (min) entre deux cartes
	public int[][] fusion(int[][] carte1, int[][] carte2) {

		int[][] res = new int[w][h];
		
		for(int i = 0; i<w; i++){
			for(int j=0; j<h; j++){
				res[i][j] = carte1[i][j]*carte2[i][j];
//				res[i][j] = Math.min(carte1[i][j], carte2[i][j]);
			}
		}
		
		int[][] tmp = res.clone();
		int[][] fin = normaliser(tmp);
		return fin;
	}
	
	
	//repasse à une echelle entre 0 et 255
	public int[][] normaliser(int[][] tab){
		int[][] res = new int[w][h];
		int maxT = 0;
		
		
		//récupération de l'intensité max de la carte
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(tab[i][j]>maxT)maxT = tab[i][j];
			}
		}
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int pourcentageGris = tab[i][j]*100/maxT;
				res[i][j] = 255*pourcentageGris/100;
			}
		}
		return res;
		
	}
	
	
	public int[][] moyennementProcheDe(Point ref){
		
		int[][] res = new int[w][h];
		
		//paramètres calculés automatiquement par rapport à la demidiagonale (droite entre le centre de l'image et un coin)
		Point centre = new Point(w/2, h/2);
		Point coin = new Point(w,0);
		int x = (int)Math.abs(centre.getX()-coin.getX());
		int y = (int)Math.abs(centre.getY()-coin.getY());
		int demiDiagonale = (int)Math.sqrt((x*x)+(y*y));
		
		int seuilInf = demiDiagonale/5;
		int seuilSup = demiDiagonale/2 + (int)(demiDiagonale/(3));
		int degreMax = demiDiagonale/2;
		
		
		//affectation couleur
		//fonction : 
		//255            /\
		//	            /  \
		//127.5	       /    \
		//0	    ______/      \_________
		//      0    87  175  262    500 (distance)
		
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				int a = Math.abs((int)ref.getX()-i);
				int b = Math.abs((int)ref.getY()-j);
				int d = (int) Math.sqrt((a*a)+(b*b));
				
				if(d>=seuilInf && d<=degreMax){
					d-= seuilInf;
					int pourcentageDistance = (d*100)/(degreMax-seuilInf);
					int couleur = 255*pourcentageDistance/100;
					res[i][j]=couleur;
				}else if(d>degreMax && d<=seuilSup){
					d -= degreMax;
					int pourcentageDistance = 100-((d*100)/(seuilSup-degreMax));
					int couleur = 255*pourcentageDistance/100;
					res[i][j]=couleur;
				}else res[i][j]=0;
			}
		}
		return res;
	}
	
	

	public int[][] aDroiteDe(Point ref){
		
		//paramètres
		int seuilInf = 90;
		int seuilSup = 270;
		int degreMax = 0;
			
		double tetha;
		double angle;
		double couleur;
		double pourcentageAngle;
		
		int[][] res = new int[w][h];
		
		for (int i = (int)ref.getX(); i<w; i++) { //on peut "couper" la partie de gauche de l'image par rapport au point de ref, on sait que ce sera tout noir
			for(int j = 0; j<h; j++){
				tetha = Math.atan2(ref.getY()-j,i-ref.getX());
				angle = 180*tetha/Math.PI;
				if(angle<0) angle += 360;
				
				//affectation couleur
				//fonction : 
				//255   \				    /
				//	     \				   /
				//127.5	  \				  /
				//0	       \_____________/
				//      0  90    180    270 360 (angle)
				
				couleur = -1;
				
				//cas mu = 1
				if(angle==degreMax){
					res[i][j] = 255;
				}
				
				//cas mu = 0
				else if(angle>=seuilInf && angle <=seuilSup){
					res[i][j] = 0;
				}
				
				//cas mu flou quartan supérieur
				else if(angle<seuilInf && angle >degreMax){
					pourcentageAngle = 100- (angle*100/(seuilInf-degreMax));
					couleur = 255*pourcentageAngle/100;
					res[i][j] = (int)couleur;
				}
				
				//cas mu flou quartan inférieur
				else if(angle>seuilSup && angle<=360){
					angle -= seuilSup;
					pourcentageAngle = angle*100/(360-seuilSup);
					couleur = 255*pourcentageAngle/100;
					res[i][j] = (int)couleur;
				}
			}
		}
		return res;
	}
	
	
	public int[][] aGaucheDe(Point ref){
		
		//paramètres
		int seuilInf = 90;
		int seuilSup = 270;
		int degreMax = 180;
		
		double tetha;
		double angle;
		double couleur;
		double pourcentageAngle;
		
		int[][] res = new int[w][h];
		
		for (int i = 0; i<(int)ref.getX(); i++) { //on peut "couper" la partie de droite de l'image par rapport au point de ref, on sait que ce sera tout noir
			for(int j = 0; j<h; j++){
				tetha = Math.atan2(ref.getY()-j,i-ref.getX()); //calcul angle
				angle = 180*tetha/Math.PI; //conversion radians/degrés
				if(angle<0) angle += 360; //echelle de base -180-->180, que l'on repasse en 0-->360
				
				//affectation couleur
				//fonction : 
				//255             /\				    
				//	             /  \				   
				//127.5	        /    \				  
				//0	     ______/      \_______
				//      0     90 180  270  360 (angle)
				
				couleur = -1;
				
				//cas mu = 1
				if(angle==degreMax){
					res[i][j] = 255;
				}
				
				//cas mu = 0
				else if((angle >=0 && angle <=seuilInf) || (angle >=seuilSup && angle <= 360)){
					res[i][j] = 0;
				}
					
				//cas mu flou quartan supérieur
				else if(angle>seuilInf && angle <degreMax){
					angle -= 90;
					pourcentageAngle = (angle*100/(degreMax-seuilInf));
					couleur = 255*pourcentageAngle/100;
					res[i][j] = (int)couleur;
				}
				
				//cas mu flou quartan inférieur
				else if(angle>degreMax && angle<seuilSup){
					angle -= degreMax;
					pourcentageAngle = 100-(angle*100/(seuilSup-degreMax));
					couleur = 255*pourcentageAngle/100;
					res[i][j] = (int)couleur;
				}
			}
		}
		return res;
	}
	
	
	
	
	public int[][] enHautDe(Point ref){
		
		//paramètres
		int seuilInf = 0;
		int seuilSup = 180;
		int degreMax = 90;
		
		double tetha;
		double angle;
		double couleur;
		double pourcentageAngle;
		
		int[][] res = new int[w][h];
		
		for (int i = 0; i<w; i++) { //on peut "couper" la partie de gauche de l'image par rapport au point de ref, on sait que ce sera tout noir
			for(int j = 0; j<(int)ref.getY(); j++){
				tetha = Math.atan2(ref.getY()-j,i-ref.getX());
				angle = 180*tetha/Math.PI;
				if(angle<0) angle += 360;
				
				//affectation couleur
				//fonction : 
				//255       /\				    
				//	       /  \				   
				//127.5	  /    \				  
				//0	     /      \_____________
				//      0   90  180  270  360 (angle)
				
				couleur = -1;
				
				//cas mu = 1
				if(angle==degreMax){
					res[i][j] = 255;
				}
				
				//cas mu = 0
				else if(angle>=seuilSup && angle <=360){
					res[i][j] = 0;
				}
				
				//cas mu flou quartan supérieur
				else if(angle>degreMax && angle <seuilSup){
					angle -= degreMax;
					pourcentageAngle = 100-(angle*100/(seuilSup-degreMax));
					couleur = 255*pourcentageAngle/100;
					res[i][j] = (int)couleur;
				}
				
				//cas mu flou quartan inférieur
				else if(angle>seuilInf && angle<degreMax){
					pourcentageAngle = angle*100/(degreMax-seuilInf);
					couleur = 255*pourcentageAngle/100;
					res[i][j] = (int)couleur;
				}
			}
		}
		return res;
	}
	
	
	
	
	

}
