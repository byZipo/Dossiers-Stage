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
	
	protected double diametreTumeur;
	protected double hauteurTumeur;
	protected double epaisseurMuscles;


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

	public double getDiametreTumeur() {
		return diametreTumeur;
	}

	public void setDiametreTumeur(double diametreTumeur) {
		this.diametreTumeur = diametreTumeur;
	}

	public double getHauteurTumeur() {
		return hauteurTumeur;
	}

	public void setHauteurTumeur(double hauteurTumeur) {
		this.hauteurTumeur = hauteurTumeur;
	}

	public double getEpaisseurMuscles() {
		return epaisseurMuscles;
	}

	public void setEpaisseurMuscles(double epaisseurMuscles) {
		this.epaisseurMuscles = epaisseurMuscles;
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
	 * @param content : la valeur associ�e � la caract�ristique
	 */
	public void setCaracByString(String carac, String content){
		
		double val = 0;
		try{
			val = Double.parseDouble(content);
		}catch(Exception e){
			System.err.println("ERREUR CARACTERISTIQUE PROBLEME ("+carac+") : "+content+" n'est pas un double");
		}
		
		
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
	
	/**
	 * Calcule la similarit� des caract�ristiques NonImage selon la formule de Perner entre this (nouveau cas)
	 * et un cas de la base de cas
	 * @param p : un Probl�me de la base de cas
	 * @param alpha : un poids 
	 * @param beta : un poids
	 * @param gamma : un poids
	 * @return : la valeur de similarit�
	 */
	public double getSimNonImage(Probleme p, double alpha, double beta, double gamma){
		
		int Ai = this.getAI(p);
		int Di = this.getDI(p);
		int Ei = this.getEI(p);
		
		return (double) (Ai / (double)(alpha*Ai + beta*Di * gamma*Ei));
	}
	
	
	/**
	 * R�alise le calcul de la fonction both(x,y) pour chaque carac (x et y) entre this et un Probl�me de la base
	 * both(120,125) = 4 
	 * 01111000
	 * 01111101
	 * --> il suffit de compter le nombre de '1' � la m�me position
	 * @param p : le nouveau Probl�me
	 * @return la somme des both(x,y) pour chaque x de p et chaque y de this
	 */
	public int getAI(Probleme p){
		
		ArrayList<Double> caracsNonImageT = getListeNonImage(this);
		ArrayList<Double> caracsNonImageP = getListeNonImage(p);
		
		int res = 0;
		
		for (int i = 0; i < caracsNonImageT.size(); i++) {
			
			//r�cup�ration du binaire des caracs
			String tmpT = getBinaire(caracsNonImageT.get(i));
			String tmpP = getBinaire(caracsNonImageP.get(i));
			
			//ajout de 0 en d�but de chaine si tailles diff�rentes
			//exemple : 101010 > 111 (en longueur) donc 111 devient 000111
			//ceci est n�cessaire pour la future comparaison
			while(tmpT.length()<tmpP.length()){
				tmpT = '0'+tmpT;
			}
			while(tmpP.length()<tmpT.length()){
				tmpP = '0'+tmpP;
			}
			
			//comparaison et incr�mentation si both(x,y) est respect�
			for (int j = 0; j < tmpT.length(); j++) {
				if(tmpT.charAt(j)=='1' && tmpP.charAt(j)=='1')res++;
			}
		}
		return res;
	}
	
	/**
	 * R�alise le cacul de la fonction only(x) pour chaque carac (x et y) entre this et un Probl�me de la base
	 * this = y (nouveau cas) et x = p (cas de la base)
	 * only(120) = 0 only(125) = 2 
	 * 01111000 = 120
	 * 01111101 = 125
	 * @param p : le nouveau Probl�me
	 * @return la somme des only(x) pour chaque x de p et chaque y de this
	 */
	public int getDI(Probleme p){
		ArrayList<Double> caracsNonImageT = getListeNonImage(this);
		ArrayList<Double> caracsNonImageP = getListeNonImage(p);
		
		int res = 0;
		
		for (int i = 0; i < caracsNonImageT.size(); i++) {
			
			//r�cup�ration du binaire des caracs
			String tmpT = getBinaire(caracsNonImageT.get(i));
			String tmpP = getBinaire(caracsNonImageP.get(i));
			
			//ajout de 0 en d�but de chaine si tailles diff�rentes
			//exemple : 101010 > 111 (en longueur) donc 111 devient 000111
			//ceci est n�cessaire pour la future comparaison
			while(tmpT.length()<tmpP.length()){
				tmpT = '0'+tmpT;
			}
			while(tmpP.length()<tmpT.length()){
				tmpP = '0'+tmpP;
			}
			
			//comparaison et incr�mentation si only(x) est respect�
			for (int j = 0; j < tmpT.length(); j++) {
				if(tmpT.charAt(j)=='0' && tmpP.charAt(j)=='1')res++;
			}
		}
		return res;
		
	}
	
	
	/**
	 * R�alise le cacul de la fonction only(y) pour chaque carac (x et y) entre this et un Probl�me de la base
	 * this = y (nouveau cas) et x = p (cas de la base)
	 * only(120) = 0 only(125) = 2 
	 * 01111000 = 120
	 * 01111101 = 125
	 * @param p : le nouveau Probl�me
	 * @return la somme des only(y) pour chaque x de p et chaque y de this
	 */
	public int getEI(Probleme p){
		ArrayList<Double> caracsNonImageT = getListeNonImage(this);
		ArrayList<Double> caracsNonImageP = getListeNonImage(p);
		
		int res = 0;
		
		for (int i = 0; i < caracsNonImageT.size(); i++) {
			
			//r�cup�ration du binaire des caracs
			String tmpT = getBinaire(caracsNonImageT.get(i));
			String tmpP = getBinaire(caracsNonImageP.get(i));
			
			//ajout de 0 en d�but de chaine si tailles diff�rentes
			//exemple : 101010 > 111 (en longueur) donc 111 devient 000111
			//ceci est n�cessaire pour la future comparaison
			while(tmpT.length()<tmpP.length()){
				tmpT = '0'+tmpT;
			}
			while(tmpP.length()<tmpT.length()){
				tmpP = '0'+tmpP;
			}
			
			//comparaison et incr�mentation si only(x) est respect�
			for (int j = 0; j < tmpT.length(); j++) {
				if(tmpT.charAt(j)=='1' && tmpP.charAt(j)=='0')res++;
			}
		}
		return res;
		
	}
	
	
	
	
	
	
	/**
	 * Remplit et retourne la liste des caracs NonImage du Probl�me pass� en param�tre
	 * @param probleme : le Probl�me
	 * @return : la liste
	 */
	public ArrayList<Double> getListeNonImage(Probleme probleme) {
		
		ArrayList<Double> res = new ArrayList<Double>();
		res.add(probleme.age);
		res.add(probleme.taille);
		res.add(probleme.masse);
		res.add(probleme.sexe);
		res.add(probleme.nbCoupes);
		res.add(probleme.hauteurCoupe);
		
		return res;
	}
	
	
	/**
	 * Remplit et retourne la liste des caracs Image du Probl�me pass� en param�tre
	 * @return : la liste
	 */
	public ArrayList<Double> getListeImage() {
		
		ArrayList<Double> res = new ArrayList<Double>();
		res.add(this.moyenne);
		res.add(this.asymetrie);
		res.add(this.variance);
		res.add(this.kurtosis);
		
		return res;
	}


	/**
	 * Calcule la valeur binaire d'un double
	 * @param a : le double
	 * @return : le String repr�sentant le binaire de a
	 */
	public String getBinaire(double a){
		return Integer.toString((int)a,2);
	}
	
	
	
	/**
	 * Calcule la similarit� des caract�ristiques Image selon la formule de Perner entre this (nouveau cas)
	 * et p (cas de la base)
	 * @param p : le Probleme de la base de cas
	 * @param w : le facteur de poids
	 * @param listeMin : la liste des valeurs min pour chaque carac de la base
	 * @param listeMax : la liste des valeurs max pour chaque carac de la base
	 * @return : la valeur de similarit�
	 */
	public double getSimImage(Probleme p, double w, ArrayList<Double> listeMin, ArrayList<Double> listeMax){
		double res = 0;
		ArrayList<Double> caracsImageT = this.getListeImage();
		ArrayList<Double> caracsImageP = p.getListeImage();
		int n = caracsImageT.size();
		for (int i = 0; i < caracsImageT.size(); i++) {
			double minI = listeMin.get(i);
			double maxI = listeMax.get(i);
			
			double xI = caracsImageP.get(i);
			double yI = caracsImageT.get(i);
			double partieGauche = (double)(xI-minI)/(maxI-minI);
			double partieDroite = (double)(yI-minI)/(maxI-minI);
			
			res += (double) w * Math.abs(partieGauche-partieDroite);
		}
		return 1 - (res/n);
	}
	
	/**
	 * Calcule la similarit� globale entre deux cas, � partir des calculs de 
	 * la similarit� Image et de la similarit� NonImage
	 * @param simNonImage : la valeur de similarit� NonImage
	 * @param simImage : la valeur de similarit� Image
	 * @param wNI : le poids associ� � la similarit� NonImage
	 * @param wI : le poids associ� � la similarit� Image
	 * @return : la similarit� globale
	 */
	public double getSimGlobale(double simNonImage, double simImage, double wNI, double wI) {
		return (double)((double)(wNI*simNonImage) + (double)(wI*simImage));
	}	
	
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append("PROBLEME : \n");
		st.append("| NonImage : age:"+age+" , taille:"+taille+" , masse:"+masse+" , sexe:"+sexe+" , nbCoupes:"+nbCoupes+" , hauteurCoupe:"+hauteurCoupe+" , diam�tre tumeur:"+diametreTumeur+" , hauteur tumeur:"+hauteurTumeur+" , epaisseur muscles:"+epaisseurMuscles+" \n| Image : moyenne:"+moyenne+" , asymetrie:"+asymetrie+" , variance:"+variance+" , kurtosis:"+kurtosis+" ");
		for(int i = 0; i<positonFloueTumeur.size(); i++){
			st.append("\n| Relation"+(i+1)+" : "+positonFloueTumeur.get(i).toStringSansSeuils()+" ");
		}
		return st.toString();
	}



	
}

