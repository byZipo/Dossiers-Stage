package RegionGrow.baseDeCas;

import RegionGrow.main.Constantes.TypeTraitement;

/**
 * Un traitement est repr�sent� par un type (�num�ration) et des param�tres
 * Il permet d'application des filtres � une image, comme une moyenne par exemple
 * @author Thibault DELAVELLE
 *
 */
public class Traitement {
	
	//type de traitement
	protected TypeTraitement type;
	
	//param�tres des traitements (parfois null selon le traitement, ex: un Median ne n�cessite pas de radius)
	protected double radius;
	protected double seuil;
	
	/**
	 * Constructeur (certains param�tres sont optionnels en fonction du type de triatement
	 * @param nom : le nom du traitement (enum�ration)
	 * @param radius : premier param�tre (optionnel, mettre � -1 dans ce cas)
	 * @param seuil : deuxi�me param�tre (optionnel, mettre � -1 dans ce cas)
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
	 * Affectation du seuil du traitement � partir d'un String, avec test de chaine vide (champ non renseign�)
	 * si le champ est vide ou non renseign� on affecte la valeur -1
	 * fonction utilis�e par le parser XML
	 * une gestion des erreurs est d�finie pour v�rifier le format
	 * @param seuil : le seuil
	 */
	public void setSeuil(String seuil){
		double val = -1;
		if(!seuil.isEmpty() && !seuil.equals(" ") && seuil != null){
			try{
				val = Double.parseDouble(seuil);
			}catch(Exception e){
				System.err.println("ERREUR seuil TRAITEMENT : "+seuil+" n'est pas un double");
			}
		}
		this.seuil=val;
	}
	
	
	/**
	 * Affectation du radius du traitement � partir d'un String, avec test de chaine vide (champ non renseign�)
	 * si le champ est vide ou non rensign� on affecte la valeur -1
	 * fonction utilis�e par le parser XML
	 * une gestion des erreurs est d�finie pour v�rifier le format
	 * @param radius : le radius
	 */
	public void setRadius(String radius){
		double val = -1;
		if(!radius.isEmpty() && !radius.equals(" ") && radius!= null){
			try{
				val = Double.parseDouble(radius);
			}catch(Exception e){
				System.err.println("ERREUR radius TRAITEMENT : "+radius+" n'est pas un double");
			}
		}
		this.radius=val;
	}
	
	/**
	 * d�finit le type de traitement
	 * @param traitement : le traitement
	 */
	public void setTypeTraitementByString(String traitement){
		try{
			this.type = TypeTraitement.valueOf(traitement);
		}catch(Exception e){
			System.err.println("ERREUR TypeTraitement TRAITEMENT : "+traitement+" n'est pas un type de traitement existant");
		}
	}
	
	
	/**
	 * affiche le traitement
	 */
	public String toString(){
		return "Type : "+type+" , radius:"+radius+" , seuil:"+seuil;
	}
	
	

}
