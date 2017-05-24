package RegionGrow.ontologieRelationsSpatiales;

import java.util.Objects;

import RegionGrow.ontologieAnatomie.ObjetAnatomie;

/**
 * Super classe de l'ontologie des relations spatiales entre objets
 * @author Thibault DELAVELLE
 *
 */
public abstract class RelationSpatiale {
	

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


	/**
	 * D�finit la r�f�rence de la relation spatiale � partir d'un String 
	 * (Convertit le String en instance de la classe de l'ontologie associ�e)
	 * M�thode utilis�e par le parser XML
	 * @param ref: le Sring corresponant � l'ObjetAnatomique
	 */
	public void setReferenceByString(String ref) {
		String nomComplet = "RegionGrow.ontologieAnatomie."+ref; //il faut ajouter le chemin vers la classe
		try {
			this.reference = (ObjetAnatomie) Class.forName(nomComplet).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("ERREUR REFERENCE DE RELATION FLOUE : "+ref+" n'est pas un objet anatomique de l'ontologie");
		}
	}
	
	/**
	 * Affectation du seuilInf � partir d'un String que l'on convertit en double
	 * Fonction utilis�e par le parser XML
	 * Une gestion des erreurs est pr�sente pour v�rifier le format
	 * @param seuilInf : la cha�ne correspondant au seuilInf
	 */
	public void setSeuilInfByString(String seuilInf){
		try{
			this.seuilInf = Double.parseDouble(seuilInf);
		}catch(Exception e){
			System.err.println("ERREUR seuilInf RELATION FLOUE : "+seuilInf+" n'est pas un double");
		}
	}
	
	/**
	 * Affectation du seuilSup � partir d'un String que l'on convertit en double
	 * Fonction utilis�e par le parser XML
	 * Une gestion des erreurs est pr�sente pour v�rifier le format
	 * @param seuilSup : la cha�ne correspondant au seuilSup
	 */
	public void setSeuilSupByString(String seuilSup){
		try{
			this.seuilSup = Double.parseDouble(seuilSup);
		}catch(Exception e){
			System.err.println("ERREUR seuilSup RELATION FLOUE : "+seuilSup+" n'est pas un double");
		}
	}
	
	
	/**
	 * Affectation du degreMax � partir d'un String que l'on convertit en double
	 * Fonction utilis�e par le parser XML
	 * Une gestion des erreurs est pr�sente pour v�rifier le format
	 * @param degreMax : la cha�ne correspondant au degreMax
	 */
	public void setDegreMaxByString(String degreMax){
		try{
			this.degreMax = Double.parseDouble(degreMax);
		}catch(Exception e){
			System.err.println("ERREUR degreMax RELATION FLOUE : "+degreMax+" n'est pas un double");
		}
	}
	
	
	/**
	 * toString compl�te, pour la partie Solution o� tous les attributs sont renseign�s
	 */
	public String toString(){
		return "Type:"+getClass().getSimpleName()+" reference:"+((reference!=null)?reference.toString():"null")+" seuilInf:"+seuilInf+" seuilSup:"+seuilSup+" degreMax:"+degreMax+" ";
	}
	
	/**
	 * toString sans les seuils, pour l'affichage de la partie probl�me o� ces attributs sont non renseign�s
	 * @return
	 */
	public String toStringSansSeuils(){
		return "Type:"+getClass().getSimpleName()+" reference:"+((reference!=null)?reference.toString():"null")+" ";
	}
	
	
	public String getNom(){
		return this.getClass().getName();
	}
	
	/**
	 * red�finition de la m�thode equals pour pouvoir comparer deux relations spatiales
	 * compare � la fois le nom de la classe et le nom de l'objet anatomique de r�f�rence
	 * @return true si les deux relations ont le m�me nom, false sinon
	 */
	@Override
	public boolean equals(Object o){
		if(this.getNom().equals(o.getClass().getName())){
			String s = ((RelationSpatiale) o).getReference().getNom();
			if(this.reference.getNom().equals(s))return true;
		}
		return false;
	}
	
	/**
	 * red�finition de la m�thode hashCode() pour pouvoir comparer deux relations spatiales
	 * @return un hash� construit � partir des attributs de la classe
	 */
	@Override
	public int hashCode(){
		return Objects.hash(reference,seuilInf,seuilSup,degreMax);
	}
}
