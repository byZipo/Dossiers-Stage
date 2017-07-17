package RegionGrow.main; // ATTENTION : � enlever si l'on veux utiliser le plugin depuis l'interface ImageJ
import static RegionGrow.main.Constantes.BLANC;
import static RegionGrow.main.Constantes.MARQUE;
import static RegionGrow.main.Constantes.MUSCLES_A_ENLEVER;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFileChooser;

import RegionGrow.baseDeCas.BaseDeCas;
import RegionGrow.baseDeCas.Cas;
import RegionGrow.baseDeCas.Germe;
import RegionGrow.baseDeCas.Probleme;
import RegionGrow.baseDeCas.Traitement;
import RegionGrow.lecture.LectureFichier;
import RegionGrow.ontologieAnatomie.ObjetAnatomie;
import RegionGrow.ontologieAnatomie.Rein;
import RegionGrow.ontologieAnatomie.TumeurRenale;
import RegionGrow.ontologieRelationsSpatiales.RelationSpatiale;
// Importation des paquets ImageJ necessaires. 
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.filter.RankFilters;
import ij.plugin.filter.UnsharpMask;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

/**
 * Classe de segmentation d'image 2D par croissance de r�gion, utilisant la biblioth�que ImageJ
 * Source ImageJ : (https://imagej.net)
 * La classe etant un "plugin" ImageJ, elle h�rtie de deux m�thodes : 
 * setup() et run() appel�es au lancement du programme
 * @author Thibault DELAVELLE
 *
 */
public class Croissance_Regions implements PlugInFilter {


	//image de base en entr�e
	protected ImageProcessor ip;
	//dimensions image
	protected int h,w;
	//nouvelle image dans laquelle on va dessiner la segmentation
	protected ImageProcessor ipr;
	//couleur de chaque pixel de l'image
	protected int[][] couleursPixels;

	
	//fonction appel�e automatiquement pas ImageJ au lancement du plugin
	public int setup(String arg, ImagePlus imp) {
		
		//L'image de base est automatiquement convertie en image 8-bit (pour obtenir une image en niveaux de gris)
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToGray8();
		imp.updateAndDraw();
		return DOES_8G;
	}

	//fonction principale, qui lit et construit la base de cas, puis fait la segmentation (fonction appel�e automtiquement)
	public void run(ImageProcessor i){
		
		this.ip = i;
		
		//hauteur et largeur de l'image
		h=ip.getHeight();
		w=ip.getWidth();
		
		//lecture du fichier de la base de cas, et construction de la base
		LectureFichier l = new LectureFichier();
		BaseDeCas base = null;
		try {
			base = l.LectureFichierBaseEnLigne("BaseDeCasEnLigne.txt");
			BaseDeCas test = l.parserXML("BaseDeCas.xml");
			base = test;
			//System.out.println(base.toString());
			//IJ.log("////////////////Avant segmentation : ///////////////\n");
			//IJ.log(base.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//recuperation des caracteristiques images de l'entree
		ImageStatistics stats = ip.getStatistics();
		IJ.log("\nSTATISTIQUES IMAGE EN ENTREE : moyenne : "+stats.mean+" asymetrie : "+stats.skewness+" ecart-type : "+stats.stdDev+" kurtosis : "+stats.kurtosis+"\n");

		//les caracs nonImage sont en dur forcement
		Probleme pEntree = new Probleme(3,100,22,1,50,5,stats.mean,stats.skewness,stats.stdDev,stats.kurtosis);

		
		// RaPC !!! (basique pour essayer pour le moment
		//indice du numero de cas
		int indiceBase = getMeilleurProbleme(ip,pEntree,base);
		Cas casRememore = base.getCas(indiceBase);
		
		ArrayList<Germe> lgermes = casRememore.getSolution().getGermesUtiles();
		ArrayList<Germe> lgermesInutiles = casRememore.getSolution().getGermesInutiles();
		
		
		//pretaitements
		doPretraitements(casRememore);
		
		//adaptation position des germes en fonction de la couleur suppos�e sous le germe
		//lgermes = adaptationGermes(lgermes);
		
		//suppression des muscles
		if(MUSCLES_A_ENLEVER)ip = supprimerObjets(lgermesInutiles);
		
		//calcul de la position du germe de la tumeur
		
		//je peux effectivemnt
		/*Germe t1 = recupererGermeTumeur(casRememore);
		Germe t2 = new Germe(t1.getX()-50,t1.getY()-30,t1.getSeuilGlobal(),t1.getSeuilLocal());
		t2.setLabelObjet(new TumeurRenale());*/
		//t2.setColor();
		lgermes.add(recupererGermeTumeur(casRememore));
		//lgermes.add(t2);
		
		
		//TODO en r�alit� ici il faut appeler la m�thode avec pEntree et casRememore
		//int[][] boiteEnglobante = getBoiteEnglobante(casRememore.getProbleme(), casRememore);
		
		//segmentation
		segmentation(lgermes, true);
		
		
		/* 
		 * TODO ICI il faudra ajouter un truc du genre : 
		 * if(isSatifaisante(segmentation(legermes))) base = ajouterDansBaseDeCas(pEntree, base.getCas(indiceBase).getSolution());
		 * ecritureXML(base);
		 */
		
	}
	
	
	/**
	 * R�alise la segmentation par croissance de r�gions
	 * @param lgermes : la liste de germes
	 * @param affichage : pour activer/d�sactiver l'affichage du r�sultat
	 */
	public void segmentation(ArrayList<Germe> lgermes, boolean affichage){
		
		
		//affichage de l'image pretrait�e
		ImagePlus pretraitee = new ImagePlus("Pretaitee",ip);
		if(affichage){
			pretraitee.show();
			pretraitee.updateAndDraw();
		}
		//creation de la nouvelle image dans laquelle on va dessiner la segmentation
		ImageProcessor ipDT= new ColorProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("Croissance Regions", ipDT);
		ipr = imageDT.getProcessor();
		
		//tableau des intensites en niveau de gris des pixels de l'image
		int[][] pixelsA = ip.getIntArray();

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
			
			//a�chaque region on associe une couleur aleatoire
			Random r = new Random();
			color = r.nextInt(BLANC);
			color = g.getCouleur();
			
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
					//si le pixel est deja� marque (i.e. visite) on passe au pixel suivant
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
				if(affichage){
					imageDT.show();
					imageDT.updateAndDraw();
				}

			}
			//temporisateur pour l'affichage, entre chaque affichage de region
			/*try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}

		//fusionRegions(new Point(154,200), new Point(113,213), couleursRegions);
		fermeture();
		
		if(affichage){
			imageDT.show();
			imageDT.updateAndDraw();
		}
		//reecriture de la base de cas dans le fichier TXT
		/*try {
			l.ecritureFichierBaseEnLigne("BaseDeCasEnLigne.txt", base);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * R�alise une segmentation de l'image en placant des germes sur les muscles, puis les supprime
	 * @param base
	 * @param indiceBase
	 * @param l
	 * @return l'image de base sans les muscles
	 */
	public ImageProcessor supprimerObjets(ArrayList<Germe> lgermes){
		//on fait une segmentation classique, il faut que les germes soient aux positions des muscles
		segmentation(lgermes, true);
		
		//on cr�e une nouvelle image, qui sera une copie de celle de base, mais sans les muscles
		ImageProcessor ipMuscle= new ByteProcessor(w,h);
		ImagePlus imageMuscles= new ImagePlus("Suppression muscles", ipMuscle);
		ImageProcessor iprM = imageMuscles.getProcessor();
		int[][] pixelsM = ip.getIntArray();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				//pour savoir si un pixel fait partie d'un muscle il suffit de regarder si dans couleursPixels on a une valeur >0
				if(!(couleursPixels[i][j]>0))iprM.putPixel(i, j, pixelsM[i][j]); 
			}
		}
		imageMuscles.show();
		imageMuscles.updateAndDraw();
		return ipMuscle;
	}


	
	/**
	 * Calcul de similarite entre le probleme courrant et ceux de la base, pour retourner l'indice du plus similaire
	 * La formule utilis�e est un "mix" entre les formules de Perner et l'indice MSSIM
	 * @param ip : L'ImageProcessor, donc l'image du nouveau Probl�me
	 * @param p : le nouveau Probl�me � comparer
	 * @param base : la base de cas
	 * @return l'indice du meilleur probl�me = le plus similaire
	 */
	public int getMeilleurProbleme(ImageProcessor ip,Probleme p, BaseDeCas base){
		double best = Integer.MIN_VALUE;
		int indice = 0;
		
		System.out.println("--------------------------------------------------");
		System.out.println("CALCUL DE SIMIALRITE");
		
		//calcul de similarit� pour chaque cas de la base de cas
		for(int i = 0 ; i < base.getTailleBase(); i++){
			System.out.println("Cas "+(i+1)+" :");
			double similarite = 0;
			
			//Probl�me courant de la base
			Probleme tmp = base.getCas(i).getProbleme();
			
			//Formules de Perner
			double simNonImage = p.getSimNonImage(tmp, 1, 1, 0.5);
			double simImage = p.getSimImage(tmp, 1, base.getValeursMinImage(), base.getValeursMaxImage());
			System.out.println("(Perner) SimImage:"+simImage+" SimNonImage:"+simNonImage+" SimGlobale:"+p.getSimGlobale(simNonImage, simImage,0.5,0.5));
			
			//MSSIM
			double MMSIM = p.getMSSIM(ip, tmp);
			System.out.println("(MSSIM) Similarit� Image: "+MMSIM);
			
			//Similarit� globale
			similarite = (MMSIM/2) + (simNonImage/2);
			System.out.println("SIMILARITE GLOBALE avec cas"+(i+1)+" : "+similarite+"\n");
			
			//Mise � jour du "meilleur" cas si sa similairt� est plus grande
			if(similarite>best){
				best = similarite;
				indice = i;
			}
		}
		System.out.println("MEILLEURE SIMILARITE : "+best+" --> INDICE CAS : "+(indice+1));
		System.out.println("--------------------------------------------------");
		return indice;
	}
	
	
	public int getMeilleurProbleme2(Probleme p, BaseDeCas base){
		
		//On commence par r�cup�rer la liste des cas ayant les m�mes relations spatiales (l'ordre n'a pas d'importance)
		ArrayList<Cas> potentiels = getCasMemesRelations(p,base);
		System.out.println("Nombre de cas potentiels (m�mes relations spatiales) : "+potentiels.size());
		for(Cas c : potentiels){
			System.out.println(c.getProbleme().getPositonFloueTumeur().toString());
		}
		//TODO calcul du meilleur cas parmis potentiels, et r�cup�ration des param�tres des fcts floues
		//TODO mesure de similarit� sur les crit�res Image et NonImage (+poids) pour r�cup�rer les seuils globaux et locaux pour la tumeur
		
		//TODO r�cup�rer aussi les autres germes (des autres organes) + leurs seuils
		
		//du coup la m�thode ne retournera plus un entier, mais plusieurs choses ^^
		return 0;
	}
	
	/**
	 * Retourne une liste de cas ayant les m�mes relations spatiales que le probl�me  pass� en param�tre
	 * Deux relations sont identiques si elles ont le m�me nome et la m�me r�f�rence
	 * L'ordre des relations n'a pas d'importance
	 * @param p : le probl�me
	 * @param base : la base de cas
	 * @return la liste des cas ayant les m�mes relations spatiales
	 */
	public ArrayList<Cas> getCasMemesRelations(Probleme p, BaseDeCas base){
		
		ArrayList<RelationSpatiale> pRel = p.getPositonFloueTumeur();
		ArrayList<Cas> res = new ArrayList<Cas>();
		
		//pour chaque cas de la base
		for (int i = 0; i < base.getTailleBase(); i++) {
			Cas c = base.getCas(i);
			ArrayList<RelationSpatiale> cRel = c.getProbleme().getPositonFloueTumeur();
			
			//on compare les deux listes de relations spatiales
			//il faut une �galit� sans importance d'ordre (ex:{A,B,C,D} et {A,C,D,B})
			//(il ne peut pas y avoir de doublons dans les listes)
			boolean listesEgales = true;
			if(cRel.size()!=pRel.size())listesEgales=false;
			else{
				for (RelationSpatiale r : cRel) {
					if(!pRel.contains(r)){
						listesEgales=false;
						break;
					}
				}
			}
			//si les listes de relation sont �gales, on peut ajouter la cas au r�sultat
			if(listesEgales){
				res.add(c);
			}
		}
		return res;
	}



	/**
	 * R�alise la fusion de deux r�gions, i.e. leur attribue la m�me couleur de segmentation
	 * TODO : on pourrait la modifier pour comparer l'objetAnatomique et non plus la couleur
	 * @param germe1 : le germe de l'objet 1
	 * @param germe2 : le germe de l'objet 2
	 * @param couleursRegions : la couleur � donner � la nouvelle r�gion (fusion des deux)
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
	 * R�alise une dilatation morphologique de l'image
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
	 * R�alise une �rosion morphologique de l'image
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
	 * R�alise une fermeture morphologique qui correspond � : 
	 * Dilatation puis �rosion
	 */
	public void fermeture(){
		dilatation();
		erosion();
	}

	/**
	 * R�alise une ouverture morphologique qui correspond � :
	 * Erosion puis dilatation
	 */
	public void ouverture(){
		erosion();
		dilatation();
	}
	
	/**
	 * Applique tous les pr�traitements sur l'image de base par rapport aux traitements stock�s dans un cas
	 * @param cas : le cas contenant les traitements � appliquer
	 */
	public void doPretraitements(Cas cas){
		
		System.out.println("\n-----------------------------------");
		System.out.println("PRETRAITEMENTS avant segmentation : ");
		
		//pour chaque pretraitement
		for (int i = 0; i < cas.getSolution().getNbPreTraitements(); i++) {
			//r�cup�ration du traitement
			Traitement t = cas.getSolution().getPretraitement(i);
			//action d�pendante du type de traitement
			switch(t.getTypeTraitement()){
			case Unsharped:
				UnsharpMask mask = new UnsharpMask();
				FloatProcessor fp = null;
				//une image RGB dispose de trois canaux (R, G et B), il faut appliquer la mask sur chacun
				for(int canal = 0; canal < ip.getNChannels(); canal++){
					fp = ip.toFloat(canal, fp);
					fp.snapshot();
					//application du mask avec les param�tres stock�s dans le cas
					mask.sharpenFloat(fp, t.getRadius(), (float)t.getSeuil());
					ip.setPixels(canal,fp);
				}
				System.out.println("Unsharped Mask appliqu�");
				break;
			case Median:
				RankFilters rk = new RankFilters();
				//filtre m�dian avec le raidus du cas
				rk.rank(ip, t.getRadius(), RankFilters.MEDIAN);
				System.out.println("Filtre m�dian appliqu�");
				break;
			case Moyenneur:
				RankFilters rk2 = new RankFilters();
				//filtre moyenneur avec le raidus du cas
				rk2.rank(ip, t.getRadius(), RankFilters.MEAN);
				System.out.println("Filtre moyenneur appliqu�");
				break;
			default:
				System.err.println("ERREUR : TRAITEMENT "+t.getTypeTraitement()+" NON PRIS EN CHARGE");
				break;
			}
		}
	}
	
	/**
	 * Fait appel aux m�thodes de calcul de la position du germe de la tumeur :
	 * La classe GestionRelationsSpatiales
	 * @param casRememore : la cas rem�mor� de la base de cas
	 * @return le germe de la tumeur
	 */
	public Germe recupererGermeTumeur(Cas casRememore){
		GestionRelationsSpatiales gr = new GestionRelationsSpatiales(w,h,ip);
		
		ArrayList<RelationSpatiale> lr = casRememore.getSolution().getPositionFloueTumeur();
		for(int indice = 0 ; indice < lr.size() ; indice++){
			Point ref = getCentreGravite(lr.get(indice).getReference(),casRememore.getSolution().getGermesUtiles(), casRememore.getSolution().getGermesInutiles());
			lr.get(indice).getReference().setPosition(ref);
		}
		Point p1 = gr.calculeGerme(lr);
		System.out.println("POSITION FLOUE DE LA TUMEUR : ("+p1.getX()+","+p1.getY()+")");
		Germe tumeur = new Germe((int)p1.getX(), (int)p1.getY(), casRememore.getSolution().getSeuilGlobal(), casRememore.getSolution().getSeuilLocal());
		tumeur.setLabelObjet(new TumeurRenale());
		tumeur.setColor();
		return tumeur;
	}

	
	/**
	 * Calcule le centre de gravit� de l'objet anatomique de r�f�rence pass� en param�tre
	 * le calcul est bas� sur les moments g�om�triques, et requiert la position du centre de gravit� d'un objet identique
	 * dans la partie solution de la base de cas
	 * Une erreur est d�clench�e si aucun objet identique n'est trouv�
	 * On entend par identique deux objets de la m�me classe
	 * @param reference : l'objet anatomique de r�f�rence
	 * @param germesUtiles : la liste des germes utiles de la solution
	 * @param germesInutiles : la liste des germes inutiles de la solution
	 * @return le centre de gravit� de l'objet de r�f�rence (un Point)
	 */
	public Point getCentreGravite(ObjetAnatomie reference, ArrayList<Germe> germesUtiles, ArrayList<Germe> germesInutiles) {
		//Pour chaque germe utile, on va rechercher l'objet identique � l'objet de r�f�rence, et le segmenter
		//d�s qu'on a trouv� l'objet identique, on arr�te la boucle
		
		boolean trouve = false;
		for (int i = 0; i < germesUtiles.size(); i++) {
			Germe g = germesUtiles.get(i);
			if(g.getLabelObjet().getClass().getName().equals(reference.getClass().getName())){
				Germe ref = new Germe(g.getX(), g.getY(), 35,20);
				ArrayList<Germe> lgermes = new ArrayList<Germe>();
				lgermes.add(ref);
				segmentation(lgermes, false);
				trouve = true;
				break;
			}
		}
		
		//si on n'a pas trouv� d'objet identique, on cherche dans la liste des germes inutiles
		if(!trouve){
			for (int i = 0; i < germesInutiles.size(); i++) {
				Germe g = germesInutiles.get(i);
				if(g.getLabelObjet().getClass().getName().equals(reference.getClass().getName())){
					Germe ref = new Germe(g.getX(), g.getY(), 35,20);
					ArrayList<Germe> lgermes = new ArrayList<Germe>();
					lgermes.add(ref);
					segmentation(lgermes, false);
					trouve = true;
					break;
				}
			}
		}
		
		//si on n'a toujours pas trouv�, cela signifie que l'objet de r�f�rence n'est pas resens� dans la base de cas XML
		// donc il est impossible de le segmenter pour trouver son centre de gravit�, puisque l'on a aucun point de d�part
		if(!trouve){
			System.err.println("ERREUR CENTRE DE GRAVITE : aucun objet de r�f�rence ne correspond dans la liste des germes utiles/inutiles ");
			return null; 
		}
		
		//calcul du centre de gravit� de la forme segment�e gr�ce au tableau colorPixels
		//on utilise les moments g�om�triques pour le calcul :
		//mPQ = sommeXsommeY x^P * y^Q * f(x,y) avec f(x,y) = 1 si (x,y) appartient � la forme, 0 sinon
		//centre de gravit� = (m10/m00, m01/m00)
		int m10 = 0;
		int m01 = 0;
		int m00 = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(couleursPixels[i][j]>0){
					m10 += i;
					m01 += j;
					m00 += 1;
				}
			}
		}
		Point centreGravite = new Point((m10/m00),(m01/m00));
		System.out.println("Centre de gravit� de l'objet de r�f�rence : "+reference.toString()+" : ("+centreGravite.getX()+","+centreGravite.getY()+")");
		return centreGravite;
	}
	
	
	/*TODO je ne vois pas comment coder cette m�thode, sachant que j'ai � ma disposition : 
	 * 	- nbCoupes, hauteurCoupe
	 * 	- diametreTumeur, hauteurTumeur
	 * 	- la position du germe de la tumeur calcul� d'apr�s la solution du casRememore
	 */
	public int[][] getBoiteEnglobante(Probleme pb, Cas casRememore){
		/*double diametre = pb.getDiametreTumeur();
		double hauteur = pb.getHauteurTumeur();
		Germe g = this.recupererGermeTumeur(casRememore);
		Point centreTumeur = new Point(g.getX(), g.getY());
		int[][] boiteEnglobante = new int[w][h];
		*/
		return null;
	}
	
	
	/**
	 * Adapte la position des germes en fonction de leur caract�ristique pr�dominante  
	 * par exemple le rein est toujours clair donc on s'attend � un pixel clair, sinon on adapte
	 * @param lgermes : la liste des germes
	 * @return : la liste des germes adapt�e
	 */
	public ArrayList<Germe> adaptationGermes(ArrayList<Germe> lgermes){
		
		
		int[][] image = ip.getIntArray();
		
		for(int i = 0 ; i < lgermes.size() ; i++){
			Germe g = lgermes.get(i);
			
			
			//Si le germe a pour label un Rein (Droit ou Gauche)
			if(g.getLabelObjet() instanceof Rein){
				//le rein est g�n�ralement clair, donc � forte intensit�, on a donc faux si on a une faible valeur
				int alpha = 1;
				//tant qu'on n'a pas trouv� de pixel � l'intensit� suffistante
				while(image[g.getX()][g.getY()]<Constantes.SEUIL_INF_COULEUR_REIN){
					
					//on rgearde le voisinnage (8-connexe) du germe et on cherche un voisin potentiel
					for(int x = -alpha ; x <= alpha ; x++){
						for(int y = -alpha ; y <= alpha ; y++){
							
							//si un voisin correspond, on remplace l'ancien germe par celui-ci
							if(image[g.getX()+alpha][g.getY()+alpha] >= Constantes.SEUIL_INF_COULEUR_REIN) {
								System.out.println("Adaptation de la position d'un germe de type : "+g.getLabelObjet().getNomSimple()+" --> ancienne pos : ("+g.getX()+","+g.getY()+") nouvelle pos : ("+(g.getX()+alpha)+","+(g.getY()+alpha)+")");
								lgermes.get(i).setPos(g.getX()+alpha, g.getY()+alpha);
								
							}
						}
					}
					
					//augmentation du rayon de recherche
					alpha++;
					g = lgermes.get(i);
				}
			}
			
			
			//Si le germe a pour label un blahblahblah TODO
		}
		
		return lgermes;
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		Croissance_Regions c = new Croissance_Regions();
		LectureFichier l = new LectureFichier();
		BaseDeCas base = l.parserXML("BaseDeCas.xml");
		Probleme p = new Probleme();
		
		/*ColonneVertebrale c1 = new ColonneVertebrale();
		c1.setPosition(new Point(391,374));
		
		AGaucheDe rs1 = new AGaucheDe();
		rs1.setReference(c1);
		rs1.setSeuilInf(90);
		rs1.setSeuilSup(270);
		rs1.setDegreMax(180);
		
		EnHautDe rs2 = new EnHautDe();
		rs2.setReference(c1);
		rs2.setSeuilInf(0);
		rs2.setSeuilSup(180);
		rs2.setDegreMax(90);
		
		ProcheDe rs3 = new ProcheDe();
		rs3.setReference(c1);
		
		p.ajouterRelationFloue(rs1);
		p.ajouterRelationFloue(rs2);
		p.ajouterRelationFloue(rs3);
		
		c.getMeilleurProbleme2(p, base);*/
		JFileChooser dialogue = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path = null;
		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			 path = dialogue.getSelectedFile().toPath();
		}
		ImagePlus im = new ImagePlus(path.toString());
		//GestionRelationsSpatiales g = new GestionRelationsSpatiales(im.getWidth(), im.getHeight());
		//il suffit de faire appel aux methodes de base d'un plugin ImageJ : setup() et run()
		c.setup("", im);
		//g.setup("", im);
		ImageProcessor i = im.getProcessor();
		c.ip=i;
		c.h=c.ip.getHeight();
		c.w=c.ip.getWidth();
		
		//CROISSANCE DE REGIONS MANUELLE POUR L'IMAGE LISA_2
		/*
		ArrayList<Germe> lgermes = new ArrayList<Germe>();
		ColonneVertebrale c1 = new ColonneVertebrale();
		c1.setPosition(new Point(318,274));
		TumeurRenale t = new TumeurRenale();
		t.setPosition(new Point(0,0));
		ReinGauche reinG = new ReinGauche();
		reinG.setPosition(new Point(0,0));
		ReinDroit reinD = new ReinDroit();
		reinD.setPosition(new Point(0,0));
		VeineRenale veine = new VeineRenale();
		veine.setPosition(new Point(0, 0));
		ArtereRenale artere = new ArtereRenale();
		artere.setPosition(new Point(0, 0));
		
		
		Germe g1 = new Germe();
		g1.setPos(318, 270);
		g1.setLabelObjet(c1);
		g1.setColor();
		g1.setSeuilLocal(30);
		g1.setSeuilGlobal(45);
		
		Germe g2 = new Germe();
		g2.setPos(389,267);
		g2.setLabelObjet(c1);
		g2.setColor();
		g2.setSeuilLocal(30);
		g2.setSeuilGlobal(45);
		
		Germe g3 = new Germe();
		g3.setPos(265, 209);
		g3.setLabelObjet(t);
		g3.setColor();
		g3.setSeuilLocal(20);
		g3.setSeuilGlobal(20);
		
		Germe gt2 = new Germe();
		gt2.setPos(254, 146);
		gt2.setLabelObjet(t);
		gt2.setColor();
		gt2.setSeuilLocal(20);
		gt2.setSeuilGlobal(20);
		
		Germe gt3 = new Germe();
		gt3.setPos(213, 158);
		gt3.setLabelObjet(t);
		gt3.setColor();
		gt3.setSeuilLocal(20);
		gt3.setSeuilGlobal(20);
		
		
		Germe gt4 = new Germe();
		gt4.setPos(339, 221);
		gt4.setLabelObjet(t);
		gt4.setColor();
		gt4.setSeuilLocal(20);
		gt4.setSeuilGlobal(20);
		
		Germe gt5 = new Germe();
		gt5.setPos(313, 156);
		gt5.setLabelObjet(t);
		gt5.setColor();
		gt5.setSeuilLocal(50);
		gt5.setSeuilGlobal(50);

		Germe gt6 = new Germe();
		gt6.setPos(276, 140);
		gt6.setLabelObjet(t);
		gt6.setColor();
		gt6.setSeuilLocal(20);
		gt6.setSeuilGlobal(20);
		
		
		Germe g4 = new Germe();
		g4.setPos(232, 127);
		g4.setLabelObjet(reinG);
		g4.setColor();
		g4.setSeuilLocal(10);
		g4.setSeuilGlobal(15);
		
		Germe g5 = new Germe();
		g5.setPos(330, 363);
		g5.setLabelObjet(reinD);
		g5.setColor();
		g5.setSeuilLocal(15);
		g5.setSeuilGlobal(30);
		
		Germe veine1 = new Germe();
		veine1.setPos(261, 292);
		veine1.setLabelObjet(veine);
		veine1.setColor();
		veine1.setSeuilGlobal(30);
		veine1.setSeuilLocal(20);
		
		Germe veine2 = new Germe();
		veine2.setPos(211, 255);
		veine2.setLabelObjet(veine);
		veine2.setColor();
		veine2.setSeuilGlobal(30);
		veine2.setSeuilLocal(20);
		
		Germe artere1 = new Germe();
		artere1.setPos(292,286);
		artere1.setLabelObjet(artere);
		artere1.setColor();
		artere1.setSeuilGlobal(30);
		artere1.setSeuilLocal(20);
		
		Germe artere2 = new Germe();
		artere2.setPos(302,244);
		artere2.setLabelObjet(artere);
		artere2.setColor();
		artere2.setSeuilGlobal(30);
		artere2.setSeuilLocal(20);
		
		Germe veine3 = new Germe();
		veine3.setPos(290, 306);
		veine3.setLabelObjet(veine);
		veine3.setColor();
		veine3.setSeuilGlobal(30);
		veine3.setSeuilLocal(20);
		
		lgermes.add(g1);
		lgermes.add(g2);
		lgermes.add(g3);
		lgermes.add(g4);
		lgermes.add(g5);
		lgermes.add(gt2);
		lgermes.add(gt3);
		lgermes.add(gt4);
		lgermes.add(gt5);
		lgermes.add(gt6);
		lgermes.add(veine1);
		lgermes.add(veine2);
		lgermes.add(veine3);
		lgermes.add(artere1);
		lgermes.add(artere2);
		
		c.segmentation(lgermes, true);
		*/
		

		
		
		
		
		
		
	}
	

}