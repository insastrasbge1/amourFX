/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.beuvron.web.amour.guiFX;

/**
 * Un problème de gestion des modules (JDK > 9) empêche l'exécution directe de la classe Main dans un jar contenant toutes les dépendances.
 * Il faudrait utiliser jlink (ou javafx-maven-plugin qui utilise jlink) pour créer une application "standalone", mais ce n'est possible
 * que si toutes les dépendance sont effectivement des modules. Les vieilles librairies sous forme de simple .jar ne sont pas
 * compatibles.
 * Hors, de nombreuses anciennnes librairie ne sont pas présentées sous forme de module.
 * par exemple nous utilisons org.apache.commons.text.StringEscapeUtils. Même la dernière version
 * 1.10.0 sortie le 28/09/2022 sur maven central n'est toujours pas compatible avec le système des
 * module de java >=9.
 * On utilise donc le bon vieux maven-assembly-plugin, mais il faut que la classe principale 
 * soit la petite classe utilitaire ci-dessous qui évite un problème au lancement.
 * @author francois
 */
public class AppLauncher {
    public static void main(String[] args) {
        Main.main(args);
    }
    
}
