package RegionGrow.main;

import java.nio.file.Path;

import javax.swing.JFileChooser;

import ij.ImagePlus;
import ij.process.ImageProcessor;


/**
 * Classe à exécuter pour lancer le programme sans passer par l'interface ImageJ
 * @author Thibault DELAVELLE
 *
 */
public class MainClass {

	public MainClass(){
		//selection de l'image a segmenter
		//le repertoire par defaut du JFileChooser est constitue a l'aide de \\ et non pas des \
		JFileChooser dialogue = new JFileChooser("C:\\Users\\Thibault\\Documents\\M2-Info\\Stage\\Images\\CT");
		Path path = null;
		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			 path = dialogue.getSelectedFile().toPath();
		}
		ImagePlus im = new ImagePlus(path.toString());
		Croissance_Regions c = new Croissance_Regions();
		//il suffit de faire appel aux methodes de base d'un plugin ImageJ : setup() et run()
		c.setup("", im);
		ImageProcessor i = im.getProcessor();
		c.run(i);
	}
	
	public static void main(String[] args) {
		new MainClass();
	}
	
}
