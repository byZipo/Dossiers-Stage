package RegionGrow.ontologieRelationsSpatiales;

import java.awt.Point;

/**
 * Relation spatiale représentant le fait qu'un objet soit à gauche d'un point de référence
 * @author Thibault DELAVELLE
 *
 */
public class AGaucheDe extends RelationDirectionnelleBinaire{

	public AGaucheDe(){
	}
	
	public int[][] getCarteDistance(int w, int h){
		
		
		
		Point ref = this.reference.getPosition();
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
	
}
