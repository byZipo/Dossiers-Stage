package RegionGrow.baseDeCas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

	/**
	 * Calcule la valeur min pour chaque carac(Image) de la partie problème de la base de cas
	 * (nécessaire pour le calcul de similarité de Perner)
	 * @return : la liste des valeurs min
	 */
	public ArrayList<Double> getValeursMinImage() {
		
		//initialisation de la liste des caracs Image, avec une valeur infinie par défaut car on cherche le min
		ArrayList<Double> res = new ArrayList<Double>();
		Probleme init = lcas.get(0).getProbleme();
		for (int i = 0; i < init.getListeImage().size(); i++) {
			res.add(i, Double.MAX_VALUE);
		}
		
		//recherche du min pour chaque cas et chaque carac
		for (int i = 0; i < lcas.size(); i++) {
			Probleme p = lcas.get(i).getProbleme();
			for (int j = 0; j < p.getListeImage().size(); j++) {
				double tmp = p.getListeImage().get(j);
				if( tmp < res.get(j) )res.set(j, tmp);
			}
		}
		
		return res;
	}

		
	/**
	 *  Calcule la valeur max pour chaque carac(Image) de la partie problème de la base de cas
	 *  (nécessaire pour le calcul de similarité de Perner)
	 * @return : la liste des valeurs max
	 */
	public ArrayList<Double> getValeursMaxImage() {
		
		//initialisation de la liste des caracs Image, avec une valeur à -inifini par défaut car on cherche le max
		ArrayList<Double> res = new ArrayList<Double>();
		Probleme init = lcas.get(0).getProbleme();
		for (int i = 0; i < init.getListeImage().size(); i++) {
			res.add(i, Double.MIN_VALUE);
		}
		
		//recherche du max pour chaque cas et chaque carac
		for (int i = 0; i < lcas.size(); i++) {
			Probleme p = lcas.get(i).getProbleme();
			for (int j = 0; j < p.getListeImage().size(); j++) {
				double tmp = p.getListeImage().get(j);
				if( tmp > res.get(j) )res.set(j, tmp);
			}
		}
		
		return res;
	}

}
