public class Germe {
	
	protected int x;
	protected int y;
	protected int couleur;
	
	//par la suite il faudra surement 1seuil local/global par germe
	
	public Germe(){
		
	}
	
	public Germe(int x, int y){
		this.x=x;
		this.y=y;
		this.couleur = 0;
	}
	
	public Germe(int x, int y, int couleur){
		this.x=x;
		this.y=y;
		this.couleur=couleur;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCouleur() {
		return couleur;
	}

	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	
	public String toString(){
		return "          position : ("+x+";"+y+") , couleur : "+couleur+" ";
	}

}
