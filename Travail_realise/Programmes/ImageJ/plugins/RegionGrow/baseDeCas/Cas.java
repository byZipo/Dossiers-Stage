package RegionGrow.baseDeCas;

/**
 * Un cas est représenté par un probleme et une solution
 * @author Thibault DELAVELLE
 *
 */
public class Cas {
	
	protected Probleme probleme;
	protected Solution solution;
	 
	
	public Cas(){
	}
	
	public Cas(Probleme probleme, Solution solution){
		this.probleme = probleme;
		this.solution = solution;
	}
	
	/**
	 * retourne le probleme du cas
	 * @return
	 */
	public Probleme getProbleme(){
		return this.probleme;
	}
	
	/**
	 * retourne la solution de cas
	 * @return
	 */
	public Solution getSolution(){
		return this.solution;
	}
	
	/**
	 * définit le probleme du cas
	 * @param p : le probleme
	 */
	public void setProbleme(Probleme p){
		this.probleme = p;
	}
	
	/**
	 * définit la solution du cas
	 * @param s : la solution
	 */
	public void setSolution(Solution s){
		this.solution = s;
	}
	
	/**
	 * définit probleme et solution du cas
	 * @param p : le probleme
	 * @param s : la solution
	 */
	public void setPbSol(Probleme p, Solution s){
		this.probleme = p;
		this.solution = s;
	}
	
	/**
	 * affiche le cas
	 */
	public String toString(){
		return probleme.toString()+"\n"+solution.toString();
	}
	

}
