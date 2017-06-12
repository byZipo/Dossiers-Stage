package RegionGrow.ontologieRelationsSpatiales;

import java.awt.Point;

/**
 * Relation spatiale représentant la proximité entre deux objets
 * @author Thibault DELAVELLE
 *
 */
public class ProcheDe extends RelationDeDistance{
	
	public ProcheDe(){
	}
	
	public int[][] getCarteDistance(int w, int h){
		Point ref = this.reference.getPosition();
		
		int[][] res = new int[w][h];
		
		//paramètres calculés automatiquement par rapport à la demidiagonale (droite entre le centre de l'image et un coin)
		Point centre = new Point(w/2, h/2);
		Point coin = new Point(w,0);
		int x = (int)Math.abs(centre.getX()-coin.getX());
		int y = (int)Math.abs(centre.getY()-coin.getY());
		int demiDiagonale = (int)Math.sqrt((x*x)+(y*y));
		
		
		//valeur à mettre pour un ProcheDe "par défaut" : 5
		int seuilInf = (int) (demiDiagonale/this.degreMax);
		int seuilSup = demiDiagonale/2 + (int)(demiDiagonale/(3));
		//int degreMax = demiDiagonale/2;
		
		
		//affectation couleur
		//fonction : 
		//255   _______      
		//	           \
		//127.5	        \
		//0	             \_________
		//      0     87 175  262 500 (distance)
		
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				int a = Math.abs((int)ref.getX()-i);
				int b = Math.abs((int)ref.getY()-j);
				int d = (int) Math.sqrt((a*a)+(b*b));
				
				if(d<seuilInf){
					res[i][j]=255;
				}else if(d>seuilSup){
					res[i][j] = 0;
				}else if(d>=seuilInf && d<=seuilSup){
					d -= seuilInf;
					int pourcentageDistance = 100-((d*100)/(seuilSup-seuilInf));
					int couleur = 255*pourcentageDistance/100;
					res[i][j]=couleur;
				}else res[i][j]=0;
			}
		}
		return res;
	}

}
