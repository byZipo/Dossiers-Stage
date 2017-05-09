//package antispam;

package RegionGrow.lecture;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import RegionGrow.baseDeCas.BaseDeCas;
import RegionGrow.baseDeCas.Cas;
import RegionGrow.baseDeCas.Germe;
import RegionGrow.baseDeCas.Probleme;
import RegionGrow.baseDeCas.Solution;

/**
 * Réalise la lecture d'une base de cas sous forme de fichier .txt ou .xml
 * @author Thibault DELAVELLE
 *
 */

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
			for(int j = 0; j<s.getNbGermesUtiles();j++){
				Germe g = s.getGermeUtile(j);
				if(j<s.getNbGermesUtiles()-1)st.append(g.getSeuilGlobal()+";"+g.getSeuilLocal()+";"+g.getX()+";"+g.getY()+";");
				else st.append(g.getSeuilGlobal()+";"+g.getSeuilLocal()+";"+g.getX()+";"+g.getY());
			}
			pw.println(st.toString());
		}
		pw.close();
	}
	
	/**
	 * Lecture d'une base de cas (fichier XML) et création de la base de cas Java associée
	 * @param fichier  : le fichier .xml contenant la base de cas (à placer à la racine du projet) 
	 * @return la base de cas Java associée
	 */
	public BaseDeCas parserXML(String fichier){
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new File(fichier));
			
			//racine du fichier xml
			final Element racine = document.getDocumentElement();
			System.out.println("RACINE : "+racine.getNodeName());
			
			//noeuds enfants de la racine --> les Cas
			/************** POUR CHAQUE CAS **********************/
			
			final NodeList listeCas = racine.getElementsByTagName("Cas");
			for (int i = 0; i < listeCas.getLength(); i++) {
					final Element cas = (Element)listeCas.item(i);
					System.out.println(cas.getNodeName()+" : "+cas.getAttribute("id"));
					
					//contenu d'un cas --> probleme + solution
					final Element probleme = (Element)cas.getElementsByTagName("Probleme").item(0);
					final Element solution = (Element)cas.getElementsByTagName("Solution").item(0);
					
					/************ PARTIE PROBLEME ****************/
					
					Probleme p = new Probleme();
					//les trois balises de la partie problème
					final Element image = (Element)probleme.getElementsByTagName("Image").item(0);
					final Element nonImage = (Element)probleme.getElementsByTagName("NonImage").item(0);
					final Element positionTumeur = (Element)probleme.getElementsByTagName("PositionTumeur").item(0);
					
					//les caractéristiques de la partie image 
					final NodeList caracsImage = image.getElementsByTagName("*");
					for (int j = 0; j < caracsImage.getLength(); j++) {
						final Element carac = (Element)caracsImage.item(j);
						//System.out.println(carac.getNodeName()+" : "+carac.getTextContent());
						try{
							double value = Integer.parseInt(carac.getTextContent());
							p.setCarac(carac.getNodeName(), value);
						}catch(NumberFormatException e){
							System.err.println("ERREUR DE FORMAT AVEC LA VALEUR DE L'ATTRIBUT "+carac.getNodeName()+" : "+carac.getTextContent()+" (double attendu)\n");
						}
						
					}
					
					//les caractéristiques de la partie non-image 
					final NodeList caracsNonImage = nonImage.getElementsByTagName("*");
					for (int j = 0; j < caracsNonImage.getLength(); j++) {
						final Element carac = (Element)caracsNonImage.item(j);
						//System.out.println(carac.getNodeName()+" : "+carac.getTextContent());
						try{
							double value = Integer.parseInt(carac.getTextContent());
							p.setCarac(carac.getNodeName(), value);
						}catch(NumberFormatException e){
							System.err.println("ERREUR DE FORMAT AVEC LA VALEUR DE L'ATTRIBUT "+carac.getNodeName()+" : "+carac.getTextContent()+" (double attendu)\n");
						}
					}
					
					System.out.println(p.toString());
					
					//les caractéristiques de la partie positionTumeur
					final NodeList positionsTumeur = positionTumeur.getElementsByTagName("relation");
					for (int j = 0; j < positionsTumeur.getLength(); j++) {
						final Element carac = (Element)positionsTumeur.item(j);
						System.out.println(carac.getNodeName()+" : "+carac.getTextContent()+"->"+carac.getAttribute("value"));
					}
					
					/****************** PARTIE SOLUTION ********************/
					
					//les quatre balises de la partie solution
					final Element objetsUtiles = (Element)solution.getElementsByTagName("ObjetsUtiles").item(0);
					final Element objetsInutiles = (Element)solution.getElementsByTagName("ObjetsInutiles").item(0);
					final Element pretraitements = (Element)solution.getElementsByTagName("PreTraitements").item(0);
					final Element positionFloueTumeur = (Element)solution.getElementsByTagName("PositionFloueTumeur").item(0);
					
					
					//les caractéristiques de la partie objetsUtiles
					final NodeList caracsUtiles = objetsUtiles.getElementsByTagName("*");
					for (int j = 0; j < caracsUtiles.getLength(); j++) {
						final Element carac = (Element)caracsUtiles.item(j);
						System.out.println(carac.getNodeName()+" type:"+carac.getAttribute("type")+" seuilGlobal:"+carac.getAttribute("seuilGlobal")+" seuilLocal:"+carac.getAttribute("seuilLocal")+" --> "+carac.getTextContent());
					}
					
					//les caractéristiques de la partie objetsInutiles
					final NodeList caracsInutiles = objetsInutiles.getElementsByTagName("*");
					for (int j = 0; j < caracsInutiles.getLength(); j++) {
						final Element carac = (Element)caracsInutiles.item(j);
						System.out.println(carac.getNodeName()+" type:"+carac.getAttribute("type")+" seuilGlobal:"+carac.getAttribute("seuilGlobal")+" seuilLocal:"+carac.getAttribute("seuilLocal")+" --> "+carac.getTextContent());
					}
					
					//les caractéristiques de la partie pretraitements
					final NodeList caracsTraitements = pretraitements.getElementsByTagName("*");
					for (int j = 0; j < caracsTraitements.getLength(); j++) {
						final Element carac = (Element)caracsTraitements.item(j);
						System.out.println(carac.getNodeName()+" type:"+carac.getAttribute("name")+" radius:"+carac.getAttribute("radius")+" seuil:"+carac.getAttribute("seuil")+" --> "+carac.getTextContent());
					}
					
					//les caractéristiques de la partie positionFloueTumeur
					final NodeList caracsPositionFloueTumeur = positionFloueTumeur.getElementsByTagName("*");
					for (int j = 0; j < caracsPositionFloueTumeur.getLength(); j++) {
						final Element carac = (Element)caracsPositionFloueTumeur.item(j);
						System.out.println(carac.getNodeName()+" seuilInf:"+carac.getAttribute("seuilInf")+" seuilSup:"+carac.getAttribute("seuilSup")+" degreMax:"+carac.getAttribute("degreMax")+" reference:"+carac.getAttribute("reference")+" --> "+carac.getTextContent());
					}
			}//fin cas
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	
	public static void main(String[] args) {
		LectureFichier l = new LectureFichier();
		//BaseDeCas base = l.LectureFichierBaseEnLigne("BaseDeCasEnLigne.txt");
		@SuppressWarnings("unused")
		BaseDeCas testXML = l.parserXML("BaseDeCas.xml");
	}
}

