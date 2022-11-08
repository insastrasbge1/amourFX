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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;


/**
 *
 * @author francois
 */
public class MainAfterLogin extends VBox {

    private VuePrincipale main;

    private Tab aimeCurrentUser;
    private Tab changeAime;
    private TabPane allTabs;

    public MainAfterLogin(VuePrincipale main) {
        this.main = main;

        this.aimeCurrentUser = new Tab("Amours de "
                + this.main.getSessionInfo().getUserName());
        this.aimeCurrentUser.setOnSelectionChanged((t) -> {
            this.aimeCurrentUser.setContent(new PanneauShowAime(this.main));
        });
        this.aimeCurrentUser.setContent(new PanneauShowAime(this.main));
        this.changeAime = new Tab("modifier");
        this.changeAime.setOnSelectionChanged((t) -> {
            this.changeAime.setContent(new PanneauModifyAime(this.main));
        });
        this.changeAime.setContent(new PanneauModifyAime(this.main));
        this.allTabs = new TabPane(this.aimeCurrentUser, this.changeAime);
        this.getChildren().addAll(this.allTabs);
        this.allTabs.getSelectionModel().select(this.aimeCurrentUser);
     }


}
