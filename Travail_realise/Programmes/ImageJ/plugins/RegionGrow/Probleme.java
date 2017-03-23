public class Probleme {
	
	
	//informations nonimage (des doubles en vue d'une normalisation)
	protected double age;
	protected double taille;
	protected double masse;
	protected double sexe;
	protected double nbCoupes;
	protected double hauteurCoupe;
	
	//informations image
	protected double moyenne;
	protected double asymetrie;
	protected double variance;
	protected double kurtosis;
	
	//poids globaux (les poids locaux sont directement appliqués aux champs par un seter())
	protected double poidsNonImage;
	protected double poidsImage;

	public Probleme(){
		
	}
	
	//constructeur sans poids
	public Probleme(double age, double taille, double masse, double sexe, double nbCoupes, double hauteurCoupe, double moyenne, double asymetrie, double variance, double kurtosis){
		this.age=age;
		this.taille = taille;
		this.masse = masse;
		this.sexe = sexe;
		this.nbCoupes = nbCoupes;
		this.hauteurCoupe = hauteurCoupe;
		this.moyenne = moyenne;
		this.asymetrie = asymetrie;
		this.variance = variance;
		this.kurtosis = kurtosis;
	}
	
	//constructeur avec poids
	public Probleme(double age, double taille, double masse, double sexe, double nbCoupes, double hauteurCoupe, double moyenne, double asymetrie, double variance, double kurtosis, double poidsNonImage, double poidsImage){
		this.age=age;
		this.taille = taille;
		this.masse = masse;
		this.sexe = sexe;
		this.nbCoupes = nbCoupes;
		this.hauteurCoupe = hauteurCoupe;
		this.moyenne = moyenne;
		this.asymetrie = asymetrie;
		this.variance = variance;
		this.kurtosis = kurtosis;
		this.poidsNonImage = poidsNonImage;
		this.poidsImage = poidsImage;
	}
	
	//ponderations locales
	public void pondererAge(double poids){
		age = (double) age*poids;
	}
	public void pondererTaille(double poids){
		taille = (double) taille*poids;
	}
	public void pondererMasse(double poids){
		masse = (double) masse*poids;
	}
	public void pondererSexe(double poids){
		sexe = (double) sexe*poids;
	}
	public void pondererNbCoupes(double poids){
		nbCoupes = (double) nbCoupes*poids;
	}
	public void pondererHauteurCoupe(double poids){
		hauteurCoupe = (double) hauteurCoupe*poids;
	}
	public void pondererMoyenne(double poids){
		moyenne = (double) moyenne*poids;
	}
	public void pondererAsymetrie(double poids){
		asymetrie = (double) asymetrie*poids;
	}
	public void pondererVariance(double poids){
		variance = (double) variance*poids;
	}
	public void pondererKurtosis(double poids){
		kurtosis = (double) kurtosis*poids;
	}

	//accesseurs
	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public double getTaille() {
		return taille;
	}

	public void setTaille(double taille) {
		this.taille = taille;
	}

	public double getMasse() {
		return masse;
	}

	public void setMasse(double masse) {
		this.masse = masse;
	}

	public double getSexe() {
		return sexe;
	}

	public void setSexe(double sexe) {
		this.sexe = sexe;
	}

	public double getNbCoupes() {
		return nbCoupes;
	}

	public void setNbCoupes(double nbCoupes) {
		this.nbCoupes = nbCoupes;
	}

	public double getHauteurCoupe() {
		return hauteurCoupe;
	}

	public void setHauteurCoupe(double hauteurCoupe) {
		this.hauteurCoupe = hauteurCoupe;
	}

	public double getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}

	public double getAsymetrie() {
		return asymetrie;
	}

	public void setAsymetrie(double asymetrie) {
		this.asymetrie = asymetrie;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getKurtosis() {
		return kurtosis;
	}

	public void setKurtosis(double kurtosis) {
		this.kurtosis = kurtosis;
	}

	public double getPoidsNonImage() {
		return poidsNonImage;
	}

	public void setPoidsNonImage(double poidsNonImage) {
		this.poidsNonImage = poidsNonImage;
	}

	public double getPoidsImage() {
		return poidsImage;
	}

	public void setPoidsImage(double poidsImage) {
		this.poidsImage = poidsImage;
	}
	
	public String toString(){
		return "NonImage : "+age+" "+taille+" "+masse+" "+sexe+" "+nbCoupes+" "+hauteurCoupe+" Image : "+moyenne+" "+asymetrie+" "+variance+" "+kurtosis;
	}
	
}
