
//Algorithme de RaPC : 
//Patron de code Java pour essayer de se rendre compte des composantes d'un RaPC au niveau implémentation


/** Class Modele **/


//RaPC pour chaque entrée
public void RaPC(){

	//récupération de la base de cas
	Base b = getCasesBase();

	//pour chaque entrée
	for(int i = 0; i<entrees.size(); i++){
		//on part du principe que tout est déjà normalisé
		Case current = entrees.get(i);

		//pour chaque classe de la base
		// récupération de la classe avec la plus grande similitude
		double simMin = Integer.MAX_VALUE;
		Classe best = null;
		for(int j = 0 ; j< b.classes.size(); j++){
			Classe reference = b.classes.get(j);
			double sim = distance(current.pb, reference.moyPb); //mesure de similitude entre deux cas
			if(sim < simMin) {
				simMin = sim;
				best = reference;
			}
		}

		//exécution de l'algorithme de segmentation par croissance de régions
		ArrayList<double> solutionRetenue = best.moySol; //on récupère la solution moyenne la meilleure
		//NORMALEMENT ICI IL Y A UNE PHASE D'ADAPTATION, on ne se contente pas de recopier la moyenne solution
		current.sol = solutionRetenue; // on la définit comme étant la solution du cas courant
		boolean isSatisfying = segmentation(current); //segmentation, avec en retour une mesure de satisfaction


		// si la segmentation est satisfaisante, on l'ajoute dans la base de cas 
		//--> voir l'article Philipp Foliguet, Guingues. "Evaluation de la segmentation d’images : état de l’art, nouveaux indices et comparaisons 2005"
		if(isSatisfying){
			best.ajouterCas(current);
			majMoyennePb(); // il faut aussi mettre à jour les moyennes de la classe dans laquelle on a ajouté le cas courant
			majMoyenneSol();
		}else{
		//trouver une moyen de modifier la solution du cas courant et re-segmenter ?
		}
	}
}

//calcul de distance entre deux vecteurs de caractéristiques numériques normalisées
public double distance(ArrayList<double> a, ArrayList<double> b){
	//reste à définir quel type de fonction de distance utiliser
}


public boolean segmentation(Case c){
	//on peut peut-être ré-utiliser les méthodes du projet de Boissard ?
}