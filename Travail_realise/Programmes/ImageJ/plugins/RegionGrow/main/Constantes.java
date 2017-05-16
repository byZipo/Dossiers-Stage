package RegionGrow.main;

/**
 * Ensemble des constantes utilisées dans le programme
 * @author Thibault DELAVELLE
 *
 */
public final class Constantes {
	
	//les types de triatements
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
	
}
