package RegionGrow.baseDeCas;
import java.util.ArrayList;

public class Solution {
	
	//seuils
	protected int seuilGlobal;
	protected int seuilLocal;
	
	//germes
	protected ArrayList<Germe> germes;
	
	public Solution(){
		
	}
	
	public Solution(int seuilGlobal, int seuilLocal, ArrayList<Germe> germes){
		this.seuilGlobal = seuilGlobal;
		this.seuilLocal = seuilLocal;
		this.germes = germes;
	}

	public void ajouterGerme(Germe g){
		this.germes.add(g);
	}
	
	public void supprimerGerme(Germe g){
		this.germes.remove(g);
	}
	
	public int getNbGermes(){
		return germes.size();
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

	public ArrayList<Germe> getGermes() {
		return germes;
	}

	public void setGermes(ArrayList<Germe> germes) {
		this.germes = germes;
	}

	public Germe getGerme(int i){
		return germes.get(i);
	}
	
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append(" Seuil Global : "+seuilGlobal);
		st.append(" Seuil Local : "+seuilLocal);
		st.append("\n   --> Germes : ");
		for(int i = 0; i<germes.size(); i++){
			st.append("\n"+germes.get(i).toString());
		}
		return st.toString();
	}

}
