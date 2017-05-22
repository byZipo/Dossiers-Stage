package RegionGrow.main; //A commenter pour utiliser sous ImageJ

import static RegionGrow.main.Constantes.BLEU;
import static RegionGrow.main.Constantes.VERT;

import java.awt.Point;
import java.util.ArrayList;

import RegionGrow.ontologieAnatomie.ColonneVertebrale;
import RegionGrow.ontologieAnatomie.ReinDroit;
import RegionGrow.ontologieRelationsSpatiales.ADroiteDe;
import RegionGrow.ontologieRelationsSpatiales.AGaucheDe;
import RegionGrow.ontologieRelationsSpatiales.EnBasDe;
import RegionGrow.ontologieRelationsSpatiales.EnHautDe;
import RegionGrow.ontologieRelationsSpatiales.MoyennementProcheDe;
import RegionGrow.ontologieRelationsSpatiales.RelationSpatiale;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;


/**
 * Classe de gestion de la logique floue des relations spatiales entre organes
 * @author Thibault DELAVELLE
 *
 */
public class GestionRelationsSpatiales implements PlugInFilter{
	
	
	//pixels de l'image
	int[][] pixelsA;
	int[][] pixelsCopy;
	
	//hauteur et largeur de l'image
	public int h,w;
	
	//composants ImageJ (pour le dessin de l'image)
	ImageProcessor ipr;
	ImagePlus imageDT;

	
	
	public  GestionRelationsSpatiales(int w, int h) {
		this.w=w;
		this.h=h;
	}
	
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToGray8();
		imp.updateAndDraw();
		return DOES_8G ;
	}

	@Override
	public void run(ImageProcessor ip) {
		h=ip.getHeight();
		w=ip.getWidth();	
		
		ImageProcessor ipDT= new ColorProcessor(w,h);
		imageDT= new ImagePlus("Fusion de relations spatiales", ipDT);
		ipr = imageDT.getProcessor();
		pixelsA = new int[w][h];
		pixelsCopy = ip.getIntArray();
		
		
		
		/*Point ref2 = new Point((w/2),(h/2)+1);
		aDroiteDe(ref2);
		
		Point ref3 = new Point((w/2)+1,(h/2));
		aDroiteDe(ref3);*/
		
		
		//calcul
		//pour chaque point de la forme de r�f�rence (A OPTIMISER en "pour chaque point du contour de la forme")
		// EN FAIT PAS BESOIN, LE CENTRE DE LA FORME SUFFIT, POUR LES TESTS ON PREND DONC UN SEUL POINT
		
		
		//calcul du centre de gravit� de la colonne vert�brale (point de r�f�rence)
		Point ref = new Point(391, 374);
		Point ref2 = new Point(154,72);
		pixelsA[(int)ref.getX()][(int)ref.getY()]=-50;
		pixelsA[(int)ref2.getX()][(int)ref2.getY()]=-50;
		
		/*int[][] haut = enHautDe(ref);
		int[][] droite = aDroiteDe(ref);
		int[][] proche = moyennementProcheDe(ref);
		int[][] gauche = aGaucheDe(ref);
		
		int[][] fusion = fusion(droite,proche);
		int[][] fusion2 = fusion(fusion, haut);
		
		fusion2 = normaliser(fusion2);
		
		//dessin de l'image + des relations spatiales floues calcul�es
		dessin(fusion2);
		*/
		
		/****************************************************/
		/*********** MAIN DE TEST **************************/
		
		
		
		ArrayList<RelationSpatiale> rel = new ArrayList<RelationSpatiale>();
		
		//objets
		ColonneVertebrale c1 = new ColonneVertebrale();
		c1.setPosition(new Point(391,374));
		
		ReinDroit rein = new ReinDroit();
		rein.setPosition(new Point(154,72));

		
		//relations
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
		
		MoyennementProcheDe rs3 = new MoyennementProcheDe();
		rs3.setReference(c1);
		
		ADroiteDe rs4 = new ADroiteDe();
		rs4.setReference(rein);
		rs4.setSeuilInf(90);
		rs4.setSeuilSup(270);
		rs4.setDegreMax(0);
		
		MoyennementProcheDe rs5 = new MoyennementProcheDe();
		rs5.setReference(rein);
		
		AGaucheDe rs6 = new AGaucheDe();
		rs6.setReference(rein);
		rs6.setSeuilInf(90);
		rs6.setSeuilSup(270);
		rs6.setDegreMax(180);
		
		EnBasDe rs7 = new EnBasDe();
		rs7.setReference(rein);
		rs7.setSeuilInf(180);
		rs7.setSeuilSup(360);
		rs7.setDegreMax(270);
		
		//ajouts dans la liste
		rel.add(rs1);
		rel.add(rs2);
		rel.add(rs3);
		//rel.add(rs4);
		//rel.add(rs5);
		rel.add(rs7);
		
		
		//calcul + affichage
		Point germe = calculeGerme(rel);
		IJ.log("POISITION GERME TUMEUR : "+germe.getX()+" "+germe.getY());
		
		/******************************************************/
		

	}
	
	
	/**
	 * Calcul la position 2D du germe, en fonction d'une liste de relations spatiales (et de leurs param�tres)
	 * @param relations : la liste des relations spatiales (qui contient aussi les param�tres des fonctions)
	 * @return le Point 2D repr�sentant le germe
	 */
	public Point calculeGerme(ArrayList<RelationSpatiale> relations){
		
		//tableau remplit de 1 pour la premi�re it�ration, qui n'a donc pas d'effet en t-norm multiplication
		System.out.println(w+" "+h);
		int[][] tab = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				tab[i][j]=1;
			}
		}
		
		//pour chaque relation, on calcule sa carte de distance et on la fusionne avec la pr�c�dente
		for(int i = 0 ; i < relations.size() ; i++){
			RelationSpatiale r = relations.get(i);
			int[][] tmp = r.getCarteDistance(w,h);
			tab = fusion(tab, tmp);
		}
		
		//normalisation pour obtenir un r�sultat entre 0 et 255
		tab = normaliser(tab);
		
		//dessin du r�sultat obtenu
		//dessin(tab);
		
		//on retourne le meilleur pixel de la carte qui devient le germe
		
		return getGerme(tab);
	}
	
	
	
	/**
	 * Dessin de l'image de base avec le r�sultat de la fusion des fonctions floues par dessus
	 * @param fusion2 : la carte de distance floue
	 */
	public void dessin(int[][] fusion2){
		//dessin
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int[] tab = {fusion2[i][j],fusion2[i][j],fusion2[i][j]}; //l'image est rgb donc requiert 3 canaux 
				if(pixelsA[i][j]!=-50)ipr.putPixel(i, j, tab); //dessin des relations spatiales floues
				else ipr.putPixel(i, j, BLEU); //dessin du point de r�f�rence
				if(fusion2[i][j]==0){
					int[] cpy = {pixelsCopy[i][j],pixelsCopy[i][j],pixelsCopy[i][j]}; 
					ipr.putPixel(i,j,cpy); //dessin de l'image de base la o� il n'y a pas de relations spatiales floues
				}
				//if(fusion2[i][j]==255)ipr.putPixel(i,j,BLANC);
				//else ipr.putPixel(i,j,0);
			}
		}
		Point germe = this.getGerme(fusion2); //calcul du germe
		ipr.putPixel((int)germe.getX(), (int)germe.getY(), VERT); //dessin du germe
		
		//mise � jour du composant ImageJ, et dessin de l'image
		imageDT.show();
		imageDT.updateAndDraw();
	}
	
	
	/**
	 * D�termine le point le plus ad�quat pour �tre un germe, sur la carte de distance floue
	 * Ce point correspond au pixel de la carte ayant pour valeur 255 (la valeur max) 
	 * @param carte : la varte de distance floue
	 * @return le germe sous forme d'un Point 2D
	 */
	public Point getGerme(int[][] carte){
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(carte[i][j]==255)return new Point(i,j);
			}
		}
		System.err.println("Aucun germe ad�quat trouv�");
		return null;
	}
	
	
	
	/**
	 * Fusion par t-norme (minimum ou multiplication) entre deux images (cartes de distance)
	 * @param carte1 : la premi�re carte
	 * @param carte2 : la deuxi�me carte
	 * @return la carte ainsi fusionn�e
	 */
	public int[][] fusion(int[][] carte1, int[][] carte2) {

		int[][] res = new int[w][h];
		
		for(int i = 0; i<w; i++){
			for(int j=0; j<h; j++){
				res[i][j] = carte1[i][j]*carte2[i][j];  // t-norme multiplication --> MIEUX
				//res[i][j] = Math.min(carte1[i][j], carte2[i][j]); //t-norme minimum
			}
		}
		int[][] tmp = res.clone();
		int[][] fin = normaliser(tmp);
		return fin;
	}
	
	
	/**
	 * Repasse � une echelle entre 0 et 255 (normalise l'image)
	 * @param tab : l'image � normaliser
	 * @return l'image normalis�e
	 */
	public int[][] normaliser(int[][] tab){
		int[][] res = new int[w][h];
		int maxT = 0;
		
		
		//r�cup�ration de l'intensit� max de la carte
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(tab[i][j]>maxT)maxT = tab[i][j];
			}
		}
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int pourcentageGris = tab[i][j]*100/maxT;
				res[i][j] = 255*pourcentageGris/100;
			}
		}
		return res;
		
	}
}
