package RegionGrow.main; //A commenter pour utiliser sous ImageJ

import static RegionGrow.main.Constantes.VERT;

import java.awt.Point;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import RegionGrow.ontologieAnatomie.ColonneVertebrale;
import RegionGrow.ontologieRelationsSpatiales.ProcheDe;
import RegionGrow.ontologieRelationsSpatiales.RelationSpatiale;
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

	
	
	public  GestionRelationsSpatiales(int w, int h, ImageProcessor ip) {
		this.w=w;
		this.h=h;
		this.run(ip);
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
		dessin(tab);
		
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
				ipr.putPixel(i, j, tab); //dessin des relations spatiales floues
				if(fusion2[i][j]==0){
					int[] cpy = {pixelsCopy[i][j],pixelsCopy[i][j],pixelsCopy[i][j]}; 
					ipr.putPixel(i,j,cpy); //dessin de l'image de base la o� il n'y a pas de relations spatiales floues
				}
			}
		}
		Point germe = this.getGerme(fusion2); //calcul du germe
		ipr.putPixel((int)germe.getX(), (int)germe.getY(), VERT); //dessin du germe
		
		//mise � jour du composant ImageJ, et dessin de l'image
		//imageDT.show();
		//imageDT.updateAndDraw();
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
	
	/**
	 * Main de test, non utilis� pour l'ex�cution principale du programme
	 * @param args
	 */
	public static void main(String[] args) {
		//selection de l'image a segmenter
		//le repertoire par defaut du JFileChooser est constitue a l'aide de \\ et non pas des \
		JFileChooser dialogue = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path = null;
		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			path = dialogue.getSelectedFile().toPath();
		}
		ImagePlus im = new ImagePlus(path.toString());
		GestionRelationsSpatiales g = new GestionRelationsSpatiales(im.getWidth(), im.getHeight(),im.getProcessor());
		//il suffit de faire appel aux methodes de base d'un plugin ImageJ : setup() et run()
		g.setup("", im);
		ImageProcessor i = im.getProcessor();
		g.run(i);
		ArrayList<RelationSpatiale> mlsdkf = new ArrayList<>();
		ProcheDe pd = new ProcheDe();
		pd.setDegreMax(5);
		
		ColonneVertebrale c1 = new ColonneVertebrale();
		c1.setPosition(new Point(264,318));
		pd.setReference(c1);
		mlsdkf.add(pd);
		//Point p  = g.calculeGerme(mlsdkf);
	}
}
