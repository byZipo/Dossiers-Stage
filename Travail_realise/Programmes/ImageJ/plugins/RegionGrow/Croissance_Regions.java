	// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage
import java.awt.Point;
import java.util.ArrayList;

public class Croissance_Regions implements PlugInFilter {


	byte[] pixels;
	int h,w;
	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		
		int SeuilGlobal = 20; //ICI ON PEUT REGLER LE SEUILLAGE
		int SeuilLocal = 600;
		/* AVEC normalisation
		 * Pour barbara : S = 20 donne de bons résultats
		 * Pour micors : idem S = 20
		 * Pour photo : S = 40
		 * Pour souris : S = 20
		 * Pour synth : S = 40
		 */
		h=ip.getHeight();
		w=ip.getWidth();	
		pixels=(byte[])ip.getPixelsCopy();
		ImageProcessor ipDT= new ByteProcessor(w,h);
		ImagePlus imageDT= new ImagePlus("Croissance Regions", ipDT);
		ImageProcessor ipr = imageDT.getProcessor();
		byte[] pixelsDT= (byte[])ipDT.getPixels();
		int[][] pixelsA = ip.getIntArray();
		//IJ.log("width : "+w+" height : "+h+" --> "+pixelsA.length+" / "+pixelsA[0].length);

		

		//germes pour le moment définis en dur
		ArrayList<Point> germes = new ArrayList<Point>();

		/*
		germes.add(new Point(154,200));
		germes.add(new Point(322,297));
		germes.add(new Point(302,337));
		germes.add(new Point(198,330));
		germes.add(new Point(245,316));
		*/

		germes.add(new Point(200,150));
		germes.add(new Point(90,172));
		germes.add(new Point(65,31));
		germes.add(new Point(108,42));
		germes.add(new Point(231,85));

		//pour la couleur de la  région
		int delta = 0;

		//pour chaque germ
		for(int i = 0; i<germes.size(); i++){

			//au début de doit les récupérer en double à cause de la Classe Point
			double xGermeBrut = germes.get(i).getX();
			double yGermeBrut = germes.get(i).getY();

			//ici je les cast donc en int
			int xGerme = (int)xGermeBrut;
			int yGerme = (int)yGermeBrut;

			//pour le calcul du seuil local (on se base sur le point précédent)
			Point precedent = new Point(xGerme,yGerme);
			int xPrecedent = (int)precedent.getX();
			int yPrecedent = (int)precedent.getY();



			//liste des voisins (4-connexité)
			ArrayList<Point> voisins = new ArrayList<Point>();
			//ajout initial pour la première itération
			voisins.add(new Point(xGerme,yGerme));
			/*voisins.add(new Point(xGerme+1,yGerme));
			voisins.add(new Point(xGerme,yGerme+1));
			voisins.add(new Point(xGerme-1,yGerme));
			voisins.add(new Point(xGerme,yGerme-1));*/


			while(!voisins.isEmpty()){

				//récuperation ud point courant
				Point courant = voisins.get(0);
				int xCourant = (int)courant.getX();
				int yCourant = (int)courant.getY();
				//IJ.log("Val courant : "+pixelsA[xCourant][yCourant]+" | Val Germe : "+pixelsA[xGerme][yGerme]+" | Val Precedent : "+pixelsA[xPrecedent][yPrecedent]);

				//Si non marqué (déjà visité) et homogénéité
				//Pour le moment je n'utilise que le seuil Global, du coup c'est peut-être pour cela que ce n'est pas super beau.
				if(pixelsA[xCourant][yCourant] != -1 && Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xGerme][yGerme])<SeuilGlobal /*&& Math.abs(pixelsA[xCourant][yCourant]-pixelsA[xPrecedent][yPrecedent])<=SeuilLocal*/){ 
					//on colore en blanc
					ipr.putPixel(xCourant,yCourant,255-delta);

					//ajout des voisins dans la lisre
					if(xCourant<w-1)voisins.add(new Point(xCourant+1,yCourant));
					if(yCourant<h-1)voisins.add(new Point(xCourant,yCourant+1));
					if(xCourant>0)voisins.add(new Point(xCourant-1,yCourant));
					if(yCourant>0)voisins.add(new Point(xCourant,yCourant-1));


					

					//marquage du point courant de la liste (sauf si c'est un pixel germe sinon on ne peux plus faire la comparaison avec le seuil)
					if(!(xCourant == xGerme && yCourant == yGerme))pixelsA[xCourant][yCourant] = -1;
				}

				//mise à jour du pixel précédent
					xPrecedent = xCourant;
					yPrecedent = yCourant;

				//mise à jour du pixel précédent
				/*if(pixelsA[xCourant][yCourant]!= -1){
					xPrecedent = xCourant;
					yPrecedent = yCourant;
				}*/
				//suppression du point courant de la liste
				//precedent = courant;
				voisins.remove(0);
				imageDT.show();
				imageDT.updateAndDraw();
				/*try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			delta += 50;
		}
		//imageDT.show();
		//imageDT.updateAndDraw();
	}
}
