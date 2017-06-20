package RegionGrow.main;

import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;


/**
 * Classe permettant le calcul de Dice entre deux images
 * @author Thibault DELAVELLE
 *
 */
public class CritereSatisfaction implements PlugInFilter{

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_RGB;
	}

	@Override
	public void run(ImageProcessor ip) {
		
	}
	
	/**
	 * Calcule le coefficient de Dice entre deux images (de mêmes dimensions)
	 * @param tab1 : la première image
	 * @param tab2 : la deuxième image
	 * @return : le coefficient, un double entre 0 et 1
	 */
	public double getDice(int[][] tab1, int[][] tab2){
		//la formule est (2*|A inter B|) / (|A| + |B|) 
		int intersection = 0; //A inter B |A inter B|
		int A = 0; //Nombre de pixels |A|
		int B = 0; //Nombre de pixels |B|
		if(tab1.length != tab2.length || tab1[0].length != tab2[0].length){
			System.err.println("ERREUR DICE : les images ne sont pas de mêmes dimensions");
			return -1;
		}
		for (int i = 0; i < tab1.length; i++) {
			for (int j = 0; j < tab1[0].length; j++) {
				//on ne prend pas en compte les pixels du fond
				if(tab1[i][j] >0 && tab1[i][j]==tab2[i][j])intersection++;
				if(tab1[i][j] >0)A++;
				if(tab2[i][j] >0)B++;
			}
		}
		System.out.println("DICE :  A inter B="+intersection+" |A|+|B|="+(A+B));
		return (double)(2*intersection)/((A+B));
	}
	
	
	/**
	 * Calcule le coefficient de Dice pour chaque objet segmenté entre deux images (de mêmes dimensions)
	 * @param tab1 : la première image
	 * @param tab2 : la deuxième image
	 * @return : le coefficient, un double entre 0 et 1
	 */
	public double[] getDiceParObjet(int[][] tab1, int[][] tab2){
		if(tab1.length != tab2.length || tab1[0].length != tab2[0].length){
			System.err.println("ERREUR DICE : les images ne sont pas de mêmes dimensions");
			return null;
		}
		double[] intersectionParOrgane = new double[256];
		double[] sommeAParOrgane = new double[256];
		double[] sommeBParOrgane = new double[256];
		for (int i = 0; i < tab1.length; i++) {
			for (int j = 0; j < tab1[0].length; j++) {
				if(tab1[i][j] >0)sommeAParOrgane[tab1[i][j]]++;
				if(tab2[i][j] >0)sommeBParOrgane[tab2[i][j]]++;
				if(tab1[i][j] >0 && tab1[i][j]==tab2[i][j])intersectionParOrgane[tab1[i][j]]++;
			}
		}
		
		double[] res = new double[256];
		for (int i = 0; i < res.length; i++) {
			if(intersectionParOrgane[i]>0)res[i] = (double)(2*intersectionParOrgane[i])/((sommeAParOrgane[i]+sommeBParOrgane[i]));
		}
		
		afficherDiceObjets(res);
		return res;
	}
	
	public void afficherTab1D(double[] tab) {
		for (int i = 0; i < tab.length; i++) {
			System.out.println("i="+i+"->"+tab[i]);
		}
	}
	
	public void afficherDiceObjets(double[] tab){
		System.out.println("Tumeur: "+tab[88]);
		System.out.println("Rein Sein: "+tab[61]);
		System.out.println("Artère: "+tab[66]);
		System.out.println("Veine: "+tab[59]);
	}

	/**
	 * Calcule le coefficient IU entre deux images (de mêmes dimensions)
	 * @param tab1 : la première image
	 * @param tab2 : la deuxième image
	 * @return : le coefficient, un double entre 0 et 1
	 */
	public double getIU(int[][] tab1, int[][] tab2){
		//la formule est (|A inter B|) / (|A unnion B|) 
		// = (|A inter B|) / (|A|+|B|-|A inter B|) 
		int intersection = 0; //A inter B |A inter B|
		int A = 0; //Nombre de pixels |A|
		int B = 0; //Nombre de pixels |B|
		if(tab1.length != tab2.length || tab1[0].length != tab2[0].length){
			System.err.println("ERREUR DICE : les images ne sont pas de mêmes dimensions");
			return -1;
		}
		for (int i = 0; i < tab1.length; i++) {
			for (int j = 0; j < tab1[0].length; j++) {
				//on ne prend pas en compte les pixels du fond
				if(tab1[i][j] >0 && tab1[i][j]==tab2[i][j])intersection++;
				if(tab1[i][j] > 0)A++;
				if(tab2[i][j] > 0)B++;
			}
		}
		System.out.println("IU :  A inter B="+intersection+" A union B="+(A+B-intersection));
		return (double)(intersection)/(A+B-intersection);
	}
	
	
	public double[] getIUParObjet(int[][] tab1, int[][] tab2){
		if(tab1.length != tab2.length || tab1[0].length != tab2[0].length){
			System.err.println("ERREUR DICE : les images ne sont pas de mêmes dimensions");
			return null;
		}
		double[] intersectionParOrgane = new double[256];
		double[] sommeAParOrgane = new double[256];
		double[] sommeBParOrgane = new double[256];
		for (int i = 0; i < tab1.length; i++) {
			for (int j = 0; j < tab1[0].length; j++) {
				if(tab1[i][j] >0)sommeAParOrgane[tab1[i][j]]++;
				if(tab2[i][j] >0)sommeBParOrgane[tab2[i][j]]++;
				if(tab1[i][j] >0 && tab1[i][j]==tab2[i][j])intersectionParOrgane[tab1[i][j]]++;
			}
		}
		
		double[] res = new double[256];
		for (int i = 0; i < res.length; i++) {
			if(intersectionParOrgane[i]>0)res[i] = (double)(intersectionParOrgane[i])/((sommeAParOrgane[i]+sommeBParOrgane[i]-intersectionParOrgane[i]));
		}
		
		afficherDiceObjets(res);
		return res;
	}
	
	
	public static void main(String[] args) {
		CritereSatisfaction d = new CritereSatisfaction();
		
		JFileChooser dialogue = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path = null;
		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			 path = dialogue.getSelectedFile().toPath();
		}
		ImagePlus im = new ImagePlus(path.toString());
		d.setup("", im);
		ByteProcessor c1 = im.getProcessor().convertToByteProcessor();
		int[][] tab1 = c1.getIntArray();
		
		
		JFileChooser dialogue2 = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path2 = null;
		if (dialogue2.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			 path2 = dialogue2.getSelectedFile().toPath();
		}
		ImagePlus im2 = new ImagePlus(path2.toString());
		d.setup("", im2);
		ByteProcessor c2 = im2.getProcessor().convertToByteProcessor();
		int[][] tab2 = c2.getIntArray();
		
		
		System.out.println(d.getDice(tab1, tab2));
		System.out.println(d.getIU(tab1, tab2));
		d.getDiceParObjet(tab1, tab2);
		d.getIUParObjet(tab1, tab2);
	}
	
	
	

}
