/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oussama;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Oussama
 */
public class Plateau {
    
    private int  libre =0;
    private int couleurBlanc = 2;
    private int couleurNoir = 1;
    private int prisParBlanc = 3;
    private int prisParNoir = 4;
    private int horsPlateau = -1;
    private int plateau [][]= new int [10][10];
   
    // intialise tous les compteurs des pions sur le plateau
    int nbPionsNoir=0;
    int nbPionsBlanc=0;
   
    //MEMORISE LES Coups possibles
    boolean coupPossibles[][]= new boolean[10][10];
    private int joueurEnCours=1;
   
    // MEMORISE les case prises
    private int lignePris[] = new int [32];
    private int colonnePris[] = new int [32];
    private int lignePrisTest[] = new int [32];
    private int colonnePrisTest[] = new int [32];
    private int numeroCases[][]= new int [10][10];
    private int colonneCases[] = new int [64];
    private int ligneCases[] = new int [64];
   
    protected int caseChoisie; 	// récupère grâce au clic du joueur sur une case
    
		// niveau de jeu
	protected int niveauJeu = 1;
        boolean trouve;
        
        	/** CONSTANTE  EVALUATION  MIN-MAX   */
	final int MINEVAL= -100000;
	final int MAXEVAL= 100000;
        
        // points des différents case pour le choix de la case par l'IA
	protected int[][] pointCase;
	
    
    
    
    public void initialiserPlateau (){
        for (int i=0;i<10;i++ ){
            for (int j=0;j<10;j++ ){
            plateau[i][0]=9;
            plateau[i][9]=9;
            plateau[0][j]=9;
            plateau[9][j]=9;
        }}
        
        // declare toutes les cases libre
        for( int i=1; i<9; i++)
        for (int j=1;j<9;j++ ){
            plateau[i][j]=0;
            // positionne les 4 pions de depart
            plateau[4][4]=2;
            plateau[4][5]=1;
            plateau[5][4]=1;
            plateau[5][5]=2;
            
           
        }
        // initialise les compteurs des pions
        nbPionsBlanc = 2;
        nbPionsNoir = 2;
    }
    // constructeurs
    public Plateau(){
        initialiserPlateau();
    }
    public void changeTourJoueur(){
		if (joueurEnCours == 1 ) joueurEnCours = 2;
		else joueurEnCours = 1;
	}
    public Plateau (int tab[][],int joueur){
        this.plateau=tab;
        this.joueurEnCours=joueur;
      
    }
    public boolean placementCorrect(int ligne, int colonne, int joueur, int adversaire, boolean joueLeCoup){	
        int i, j;
        int nbEtape;
        boolean correct = false;

                // si la case est libre
        if (plateau[ligne][colonne] == 0){
            int indice = 0;
            for (int a = -1; a < 2; a++)		// permet de savoir dans qu'elle direction on va
                for (int b = -1; b < 2; b++){	// par rapport à la case choisie{
                    nbEtape = 0;				// indique le nombre de pions adverses trouvés
                    do   {		
                        nbEtape++;
                        i = ligne + nbEtape*a;
                        j = colonne + nbEtape*b;							
                        // permet de voir s'il y a un autre pion
                        // d'une autre couleur
                    }while ( (i > 0 ) && (i < 9) && (j > 0) && (j < 9) &&(plateau[i][j] == adversaire));  

                                        // si au moins un pion d'une autre couleur est trouvé 
                                        // et qu'on se trouve dans le plateau 
                                        // et qu'un pion de même couleur est trouvé 
            if (( i > 0 ) && (i < 9)  && (j > 0) && (j < 9) && (nbEtape > 1)&& (plateau[i][j] == joueur)){
                 correct = true;

        // mémorise les coordonnées des pions pris
		for (int k = 1; k < nbEtape; k++){
		lignePrisTest[indice]= ligne + a*k;
		colonnePrisTest[indice] = colonne + b*k;
		  indice++;
		}
						
		// si le coup est vraiment joué, on exécute cette boucle
		if (joueLeCoup){	
	        for (int m = 1; m < nbEtape; m++){
				// enregistre les cases prises
		lignePris[indice]= ligne + a*m;
		colonnePris[indice] = colonne + b*m;
		indice++;
		// modifie la couleur des pions pris
		if (joueur == 2)
		plateau[ligne + a*m][colonne + b*m] = prisParNoir;
                else if ( joueur == 1 ) 
		plateau[ligne + a*m][colonne + b*m] = prisParBlanc;
		}
                // la case choisie prend la couleur du joueur en cours
		plateau[ligne][colonne] = joueur;		
		   }
	          }
		 } 
		}
		return correct;
	}
    public int getScore(int unJoueur){
		if (unJoueur == 1) return nbPionsNoir;
		else return nbPionsBlanc;
	}
    public int joueurEnCours()
	{
		return joueurEnCours;
	}
    public int getJoueurEnCours(){
        return joueurEnCours;
    }
    public int getLigne (int uneCase){
        return ligneCases[uneCase];
    }
    public int getColonne (int uneCase){
        return colonneCases[uneCase];
   
    }
    public void jouerUnCoup(int ligne, int colonne){			
		//int ligne = getLigne(unCase);		// lit la ligne de la case choisie
		//int colonne = getColonne(unCase);	// lit la colonne  de la case choisie
		int couleur = 0;			// couleur du joueur en cours
		int autreCouleur = 0;	

			// récupère la couleur du joueur en cours et celle de l'autre joueur
		if ( getJoueurEnCours() == 1) 
		{	couleur = couleurNoir;
			autreCouleur = couleurBlanc;
		}
		else if ( getJoueurEnCours() == 2) 
		{ 	couleur= couleurBlanc;
			autreCouleur = couleurNoir; 
		}

			// si le coup est correct
		if (placementCorrect(ligne, colonne, couleur, autreCouleur, true)){		
				// fin du tour du joueur : changement de joueur
			changeTourJoueur();
				// remet à zéro le tableau des cases pris.
			reinitialiseCasesPrises();
				// calcul du score 
			calculScore();
				// si un des 2 joueurs ne peut jouer on passe le tour 
			if ((nbPionsBlanc + nbPionsNoir) < 64) 
			{
				if (jblancPeutPlusJouer() == true) joueurEnCours = 1;
				if (jnoirPeutPlusJouer() == true) joueurEnCours = 2;
			} 
		}
	}
    // vérifie si le joueur blanc ne peut plus jouer
	public boolean jblancPeutPlusJouer(){
		boolean jblancPeutPlusJouer = true;
		
		for (int y = 1; y < 9; y++ )
	        for (int z = 1; z < 9; z++ )		
		    if (placementCorrect(y, z, couleurBlanc, couleurNoir, false))
			jblancPeutPlusJouer = false;
					
		return jblancPeutPlusJouer;
	}
        public boolean jnoirPeutPlusJouer(){
		boolean jnoirPeutPlusJouer = true;
		
		for (int y = 1; y < 9; y++ )
			for (int z = 1; z < 9; z++ )		
					if (placementCorrect(y, z, couleurNoir, couleurBlanc, false))
						jnoirPeutPlusJouer = false;
					
		return jnoirPeutPlusJouer;
	}
    public int getNumeroCase(int ligne,int colonne){
        return numeroCases[ligne][colonne];
    }
    public boolean partieEstFinie()
	{
		boolean partieFinie = true;
		
		for (int y = 1; y < 9; y++ )
			for (int z = 1; z < 9; z++ )
					if ((placementCorrect(y, z, couleurBlanc, couleurNoir, false) ) || 
						(placementCorrect(y, z, couleurNoir, couleurBlanc, false)) )
							partieFinie = false;
		
		return partieFinie;
	}
    
    public boolean [][] getCoupsPossibles() {
        return coupPossibles;
    }
    public void coupsPossibles(){
        // pour le joueur noir
        if (getJoueurEnCours() == 1){
            for (int i = 1 ; i < 9; i++)
                for (int j = 1; j < 9 ; j++)
                    if (placementCorrect(i, j, couleurNoir, couleurBlanc, false))
                       coupPossibles[i][j] = true;
        }

                // pour le joueur blanc
        else if (getJoueurEnCours() == 2){
                for (int m = 1 ; m < 9; m++)
                        for (int n = 1; n < 9 ; n++)
                                if (placementCorrect(m, n,couleurBlanc, couleurNoir, false))
                                        coupPossibles [m][n] = true;
        }
    }
     private void reinitialiseCasesPrises() {
       for (int i=0; i<32 ; i++){
           lignePris[i]=0;
           colonnePris[i]=0;
       }
    }
  public int getNbPionsNoir (){
      return nbPionsNoir;
  }
   public int getNbPionsBlanc (){
      return nbPionsBlanc;
  }
   
    // calcul du nombre de pions de chaque joueur
	public void calculScore(){
		nbPionsBlanc = 0;
		nbPionsNoir = 0;
		updatePlateau();
		for (int i = 1; i < 9; i++)
			for (int j = 1; j < 9; j++){
				if (plateau[i][j] == couleurNoir )
					nbPionsNoir++;
                                if (plateau[i][j] == couleurBlanc )
					nbPionsBlanc++;
			}
        }
        

    public void remiseAZeroCoupPossibles(){
        for ( int i=1;i<9;i++)
            for ( int j=1;j<9;j++)
                coupPossibles[i][j]=false;
    }
public int [][] getPlateau () {
    return plateau;
}
public void updatePlateau (){
    for(int i=0; i<10;i++){
        for(int j=0;j<10;j++){
            if(plateau[i][j]==1 || plateau[i][j]==3)
                plateau[i][j]= 1;
            else if (plateau[i][j]==2 || plateau[i][j]==4)
                plateau[i][j]=2;
    }}
}
//TEST
    public void affichePlateau( ){
        for (int i=0;i<10;i++ ){
            for (int j=0;j<10;j++ )
               System.out.print(plateau[i][j] + "   "     );
             System.out.println();
             
        }
    }
       public void afficheScore(){
        getScore(couleurNoir);
               System.out.print("le nombre de pions noir est : " +nbPionsNoir);
               System.out.println();
                getScore(couleurBlanc);
                  System.out.print("le nombre de pions blanc est : "+nbPionsBlanc);             
					
             
        }
    
 //Affichage des coups possibles
    public void afficheCoupPossible() {
        for (int i=0;i<10;i++ ){
            for (int j=0;j<10;j++ )
               System.out.print(coupPossibles[i][j] + "   "     );
             System.out.println();
             
        }
    
    
    
    }
    public int IAchoix(){
		trouve = false;
		int caseChoisie = -1;

				
		if (niveauJeu == 1)
			caseChoisie = niveau1();	
			
                return caseChoisie;		
	}
    public int niveau1(){
                int aleatoire = -1;
		int choixCase = -1;
		
		
		// selon la valeur, la recherche commencera d'un coté ou d'un autre
		while ( (aleatoire < 1 ) || (aleatoire >= 5) )
				aleatoire = (int) (Math.random()*10) + 1;
		
		if (aleatoire == 1) {
			while (choixCase == -1)
				for (int i = 1; i < 9; i++)
					for (int j = 1; j < 9; j++)
						if (placementCorrect(i, j, couleurBlanc, couleurNoir, false))
							choixCase = getNumeroCase(i, j); }
		
		else if (aleatoire == 2) {
			while (choixCase == -1)
				for (int i= 1; i < 9; i++)
					for (int j = 8; j > 0; j--)
						if (placementCorrect(i, j, couleurBlanc, couleurNoir, false))
							choixCase = getNumeroCase(i, j); }
		

		else if (aleatoire == 3) {
			while (choixCase == -1)
				for (int i = 8; i > 0; i--)
					for (int j = 1; j < 9; j++)
						if (placementCorrect(i, j, couleurBlanc, couleurNoir, false))
							choixCase = getNumeroCase(i, j); }
		

		else if (aleatoire == 4) {
			while (choixCase == -1)
				for (int i = 8; i> 0; i--)
					for (int j = 8; j > 0; j--)
						if (placementCorrect(i, j, couleurBlanc, couleurNoir, false))
							choixCase = getNumeroCase(i, j); }
		
		return choixCase;
	}
    public  void initialiseTableauPoint(){
			// mémorise le nombre de points de chaque case.
		
		pointCase = new int[][]{
						{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				
						{-1, 1000, 100, 700, 400, 400, 700, 100, 1000, -1},
						
						{-1, 100, 0, 310, 310, 310, 310, 0, 100, -1},
						
						{-1, 700, 310, 350, 325, 325, 350, 310, 700, -1},
						
						{-1, 400, 310, 325, 500, 500, 325, 310, 400, -1},
						
						{-1, 400, 310, 325, 500, 500, 325, 310, 400, -1},
						
						{-1, 700, 310, 350, 325, 325, 350, 310, 700, -1},
						
						{-1, 100, 0, 310, 310, 310, 310, 0, 100, -1},
						
						{-1, 1000, 100, 700, 400, 400, 700, 100, 1000, -1},
						
						{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
		            };
	}
    public void modifiePoint(){
		if (joueurEnCours() == 1){
			if (plateau[1][1] == couleurNoir){
				pointCase[1][2] = 1000;
				pointCase[2][1] = 1000;
				pointCase[2][2] = 1000;
			}
			if (plateau[1][8] == couleurNoir){
				pointCase[1][7] = 1000;
				pointCase[2][8] = 1000;
				pointCase[2][7] = 1000;
			}
			if (plateau[8][1] == couleurNoir){
				pointCase[7][1] = 1000;
				pointCase[7][2] = 1000;
				pointCase[8][2] = 1000;
			}
			if (plateau[8][8] == couleurNoir)
			{
				pointCase[7][8] = 1000;
				pointCase[7][7] = 1000;
				pointCase[8][7] = 1000;
			}
		}
		else if (joueurEnCours() == 2)
		{
			if (plateau[1][1] == couleurBlanc)
			{
				pointCase[1][2] = 1000;
				pointCase[2][1] = 1000;
				pointCase[2][2] = 1000;
			}
			if (plateau[1][8] == couleurBlanc)
			{
				pointCase[1][7] = 1000;
				pointCase[2][8] = 1000;
				pointCase[2][7] = 1000;
			}
			if (plateau[8][1] == couleurBlanc)
			{
				pointCase[7][1] = 1000;
				pointCase[7][2] = 1000;
				pointCase[8][2] = 1000;
			}
			if (plateau[8][8] == couleurBlanc)
			{
				pointCase[7][8] = 1000;
				pointCase[7][7] = 1000;
				pointCase[8][7] = 1000;
			}
		}
	}
    /** Gestion  recherche  MINMAX  */
	int compteur;
        public int calcIA(int prof) 
	{
	 compteur = 0;
         int i,j,tmp;
	 int max = MINEVAL;
	 int maxi=-1, maxj=-1;
	 int couleur = 0;
         int autreCouleur = 0;
        
         if (joueurEnCours() == 1)
		{
			couleur = couleurNoir;
			autreCouleur = couleurBlanc;
		}
		else if (joueurEnCours() == 2)
		{
			couleur = couleurBlanc;
			autreCouleur = couleurNoir;
		}
         /* teste si une des cases en coins est jouable
			 si c'est le cas, on le joue*/
		
                if (placementCorrect(1, 1, couleurBlanc, couleurNoir, false))
			return getNumeroCase(1, 1);

		if (placementCorrect(8, 8, couleurBlanc, couleurNoir, false))
			return getNumeroCase(8, 8);

		if (placementCorrect(1, 8, couleurBlanc, couleurNoir, false))
			return getNumeroCase(1, 8);

		if (placementCorrect(8, 1, couleurBlanc, couleurNoir, false))
			return getNumeroCase(8, 1);
//Si la profondeur est nulle ou la partie est finie,
	    	//on ne fait pas le calcul
	    if((prof > 0) && (nbPionsBlanc + nbPionsNoir) < 64){
	        //On parcourt les cases du morpion
	        for(i=1; i<9; i++)
	            for(j=1; j<9;j++)
	                {
                             //Si la case est vide
	                if (placementCorrect(i, j, couleur, autreCouleur, false)){
	             //On simule qu'on joue cette case
	                    jouerUnCoup(i,j);
	                    	//On appelle la fonction calcMin pour lancer l'IA
	                    tmp = calcMin(prof-1);
	                    	//Si ce score est plus grand
	                    if (tmp >= max)
	                        {
	                        //On le choisit
	                        max = tmp;
	                        maxi = i;
	                        maxj = j;
	                        }
	                    
	                    }
	                }
	        }
	    	//on retourne la meilleure case
	    return getNumeroCase(maxi, maxj);
		
                        }
        	public int calcMax(int prof) {
		compteur++;
	    int i,j,tmp;
	    int max = MINEVAL;
	    int couleur = 0;
		int autreCouleur = 0;
		boolean nePeutPasJouer = true;
		System.out.println("nbCoup : "+compteur);
		
		if (joueurEnCours() == 1)
		{
			couleur = couleurNoir;
			autreCouleur = couleurBlanc;
		}
		else if (joueurEnCours() == 2)
		{
			couleur = couleurBlanc;
			autreCouleur = couleurNoir;
		}

	    //Si on est à la profondeur voulue ou que la partie est finie, on retourne l'évaluation
	    if((prof==0) || (nbPionsBlanc + nbPionsNoir) >= 64)
	        return evalue();

	    	//On parcourt le plateau de jeu et on le joue si la case est vide
	    for(i = 1; i < 9; i++)
	        for(j = 1; j < 9; j++)
	        {
	            if(placementCorrect(i, j, couleur, autreCouleur, false)){
	            	
	                //jouerUnCoup(getNumeroCase(i, j));
	                tmp = calcMin(prof-1);
	                if(tmp > max)
	                {
	                    max = tmp;
	                }
	                
	                
	             }
	        }
	    if  (nePeutPasJouer) changeTourJoueur();
	    
	    return max;
	}
public int calcMin(int prof) 
	{
		compteur++;
		int i,j,tmp;
	    int min = MAXEVAL;
	    int couleur = 0;
		int autreCouleur = 0;
		boolean nePeutPasJouer = true;
		System.out.println("nbCoup : "+compteur);
		
		if (joueurEnCours() == 1)
		{
			couleur = couleurNoir;
			autreCouleur = couleurBlanc;
		}
		else if (joueurEnCours() == 2)
		{
			couleur = couleurBlanc;
			autreCouleur = couleurNoir;
		}

		 	//Si on est à la profondeur voulue ou que la partie est finie, on retourne l'évaluation
	    if((prof==0) || (nbPionsBlanc + nbPionsNoir) >= 64)
	        return evalue();

	    	//On parcourt le plateau de jeu et on le joue si la case est vide
	    for(i = 1; i < 9; i++)
	        for(j = 1; j < 9; j++){
	            if(placementCorrect(i, j, couleur, autreCouleur, false)){
	            	
	               // jouerUnCoup(getNumeroCase(i, j));
	                tmp = calcMax(prof-1);
	                if(tmp < min)
	                {
	                    min = tmp;
	                }
	                
	              }
	          }
	    if  (nePeutPasJouer) changeTourJoueur();
	    
	    return min;
	}
       public int evalue(){
		int score = 0;
		calculScore();
		
			// si la partie est finie
		if ((nbPionsBlanc + nbPionsNoir) >= 64){
			if (nbPionsBlanc > nbPionsNoir ) return score =  100000 - (nbPionsBlanc - nbPionsNoir);
			else if (nbPionsBlanc < nbPionsNoir) return score =  -100000 + (nbPionsNoir - nbPionsBlanc);
			else  return score = 0;
		}
		
			// sinon on met à jour le tableau des points puis on fait le compte
		modifiePoint();		
		for (int i = 1; i < 9; i++)
			for (int j = 1; j < 9; j++)
			{
				if (plateau[i][j] == couleurNoir || plateau[i][j] == prisParNoir)
					score -= pointCase[i][j];
				else if (plateau[i][j] == couleurBlanc || plateau[i][j] == prisParBlanc)
					score += pointCase[i][j];
			}
		
			// si blanc possède les cases du bord
		for (int i = 1; i < 9; i++)
			if (plateau[i][1] == couleurBlanc ||  plateau[i][1] == prisParBlanc)
				score += 1000;
		for (int i = 1; i < 9; i++)
			if (plateau[i][8] == couleurBlanc ||  plateau[i][1] == prisParBlanc)
				score += 1000;
		for (int i = 1; i < 9; i++)
			if (plateau[1][i] == couleurBlanc ||  plateau[i][1] == prisParBlanc)
				score += 1000;
		for (int i = 1; i < 9; i++)
			if (plateau[8][i] == couleurBlanc ||  plateau[i][1] == prisParBlanc)
				score += 1000;
		
		/**
			// 3ème et 6ème ligne et 3ème et 6ème colonne
		for (int i = 3; i < 7; i++)
			if (casesPlateau[i][1] == blanc ||  casesPlateau[i][1] == prisParBlanc)
				score += 1000;
		for (int i = 3; i < 7; i++)
			if (casesPlateau[i][8] == blanc ||  casesPlateau[i][1] == prisParBlanc)
				score += 1000;
		for (int i = 3; i < 7; i++)
			if (casesPlateau[1][i] == blanc ||  casesPlateau[i][1] == prisParBlanc)
				score += 1000;
		for (int i = 3; i < 7; i++)
			if (casesPlateau[8][i] == blanc ||  casesPlateau[i][1] == prisParBlanc)
				score += 1000;
		*/
		
			
			// si noir a des coins on diminu le score
		if (plateau[1][1] == couleurNoir)
			score -= 5000;
		if (plateau[1][8] == couleurNoir)
			score -= 5000;
		if (plateau[8][1] == couleurNoir)
			score -= 5000;
		if (plateau[8][8] == couleurNoir)
			score -= 5000;
		
			// si c'est lIA qui les a on augemente le score
		if (plateau[1][1] == couleurBlanc)
			score += 5000;
		if (plateau[1][8] == couleurBlanc)
			score += 5000;
		if (plateau[8][1] == couleurBlanc)
			score += 5000;
		if (plateau[8][8] == couleurBlanc)
			score += 5000;
		
		
			// si noir possède les coins au centre
		if (plateau[3][3] == couleurNoir)
			score -= 2500;
		if (plateau[3][6] == couleurNoir)
			score -= 2500;
		if (plateau[6][3] == couleurNoir)
			score -= 2500;
		if (plateau[6][6] == couleurNoir)
			score -= 2500;
		
		// si blanc possède les coins au centre
		if (plateau[3][3] == couleurBlanc)
			score += 2500;
		if (plateau[3][6] == couleurBlanc)
			score += 2500;
		if (plateau[6][3] == couleurBlanc)
			score += 2500;
		if (plateau[6][6] == couleurBlanc)
			score += 2500;
		
		
		
		
			// si  noir possède les 3 cases en coins
		if (pointCase[1][1] == couleurNoir && ( pointCase[1][2] == couleurNoir || pointCase[1][2] == prisParNoir) 	
				&& (pointCase[2][1] == couleurNoir || pointCase[2][1] == prisParNoir ))
			score -= 10000;
		
		if (plateau[1][8] == couleurNoir && (pointCase[1][7] == couleurNoir || pointCase[1][7] == prisParNoir) 
				&& (pointCase[2][8] == couleurNoir || pointCase[2][8] == prisParNoir));
			score -= 10000;
			
		if (plateau[8][1] == couleurNoir && (pointCase[7][1] == couleurNoir || pointCase[7][1] == prisParNoir )
				&& (pointCase[7][2] == couleurNoir || pointCase[7][2] == prisParNoir))
			score -= 10000;
		
		if (plateau[8][8] == couleurNoir&& (pointCase[7][8] == couleurNoir || pointCase[7][8] == prisParNoir) 
				&& (pointCase[7][7] == couleurNoir || pointCase[7][7] == prisParNoir ));
			score -= 10000;
			
			
			
			
			// si noir les 4 cases de coins 
		if (pointCase[1][1] == couleurNoir && ( pointCase[1][2] == couleurNoir || pointCase[1][2] == prisParNoir) 	
				&& (pointCase[2][1] == couleurNoir || pointCase[2][1] == prisParNoir )
				&& (pointCase[2][2] == couleurNoir || pointCase[2][2] == prisParNoir))
			score -= 15000;
		
		if (plateau[1][8] == couleurNoir && (pointCase[1][7] == couleurNoir || pointCase[1][7] == prisParNoir) 
				&& (pointCase[2][8] == couleurNoir|| pointCase[2][8] == prisParNoir)
				&& (pointCase[2][7] == couleurNoir || pointCase[2][7] == prisParNoir));
			score -= 15000;
			
		if (plateau[8][1] == couleurNoir && (pointCase[7][1] == couleurNoir|| pointCase[7][1] == prisParNoir )
				&& (pointCase[7][2] == couleurNoir || pointCase[7][2] == prisParNoir)
				&& (pointCase[7][2] == couleurNoir || pointCase[7][2] == prisParNoir) )
			score -= 15000;
		
		if (plateau[8][8] == couleurNoir && (pointCase[7][8] == couleurNoir || pointCase[7][8] == prisParNoir) 
				&& (pointCase[7][7] == couleurNoir || pointCase[7][7] == prisParNoir ) 
				&& (pointCase[7][7] == couleurNoir || pointCase[7][7] == prisParNoir));
			score -= 15000;
			
			
			
			// si  blanc possède les 3 cases en coins
		if (pointCase[1][1] == couleurBlanc && ( pointCase[1][2] == couleurBlanc || pointCase[1][2] == prisParBlanc) 	
				&& (pointCase[2][1] == couleurBlanc || pointCase[2][1] == prisParBlanc ))
			score += 10000;
		
		if (plateau[1][8] == couleurBlanc&& (pointCase[1][7] == couleurBlanc || pointCase[1][7] == prisParBlanc) 
				&& (pointCase[2][8] == couleurBlanc || pointCase[2][8] == prisParBlanc));
			score += 10000;
			
		if (plateau[8][1] == couleurBlanc && (pointCase[7][1] == couleurBlanc|| pointCase[7][1] == prisParBlanc )
				&& (pointCase[7][2] == couleurBlanc || pointCase[7][2] == prisParBlanc) )
			score += 10000;
		
		if (plateau[8][8] == couleurBlanc&& (pointCase[7][8] == couleurBlanc || pointCase[7][8] == prisParBlanc) 
				&& (pointCase[7][7] == couleurBlanc|| pointCase[7][7] == prisParBlanc ));
			score += 10000;
				
				
			// si blanc les 4 cases de coins 
		if (pointCase[1][1] == couleurBlanc && ( pointCase[1][2] == couleurBlanc || pointCase[1][2] == prisParBlanc) 	
				&& (pointCase[2][1] == couleurBlanc || pointCase[2][1] == prisParBlanc )
				&& (pointCase[2][2] == couleurBlanc || pointCase[2][2] == prisParBlanc))
			score += 15000;
		
		if (plateau[1][8] == couleurBlanc && (pointCase[1][7] == couleurBlanc || pointCase[1][7] == prisParBlanc) 
				&& (pointCase[2][8] == couleurBlanc|| pointCase[2][8] == prisParBlanc)
				&& (pointCase[2][7] == couleurBlanc || pointCase[2][7] == prisParBlanc));
			score += 15000;
			
		if (plateau[8][1] == couleurBlanc && (pointCase[7][1] ==couleurBlanc || pointCase[7][1] == prisParBlanc )
				&& (pointCase[7][2] == couleurBlanc || pointCase[7][2] == prisParBlanc)
				&& (pointCase[7][2] == couleurBlanc || pointCase[7][2] == prisParBlanc) )
			score += 15000;
		
		if (plateau[8][8] == couleurBlanc && (pointCase[7][8] == couleurBlanc || pointCase[7][8] == prisParBlanc) 
				&& (pointCase[7][7] == couleurBlanc || pointCase[7][7] == prisParBlanc ) 
				&& (pointCase[7][7] == couleurBlanc || pointCase[7][7] == prisParBlanc));
			score += 15000;

		
			// réinitialise le tableau des points
		initialiseTableauPoint();
		
		
		
		return score;
	}	
}
	
        
    

    
    

   

