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

import fr.insa.beuvron.web.amour.bdd.GestionBdD;
import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.VBox;

/**
 *
 * @author francois
 */
public class ListeDesUtilisateurs extends VBox {
    
    private VuePrincipale main;
    
    private UtilisateurTable vgAllUsers;

    public ListeDesUtilisateurs(VuePrincipale main) {
        this.main = main;
        this.getChildren().add(new BigLabel("liste de tous les utilisateurs",30));
        try {
            this.vgAllUsers = new UtilisateurTable(this.main,
                    GestionBdD.tousLesUtilisateurs(this.main.getSessionInfo().getConBdD()));
            this.getChildren().add(this.vgAllUsers);
        } catch (SQLException ex) {
            this.getChildren().add(new BigLabel("Problème BdD : " + ex.getLocalizedMessage(),30));
        }
    }
}
