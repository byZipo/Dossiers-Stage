package RegionGrow.main;

/**
 * Ensemble des constantes utilis�es dans le programme
 * @author Thibault DELAVELLE
 *
 */
public final class Constantes {
	
	//les types de triatements
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
	
}
