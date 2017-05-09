package RegionGrow.baseDeCas;

/**
 * Un traitement est repr�sent� par un type (�num�ration) et des param�tres
 * @author Thibault DELAVELLE
 *
 */
public class Traitement {
	
	//type de traitement
	public enum TypeTraitement {Unsharped,Moyenneur,Median};
	protected TypeTraitement  type;
	
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

	public TypeTraitement getTypeTraitement() {
		return type;
	}

	public void getTypeTraitement(TypeTraitement type) {
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
	
	public String toString(){
		return "Type : "+type+" , radius:"+radius+" , seuil:"+seuil;
	}
	
	

}
