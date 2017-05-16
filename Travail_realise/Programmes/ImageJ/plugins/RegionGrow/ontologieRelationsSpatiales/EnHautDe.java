package RegionGrow.ontologieRelationsSpatiales;

import java.awt.Point;

/**
 * Relation spatiale représentant le fait qu'un objet soit en haut d'un point de référence
 * @author Thibault DELAVELLE
 *
 */
public class EnHautDe extends RelationDirectionnelleBinaire{

	public EnHautDe(){
	}
	
	public int[][] getCarteDistance(int w, int h){
		
		
		
		Point ref = this.reference.getPosition();
		
		double tetha;
		double angle;
		double couleur;
		double pourcentageAngle;
		
		int[][] res = new int[w][h];
		
		for (int i = 0; i<w; i++) { //on peut "couper" la partie basse de l'image par rapport au point de ref, on sait que ce sera tout noir
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
