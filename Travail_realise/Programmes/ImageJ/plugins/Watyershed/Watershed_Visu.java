import ij.*;
import ij.process.*;
import java.awt.*;
import ij.plugin.filter.*;
import ij.gui.*;
import java.util.HashMap;

public class Watershed_Visu implements PlugInFilter {

     /**
     * Convolution d'un pixel d'une image en niveaux de gris par un masque 3x3.
     */
    public static float convolue(ImageProcessor ip, int x, int y, float[][] m){
    	int w = ip.getWidth();
    	int h = ip.getHeight();
    	float acc = 0.0f;
    	for ( int i = -1; i <= 1; i++ )  
    		for ( int j = -1; j <= 1; j++ )
    			if ( ( x + i >= 0 ) && ( x + i < w ) &&	( y + j >= 0 ) && ( y + j < h ) )
    				acc += ip.getPixel(x+i, y+j)* m[1-i][1-j];
    	return acc; 
    }

    /**
     * Calcul de l'image de la norme du gradient.
     */
    public static FloatProcessor imageNormeGradient(ImageProcessor ip){
    	int w = ip.getWidth();
    	int h = ip.getHeight();

    	float[][] mask_dx = {{1,0,-1},{2,0,-2},{1,0,-1}};
    	float[][] mask_dy = {{1,2,1},{0,0,0},{-1,-2,-1}};
    	FloatProcessor fp = new FloatProcessor(w, h);
    	for (int x = 0; x < w; x++)
    		for (int y = 0; y < h; y++){
      			float dx = convolue( ip, x, y, mask_dx );
    			float dy = convolue( ip, x, y, mask_dy );
    			fp.setf(x, y, (float)Math.sqrt(dx*dx+dy*dy));
		}
    	return fp;
    }
    
 
    public int setup(String arg, ImagePlus imp) {
    	return DOES_8G;
    }
    
    
    public void run( ImageProcessor ip ) {
	// paramètres : 
	//  calcul à partir de l'image initiale ou calcul à partir de l'image de gradient
	//  <epsilon> : considere que les pixels d'altitude proches (<epsilon) sont de meme altitude
 
    	GenericDialog gd = new GenericDialog( "Watershade parameters", IJ.getInstance() );
    	gd.addNumericField( "epsilon", 5, 0, 3, "" );
    	String[] IIChoice = {"Image initiale", "Gradient"};
    	gd.addChoice("Image en entrée", IIChoice, "Gradient"); 
    	gd.showDialog();
    	if ( gd.wasCanceled() ) {
    		IJ.error( "PlugIn cancelled" );
    		return;
    	}
    	float epsilon = (float)gd.getNextNumber();	
    	int input = gd.getNextChoiceIndex();
    	
    	FloatProcessor in; // image en entree du watershed.
	
	    if (input == 1)
	    	in = imageNormeGradient(ip);
	    else // input == 0 {
		    in = new FloatProcessor(ip.getIntArray());


	    // Instancie un objet Watershed.
	    Watershed ws = new Watershed( in, epsilon );
	    // Calcule le watershed.
	    IntImage ws_image = ws.calculeWatershed();
	    // Ferme le watershed.
	    ws_image = Watershed.fermeWatershed( ws_image );

	    // Prepare la visualisation de l'image watershed.
	    Color[] mes_couleurs = new Color[2];
	    mes_couleurs[0] = new Color(0, 0, 0); // WATERSHED
	    mes_couleurs[1] = new Color(255, 255, 255); // UNLABELLED
	    ColorProcessor wsip = ws_image.createRandomColorImage(mes_couleurs);
	    
	    // Affichage du résultat
    	
    	if (input == 1){
    		ImagePlus visu1 = new ImagePlus("Image du gradient", in);
        	visu1.show();
        	visu1.updateAndDraw();
    	}
 		ImagePlus visu2 = new ImagePlus("Résultat du watershed", wsip);
    	visu2.show();
    	visu2.updateAndDraw();

        // Image par moyenne couleur

        int w = ws_image.getWidth();
        int h = ws_image.getHeight();

        HashMap<Integer,int[]> labelMap = new HashMap<Integer,int[]>();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int label = ws_image.getPixel(i,j);
                int[] aux = new int[2];
                if(labelMap.containsKey(label)){
                    aux = labelMap.get(label);
                    aux[0] += ip.get(i,j);
                    aux[1] += 1;
                }else{
                    aux[0] = ip.get(i,j);
                    aux[1] = 1;
                }
                labelMap.put(label,aux);
            }
        }

        int[][] pixels = (int[][])ip.getIntArray();
        int L = pixels.length;
        int H = pixels[0].length;
        ImageProcessor imgproc = new ByteProcessor(L,H);
        ImagePlus img = new ImagePlus("LPE niveau de gris", imgproc);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int label = ws_image.getPixel(i,j);
                int somme = labelMap.get(label)[0];
                int nbFois = labelMap.get(label)[1];
                int moyenne = (int)(somme/nbFois);
                imgproc.set(i,j,moyenne);
            }
        }

        img.show();
        img.updateAndDraw();

	}
}

