package RegionGrow.main;

/**
 * Ensemble des constantes utilisées dans le programme
 * @author Thibault DELAVELLE
 *
 */
public final class Constantes {
	
	//les types de traitements
	public static enum TypeTraitement {Unsharped,Moyenneur,Median};
	
	//la couleur blanche sur 3 canaux RGB en conversion entière (= 24 bits à 1 en binaire)
	public static final int BLANC = 16777215;
	
	//la couleur rouge sur 3 canaux RGB en conversion entière (=111111110000000000000000)
	public static final int ROUGE = 16711680;

	//la couleur verte sur 3 canaux RGB en conversion entière (=1111111100000000)
	public static final int VERT = 65280;
	
	//la couleur bleue sur 3 canaux RGB en conversion entière (=11111111)
	public static final int BLEU = 255;
	
	//valeur associée à un pixel marqué dans la croissance région
	public static final int MARQUE = -1;
	
	
	/* COULEURS DES ORGANES ***********
		 procédure pour déterminer la couleur en int : 
		 1. récupérer le RGB de la couleur souhaitée
		 2. convertir le RGB en Hexa (http://www.rgbtohex.net/)
		 3. convertir l'hexa en décimal (http://www.binaryhexconverter.com/hex-to-decimal-converter)
	****************************************/
	
	//la couleur de la tumeur
	public static final int COULEUR_TUMEUR_RENALE = 4749128;
	
	//la couleur de l'artere rénale
	public static final int COULEUR_ARTERE_RENALE = 7090727;
	
	//la couleur de la veine rénale
	public static final int COULEUR_VEINE_RENALE = 19303;
	
	//la couleur de la cavité rénale
	public static final int COULEUR_CAVITE_RENALE = 7237954;
	
	//la couleur du rein
	public static final int COULEUR_REIN = 5911593;
	
	//la couleur de la colonne vertébrale
	public static final int COULEUR_COLONNE_VERTEBRALE = 16777215;
	
	/*-***************************************-*/
	
	//activer/desactiver la suppresion des muscles lors de la segmentation
	public static final boolean MUSCLES_A_ENLEVER = true;
	
	//définit l'intensité minimale en niveau de gris d'un rein sur les scanners (avec prétraitement)
	public static final int SEUIL_INF_COULEUR_REIN = 150;
	
}
