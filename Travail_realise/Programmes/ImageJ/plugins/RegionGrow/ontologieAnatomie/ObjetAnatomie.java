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

}
