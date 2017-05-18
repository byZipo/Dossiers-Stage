package RegionGrow.baseDeCas;
import java.util.ArrayList;

import RegionGrow.ontologieRelationsSpatiales.RelationSpatiale;


/**
 * Une solution est représentée par une liste de gemres utiles, une liste de germes inutiles et une liste de prétraitements
 * @author Thibault DELAVELLE
 *
 */
public class Solution {
	
	//seuils (inutilisés ici car désormais on utilise un seuillage par germe)
	//protected int seuilGlobal;
	//protected int seuilLocal;
	
	//germes
	//les germes utiles sont les objets à segmenter, les inutiles sont les muscles par exemple
	protected ArrayList<Germe> germesUtiles;
	protected ArrayList<Germe> germesInutiles;
	
	//pretraitements
	protected ArrayList<Traitement> pretraitements;
	
	//liste de relations spatiales floues concernant la position de la tumeur (avec les valeurs des seuils flous)
	protected ArrayList<RelationSpatiale> positonFloueTumeur;
	
	
	/**
	 * Constructeur vide qui initialise simplement les listes de gemres utiles et inutiles, les pretraitements et les positions floues
	 */
	public Solution(){
		this.germesInutiles = new ArrayList<Germe>();
		this.germesUtiles = new ArrayList<Germe>();
		this.pretraitements = new ArrayList<Traitement>();
		this.positonFloueTumeur = new ArrayList<RelationSpatiale>();
	}

	/**
	 * Constructeur simple sans germes inutiles, ni prétraitements
	 * @param germes : les gemres des objets à segmenter
	 */
	public Solution(ArrayList<Germe> germes){
		this.germesUtiles = germes;
		this.germesInutiles = new ArrayList<Germe>();
		this.pretraitements = new ArrayList<Traitement>();
		this.positonFloueTumeur = new ArrayList<RelationSpatiale>();
	}
	
	/**
	 * Constructeur complet
	 * @param germesUtiles : les germes des objets à segmenter
	 * @param germesInutiles : les gemres des objets à supprimer
	 * @param pretraitements : les prétraitements à effectuer avant la segmentation
	 */
	public Solution(ArrayList<Germe> germesUtiles, ArrayList<Germe> germesInutiles, ArrayList<Traitement> pretraitements){
		this.germesUtiles = germesUtiles;
		this.germesInutiles=germesInutiles;
		this.pretraitements=pretraitements;
		this.positonFloueTumeur = new ArrayList<RelationSpatiale>();
	}

	public void ajouterGermeUtile(Germe g){
		this.germesUtiles.add(g);
	}
	
	public void supprimerGermeUtile(Germe g){
		this.germesUtiles.remove(g);
	}
	
	public int getNbGermesUtiles(){
		return germesUtiles.size();
	}
	

	public ArrayList<Germe> getGermesUtiles() {
		return germesUtiles;
	}

	public void setGermesUtiles(ArrayList<Germe> germes) {
		this.germesUtiles = germes;
	}

	public Germe getGermeUtile(int i){
		return germesUtiles.get(i);
	}
	
	public Germe getGermeInutile(int i){
		return germesInutiles.get(i);
	}
	
	public void ajouterGermeInutile(Germe g){
		this.germesInutiles.add(g);
	}
	
	public void addPretraitement(Traitement t){
		pretraitements.add(t);
	}
	
	public Traitement getPretraitement(int i){
		return pretraitements.get(i);
	}
	
	public RelationSpatiale getRelationSpatiale(int i){
		return this.positonFloueTumeur.get(i);
	}
	
	public void ajouterRelationFloue(RelationSpatiale r){
		this.positonFloueTumeur.add(r);
	}
	
	public int getNbPreTraitements(){
		return this.pretraitements.size();
	}
	
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append("\nSOLUTION:");
		st.append("\n   --> Germes Utiles: ");
		for(int i = 0; i<germesUtiles.size(); i++){
			st.append("\n	"+germesUtiles.get(i).toString());
		}
		st.append("\n   --> Germes Inutiles: ");
		for (int i = 0; i < germesInutiles.size(); i++) {
			st.append("\n	"+germesInutiles.get(i).toString());
		}
		st.append("\n   --> Pretraitements: ");
		for (int i = 0; i < pretraitements.size(); i++) {
			st.append("\n	"+pretraitements.get(i).toString());
		}
		st.append("\n   --> PositionFloueTumeur: ");
		for (int i = 0; i < positonFloueTumeur.size(); i++) {
			st.append("\n	"+positonFloueTumeur.get(i).toString());
		}
		return st.toString();
	}

}
