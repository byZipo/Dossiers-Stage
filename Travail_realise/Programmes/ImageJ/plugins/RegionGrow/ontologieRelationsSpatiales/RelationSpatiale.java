package RegionGrow.ontologieRelationsSpatiales;

import RegionGrow.main.Constantes.TypeRelation;
import RegionGrow.ontologieAnatomie.ObjetAnatomie;

/**
 * Super classe de l'ontologie des relations spatiales entre objets
 * @author Thibault DELAVELLE
 *
 */
public abstract class RelationSpatiale {
	
	//Le type de la relation (son nom)
	protected TypeRelation type;

	//Objet de référence
	protected ObjetAnatomie reference;
	
	//paramètres pour la fonction floue associée
	protected double seuilInf;
	protected double seuilSup;
	protected double degreMax;
	
	
	public int[][] getCarteDistance(int w, int h) {
		return null;
	}
	
	
	public ObjetAnatomie getReference() {
		return reference;
	}
	public void setReference(ObjetAnatomie reference) {
		this.reference = reference;
	}
	public double getSeuilInf() {
		return seuilInf;
	}
	public void setSeuilInf(double seuilInf) {
		this.seuilInf = seuilInf;
	}
	public double getSeuilSup() {
		return seuilSup;
	}
	public void setSeuilSup(double seuilSup) {
		this.seuilSup = seuilSup;
	}
	public double getDegreMax() {
		return degreMax;
	}
	public void setDegreMax(double degreMax) {
		this.degreMax = degreMax;
	}
	public TypeRelation getType() {
		return type;
	}
	public void setType(TypeRelation type) {
		this.type = type;
	}
}
