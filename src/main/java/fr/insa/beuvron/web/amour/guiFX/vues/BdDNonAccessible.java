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
package fr.insa.beuvron.web.amour.guiFX.vues;

import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import javafx.scene.layout.VBox;


/**
 *
 * @author francois
 */
public class BdDNonAccessible extends VBox {
    
    private VuePrincipale main;
    
    public BdDNonAccessible(VuePrincipale main) {
        this.main = main;
        this.getChildren().add(new BigLabel("Base de donnée non accessible",30));
        if (ConfigGenerale.AFFICHE_PANNEAU_CONNEXION_BDD) {
            this.getChildren().add(new DefConnectionBDD(main));
        }
    }
    
}
