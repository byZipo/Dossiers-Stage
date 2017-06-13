package RegionGrow.baseDeCas;

import static RegionGrow.main.Constantes.BLANC;

import java.util.Random;

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
		if(x != (int)x)System.err.println("PAS INT");
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setPos(int x, int y){
		this.x=x;
		this.y=y;
	}

	public int getCouleur() {
		if(couleur==0){
			Random r = new Random();
			return r.nextInt(BLANC);
		}
		else return couleur;
	}

	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	
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
	
	public ObjetAnatomie getLabelObjet() {
		return labelObjet;
	}
	public void setLabelObjet(ObjetAnatomie labelObjet) {
		this.labelObjet = labelObjet;
	}
	
	/**
	 * Affecte la position du germe à partir d'un String du type "150 153" avec x=150 et y=153
	 * Fonction utilisée dans le parser XML
	 * La fonction dispose d'une gestion des exceptions (affichage d'un message d'erreur en cas de mauvais format)
	 * @param position : la chaîne représentant la position du germe
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
	 * Affecte le seuilLocal du germe à partir d'un String 
	 * Fonction utilisée dans le parserXML
	 * La fonction dispose d'une gestion des exceptions en cas de mauvais format
	 * @param seuil : le seuilLocal sous forme de chaîne
	 */
	public void setSeuilLocalByString(String seuil){
		try{
			this.seuilLocal = Integer.parseInt(seuil);
		}catch(Exception e){
			System.err.println("ERREUR seuilLocal GERME : "+seuil+" n'est pas un entier");
		}
	}
	
	
	/**
	 * Affecte le seuilGlobal du germe à partir d'un String 
	 * Fonction utilisée dans le parserXML
	 * La fonction dispose d'une gestion des exceptions en cas de mauvais format
	 * @param seuil : le seuilGlobal sous forme de chaîne
	 */
	public void setSeuilGlobalByString(String seuil){
		try{
			this.seuilGlobal= Integer.parseInt(seuil);
		}catch(Exception e){
			System.err.println("ERREUR seuilGlobal GERME : "+seuil+" n'est pas un entier");
		}
	}
	
	/**
	 * affecte la couleur du germe par rapport à la couleur de l'objet anatomique
	 * la couleur de l'objet anatomique est une constante pour chaque organe, que l'on doit initialiser à la lecture 
	 */
	public void setColor(){
		labelObjet.setCouleur();
		this.couleur = labelObjet.getCouleur();
	}
	
	
	
	public String toString(){
		return "position : ("+x+";"+y+") , seuilGlobal : "+seuilGlobal+" , seuilLocal : "+seuilLocal+" , couleur : "+couleur+" , type:"+labelObjet;
	}


}
