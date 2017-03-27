// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage
import ij.process.ImageConverter;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;


public class Croissance_Regions2 implements PlugInFilter {


	//dimensions image
	protected int h,w;
	//nouvelle image dans laquelle on va dessiner la segmentation
	protected ImageProcessor ipr;
	//couleur de chaque pixel de l'image
	protected int[][] couleursPixels;

	ImagePlus image;

	public int setup(String arg, ImagePlus imp) {

		//L'image de base est automatiquement convertie en image 8-bit (pour obtenir une image en niveaux de gris)
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToGray8();
		this.image=imp;
		imp.updateAndDraw();
		return DOES_8G;
	}

	//fonction principale, qui lit et construit la base de cas, puis fait la segmentation
	public void run(ImageProcessor ip){
		
		//indice du numéro de cas, pour la phase de test car pour le moment on ne fait pas de choix (RaPC) sur la base
		int indiceBase = 0;

		//lecture du fichier de la base de cas, et construction de la base
		LectureFichier l = new LectureFichier();
		BaseDeCas base = null;
		try {
			base = l.LectureFichierBaseEnLigne("BaseDeCasEnLigne.txt");
			//System.out.println(base.toString());
			IJ.log("////////////////Avant segmentation : ///////////////\n");
			IJ.log(base.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//récupération des caractéristiques images de l'entrée
		ImageStatistics stats = ip.getStatistics();
		IJ.log("\nSTATISTIQUES IMAGE EN ENTREE : moyenne : "+stats.mean+" asymetrie : "+stats.skewness+" ecart-type : "+stats.stdDev+" kurtosis : "+stats.kurtosis+"\n");

		//les caracs nonImage sont en dur forcément
		Probleme pEntree = new Probleme(5,175,22,0,4,3,stats.mean,stats.skewness,stats.stdDev,stats.kurtosis);


		// RaPC !!! (basique pour essayer pour le moment)
		indiceBase = getMeilleurProbleme(pEntree,base);

		//Seuils
		int SeuilGlobal = base.getCas(indiceBase).getSolution().getSeuilGlobal(); 
		int SeuilLocal = base.getCas(indiceBase).getSolution().getSeuilLocal();
		
		//hauteur et largeur de l'image
		h=ip.getHeight();
		w=ip.getWidth();	

		//création de la nouvelle image dans laquelle on va dessiner la segmentation
		ImageProcessor ipDT= new ColorProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("Croissance Regions", ipDT);
		ipr = imageDT.getProcessor();
		
		//tableau des intensités en niveau de gris des pixels de l'image
		int[][] pixelsA = ip.getIntArray();


		//germes 
		//POUR LE MOMENT J'AI UNE LISTE DE Germes ET UNE LISTE DE Point, IL FAUDRA CHANGER CA, UNE SEULE SUFFIT !
		ArrayList<Point> germes = new ArrayList<Point>();
		ArrayList<Germe> lgermes = base.getCas(indiceBase).getSolution().getGermes();
		for(int i = 0 ; i < lgermes.size() ; i++){
			Point p = new Point(lgermes.get(i).getX(), lgermes.get(i).getY());
			germes.add(p);
		}

		
		
		//TEST image reinbeau.jpg
		/*germes.add(new Point(154,200));
		germes.add(new Point(322,297));
		germes.add(new Point(302,337));
		germes.add(new Point(198,330));
		germes.add(new Point(245,316));
		germes.add(new Point(185,204));
		germes.add(new Point(305,215));
		germes.add(new Point(250,235));
		germes.add(new Point(224,234));
		germes.add(new Point(241,209));
		germes.add(new Point(92,332));
		germes.add(new Point(113,213));
		germes.add(new Point(185,261));
		germes.add(new Point(257,207));
		germes.add(new Point(358,233));
		germes.add(new Point(388,262));*/
		

		

		//TEST image synth.pgm
		/*germes.add(new Point(200,150));
		germes.add(new Point(90,172));
		germes.add(new Point(65,31));
		germes.add(new Point(108,42));
		germes.add(new Point(231,85));*/

		//TEST image rein1_base.png
		//germes.add(new Point(164,61)); //rein --> seuils optimaux 53/15
		//germes.add(new Point(75,114));
			//germes.add(new Point(229,113));
		//germes.add(new Point(223,146));
		//germes.add(new Point(271,438));
		//germes.add(new Point(191,157)); //tumeur
		//germes.add(new Point(369,233)); //rond 1
		//germes.add(new Point(425,233)); //rond 2


		//liste de correspondance germe (donc région) --> couleur, utile pour la fusion de deux régions
		HashMap<Point,Integer> couleursRegions = new HashMap<Point,Integer>();
		//stockage des couleurs des pixels de l'image, nécessaire pour la fusion et pour la fermeture (dilatation et erosion)
		couleursPixels = new int[w][h];

		//pour la couleur de la région (couleur initiale n'a pas d'importance)
		int color = 255;

		//pour chaque germe
		for(int i = 0; i<germes.size(); i++){

			//à chaque région on associe une couleur aléatoire
			Random r = new Random();
			color = r.nextInt(16777215);
			couleursRegions.put(germes.get(i),color);
			lgermes.get(i).setCouleur(color);

			//récupération des coordonées 2D du germe
			int xGerme = (int)germes.get(i).getX();
			int yGerme = (int)germes.get(i).getY();

			//l'intensité moyennne de la région courante (nécessite le nombre de pixels de la région pour la m.a.j.)
			double moyenneRegion = pixelsA[xGerme][yGerme];
			int nbPixelsRegion = 0;

			//liste des points à évaluer, utilisée comme une pile (LIFO)
			ArrayList<Point> liste = new ArrayList<Point>();

			//ajout initial du germe pour la première itération
			liste.add(new Point(xGerme,yGerme));
			ipr.putPixel(xGerme,yGerme,color);

			//tant qu'on a des pixels à évaluer (i.e. des pixels potentiellement de la même région)
			while(!liste.isEmpty()){

				//récuperation du point courant
				Point courant = liste.get(0);
				int xCourant = (int)courant.getX();
				int yCourant = (int)courant.getY();
				//IJ.log("Val courant : "+pixelsA[xCourant][yCourant]+" | Val Germe : "+pixelsA[xGerme][yGerme]+" | Val Precedent : "+pixelsA[xPrecedent][yPrecedent]);

				//si le pixel est déjà marqué (i.e. visité) on passe au pixel suivant
				if(pixelsA[xCourant][yCourant]==-1){
					liste.remove(0);
					continue;
				}


				//pour chaque voisin en 4-connexité (non visité) du pixel courant, on va tester son homogénéité, et l'ajouter ou non à la région

				//droite
				if(xCourant<w-1 && pixelsA[xCourant+1][yCourant] != -1){
					if(Math.abs(pixelsA[xCourant+1][yCourant]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant+1][yCourant])<=SeuilLocal)	{
						liste.add(new Point(xCourant+1,yCourant));
						ipr.putPixel(xCourant+1,yCourant,color);
						//mise à jour de la moyenne de la région
						couleursPixels[xCourant+1][yCourant] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant+1][yCourant])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}

				}

				//bas
				if(yCourant<h-1 && pixelsA[xCourant][yCourant+1] != -1){
					if(Math.abs(pixelsA[xCourant][yCourant+1]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant][yCourant+1])<=SeuilLocal)	{
						liste.add(new Point(xCourant,yCourant+1));
						ipr.putPixel(xCourant,yCourant+1,color);
						couleursPixels[xCourant][yCourant+1] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant][yCourant+1])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}

				}


				//gauche
				if(xCourant>0  && pixelsA[xCourant-1][yCourant] != -1){
					if(Math.abs(pixelsA[xCourant-1][yCourant]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant-1][yCourant])<=SeuilLocal)	{
						liste.add(new Point(xCourant-1,yCourant));
						ipr.putPixel(xCourant-1,yCourant,color);
						couleursPixels[xCourant-1][yCourant] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant-1][yCourant])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}

				//haut
				if(yCourant>0  && pixelsA[xCourant][yCourant-1] != -1){
					if(Math.abs(pixelsA[xCourant][yCourant-1]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant][yCourant-1])<=SeuilLocal)	{
						liste.add(new Point(xCourant,yCourant-1));
						ipr.putPixel(xCourant,yCourant-1,color);
						couleursPixels[xCourant][yCourant-1] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant][yCourant-1])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}


				//marquage du point courant de la liste
				pixelsA[xCourant][yCourant] = -1;

				//suppression du point courant de la liste
				liste.remove(0);

				//affichage du résultat en cours
				imageDT.show();
				imageDT.updateAndDraw();

				//temporisateur pour l'affichage
				/*try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//fusionRegions(new Point(154,200), new Point(113,213), couleursRegions);
		fermeture();
		//ouverture();
		imageDT.show();
		imageDT.updateAndDraw();
		IJ.log("\n\n////////////////Apres segmentation : ///////////////\n");
		IJ.log(base.toString());

		//réécriture de la base de cas dans le fichier TXT
		try {
			l.ecritureFichierBaseEnLigne("BaseDeCasEnLigne.txt", base);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	//calcul de similarité entre le probleme courrant et ceux de la base, pour retourner l'indice du plus similaire
	//pour le moment on fait une distance de Manhattan sans pondération, donc pas très optimisée
	public int getMeilleurProbleme(Probleme p, BaseDeCas base){
		double best = Integer.MAX_VALUE;
		int indice = 0;

		//somme des carctéristiques du probleme d'entrée
		double valEntree = p.getSommeCaracs(); 
		for(int i = 0 ; i < base.getTailleBase(); i++){
			double distance = 0;
			Probleme tmp = base.getCas(i).getProbleme();
			//somme des caractéristiques du problème courant de la base
			double valTmp = tmp.getSommeCaracs();
			//calcul de distance entre les deux problèmes
			distance = Math.abs(valTmp - valEntree);
			IJ.log("DISTANCE avec cas"+(i+1)+" : "+distance);
			if(distance<best){
				best = distance;
				indice = i;
			}
		}
		IJ.log("MEILLEURE SIMILARITE : "+best+" --> INDICE CAS : "+(indice+1));
		return indice;
	}



	//réalise la fusion de deux régions, i.e. leur attribue la même couleur de segmentation
	public void fusionRegions(Point germe1, Point germe2, HashMap<Point, Integer> couleursRegions){
		int color1 = couleursRegions.get(germe1);
		int color2 = couleursRegions.get(germe2);
		//la couleur de fusion est la moyenne des couleurs de chaque région
		int colorFusion = (color1+color2)/2;

		for(int i = 0; i<w; i++){
			for(int j = 0; j<h; j++){
				if(couleursPixels[i][j]==color1 || couleursPixels[i][j]==color2){
					ipr.putPixel(i,j,colorFusion);
					couleursPixels[i][j] = colorFusion;
				}			
			}
		}
	}



	//fonction de dilatation morphologique de l'image
	public void dilatation(){
		//int[][] masque = {{0,1,0},{1,1,1},{0,1,0}};
		int[][] masque = {{0,0,1,0,0},{0,1,1,1,0},{1,1,1,1,1},{0,1,1,1,0},{0,0,1,0,0}};
		int mSize = masque.length/2;
		//besoin d'une copie sinon si on modifie directement le vrai tableau en va colorier toute l'image
		int[][] tmp = new int[w][h];
		for(int i = 0 ; i < w ; i++){
			tmp[i] = couleursPixels[i].clone();
		}

		//pour chaque pixel de l'image
		for(int i=mSize; i<w-mSize;i++){
			for(int j=mSize; j<h-mSize; j++){
				int color = 0;
				boolean valide = false;
				//convolution
				for(int k=-mSize; k<=mSize; k++){
					for(int l=-mSize; l<=mSize; l++){
						if(masque[k+mSize][l+mSize]==1 && couleursPixels[i+k][j+l]>0){
							valide = true;
							color = couleursPixels[i+k][j+l];
						}
					}
				}
				//dessin et modification tableau
				if(valide && couleursPixels[i][j]==0){
					ipr.putPixel(i,j,color);
					tmp[i][j] = color;
				}
			}
		}

		//copiage dans le vrai tableau
		for(int i = 0 ; i < w ; i++){
			couleursPixels[i] = tmp[i].clone();
		}
	}

	//fonction d'erosion morphologique de l'image
	public void erosion(){
		//int[][] masque = {{0,1,0},{1,1,1},{0,1,0}};
		int[][] masque = {{0,0,1,0,0},{0,1,1,1,0},{1,1,1,1,1},{0,1,1,1,0},{0,0,1,0,0}};
		int mSize = masque.length/2;
		int[][] tmp = new int[w][h];
		for(int i = 0 ; i < w ; i++){
			tmp[i] = couleursPixels[i].clone();
		}
		for(int i=mSize; i<w-mSize;i++){
			for(int j=mSize; j<h-mSize; j++){
				boolean valide = true;
				for(int k=-mSize; k<=mSize; k++){
					for(int l=-mSize; l<=mSize; l++){
						if(masque[k+mSize][l+mSize]==1 && couleursPixels[i+k][j+l]==0)valide = false;
					}
				}
				if(!valide && couleursPixels[i][j]>0){
					ipr.putPixel(i,j,0);
					tmp[i][j] = 0;
				}
			}
		}
		for(int i = 0 ; i < w ; i++){
			couleursPixels[i] = tmp[i].clone();
		}

	}

	//fermeture morphologique, qui correspond à une dilatation puis une erosion
	public void fermeture(){
		dilatation();
		erosion();
	}

	//ouverture morphologique, qui correspond à une erosion puis une dilatation
	public void ouverture(){
		erosion();
		dilatation();
	}

}