package RegionGrow.baseDeCas;
import java.util.ArrayList;


/**
 * Une solution est repr�sent�e par une liste de gemres utiles, une liste de germes inutiles et une liste de pr�traitements
 * @author Thibault DELAVELLE
 *
 */
public class Solution {
	
	//seuils (inutilis�s ici car d�sormais on utilise un seuillage par germe)
	//protected int seuilGlobal;
	//protected int seuilLocal;
	
	//germes
	//les germes utiles sont les objets � segmenter, les inutiles sont les muscles par exemple
	protected ArrayList<Germe> germesUtiles;
	protected ArrayList<Germe> germesInutiles;
	
	//pretraitements
	protected ArrayList<Traitement> pretraitements;
	

	/**
	 * Constructeur simple sans germes inutiles, ni pr�traitements
	 * @param germes : les gemres des objets � segmenter
	 */
	public Solution(ArrayList<Germe> germes){
		this.germesUtiles = germes;
		this.germesInutiles = new ArrayList<Germe>();
		this.pretraitements = new ArrayList<Traitement>();
	}
	
	/**
	 * Constructeur complet
	 * @param germesUtiles : les germes des objets � segmenter
	 * @param germesInutiles : les gemres des objets � supprimer
	 * @param pretraitements : les pr�traitements � effectuer avant la segmentation
	 */
	public Solution(ArrayList<Germe> germesUtiles, ArrayList<Germe> germesInutiles, ArrayList<Traitement> pretraitements){
		this.germesUtiles = germesUtiles;
		this.germesInutiles=germesInutiles;
		this.pretraitements=pretraitements;
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
	
	public void setGermeInutile(Germe g){
		this.germesInutiles.add(g);
	}
	
	public void addPretraitement(Traitement t){
		pretraitements.add(t);
	}
	
	public Traitement getPretraitement(int i){
		return pretraitements.get(i);
	}
	
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append("\n   --> Germes Utiles: ");
		for(int i = 0; i<germesUtiles.size(); i++){
			st.append("\n"+germesUtiles.get(i).toString());
		}
		st.append("\n   --> Germes Inutiles: ");
		for (int i = 0; i < germesInutiles.size(); i++) {
			st.append("\n"+germesInutiles.get(i).toString());
		}
		st.append("\n   --> Pretraitements: ");
		for (int i = 0; i < pretraitements.size(); i++) {
			st.append("\n"+pretraitements.get(i).toString());
		}
		return st.toString();
	}

}
