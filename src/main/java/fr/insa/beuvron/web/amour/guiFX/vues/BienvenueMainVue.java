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

import fr.insa.beuvron.web.amour.guiFX.JavaFXUtils;
import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


/**
 *
 * @author francois
 */
public class BienvenueMainVue extends VBox{
    
    private VuePrincipale main;
    
    public BienvenueMainVue(VuePrincipale main) {
        this.main = main;
        JavaFXUtils.addSimpleBorder(this, Color.GREEN, 2);
        this.setAlignment(Pos.CENTER);
        BigLabel entete = new BigLabel("bienvenu dans ce super programme",30);
        
        this.getChildren().add(entete);
        this.getChildren().add(new Label("merci de vous connecter"));
        TextArea taMoche = new TextArea("Cette interface en javaFX est la traduction quasi à l'identique\n"
                + "d'une interface web faite en vaadin\n"
                + "==> elle n'était déjà pas très belle en vaadin,\n"
                + "==> on pourrait faire moins moche et plus adapté à JavaFX");
        taMoche.setEditable(false);
        // indique que taMoche occupe tout l'espace horizontal de this
        taMoche.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(taMoche);
        if (ConfigGenerale.AFFICHE_RAZ_DATABASE) {
            this.getChildren().add(new InitOrResetDatabase(this.main));
        }
    }
    
}
