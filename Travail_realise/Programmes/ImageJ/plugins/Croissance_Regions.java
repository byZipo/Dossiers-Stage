//Il ne faut pas mettre de nom de package pour ImageJ !
//package RegionGrow.main; //à enlever si l'on veux utiliser l'interface ImageJ
import static RegionGrow.main.Constantes.BLANC;
import static RegionGrow.main.Constantes.MARQUE;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import RegionGrow.baseDeCas.BaseDeCas;
import RegionGrow.baseDeCas.Germe;
import RegionGrow.baseDeCas.Probleme;
import RegionGrow.lecture.LectureFichier;
// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-clef package)
// pour classes ImagePlus et IJ
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.ByteProcessor;
// pour classe ImageProcessor
import ij.process.ColorProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

/**
 * Classe de segmentation d'image 2D par croissance de région, utilisant la bibliothèque ImageJ
 * Source ImageJ : (https://imagej.net)
 * @author Thibault DELAVELLE
 *
 */
public class Croissance_Regions implements PlugInFilter {


	//image de base en entrée
	protected ImageProcessor ip;
	//dimensions image
	protected int h,w;
	//nouvelle image dans laquelle on va dessiner la segmentation
	protected ImageProcessor ipr;
	//couleur de chaque pixel de l'image
	protected int[][] couleursPixels;

	
	//fonction appelée automatiquement pas ImageJ au lancement du plugin
	public int setup(String arg, ImagePlus imp) {
		
		//L'image de base est automatiquement convertie en image 8-bit (pour obtenir une image en niveaux de gris)
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToGray8();
		imp.updateAndDraw();
		return DOES_8G;
	}

	//fonction principale, qui lit et construit la base de cas, puis fait la segmentation (fonction appelée automtiquement)
	public void run(ImageProcessor i){
		
		this.ip = i;
		//indice du numero de cas, pour la phase de test car pour le moment on ne fait pas de choix (RaPC) sur la base
		int indiceBase = 0;

		//lecture du fichier de la base de cas, et construction de la base
		LectureFichier l = new LectureFichier();
		BaseDeCas base = null;
		try {
			base = l.LectureFichierBaseEnLigne("BaseDeCasEnLigne.txt");
			BaseDeCas test = l.parserXML("BaseDeCas.xml");
			//System.out.println(base.toString());
			IJ.log("////////////////Avant segmentation : ///////////////\n");
			IJ.log(base.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//recuperation des caracteristiques images de l'entree
		ImageStatistics stats = ip.getStatistics();
		IJ.log("\nSTATISTIQUES IMAGE EN ENTREE : moyenne : "+stats.mean+" asymetrie : "+stats.skewness+" ecart-type : "+stats.stdDev+" kurtosis : "+stats.kurtosis+"\n");

		//les caracs nonImage sont en dur forcement
		Probleme pEntree = new Probleme(5,175,22,0,4,3,stats.mean,stats.skewness,stats.stdDev,stats.kurtosis);


		// RaPC !!! (basique pour essayer pour le moment)
		indiceBase = getMeilleurProbleme(pEntree,base);
		//indiceBase = 4;
		
		//pour les test de suppression des muscles
		boolean musclesAEnlever = false;
		if(musclesAEnlever)ip = supprimerMuscles(base, 3, l);
		segmentation(base, indiceBase, l);
		
	}
	
	
	/**
	 * Réalise la segmentation par croissance de régions
	 * @param base : la base de cas du RàPC
	 * @param indiceBase : l'indice du cas à utiliser dans la base de cas (calculé grâce au RàPC)
	 * @param l : la classe de lecture du fichier de base de cas
	 */
	public void segmentation(BaseDeCas base, int indiceBase, LectureFichier l){
				
		//hauteur et largeur de l'image
		h=ip.getHeight();
		w=ip.getWidth();	
		//creation de la nouvelle image dans laquelle on va dessiner la segmentation
		ImageProcessor ipDT= new ColorProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("Croissance Regions", ipDT);
		ipr = imageDT.getProcessor();
		
		//tableau des intensites en niveau de gris des pixels de l'image
		int[][] pixelsA = ip.getIntArray();

		//germes 
		ArrayList<Germe> lgermes = base.getCas(indiceBase).getSolution().getGermesUtiles();

		//liste de correspondance germe (donc region) --> couleur, utile pour la fusion de deux regions
		HashMap<Point,Integer> couleursRegions = new HashMap<Point,Integer>();
		//stockage des couleurs des pixels de l'image, necessaire pour la fusion et pour la fermeture (dilatation et erosion)
		couleursPixels = new int[w][h];

		//pour la couleur de la region (couleur initiale n'a pas d'importance)
		int color = 255;

		//pour chaque germe
		for(int i = 0; i<lgermes.size(); i++){
			
			Germe g = lgermes.get(i);
			//recuperation des coordonees 2D du germe
			int xGerme = (int)g.getX();
			int yGerme = (int)g.getY();
			
			//seuils pour chaque germe
			int SeuilGlobal = g.getSeuilGlobal(); 
			int SeuilLocal = g.getSeuilLocal();
			
			//a chaque region on associe une couleur aleatoire
			Random r = new Random();
			color = r.nextInt(BLANC);
			couleursRegions.put(new Point(xGerme, yGerme),color);
			g.setCouleur(color);

			//l'intensite moyennne de la region courante (necessite le nombre de pixels de la region pour la m.a.j.)
			double moyenneRegion = pixelsA[xGerme][yGerme];
			int nbPixelsRegion = 0;

			//liste des points a evaluer, utilisee comme une pile (LIFO)
			ArrayList<Point> liste = new ArrayList<Point>();

			//ajout initial du germe pour la premiere iteration
			liste.add(new Point(xGerme,yGerme));
			ipr.putPixel(xGerme,yGerme,color);

			//tant qu'on a des pixels a evaluer (i.e. des pixels potentiellement de la meme region)
			while(!liste.isEmpty()){

				//recuperation du point courant
				Point courant = liste.get(0);
				int xCourant = (int)courant.getX();
				int yCourant = (int)courant.getY();
				//IJ.log("Val courant : "+pixelsA[xCourant][yCourant]+" | Val Germe : "+pixelsA[xGerme][yGerme]+" | Val Precedent : "+pixelsA[xPrecedent][yPrecedent]);
					//si le pixel est deja  marque (i.e. visite) on passe au pixel suivant
				if(pixelsA[xCourant][yCourant] == MARQUE){
					liste.remove(0);
					continue;
				}

				//pour chaque voisin en 4-connexite (non visite) du pixel courant, on va tester son homogeneite, et l'ajouter ou non a la region
					//droite
				if(xCourant<w-1 && pixelsA[xCourant+1][yCourant] != MARQUE){
					if(Math.abs(pixelsA[xCourant+1][yCourant]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant+1][yCourant])<=SeuilLocal)	{
						liste.add(new Point(xCourant+1,yCourant));
						ipr.putPixel(xCourant+1,yCourant,color);
						//mise a jour de la moyenne de la region
						couleursPixels[xCourant+1][yCourant] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant+1][yCourant])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}

				//bas
				if(yCourant<h-1 && pixelsA[xCourant][yCourant+1] != MARQUE){
					if(Math.abs(pixelsA[xCourant][yCourant+1]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant][yCourant+1])<=SeuilLocal)	{
						liste.add(new Point(xCourant,yCourant+1));
						ipr.putPixel(xCourant,yCourant+1,color);
						couleursPixels[xCourant][yCourant+1] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant][yCourant+1])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}

				//gauche
				if(xCourant>0  && pixelsA[xCourant-1][yCourant] != MARQUE){
					if(Math.abs(pixelsA[xCourant-1][yCourant]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant-1][yCourant])<=SeuilLocal)	{
						liste.add(new Point(xCourant-1,yCourant));
						ipr.putPixel(xCourant-1,yCourant,color);
						couleursPixels[xCourant-1][yCourant] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant-1][yCourant])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}

				//haut
				if(yCourant>0  && pixelsA[xCourant][yCourant-1] != MARQUE){
					if(Math.abs(pixelsA[xCourant][yCourant-1]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant][yCourant-1])<=SeuilLocal)	{
						liste.add(new Point(xCourant,yCourant-1));
						ipr.putPixel(xCourant,yCourant-1,color);
						couleursPixels[xCourant][yCourant-1] = color;
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant][yCourant-1])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}

				//marquage du point courant de la liste
				pixelsA[xCourant][yCourant] = MARQUE;

				//suppression du point courant de la liste
				liste.remove(0);

				//affichage du resultat en cours
				imageDT.show();
				imageDT.updateAndDraw();

			}
			//temporisateur pour l'affichage, entre chaque affichage de region
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//fusionRegions(new Point(154,200), new Point(113,213), couleursRegions);
		fermeture();
		
		imageDT.show();
		imageDT.updateAndDraw();
		IJ.log("\n\n////////////////Apres segmentation : ///////////////\n");
		IJ.log(base.toString());

		//reecriture de la base de cas dans le fichier TXT
		try {
			l.ecritureFichierBaseEnLigne("BaseDeCasEnLigne.txt", base);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Réalise une segmentation de l'image en placant des germes sur les muscles, puis les supprime
	 * TODO : A MODIFIER POUR SUPPRIMER UNE LISTE DE GERME DE LA CLASSE SOLUTION
	 * @param base
	 * @param indiceBase
	 * @param l
	 * @return l'image de base sans les muscles
	 */
	public ImageProcessor supprimerMuscles(BaseDeCas base, int indiceBase,LectureFichier l){
		//on fait une segmentation classique, il faut que les germes soient aux positions des muscles
		segmentation(base, indiceBase, l);
		
		//on crée une nouvelle image, qui sera une copie de celle en paramètre, mais sans les muscles
		ImageProcessor ipMuscle= new ByteProcessor(w,h);
		ImagePlus imageMuscles= new ImagePlus("Suppression muscles", ipMuscle);
		ImageProcessor iprM = imageMuscles.getProcessor();
		int[][] pixelsM = ip.getIntArray();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				//pour savoir si un pixel fait parti d'un muscle il suffit de regarder si dans couleursPixels on a une valeur >0
				if(!(couleursPixels[i][j]>0))iprM.putPixel(i, j, pixelsM[i][j]); 
			}
		}
		imageMuscles.show();
		imageMuscles.updateAndDraw();
		return ipMuscle;
	}


	
	/**
	 * Calcul de similarite entre le probleme courrant et ceux de la base, pour retourner l'indice du plus similaire
	 * Pour le moment on fait une distance de Manhattan sans ponderation, donc pas tres optimisée 
	 * @param p : le Problème à comparer
	 * @param base : la base de cas
	 * @return le meilleur problème = le plus similaire
	 */
	public int getMeilleurProbleme(Probleme p, BaseDeCas base){
		double best = Integer.MAX_VALUE;
		int indice = 0;

		//somme des carcteristiques du probleme d'entree
		double valEntree = p.getSommeCaracs(); 
		for(int i = 0 ; i < base.getTailleBase(); i++){
			double distance = 0;
			Probleme tmp = base.getCas(i).getProbleme();
			//somme des caracteristiques du probleme courant de la base
			double valTmp = tmp.getSommeCaracs();
			//calcul de distance entre les deux problemes
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



	/**
	 * Réalise la fusion de deux régions, i.e. leur attribue la même couleur de segmentation
	 * TODO : on pourrait la modifier pour comparer l'objetAnatomique et non plus la couleur
	 * @param germe1 : le germe de l'objet 1
	 * @param germe2 : le germe de l'objet 2
	 * @param couleursRegions : la couleur à donner à la nouvelle région (fusion des deux)
	 */
	public void fusionRegions(Point germe1, Point germe2, HashMap<Point, Integer> couleursRegions){
		int color1 = couleursRegions.get(germe1);
		int color2 = couleursRegions.get(germe2);
		//la couleur de fusion est la moyenne des couleurs de chaque region
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



	/**
	 * Réalise une dilatation morphologique de l'image
	 */
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

	/**
	 * Réalise une érosion morphologique de l'image
	 */
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

	/**
	 * Réalise une fermeture morphologique qui correspond à : 
	 * Dilatation puis érosion
	 */
	public void fermeture(){
		dilatation();
		erosion();
	}

	/**
	 * Réalise une ouverture morphologique qui correspond à :
	 * Erosion puis dilatation
	 */
	public void ouverture(){
		erosion();
		dilatation();
	}

}