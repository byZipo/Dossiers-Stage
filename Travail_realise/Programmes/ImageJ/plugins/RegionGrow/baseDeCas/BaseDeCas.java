package RegionGrow.baseDeCas;
import java.util.ArrayList;

/**
 * Super classe de la base de cas
 * @author Thibault DELAVELLE
 *
 */
public class BaseDeCas {
	
	//taille de la base
	protected int taille;
	//liste des cas
	protected ArrayList<Cas> lcas;
	
	
	public BaseDeCas(){
		this.lcas = new ArrayList<Cas>();
		this.taille = lcas.size();
	}

	public Cas getCas(int i){
		return lcas.get(i);
	}

	public void ajouterCas(Cas c){
		this.lcas.add(c);
		this.taille++;
	}

	public void supprimerCas(Cas c){
		this.lcas.remove(c);
		this.taille--;
	}


	public int getTailleBase(){
		return this.taille;
	}

	public void setTailleBase(int t){
		this.taille = t;
	}

	public void setTailleBase(){
		this.taille = lcas.size();
	}

	public String toString(){
		StringBuilder st = new StringBuilder();
		for(int i = 0 ; i < taille ; i++){
			st.append("Cas"+(i+1)+" : "+lcas.get(i).toString()+"\n");
		}
		return st.toString();	
	}

}
