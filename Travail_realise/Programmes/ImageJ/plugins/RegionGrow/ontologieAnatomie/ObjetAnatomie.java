package RegionGrow.ontologieAnatomie;

import java.awt.Point;

/**
 * Super classe des objets de l'anatomie présents sur les scanners abdominaux (organes, os, etc.)
 * @author Thibault DELAVELLE
 *
 */
public abstract class ObjetAnatomie {
	
	//la couleur de l'objet
	protected int couleur;
	//la position du point de référence de l'objet
	protected Point position;
	
	public int getCouleur() {
		return couleur;
	}
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public void setCouleur(){
		
	}
	public String getNom(){
		return this.getClass().getName();
	}
	
	public String getNomSimple(){
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Retourne le nom de l'objet anatomique concerné
	 */
	public String toString(){
		//getSimpleName() retourne le nom de la classe sans le nom du package
		return this.getClass().getSimpleName();
	}

}
