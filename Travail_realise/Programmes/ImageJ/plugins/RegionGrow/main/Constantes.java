package RegionGrow.main;

/**
 * Ensemble des constantes utilis�es dans le programme
 * @author Thibault DELAVELLE
 *
 */
public final class Constantes {
	
	//les types de traitements
	public static enum TypeTraitement {Unsharped,Moyenneur,Median};
	
	//la couleur blanche sur 3 canaux RGB en conversion enti�re (= 24 bits � 1 en binaire)
	public static final int BLANC = 16777215;
	
	//la couleur rouge sur 3 canaux RGB en conversion enti�re (=111111110000000000000000)
	public static final int ROUGE = 16711680;

	//la couleur verte sur 3 canaux RGB en conversion enti�re (=1111111100000000)
	public static final int VERT = 65280;
	
	//la couleur bleue sur 3 canaux RGB en conversion enti�re (=11111111)
	public static final int BLEU = 255;
	
	//valeur associ�e � un pixel marqu� dans la croissance r�gion
	public static final int MARQUE = -1;
	
	
	/* COULEURS DES ORGANES ***********
		 proc�dure pour d�terminer la couleur en int : 
		 1. r�cup�rer le RGB de la couleur souhait�e
		 2. convertir le RGB en Hexa (http://www.rgbtohex.net/)
		 3. convertir l'hexa en d�cimal (http://www.binaryhexconverter.com/hex-to-decimal-converter)
	****************************************/
	
	//la couleur de la tumeur
	public static final int COULEUR_TUMEUR_RENALE = 4749128;
	
	//la couleur de l'artere r�nale
	public static final int COULEUR_ARTERE_RENALE = 7090727;
	
	//la couleur de la veine r�nale
	public static final int COULEUR_VEINE_RENALE = 19303;
	
	//la couleur de la cavit� r�nale
	public static final int COULEUR_CAVITE_RENALE = 7237954;
	
	//la couleur du rein
	public static final int COULEUR_REIN = 5911593;
	
	//la couleur de la colonne vert�brale
	public static final int COULEUR_COLONNE_VERTEBRALE = 16777215;
	
	/*-***************************************-*/
	
	//activer/desactiver la suppresion des muscles lors de la segmentation
	public static final boolean MUSCLES_A_ENLEVER = true;
	
	//d�finit l'intensit� minimale en niveau de gris d'un rein sur les scanners (avec pr�traitement)
	public static final int SEUIL_INF_COULEUR_REIN = 150;
	
}
