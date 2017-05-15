package RegionGrow.baseDeCas;

import java.lang.reflect.Field;
import java.util.ArrayList;

import RegionGrow.ontologieRelationsSpatiales.RelationSpatiale;

/**
 * Un probl�me est r�pr�sent� par des caract�ristiques image et non-image, et des poids pour chacun d'eux
 * @author Thibault DELAVELLE
 *
 */
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
	
	//poids globaux (les poids locaux sont directement appliqu�s aux champs par un seter())
	protected double poidsNonImage;
	protected double poidsImage;
	
	//liste de relations spatiales floues concernant la position de la tumeur
	protected ArrayList<RelationSpatiale> positonFloueTumeur;

	public Probleme(){
		this.positonFloueTumeur = new ArrayList<RelationSpatiale>();
	}
	
	

	/**
	 * Constructeur sans poids
	 * @param age : l'�ge en ann�es du patient
	 * @param taille : la taille en cm du patient
	 * @param masse : la masse en kg du patient
	 * @param sexe : le sexe (0=F, 1=M) du patient
	 * @param nbCoupes : le nombre de coupes du scanner complet
	 * @param hauteurCoupe : la hauteur de la coupe par rapport � l'ensemble des coupes du scanner complet
	 * @param moyenne : la moyenne de niveaux de gris de l'image
	 * @param asymetrie : l'asym�trie de l'image
	 * @param variance : la variance de l'image
	 * @param kurtosis : le kurtosis de l'image
	 */
	public Probleme(double age, double taille, double masse, double sexe, double nbCoupes, double hauteurCoupe, double moyenne, double asymetrie, double variance, double kurtosis){
		this.positonFloueTumeur = new ArrayList<RelationSpatiale>();
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
	
	/**
	 * Constructeur complet avec poids
	 * @param age : l'�ge en ann�es du patient
	 * @param taille : la taille en cm du patient
	 * @param masse : la masse en kg du patient
	 * @param sexe : le sexe (0=F, 1=M) du patient
	 * @param nbCoupes : le nombre de coupes du scanner complet
	 * @param hauteurCoupe : la hauteur de la coupe par rapport � l'ensemble des coupes du scanner complet
	 * @param moyenne : la moyenne de niveaux de gris de l'image
	 * @param asymetrie : l'asym�trie de l'image
	 * @param variance : la variance de l'image
	 * @param kurtosis : le kurtosis de l'image
	 * @param poidsNonImage : le poids global associ� � l'ensemble des caract�ristiques image
	 * @param poidsImage : le poids global associ� � l'ensemble des caract�ristiques non-image
	 */
	public Probleme(double age, double taille, double masse, double sexe, double nbCoupes, double hauteurCoupe, double moyenne, double asymetrie, double variance, double kurtosis, double poidsNonImage, double poidsImage){
		this.positonFloueTumeur = new ArrayList<RelationSpatiale>();
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
	
	
	//m�thode tr�s pratique pour la lecture fichier, o� l'on re�oit un String que l'on veut convertir en attribut pour le setter.
	/**
	 * Affecte une valeur � une caract�ristique du probl�me en fonction de son nom sous forme de String
	 * @param carac : le String associ� � l'attribut Java des caract�ristiques de la classe Probleme (ex:"kurtosis")
	 * @param val : la valeur associ�e � la caract�ristique
	 */
	public void setCaracByString(String carac, double val){
		
		Class<?> c = this.getClass();
		try {
			//r�cup�ration de l'attribut correspondant au String (ex:Probleme.kurtosis)
			Field f = c.getDeclaredField(carac);
			f.setAccessible(true);
			try {
				//la v�rification du type est optionnelle/inutile dans ce cas (on ne re�oit que des doubles)
				/*Type t = f.getGenericType();
				if(t.getTypeName().equals("double"))*/
				f.setDouble(this, val);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				//e.printStackTrace();
				System.err.println("VALEUR "+val+" non correcte pour l'attribut "+carac);
			}
		} catch (NoSuchFieldException | SecurityException e) {
			//e.printStackTrace();
			System.err.println("ATTRIBUT "+carac+" inexistant dans la base de cas");
		}
	}
	
	public ArrayList<RelationSpatiale> getPositonFloueTumeur() {
		return positonFloueTumeur;
	}

	public void setPositonFloueTumeur(ArrayList<RelationSpatiale> positonFloueTumeur) {
		this.positonFloueTumeur = positonFloueTumeur;
	}
	
	public RelationSpatiale getRelationSpatiale(int i){
		return positonFloueTumeur.get(i);
	}
	
	public void ajouterRelationFloue(RelationSpatiale r){
		this.positonFloueTumeur.add(r);
	}
	
	
	
	/**
	 * R�alise la somme des caract�ristiques avec leur pond�ration
	 * @return la somme
	 */
	public double getSommeCaracs(){
		//je mets les pond�rations ici en fait
		return (double)(age+(taille*0.05)+masse+sexe+nbCoupes+hauteurCoupe+moyenne+asymetrie+variance+kurtosis);
	}
	
	
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append("PROBLEME : \n");
		st.append("| NonImage : age:"+age+" , taille:"+taille+" , masse:"+masse+" , sexe:"+sexe+" , nbCoupes:"+nbCoupes+" , hauteurCoupe:"+hauteurCoupe+" \n| Image : moyenne:"+moyenne+" , asymetrie:"+asymetrie+" , variance:"+variance+" , kurtosis:"+kurtosis+" ");
		for(int i = 0; i<positonFloueTumeur.size(); i++){
			st.append("\n| Relation"+(i+1)+" : "+positonFloueTumeur.get(i).toStringSansSeuils()+" ");
		}
		return st.toString();
	}
	
}

