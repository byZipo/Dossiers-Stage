package RegionGrow.ontologieRelationsSpatiales;

import java.awt.Point;

import RegionGrow.main.Constantes.TypeRelation;

/**
 * Relation spatiale représentant le fait qu'un objet soit à droite d'un point de référence
 * @author Thibault DELAVELLE
 *
 */
public class ADroiteDe extends RelationDirectionnelleBinaire{

	public ADroiteDe(){
		this.type = TypeRelation.ADroiteDe;
	}
	
	
	public int[][] getCarteDistance(int w, int h){
		
		
		Point ref = this.reference.getPosition();
	
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
	
}
