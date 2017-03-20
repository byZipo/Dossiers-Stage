	// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage
import java.awt.Point;
import java.util.ArrayList;

public class Croissance_Regions2 implements PlugInFilter {


	byte[] pixels;
	int h,w;
	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		
		//Seuils
		int SeuilGlobal = 10; 
		int SeuilLocal = 10;
		
		h=ip.getHeight();
		w=ip.getWidth();	
		pixels=(byte[])ip.getPixelsCopy();
		ImageProcessor ipDT= new ByteProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("Croissance Regions", ipDT);
		ImageProcessor ipr = imageDT.getProcessor();
		byte[] pixelsDT= (byte[])ipDT.getPixels();
		int[][] pixelsA = ip.getIntArray();


		//germes pour le moment définis en dur
		ArrayList<Point> germes = new ArrayList<Point>();

		//TEST image reinbeau.jpg
		/*germes.add(new Point(154,200));
		germes.add(new Point(322,297));
		germes.add(new Point(302,337));
		germes.add(new Point(198,330));
		germes.add(new Point(245,316));*/
		
		//TEST image synth.pgm
		/*germes.add(new Point(200,150));
		germes.add(new Point(90,172));
		germes.add(new Point(65,31));
		germes.add(new Point(108,42));
		germes.add(new Point(231,85));*/

		//TEST image rein1_base.png
		germes.add(new Point(164,61));
		germes.add(new Point(191,157));
		germes.add(new Point(369,233));
		germes.add(new Point(425,233));
		

		//pour la couleur de la  région
		int delta = 0;

		//pour chaque germe
		for(int i = 0; i<germes.size(); i++){

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
			ipr.putPixel(xGerme,yGerme,255-delta);

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
						ipr.putPixel(xCourant+1,yCourant,255-delta);
						//mise à jour de la moyenne de la région
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant+1][yCourant])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}

				}

				//bas
				if(yCourant<h-1 && pixelsA[xCourant][yCourant+1] != -1){
					if(Math.abs(pixelsA[xCourant][yCourant+1]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant][yCourant+1])<=SeuilLocal)	{
						liste.add(new Point(xCourant,yCourant+1));
						ipr.putPixel(xCourant,yCourant+1,255-delta);
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant][yCourant+1])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}

				}


				//gauche
				if(xCourant>0  && pixelsA[xCourant-1][yCourant] != -1){
					if(Math.abs(pixelsA[xCourant-1][yCourant]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant-1][yCourant])<=SeuilLocal)	{
						liste.add(new Point(xCourant-1,yCourant));
						ipr.putPixel(xCourant-1,yCourant,255-delta);
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant-1][yCourant])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}
				
				//haut
				if(yCourant>0  && pixelsA[xCourant][yCourant-1] != -1){
					if(Math.abs(pixelsA[xCourant][yCourant-1]-moyenneRegion)<SeuilGlobal && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xCourant][yCourant-1])<=SeuilLocal)	{
						liste.add(new Point(xCourant,yCourant-1));
						ipr.putPixel(xCourant,yCourant-1,255-delta);
						moyenneRegion = ((moyenneRegion*nbPixelsRegion)+pixelsA[xCourant][yCourant-1])/(nbPixelsRegion+1);
						nbPixelsRegion++;
					}
				}


				//marquage du point courant de la liste (sauf si c'est un pixel germe sinon on ne peux plus faire la comparaison avec le seuil)
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

			//mise à jour de la couleur de la région
			delta += 50;
		}
		imageDT.show();
		imageDT.updateAndDraw();
	}
}
