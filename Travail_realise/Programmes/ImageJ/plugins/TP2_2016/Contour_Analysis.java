import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

public class Contour_Analysis implements PlugInFilter {
    ImagePlus imp;
    int W, H; // taille de l'image initiale
    int iMin, iMax, jMin, jMax; // coordonnées de la ROI contenant l'objet analysé
    int GL; // niveau de gris de l'objet analysé
    int ZL; // niveau de zoom de la visualisation
    private static final int NSIZE = 5;


    /**
     * Set ups this filter. Process only 8 bits grey-level images.
     */
    public int setup(String arg, ImagePlus imp) {
	if ( arg == "about" ) {
	    IJ.showMessage( "about Contour_Analysis ...", 
			    "displays the contour's dominant points");
	    return DONE;
	}
	this.imp = imp;
	return DOES_8G;
    }



    private int getIVisu(Path p, int ind){
	return (p.getContourX(ind)-iMin+1)*ZL;
    }

    private int getJVisu(Path p, int ind){
	return (H-1-p.getContourY(ind)-jMin)*ZL;
    }

    private int getIVisu(int x){
	return (x-iMin+1)*ZL;
    }

    private int getJVisu(int y){
	return (H-1-y-jMin)*ZL;
    }

    public double lambda(double t){
    	if(t<=0.5)return (double)2*t;
    	else return (double)2*(1-t);
    }

    public void run( ImageProcessor ip ) {
	// Permet de recuperer des parametres.
	GenericDialog gd = new GenericDialog( "Contour Extraction parameters",
					      IJ.getInstance() );
	gd.addNumericField( "gray level", 0, 0, 3, "" );
	String[] ZLChoice = {"2", "4", "8", "16"};
	gd.addChoice("zoom level", ZLChoice, "4"); 
	gd.showDialog();
	if ( gd.wasCanceled() ) {
	    IJ.error( "PlugIn cancelled" );
	    return;
	}
	GL = (int) gd.getNextNumber();
	ZL = (int)Math.pow(2, gd.getNextChoiceIndex()+1);
	W = ip.getWidth();
	H = ip.getHeight();

	// Crée le contour de la région
	int margin = 3;
	FreemanCode c = new FreemanCode(ip, GL);
	Path p = new Path(c);
	iMin = (int)p.getPMin().getX() - margin + 1;
	iMax = (int)p.getPMax().getX() + margin;
	jMax = H-1 - ((int)p.getPMin().getY() - margin + 1);
	jMin = H-1 - ((int)p.getPMax().getY() + margin);


	// Crée l'image de sortie
	ip.setRoi(iMin, jMin, iMax-iMin+1, jMax-jMin+1);
	ImageProcessor ipz = ip.resize((iMax-iMin+1)*ZL, (jMax-jMin+1)*ZL);

	// Tracé de la grille 1/2
	ipz.setColor(Color.black);
	for(int i=0; i<W*ZL; i+=ZL) 
	    for(int j=0; j<H*ZL; j+=ZL) 
		ipz.drawPixel(i, j);

	// Tracé du chemin
	ImageProcessor ipzc = ipz.convertToRGB();
	ipzc.setColor(Color.red);
	int ind, i1, j1, i2=0, j2=0;
	for(ind=0; ind<p.getLength()-1; ind++){
	    i1 = getIVisu(p, ind); 
	    j1 = getJVisu(p, ind); 
	    i2 = getIVisu(p, ind+1); 
	    j2 = getJVisu(p, ind+1); 
	    ipzc.drawLine(i1, j1, i2, j2);
	}
	i1 = i2;
	j1 = j2;
	i2 = getIVisu(p, 0); 
	j2 = getJVisu(p, 0); 
	ipzc.drawLine(i1, j1, i2, j2);

	// Tracé des normales associées aux tangentes symétriques
	ipzc.setColor(Color.green);
	for(ind = 0; ind < p.getLength(); ind+=5){
	    int x = p.getContourX(ind);
	    int y = p.getContourY(ind);
	    int a = p.getContourTY(ind);
	    int b = p.getContourTX(ind);
	    double norme = Math.sqrt(a*a+b*b);
	    i1 = getIVisu(x);
	    j1 = getJVisu(y);
	    i2 = getIVisu((int)(x-(NSIZE*a)/norme+0.5));
	    j2 = getJVisu((int)(y+(NSIZE*b)/norme+0.5));
	    ipzc.drawLine(i1, j1, i2, j2);

	}

	/************************************************************************
	* Affichage dans la console Log pour chaque point du contour des indices des points 
	* de début et de fin de la tangente positive, c'est à dire du segment de droite discrete 
	* le plus long dans le sens du parcours
	**************************************************************************/

	
	// Question II.1
	
	int n = c.getLength();
	for(int i = 0; i < n; i++){
		IJ.log("Point "+i+" : "+i+" --> "+ (i+p.getContourTPLgr(i))+" : "+p.getContourTPX(i)+", "+p.getContourTPY(i));			
	}	
	
	// on a n points sur le contour
	// p.getContourTPLgr(i) donne l'autre bout du segement discret depuis le point i
	//on cherche le plus long segment entre p.getContourTPLgr(i)
	HashMap<Integer, Integer> MS = new HashMap<Integer,Integer>();
	int i = 0;
	int a = 0;
	int tmp = 0; 
	int temporisateur = 0;
	int temporisateurA = 0;
	int temporisateurTMP = 0;
	IJ.log("Taille C : "+n);
	IJ.log("segments maximaux du contour : ");
	while(i < n){
		a = (i+p.getContourTPLgr(i%n))%n;
		tmp = i%n;
		while(((i+p.getContourTPLgr(i%n))%n) == a){
			i++;
		}
		if(temporisateur > 0) {
			IJ.log("Point "+tmp+" : "+tmp+" --> "+a+" : "+p.getContourTPX(tmp)+", "+p.getContourTPY(tmp));
			MS.put(tmp, a);
		}
		if(temporisateur == 0){
			temporisateurA = a;
			temporisateurTMP = tmp;
		}
		temporisateur++;
	}
	
	if(a == temporisateurA)IJ.log("");
	else {
		IJ.log("Point "+temporisateurTMP+" : "+temporisateurTMP+" --> "+temporisateurA+" : "+p.getContourTPX(temporisateurTMP)+", "+p.getContourTPY(temporisateurTMP));
		MS.put(temporisateurTMP, temporisateurA);
	}
	
	//le probleme est que si la plus long segement de d�part est 119 --> 9 on ne peut pas le calculer des le premier parcourt car on d�marre � 0.
	// donc soit on reparcourt la liste pour supprimer 0 --> 9, soit on le fait intelligement pendant le parcours principal.
	
	
	// Question II.2
		IJ.log("Calcul des tangentes et des normales selon l'estimateur");
		// calcul de eMS(i)
			// pour tous les C
		for(int z = 0; z<n ; z+=5){
			
			double sommeEMS = 0;
			double Teta = 0;
			double sommeTMP = 0;
			// pour tous les SM
			int Ai = z;
			for(Entry<Integer, Integer> entry : MS.entrySet()){
			
			int keyEntry = (int)entry.getKey();
			int valueEntry = (int)entry.getValue();
			//si i appartient au MS courrant
			double eMS = 0;
			//cas normal
			if((Ai >= keyEntry) && (Ai <= valueEntry)){
				double up = (double)Math.abs(Ai-keyEntry);
				double down = (double)Math.abs(valueEntry-keyEntry);
				double frac = (double)(up/down);
				eMS = lambda(frac);
			//cas sp�cial par ex: 45-> 11 
			}else if(keyEntry >= valueEntry){ 
				// cas entre 45 et la fin
				if(Ai >= keyEntry){
					double up = (double)Math.abs(Ai-keyEntry);
					double down = (double)Math.abs((n-keyEntry)+valueEntry);
					double frac = (double)(up/down);
					eMS = lambda(frac);
				}
				// cas entre d�but et 11
				else if((Ai <= keyEntry) && (Ai <= valueEntry)){
					double up = (double)Math.abs((n-keyEntry)+Ai);
					double down = (double)Math.abs((n-keyEntry)+valueEntry);
					double frac = (double)(up/down);
					eMS = lambda(frac);
				}
				
			}
			// cas hors segment
			else{
				eMS = 0;
			}
			// affichage des eMS(i)
			IJ.log("Point"+Ai+" par rapport au segment : "+keyEntry+" -> "+valueEntry+" eMS(i) : "+eMS);
			
			// somme eMS
			sommeEMS += eMS;
			
			// Theta
			int aTeta = p.getContourTPX(keyEntry);
			int bTeta = p.getContourTPY(keyEntry);
			Teta = (double)Math.atan2((double)aTeta, bTeta);
			sommeTMP += (double)eMS * Teta;
		}
			// calcul de theta(i)
			IJ.log("SOMME EMS("+z+") : "+sommeEMS);
			double tetaFinal = (double) sommeTMP/sommeEMS;
			IJ.log("DIRECTION TANGENTE : "+tetaFinal+" Vecteur normal : ("+Math.cos(tetaFinal)+" , "+Math.sin(tetaFinal)+")");
			
			// dessin des normales
			double amdr = (double) Math.cos(tetaFinal);
			double bmdr = (double) Math.sin(tetaFinal);
			ipzc.setColor(Color.blue);
			double norme = (double) Math.sqrt(amdr*amdr+bmdr*bmdr);
			i1 = getIVisu(p.getContourX(Ai));
		    j1 = getJVisu(p.getContourY(Ai));
		    i2 = getIVisu((int)(p.getContourX(Ai)-(NSIZE*amdr)/norme+0.5));
		    j2 = getJVisu((int)(p.getContourY(Ai)+(NSIZE*bmdr)/norme+0.5));
		    ipzc.drawLine(i1, j1, i2, j2);
	}
	
	
		
	
	
	//*********************************************************************************
	
	ImagePlus visu = new ImagePlus("Discrete contour + normals", ipzc);

	// displays it in a window.
	visu.show();
	// forces the redisplay.
	visu.updateAndDraw();

    }

}
			      