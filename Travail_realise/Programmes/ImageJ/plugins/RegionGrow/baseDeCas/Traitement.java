package RegionGrow.baseDeCas;

import RegionGrow.main.Constantes.TypeTraitement;

/**
 * Un traitement est représenté par un type (énumération) et des paramètres
 * Il permet d'application des filtres à une image, comme une moyenne par exemple
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
	 * Affectation du seuil du traitement à partir d'un String, avec test de chaine vide (champ non renseigné)
	 * si le champ est vide ou non renseigné on affecte la valeur -1
	 * fonction utilisée par le parser XML
	 * une gestion des erreurs est définie pour vérifier le format
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
	 * Affectation du radius du traitement à partir d'un String, avec test de chaine vide (champ non renseigné)
	 * si le champ est vide ou non rensigné on affecte la valeur -1
	 * fonction utilisée par le parser XML
	 * une gestion des erreurs est définie pour vérifier le format
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
	 * définit le type de traitement
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
