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

	//Objet de r�f�rence
	protected ObjetAnatomie reference;
	
	//param�tres pour la fonction floue associ�e
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

	/**
	 * D�finit la r�f�rence de la relation spatiale � partir d'un String 
	 * (Convertit le String en instance de la classe de l'ontologie associ�e)
	 * M�thode utilis�e pour le parse de la base XML
	 * @param ref: le Sring corresponant � l'ObjetAnatomique
	 */
	public void setReferenceByString(String ref) {
		String nomComplet = "RegionGrow.ontologieAnatomie."+ref; //il faut ajouter le chemin vers la classe
		try {
			this.reference = (ObjetAnatomie) Class.forName(nomComplet).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println(ref+" n'est pas un objet anatomique de l'ontologie");
			e.printStackTrace();
		}
	}
	
	/**
	 * toString compl�te, pour la partie Solution o� tous les attributs sont renseign�s
	 */
	public String toString(){
		return "Type:"+type+" reference:"+reference.toString()+" seuilInf:"+seuilInf+" seuilSup:"+seuilSup+" degreMax:"+degreMax+" ";
	}
	
	/**
	 * toString sans les seuils, pour l'affichage de la partie probl�me o� ces attributs sont non renseign�s
	 * @return
	 */
	public String toStringSansSeuils(){
		return "Type:"+type+" reference:"+reference.toString()+" ";
	}
}
