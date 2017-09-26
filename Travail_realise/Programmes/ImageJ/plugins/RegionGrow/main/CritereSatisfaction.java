package RegionGrow.main;

import java.awt.Color;
import java.util.Arrays;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;


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
	 * calcule l'indice DICE pour chaque structure anatomique entre deux images
	 * @param tab1 : premiere image
	 * @param tab2 : deuxieme image
	 * @return un tableau contenant le DICE pour chaque structure (= pour chaque couleur convertie en niveaux de gris)
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
	
	/**
	 * affiche le contenu d'un tableau à une dimension
	 * @param tab : le tableau à afficher
	 */
	public void afficherTab1D(double[] tab) {
		for (int i = 0; i < tab.length; i++) {
			System.out.println("i="+i+"->"+tab[i]);
		}
	}
	
	/**
	 * afficher le dice pour les structures anatomiques en fonction de leur couleur :
	 * (88 = tumeur, 61 = rein, 66 = artère, 59 = veine)
	 * @param tab : le tableau des DICE pour chaque couleur
	 */
	public void afficherDiceObjets(double[] tab){
		System.out.println("Tumeur: "+tab[88]);
		System.out.println("Rein Sein: "+tab[61]);
		//System.out.println("Artère: "+tab[66]);
		//System.out.println("Veine: "+tab[59]);
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
	
	/**
	 * calcule l'indice IU pour chaque structure anatomique entre deux images
	 * @param tab1 : premiere image
	 * @param tab2 : deuxieme image
	 * @return un tableau contenant l'IU pour chaque structure (= pour chaque couleur convertie en niveaux de gris)
	 */
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
	
	
	/**
	 * fonction "bricolage" post-traitement pour supprimer le gris résultant
	 * lors de l'éxécution d'un algo de Watershed
	 * @param c : l'image
	 */
	public void supprimerGris(ColorProcessor c){
		
		
		ImageProcessor ipDT= new ColorProcessor(c.getWidth(),c.getHeight());
		ImagePlus imageDT= new ImagePlus("Croissance Regions", ipDT);
		ImageProcessor ipr = imageDT.getProcessor();
		
		
		for (int i = 0; i < c.getWidth(); i++) {
			for (int j = 0; j < c.getHeight(); j++) {
				Color col = c.getColor(i, j);
				if(col.getRed() ==  72 && col.getGreen() == 119 && col.getBlue() ==72){
					ipr.putPixel(i, j, new int[]{72,119,72});
				}else if(col.getRed() ==  90 && col.getGreen() == 52 && col.getBlue() ==41){
					ipr.putPixel(i, j, new int[]{90,52,41});
				}
				else if(col.getRed() ==  0 && col.getGreen() == 75 && col.getBlue() ==103){
					ipr.putPixel(i, j, new int[]{0,75,103});
				}
				else if(col.getRed() ==  108 && col.getGreen() == 50 && col.getBlue() ==39){
					ipr.putPixel(i, j, new int[]{108,50,39});
				}
				//System.out.print(c.getColor(262,266 ).getBlue()+" ");
				
			}
		}
		
		imageDT.show();
		imageDT.updateAndDraw();
		
		/*FileSaver save = new FileSaver(imageDT);
		save.saveAsPng();*/
		
	}
	
	
	/**
	 * calcule la valeur du critere SSIM entre deux images
	 * @param ip1 : première image
	 * @param ip2 : deuxième image
	 * @return : la valeur du critère SSIM
	 */
	public double getSSIM(ImageProcessor ip1, ImageProcessor ip2){
		
		
		double k1 = 0.01;
		double k2 = 0.03;
		
		ImageStatistics st1 = ip1.getStatistics();
		ImageStatistics st2 = ip2.getStatistics();
		
		double mu1 = st1.mean;
		double mu2 = st2.mean;
		
		double sigma1 = (double)(st1.stdDev);
		double sigma2 = (double)(st2.stdDev);
		
		double c1 = (double)(k1*255)*(k1*255);
		double c2 = (double)(k2*255)*(k2*255);
		
		double numerateur = (double)(((double)(2*mu1*mu2)+c1)*((double)(2*sigma1*sigma2)+c2));
		double denominateur = (double)(((double)(mu1*mu1) + (double)(mu2*mu2)+ c1)*((double)(sigma1*sigma1)+(double)(sigma2*sigma2)+c2));
		
		
		return (double)(numerateur/denominateur);
	}
	
	
	/**
	 * calcule la valeur du critere MSSIM entre deux images
	 * @param ip1 : première image
	 * @param ip2 : deuxième image
	 * @return : la valeur du critère MSSIM
	 */
	public double getMSSIM(ImageProcessor ip1, ImageProcessor ip2){
		
		int pas = 50;
		double res = 0;
		int nbWindows = 0;
		
		for (int i = 0; i < (ip1.getWidth()>ip2.getWidth()?ip2.getWidth():ip1.getWidth()); i+=pas) {
			for (int j = 0; j < (ip1.getHeight()>ip2.getHeight()?ip2.getHeight():ip1.getHeight()); j+=pas){
				nbWindows++;
				ip1.setRoi(i, j, pas, pas);
				ip2.setRoi(i, j, pas, pas);
				//Roi r = new Roi(i, j, pas, pas);
				ImageProcessor tmp1 = ip1.crop();
				ImageProcessor tmp2 = ip2.crop();
				res += getSSIM(tmp1,tmp2);
			}
		}
		
		return res/nbWindows;
	}
	
	
	/**
	 * Calcule DICE + IU sur les images résultat de la validation croisée
	 * par rapport aux vérités terrain
	 */
	public void resultatsValidationCroisee(){
		
		
		double[] diceMoyen = new double[256];
		double[] iuMoyen = new double[256];
		double[][] diceMedian = new double[256][16];
		double[][] iuMedian = new double[256][16];
		for (int i = 3; i <= 18; i++) {
			String pathResultats = "C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\ResultatsSansAdaptation\\";
			String pathTerrain = "C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\VeritesTerrain\\";
			pathResultats +=i+".png";
			pathTerrain +=i+".png";
			
			
			ImagePlus im = new ImagePlus(pathResultats);
			this.setup("", im);
			ByteProcessor c1 = im.getProcessor().convertToByteProcessor();
			int[][] tab1 = c1.getIntArray();
			
			ImagePlus im2 = new ImagePlus(pathTerrain);
			this.setup("", im2);
			ByteProcessor c2 = im2.getProcessor().convertToByteProcessor();
			int[][] tab2 = c2.getIntArray();
			
			//System.out.println(getDice(tab1, tab2));
			//System.out.println(getIU(tab1, tab2));
			double[] dice = getDiceParObjet(tab1, tab2);
			double[] iu = getIUParObjet(tab1, tab2);
			
			for (int j = 0; j < iu.length; j++) {
				diceMoyen[j] += dice[j];
				iuMoyen[j] += iu[j];
				diceMedian[j][i-3] = dice[j];
				iuMedian[j][i-3] = iu[j];
			}
			
			System.out.println("--------------------------------");
		}
		
		System.out.println("-- RESULTATS GLOBAUX --");
		System.out.println("Dice moyen par organe :");
		System.out.println("Tumeur: "+diceMoyen[88]/16);
		System.out.println("Rein Sein: "+diceMoyen[61]/16);
		System.out.println("IU moyen par organe : ");
		System.out.println("Tumeur: "+iuMoyen[88]/16);
		System.out.println("Rein Sein: "+iuMoyen[61]/16);
		
		double[] tmpMT = diceMedian[88];
		double[] tmpMR = diceMedian[61];
		double[] tmpIT = iuMedian[88];
		double[] tmpIR = iuMedian[61];
		
		Arrays.sort(tmpMT);
		Arrays.sort(tmpMR);
		Arrays.sort(tmpIT);
		Arrays.sort(tmpIR);
		
		System.out.println("---------------------------------------");
		System.out.println("Dice median par organe : ");
		double medianMT;
		if (tmpMT.length % 2 == 0) medianMT = ((double)tmpMT[tmpMT.length/2] + (double)tmpMT[tmpMT.length/2 - 1])/2;
		else medianMT = (double) tmpMT[tmpMT.length/2];
		double medianMR;
		if (tmpMR.length % 2 == 0) medianMR = ((double)tmpMR[tmpMR.length/2] + (double)tmpMR[tmpMR.length/2 - 1])/2;
		else medianMR = (double) tmpMR[tmpMR.length/2];
		System.out.println("Tumeur: "+medianMT);
		System.out.println("Rein: "+medianMR);
		
		
		System.out.println("IU median par organe : ");
		double medianIT;
		if (tmpMT.length % 2 == 0) medianIT = ((double)tmpIT[tmpIT.length/2] + (double)tmpIT[tmpIT.length/2 - 1])/2;
		else medianIT = (double) tmpIT[tmpIT.length/2];
		double medianIR;
		if (tmpMR.length % 2 == 0) medianIR = ((double)tmpIR[tmpIR.length/2] + (double)tmpIR[tmpIR.length/2 - 1])/2;
		else medianIR = (double) tmpIR[tmpIR.length/2];
		System.out.println("Tumeur: "+medianIT);
		System.out.println("Rein: "+medianIR);
		
	}
	
	
	public static void main(String[] args) {
		
		CritereSatisfaction d = new CritereSatisfaction();
		
		/* ZONE DE TESTS */
		
		/*
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
		d.getIUParObjet(tab1, tab2);*/
		
		/////////// SUPPRESSION GRIS WATERSHED //////////////
		/*
		JFileChooser dialogue3 = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path3 = null;
		if (dialogue3.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			 path3 = dialogue3.getSelectedFile().toPath();
		}
		ImagePlus im3 = new ImagePlus(path3.toString());
		d.setup("", im3);
		ColorProcessor c3 = im3.getProcessor().convertToRGB().convertToColorProcessor();
		
		d.supprimerGris(c3);
		*/
		
		
		/*
		Opener o = new Opener();
		o.open();
		FFT fft = new FFT();
		fft.run("");
		*/
		
		/*
		int centerOfMass2 = Measurements.CENTER_OF_MASS;
		int skewness2 = Measurements.SKEWNESS;
		*/
		
		/*		
		System.out.println(Measurements.CENTER_OF_MASS);
		System.out.println(Measurements.SKEWNESS);
		System.out.println(Measurements.KURTOSIS);
		System.out.println(Measurements.MEDIAN);
		System.out.println(Measurements.MEDIAN);
		System.out.println(Measurements.STD_DEV);
		 */
		
		/* FIN ZONE DE TESTS */
		
		System.out.println("#########################################");
		d.resultatsValidationCroisee();
		
	}
	

	
	
	

}
