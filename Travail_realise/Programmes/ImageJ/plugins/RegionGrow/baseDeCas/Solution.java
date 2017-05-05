package RegionGrow.baseDeCas;
import java.util.ArrayList;

public class Solution {
	
	//seuils
	//protected int seuilGlobal;
	//protected int seuilLocal;
	
	//germes
	protected ArrayList<Germe> germes;
	
	public Solution(){
		
	}
	
	public Solution(ArrayList<Germe> germes){
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
		st.append("\n   --> Germes : ");
		for(int i = 0; i<germes.size(); i++){
			st.append("\n"+germes.get(i).toString());
		}
		return st.toString();
	}

}
