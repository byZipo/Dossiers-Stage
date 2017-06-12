package RegionGrow.ontologieRelationsSpatiales;

import java.awt.Point;


/**
 * Relation spatiale représentant la proximité moyenne entre deux objets
 * @author Thibault DELAVELLE
 *
 */
public class MoyennementProcheDe extends RelationDeDistance{

	public MoyennementProcheDe(){
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
		
		
		int seuilInf = demiDiagonale/5;
		int seuilSup = demiDiagonale/2 + (int)(demiDiagonale/(3));
		//valeur à mettre pour un MoyennementProcheDe "par défaut" : 2
		int degreMax = (int) (demiDiagonale/this.degreMax);
		
		
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
}
