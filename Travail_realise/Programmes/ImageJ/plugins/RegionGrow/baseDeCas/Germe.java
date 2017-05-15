package RegionGrow.baseDeCas;

import RegionGrow.ontologieAnatomie.ObjetAnatomie;
/**
 * Un germe est représenté par des coordonées 2D, une couleur, des seuils et un organe associé
 * @author Thibault DELAVELLE
 *
 */
public class Germe {

	//coordonnées dans l'image 2D
	protected int x;
	protected int y;
	
	//couleur pour la croissance de région
	protected int couleur;
	
	//les seuils pour la croissance de région
	protected int seuilGlobal;
	protected int seuilLocal;
	
	//l'objet (organe ou os par exemple) correspondant à la région
	protected ObjetAnatomie labelObjet;
	
	public Germe(){
		
	}
	/**
	 * Constructeur classique sans couleur ni objet
	 * @param x coordonée x
	 * @param y coordonée y
	 * @param s1 seuil Global (pour la croissance de région)
	 * @param s2 seui Local (pour la croissance de région)
	 */
	public Germe(int x, int y, int s1, int s2){
		this.x=x;
		this.y=y;
		this.seuilGlobal=s1;
		this.seuilLocal=s2;
		this.couleur = 0;
	}
	/**
	 * Constructeur semi-complet avec couleur
	 * @param x coordonée x
	 * @param y coordonée y
	 * @param s1 seuil Global (pour la croissance de région)
	 * @param s2 seui Local (pour la croissance de région)
	 * @param couleur la couleur de la région partant du germe
	 */
	public Germe(int x, int y, int s1, int s2, int couleur){
		this.x=x;
		this.y=y;
		this.seuilGlobal=s1;
		this.seuilLocal=s2;
		this.couleur=couleur;
	}
	/**
	 * Constructeur complet avec couleur et objet
	 * @param x coordonée x
	 * @param y coordonée y
	 * @param s1 seuil Global (pour la croissance de région)
	 * @param s2 seui Local (pour la croissance de région)
	 * @param couleur la couleur de la région partant du germe
	 * @param objet l'objet anatomique correspondant à la région du germe
	 */
	public Germe(int x, int y, int s1, int s2, int couleur, ObjetAnatomie objet){
		this.x=x;
		this.y=y;
		this.seuilGlobal=s1;
		this.seuilLocal=s2;
		this.couleur=couleur;
		this.labelObjet=objet;
	}
	
	public int getSeuilGlobal() {
		return seuilGlobal;
	}

	public void setSeuilGlobal(int seuilGlobal) {
		this.seuilGlobal = seuilGlobal;
	}

	public int getSeuilLocal() {
		return seuilLocal;
	}

	public void setSeuilLocal(int seuilLocal) {
		this.seuilLocal = seuilLocal;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCouleur() {
		return couleur;
	}

	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	
	public void setTypeByString(String ref) {
		String nomComplet = "RegionGrow.ontologieAnatomie."+ref; //il faut ajouter le chemin vers la classe
		try {
			this.labelObjet = (ObjetAnatomie) Class.forName(nomComplet).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println(ref+" n'est pas un objet anatomique de l'ontologie");
			e.printStackTrace();
		}
	}
	
	public ObjetAnatomie getLabelObjet() {
		return labelObjet;
	}
	public void setLabelObjet(ObjetAnatomie labelObjet) {
		this.labelObjet = labelObjet;
	}
	public String toString(){
		return "position : ("+x+";"+y+") , seuilGlobal : "+seuilGlobal+" , seuilLocal : "+seuilLocal+" , couleur : "+couleur+" , type:"+labelObjet;
	}


}
