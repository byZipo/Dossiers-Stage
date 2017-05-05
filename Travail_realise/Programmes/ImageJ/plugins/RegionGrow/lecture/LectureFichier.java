//package antispam;

package RegionGrow.lecture;
import RegionGrow.baseDeCas.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



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
			
			//int seuilGlobal = Integer.parseInt(decoupageSolution[0]);
			//int seuilLocal = Integer.parseInt(decoupageSolution[1]);
			
			ArrayList<Germe> germes = new ArrayList<Germe>();
			for(int i = 0; i < decoupageSolution.length-3; i+=4){
				int seuilGlobal = Integer.parseInt(decoupageSolution[i]);
				int seuilLocal = Integer.parseInt(decoupageSolution[i+1]);
				int posX = Integer.parseInt(decoupageSolution[i+2]);
				int posY = Integer.parseInt(decoupageSolution[i+3]);
				germes.add(new Germe(posX, posY, seuilGlobal, seuilLocal));
			}
			
			
			Probleme p = new Probleme(age, taille, masse, sexe, nbCoupes, hauteurCoupe, moyenne, asymetrie, variance, kurtosis);
			Solution s = new Solution(germes);
			Cas c = new Cas(p,s);
			base.ajouterCas(c);
		}
		br.close();
		return base;
	}

	//écriture du la base de cas (passée en paramètre) dans un fichier TXT au format "Ligne", voir fichier BaseDeCasEnLigne.txt
	public void ecritureFichierBaseEnLigne(String fichier, BaseDeCas base) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fichier)));
		String entete = "// BASE DE CAS EN LIGNE //";
		pw.println(entete);
		for(int i = 0 ; i < base.getTailleBase() ; i++){
			StringBuilder st = new StringBuilder();
			Cas c = base.getCas(i);
			Probleme p = c.getProbleme();

			//Probleme
				//NonImage
			String age = p.getAge()+"";
			String taille = p.getTaille()+"";
			String masse = p.getMasse()+"";
			String sexe = p.getSexe()+"";
			String nbCoupes = p.getNbCoupes()+"";
			String hauteurCoupe = p.getHauteurCoupe()+"";
			st.append(age+";"+taille+";"+masse+";"+sexe+";"+nbCoupes+";"+hauteurCoupe+"/");
						
				//Image
			String moyenne = p.getMoyenne()+"";
			String asymetrie = p.getAsymetrie()+"";
			String variance = p.getVariance()+"";
			String kurtosis = p.getKurtosis()+"";
			st.append(moyenne+";"+asymetrie+";"+variance+";"+kurtosis+"/");
		
			//Solution
			Solution s = c.getSolution();
				//Seuils
			//st.append(s.getSeuilGlobal()+";"+s.getSeuilLocal()+";");
				//Germes
			for(int j = 0; j<s.getNbGermes();j++){
				Germe g = s.getGerme(j);
				if(j<s.getNbGermes()-1)st.append(g.getSeuilGlobal()+";"+g.getSeuilLocal()+";"+g.getX()+";"+g.getY()+";");
				else st.append(g.getSeuilGlobal()+";"+g.getSeuilLocal()+";"+g.getX()+";"+g.getY());
			}
			pw.println(st.toString());
		}
		pw.close();
	}
	
	
	public BaseDeCas parserXML(String fichier){
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new File(fichier));
			
			//racine du fichier xml
			final Element racine = document.getDocumentElement();
			System.out.println(racine.getNodeName());
			
			//noeuds enfants de la racine --> les Cas
			final NodeList listeCas = racine.getElementsByTagName("Cas");
			for (int i = 0; i < listeCas.getLength(); i++) {
					final Element cas = (Element)listeCas.item(i);
					System.out.println(cas.getNodeName()+" : "+cas.getAttribute("id"));
					
					//contenu d'un cas --> probleme + solution
					final Element probleme = (Element)cas.getElementsByTagName("Probleme").item(0);
					final Element solution = (Element)cas.getElementsByTagName("Solution").item(0);
					
					final Element image = (Element)probleme.getElementsByTagName("Image").item(0);
					final Element nonImage = (Element)probleme.getElementsByTagName("NonImage").item(0);
					final Element positionTumeur = (Element)probleme.getElementsByTagName("PositionTumeur").item(0);
					
					
					final NodeList caracsImage = image.getElementsByTagName("*");
					for (int j = 0; j < caracsImage.getLength(); j++) {
						final Element carac = (Element)caracsImage.item(j);
						System.out.println(carac.getNodeName()+" : "+carac.getTextContent());
					}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		return null;
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

