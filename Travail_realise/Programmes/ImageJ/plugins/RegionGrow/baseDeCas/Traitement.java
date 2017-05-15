package RegionGrow.baseDeCas;

import RegionGrow.main.Constantes.TypeTraitement;

/**
 * Un traitement est représenté par un type (énumération) et des paramètres
 * @author Thibault DELAVELLE
 *
 */
public class Traitement {
	
	//type de traitement
	protected TypeTraitement type;
	
	//paramètres des traitements (parfois null selon le traitement, ex: un Median ne nécessite pas de radius)
	protected double radius;
	protected double seuil;
	
	/**
	 * Constructeur (certains paramètres sont optionnels en fonction du type de triatement
	 * @param nom : le nom du traitement (enumération)
	 * @param radius : premier paramètre (optionnel, mettre à -1 dans ce cas)
	 * @param seuil : deuxième paramètre (optionnel, mettre à -1 dans ce cas)
	 */
	public Traitement(TypeTraitement nom, double radius, double seuil){
		this.type = nom;
		this.radius=radius;
		this.seuil=seuil;
	}
	
	/**
	 * Constructeur vide
	 */
	public Traitement(){
		
	}

	public TypeTraitement getTypeTraitement() {
		return type;
	}

	public void setTypeTraitement(TypeTraitement type) {
		this.type = type;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getSeuil() {
		return seuil;
	}

	public void setSeuil(double seuil) {
		this.seuil = seuil;
	}
	
	/**
	 * seteur à partir d'un String pour le parser XML, avec test de chaine vide (champ non renseigné)
	 * si le champ est vide ou non rensigné on affecte la valeur -1
	 * @param seuil : le seuil
	 */
	public void setSeuil(String seuil){
		double val;
		if(seuil.isEmpty() || seuil.equals(" ") || seuil == null)val = -1;
		else val = Double.parseDouble(seuil);
		this.seuil=val;
	}
	
	
	/**
	 * seteur à partir d'un String pour le parser XML, avec test de chaine vide (champ non renseigné)
	 * si le champ est vide ou non rensigné on affecte la valeur -1
	 * @param radius : le radius
	 */
	public void setRadius(String radius){
		double val;
		if(radius.isEmpty() || radius.equals(" ") || radius == null)val = -1;
		else val = Double.parseDouble(radius);
		this.radius=val;
	}
	
	public String toString(){
		return "Type : "+type+" , radius:"+radius+" , seuil:"+seuil;
	}
	
	

}
