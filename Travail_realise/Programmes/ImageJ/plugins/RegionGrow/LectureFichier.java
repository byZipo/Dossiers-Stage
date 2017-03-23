//package antispam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class LectureFichier{

	//lecture et construction de la base de cas, d'après un fichier TXT au format "Ligne", voir fichier BaseDeCasEnLigne.txt
	public BaseDeCas LectureFichierBaseEnLigne(String fichier) throws IOException {
		BaseDeCas base = new BaseDeCas();
		String st = "";
		BufferedReader br = new BufferedReader(new FileReader(fichier));

		//en-tête
		br.readLine();

		String[] decoupageCas;
		String[] decoupageNonImage;
		String[] decoupageImage;
		String[] decoupageSolution;
		String problemeNonImage="";
		String problemeImage="";
		String solution="";
		
		
		while((st = br.readLine()) != null){
			decoupageCas = st.split("/");
			problemeNonImage = decoupageCas[0];
			problemeImage = decoupageCas[1];
			solution = decoupageCas[2];
			
			decoupageNonImage = problemeNonImage.split(";");
			decoupageImage = problemeImage.split(";");
			decoupageSolution = solution.split(";");
			
			double age = Double.parseDouble(decoupageNonImage[0]);
			double taille = Double.parseDouble(decoupageNonImage[1]);
			double masse = Double.parseDouble(decoupageNonImage[2]);
			double sexe = Double.parseDouble(decoupageNonImage[3]);
			double nbCoupes = Double.parseDouble(decoupageNonImage[4]);
			double hauteurCoupe = Double.parseDouble(decoupageNonImage[5]);
			
			double moyenne = Double.parseDouble(decoupageImage[0]);
			double asymetrie = Double.parseDouble(decoupageImage[1]);
			double variance = Double.parseDouble(decoupageImage[2]);
			double kurtosis = Double.parseDouble(decoupageImage[3]);
			
			int seuilGlobal = Integer.parseInt(decoupageSolution[0]);
			int seuilLocal = Integer.parseInt(decoupageSolution[1]);
			
			ArrayList<Germe> germes = new ArrayList<Germe>();
			for(int i = 2; i < decoupageSolution.length-1; i+=2){
				germes.add(new Germe(Integer.parseInt(decoupageSolution[i]), Integer.parseInt(decoupageSolution[i+1])));
			}
			
			
			Probleme p = new Probleme(age, taille, masse, sexe, nbCoupes, hauteurCoupe, moyenne, asymetrie, variance, kurtosis);
			Solution s = new Solution(seuilGlobal, seuilLocal, germes);
			Cas c = new Cas(p,s);
			base.ajouterCas(c);
		}
		br.close();
		return base;
	}
	
	public static void main(String[] args) {
		LectureFichier l = new LectureFichier();
		try {
			BaseDeCas base = l.LectureFichierBaseEnLigne("BaseDeCasEnLigne.txt");
			System.out.println(base.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

