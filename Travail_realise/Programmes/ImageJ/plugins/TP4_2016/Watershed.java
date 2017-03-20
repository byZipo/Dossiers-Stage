// CODE A  COMPLETER

import ij.process.*;
import java.util.*;

public class Watershed {
	public static final int MASK = -3;
	public static final int WATERSHED = -2;
	public static final int UNLABELLED = -1;
	public static final int FIRST_LABEL = 0;

	/* Voisinage. */
	/**
	 * Nb de voisins.
	 */
	static final int nv = 4;
	/**
	 * Deplacement en x pour obtenir les voisins.
	 */
	static int[] vx = {1,0,-1,0};
	/**
	 * Deplacement en y pour obtenir les voisins.
	 */
	static int[] vy = {0,-1,0,1};

	/**
	 * ENTREE de l'algorithme.
	 * Parametre de precision de l'altitude.
	 */
	public static float EPSILON = 5.0f;

	/**
	 * ENTREE de l'algorithme.
	 * La carte des altitudes.
	 */
	FloatProcessor i_alt;

	/**
	 * L'image des labels.
	 * NB : place ici pour etre vu de la classe interne Pixel.
	 */
	IntImage i_label;

	/**
	 * La carte des distances.
	 * NB : place ici pour etre vu de la classe interne Pixel.
	 */
	IntImage i_dist;

	/**
	 * Represente un pixel de l'image dans le processus d'immersion de
	 * la carte. Accede directement aux donnees de la classe Watershed
	 * pour simplifier le code.
	 */
	class Pixel implements Comparable {
		public int x;
		public int y;

		/**
		 * Constructeur a partir des coordonnees.
		 */
		public Pixel( int i, int j )
		{ 
			x = i; 
			y = j; 
		}

		/**
		 * Altitude du pixel considere.
		 */
		public float altitude()
		{
			return i_alt.getf( x, y );
		}

		/**
		 * Implemente le service de 'Comparable'.
		 * Utilise pour le tri.
		 */
		public int compareTo(Object o) {
			Pixel autre = (Pixel) o;
			if ( altitude() < autre.altitude() ) return -1;
			else if ( altitude() > autre.altitude() ) return 1;
			return 0;
		}

		/**
		 * @return le label actuel du pixel.
		 */
		public int label() 
		{
			return i_label.getPixel( x, y );
		}

		/**
		 * Modifie le label actuel du pixel.
		 * @param l le nouveau label
		 */
		public void setLabel( int l ) 
		{
			i_label.setPixel( x, y, l );
		}

		/**
		 * @return la distance actuelle du pixel au bord le plus
		 * proche (autre region ou watershed).
		 */
		public int d() 
		{
			return i_dist.getPixel( x, y );
		}

		/**
		 * Modifie la distance actuelle du pixel au bord le plus
		 * proche (autre region ou watershed).
		 * @param d la nouvelle distance
		 */
		public void setD( int d ) 
		{
			i_dist.setPixel( x, y, d );
		}

	}

	/**
	 * Cette classe permet de lister successivement les voisins d'un
	 * pixel. Pratique, notamment car elle gere correctement les
	 * pixels sur le bord.
	 */
	class Voisins 
	{
		Pixel _p;
		int _cur;

		/**
		 * Constructeur.
		 * @param pixel designe le pixel dont on cherche les 4 voisins
		 * directs.
		 */
		public Voisins( Pixel pixel )
		{
			_p = pixel;
			_cur = 0;
		}

		/**
		 * @return le voisin courant ou 'null' si tous les voisins ont
		 * deja ete parcourus.
		 *
		 * @see next
		 */
		public Pixel current()
		{
			int px;
			int py;
			while ( _cur < nv ) {
				px = _p.x + vx[ _cur ];
				py = _p.y + vy[ _cur ];
				if ( ( px >= 0 ) && ( py >= 0 ) 
						&& ( px < i_alt.getWidth() )
						&& ( py < i_alt.getHeight() ) )
					return new Pixel( px, py );
				_cur++;
			}
			return null;
		}

		/**
		 * Passe au voisin suivant.
		 */
		public void next()
		{
			_cur++;
		}
	}


	/**
	 * Constructeur.
	 *
	 * @param image_altitude la carte d'elevation ou image d'altitude.
	 * @param epsilon la precision sur les altitudes des pixels.
	 */
	public Watershed( FloatProcessor image_altitude, float epsilon )
	{
		System.out.println("epsilon : " + epsilon);
		i_alt = image_altitude;
		EPSILON = epsilon;
	}



	/**
	 * A partir de l'initialisation du watershed (voir constructeur),
	 * calcule la ligne de partage des eaux de l'image [i_alt].
	 *
	 * @return une reference a [i_label]
	 * @see i_alt,i_label
	 */
	public IntImage calculeWatershed()
	{
		if ( i_alt == null ) return null;

		// Initialisation des images
		int w = i_alt.getWidth();
		int h = i_alt.getHeight();
		i_label = new IntImage( w, h );
		i_dist = new IntImage( w, h );
		int label = FIRST_LABEL; // prochain label disponible
		// Contient les pixels de meme altitude a traiter dans le bon ordre.
		LinkedList<Pixel> fifo = new LinkedList<Pixel>();

		/* Trie les pixels par ordre d'altitude et initialise les images. */
		System.out.println( "Initialisation labels et distance ..." );
		System.out.println( "Tri des pixels ..." );
		Pixel[] t_pixels = new Pixel[ w * h ];
		Pixel pixel_fictif = new Pixel( -1, -1 );
		int idx = 0;
		for ( int y = 0; y < h; y++ )
			for ( int x = 0; x < w; x++ )
			{
				t_pixels[ idx ] = new Pixel( x, y );
				t_pixels[ idx ].setLabel( UNLABELLED );
				t_pixels[ idx ].setD( 0 );
				idx++;
			}
		Arrays.sort( t_pixels ); // (I) tri

		/* Traitement de chaque pixel. */
		System.out.println( "Traitement de chaque pixel ..." );
		int i = 0;
		while ( i < t_pixels.length ) {
			// Recupere tous les pixels de meme altitude
			int old_i = i;
			float altitude = t_pixels[ i ].altitude();
			while ( ( i < t_pixels.length ) &&
					( t_pixels[ i ].altitude() <= ( altitude + EPSILON )) ) {
				Pixel p = t_pixels[ i ];
				p.setLabel( MASK );

				// (II) On commence par traiter les pixels proches
				// d'un bassin ou frontiere existant. (Si pixel touche
				// un bassin existant ou une ligne de crete.)
				Voisins N = new Voisins( p );
				while ( N.current() != null ) {
					Pixel q = N.current();
					if ( ( q.label() >= FIRST_LABEL )
							|| ( q.label() == WATERSHED ) ) {
						p.setD( 1 );
						fifo.addLast( p );
						break;
					}
					N.next();
				}
				i++;
			}
			System.out.println( ( i - old_i ) + " pixels d'altitude " + altitude );

			// On traite d'abord les pixels a distance 1, puis ceux a
			// distance 2, etc.
			int d_cur = 1;
			// Pixel fictif
			fifo.addLast( pixel_fictif );
			while ( true ) {
				Pixel p = (Pixel) fifo.removeFirst();
				if ( p == pixel_fictif )  {
					// (III) pixel fictif
					if ( fifo.isEmpty() ) // cette altitude est finie
						break;
					fifo.addLast( pixel_fictif );
					d_cur++;
					p = (Pixel) fifo.removeFirst();
				}
				// IV. p est un vrai pixel
				// On examine les voisins pour l'etiqueter
				Voisins N = new Voisins( p );
				while ( N.current() != null ) {
					Pixel q = N.current();
					// IV.TODO : completez cette partie.
					// FIN IV.TODO.
					N.next();
				} // End: while ( N.current() != null )
			} // End : while ( true )

			// (V) Fin de l'etape [h,h+eps] de l'algorithme.
			// Regarde s'il y a de nouveaux bassins et les etiquette.
			// V.TODO : completez cette partie.
			// FIN V.TODO.
	} // End : while ( i < t_pixels.length )
		return i_label;
	}

	/**
	 * Cette fonction ferme le watershed de Vincent et Soille dont les
	 * labels sont dans l'image [iws].
	 *
	 * @param iws une image de labels telle que fournit par 'calculeWatershed'
	 * @return une nouvelle image de labels ou toutes les regions sont
	 * separees par des pixels etiquetes 'WATERSHED'.
	 */
	public static IntImage fermeWatershed( IntImage iws )
	{
	    // TODO
		return iws;
	}


}


