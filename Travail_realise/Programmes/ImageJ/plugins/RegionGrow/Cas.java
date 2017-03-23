

public class Cas {
	
	protected Probleme probleme;
	protected Solution solution;
	 
	
	public Cas(){
	}
	
	public Cas(Probleme probleme, Solution solution){
		this.probleme = probleme;
		this.solution = solution;
	}
	
	public Probleme getProbleme(){
		return this.probleme;
	}
	
	public Solution getSolution(){
		return this.solution;
	}
	
	public void setProbleme(Probleme p){
		this.probleme = p;
	}
	
	public void setSolution(Solution s){
		this.solution = s;
	}
	
	public void setPbSol(Probleme p, Solution s){
		this.probleme = p;
		this.solution = s;
	}
	
	public String toString(){
		return "Probleme : "+probleme.toString()+" --> Solution : "+solution.toString()+"\n";
	}
	

}
