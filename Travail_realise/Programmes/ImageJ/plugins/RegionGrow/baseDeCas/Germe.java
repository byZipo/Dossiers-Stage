package RegionGrow.baseDeCas;

import static RegionGrow.main.Constantes.BLANC;

import java.util.Random;

import RegionGrow.ontologieAnatomie.ObjetAnatomie;
/**
 * Un germe est repr�sent� par des coordon�es 2D, une couleur, des seuils et un organe associ�
 * @author Thibault DELAVELLE
 *
 */
public class Germe {

	//coordonn�es dans l'image 2D
	protected int x;
	protected int y;
	
	//couleur pour la croissance de r�gion
	protected int couleur;
	
	//les seuils pour la croissance de r�gion
	protected int seuilGlobal;
	protected int seuilLocal;
	
	//l'objet (organe ou os par exemple) correspondant � la r�gion
	protected ObjetAnatomie labelObjet;
	
	public Germe(){
		
	}
	/**
	 * Constructeur classique sans couleur ni objet
	 * @param x coordon�e x
	 * @param y coordon�e y
	 * @param s1 seuil Global (pour la croissance de r�gion)
	 * @param s2 seui Local (pour la croissance de r�gion)
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
	 * @param x coordon�e x
	 * @param y coordon�e y
	 * @param s1 seuil Global (pour la croissance de r�gion)
	 * @param s2 seui Local (pour la croissance de r�gion)
	 * @param couleur la couleur de la r�gion partant du germe
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
	 * @param x coordon�e x
	 * @param y coordon�e y
	 * @param s1 seuil Global (pour la croissance de r�gion)
	 * @param s2 seui Local (pour la croissance de r�gion)
	 * @param couleur la couleur de la r�gion partant du germe
	 * @param objet l'objet anatomique correspondant � la r�gion du germe
	 */
	public Germe(int x, int y, int s1, int s2, int couleur, ObjetAnatomie objet){
		this.x=x;
		this.y=y;
		this.seuilGlobal=s1;
		this.seuilLocal=s2;
		this.couleur=couleur;
		this.labelObjet=objet;
	}
	
	/**
	 * retourne le seuil global du germe
	 * @return le seuil global
	 */
	public int getSeuilGlobal() {
		return seuilGlobal;
	}

	/**
	 * d�finit le seuil global du germe
	 * @param seuilGlobal : le seuil
	 */
	public void setSeuilGlobal(int seuilGlobal) {
		this.seuilGlobal = seuilGlobal;
	}

	/**
	 * retourne le seuil local du germe
	 * @return : le seuil local
	 */
	public int getSeuilLocal() {
		return seuilLocal;
	}

	
	/**
	 * d�finit le seuil local du germe
	 * @param seuilLocal : le seuil local
	 */
	public void setSeuilLocal(int seuilLocal) {
		this.seuilLocal = seuilLocal;
	}

	/**
	 * retourne la coordonn�e x du germe
	 * @return : x
	 */
	public int getX() {
		return x;
	}

	/**
	 * d�finit la coordonn�e x du germe
	 * @param x : la coordonn�e x
	 */
	public void setX(int x) {
		if(x != (int)x)System.err.println("PAS INT");
		this.x = x;
	}

	/**
	 * retourne la coordonn�e y du germe
	 * @return : y
	 */
	public int getY() {
		return y;
	}

	/**
	 * d�finit la coordonn�e y du germe
	 * @param y : la coordonn�e y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * d�finit la position du germe
	 * @param x : x
	 * @param y : y
	 */
	public void setPos(int x, int y){
		this.x=x;
		this.y=y;
	}

	/**
	 * retourne la couleur du germe
	 * @return : la couleur
	 */
	public int getCouleur() {
		if(couleur==0){
			Random r = new Random();
			return r.nextInt(BLANC);
		}
		else return couleur;
	}

	/**
	 * d�finit la couleur du germe
	 * @param couleur : la couleur
	 */
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	
	/**
	 * d�finit l'objet anatomique associ� au germe
	 * @param ref : le nom de l'objet
	 */
	public void setTypeByString(String ref) {
		String nomComplet = "RegionGrow.ontologieAnatomie."+ref; //il faut ajouter le chemin vers la classe
		try {
			this.labelObjet = (ObjetAnatomie) Class.forName(nomComplet).newInstance();
			//affectation automatique de la couleur du germe en fonction de l'objet anatomique
			this.setColor();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("ERREUR TYPE OBJET ANATOMIQUE DU GERME : "+ref+" n'est pas un objet anatomique de l'ontologie");
		}
	}
	
	/**
	 * retourne l'objet anatomique associ� ua germe
	 * @return l'objet anatomique
	 */
	public ObjetAnatomie getLabelObjet() {
		return labelObjet;
	}
	
	/**
	 * d�finit l'objet anatomique du germe
	 * @param labelObjet : l'objet anatomique
	 */
	public void setLabelObjet(ObjetAnatomie labelObjet) {
		this.labelObjet = labelObjet;
	}
	
	/**
	 * Affecte la position du germe � partir d'un String du type "150 153" avec x=150 et y=153
	 * Fonction utilis�e dans le parser XML
	 * La fonction dispose d'une gestion des exceptions (affichage d'un message d'erreur en cas de mauvais format)
	 * @param position : la cha�ne repr�sentant la position du germe
	 */
	public void setPosition(String position){
		String[] positionXY = position.split(" ");
		try{
			this.x = Integer.parseInt(positionXY[0]);
			this.y = Integer.parseInt(positionXY[1]);
		}catch(Exception e){
			System.err.println("ERREUR POSITION GERME : "+e.getMessage().split("\"")[1]+" n'est pas un entier");
		}
	}
	
	/**
	 * Affecte le seuilLocal du germe � partir d'un String 
	 * Fonction utilis�e dans le parserXML
	 * La fonction dispose d'une gestion des exceptions en cas de mauvais format
	 * @param seuil : le seuilLocal sous forme de cha�ne
	 */
	public void setSeuilLocalByString(String seuil){
		try{
			this.seuilLocal = Integer.parseInt(seuil);
		}catch(Exception e){
			System.err.println("ERREUR seuilLocal GERME : "+seuil+" n'est pas un entier");
		}
	}
	
	
	/**
	 * Affecte le seuilGlobal du germe � partir d'un String 
	 * Fonction utilis�e dans le parserXML
	 * La fonction dispose d'une gestion des exceptions en cas de mauvais format
	 * @param seuil : le seuilGlobal sous forme de cha�ne
	 */
	public void setSeuilGlobalByString(String seuil){
		try{
			this.seuilGlobal= Integer.parseInt(seuil);
		}catch(Exception e){
			System.err.println("ERREUR seuilGlobal GERME : "+seuil+" n'est pas un entier");
		}
	}
	
	/**
	 * affecte la couleur du germe par rapport � la couleur de l'objet anatomique
	 * la couleur de l'objet anatomique est une constante pour chaque organe, que l'on doit initialiser � la lecture 
	 */
	public void setColor(){
		labelObjet.setCouleur();
		this.couleur = labelObjet.getCouleur();
	}
	
	
	/**
	 * affiche le germe
	 */
	public String toString(){
		return "position : ("+x+";"+y+") , seuilGlobal : "+seuilGlobal+" , seuilLocal : "+seuilLocal+" , couleur : "+couleur+" , type:"+labelObjet;
	}


}
