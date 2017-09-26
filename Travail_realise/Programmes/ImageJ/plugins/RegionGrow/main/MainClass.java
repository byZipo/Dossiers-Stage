package RegionGrow.main;

import java.nio.file.Path;

import javax.swing.JFileChooser;

import ij.ImagePlus;
import ij.process.ImageProcessor;


/**
 * Classe � ex�cuter pour lancer le programme sans passer par l'interface ImageJ
 * @author Thibault DELAVELLE
 *
 */
public class MainClass {
	
	public MainClass(){
		
		validationCroisee();
		
		
		/* ZONE DE TESTS */
		
		//selection de l'image a segmenter
		//le repertoire par defaut du JFileChooser est constitue a l'aide de \\ et non pas des \
		JFileChooser dialogue = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path = null;
		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			 path = dialogue.getSelectedFile().toPath();
		}
		ImagePlus im = new ImagePlus(path.toString());
		Croissance_Regions c = new Croissance_Regions();
		//GestionRelationsSpatiales g = new GestionRelationsSpatiales(im.getWidth(), im.getHeight());
		//il suffit de faire appel aux methodes de base d'un plugin ImageJ : setup() et run()
		c.setup(path.toString(), im);
		ImageProcessor i = im.getProcessor();
		c.run(i);
		
		/* FIN ZONE DE TESTS */
		
	}
	
	/**
	 * Main d'ex�cution pour la validation crois�e
	 * lance l'ex�cution des 16 cas de la base successivement
	 */
	public void validationCroisee(){
		//les indices vont de 3 � 18 car les deux premiers cas ne sont pas conformes (mauvais format)
		for (int i = 3; i <= 18; i++) {
			String path = "C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\BaseDeCas\\"+i+".png";
			ImagePlus im = new ImagePlus(path);
			Croissance_Regions c = new Croissance_Regions();
			c.setup(path, im);
			ImageProcessor ip = im.getProcessor();
			c.run(ip);
		}
	}
	
	/**
	 * MAIN PRINCIPAL POUR EXECUTER LE PROGRAMME
	 * @param args
	 */
	public static void main(String[] args) {
		new MainClass();
	}
	
}
