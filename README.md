# projet-pandemiage-vong_touahri_mahdavi
projet-pandemiage-vong_touahri_mahdavi created by GitHub Classroom

Étudiants : 
- Paul VONG
- Dyhia TOUAHRI
- Shahram MAHDAVI

Pour compiler le projet, taper les commandes suivantes :
- mvn compile
- mvn package

Pour exécuter le projet, taper les commandes suivantes :
java -jar projet.jar -a JARFILE -d DIFFICULTY -g CITYGRAPH -t TURNDURATION -s HANDSIZE -g

où :

1. projet.jar est le nom du jar contenant le moteur du jeu.

2. JARFILE est le chemin vers un fichier jar contenant au minimum une classe qui implémente l’interface fr.dauphine.ja.pandemiage.common.AiInterface et un fichier MANIFEST contentant la propriété AI-Class qui permet de spécifier le nom de la classe qui implémente AiInterface. Le fichier pom.xml fourni peut être utilisé pour générer automatiquement ce fichier jar. Valeur par défaut : target/pandemiage-1.0-SNAPSHOT-ai.jar.

3. DIFFICULTY est le niveau de difficulté 0 : partie d’introduction, 1 : partie normale, 2 : partie héroïque. (Voir règles du jeu.) Valeur par défaut 0.

4. CITYGRAPH est le nom du fichier contenant le graphe des villes (voir plus loin). Valeur par défaut pandemic.graphml.

5. TURNDURATION est un nombre de secondes qui représente le temps alloué à chaque joueur pour prendre sa décision. (Voir Section 
4.2.2.) Valeur par défaut 1 seconde.

6. HANDSIZE est le nombre maximum de cartes qu’un joueur peut garder dans sa main. Valeur par défaut 9.

7. L'option -g permet d'exécuter le projet avec l'interface graphique.
