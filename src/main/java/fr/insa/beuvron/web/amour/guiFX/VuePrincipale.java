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

import fr.insa.beuvron.web.amour.SessionInfo;
import fr.insa.beuvron.web.amour.bdd.GestionBdD;
import fr.insa.beuvron.web.amour.guiFX.vues.ConnectionBDDForm;
import fr.insa.beuvron.web.amour.guiFX.vues.EnteteInitialLogin;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

/**
 * vue principale.
 *
 * @author francois
 */
public class VuePrincipale extends BorderPane {

    private SessionInfo sessionInfo;
    private ScrollPane mainContent;

    public void setEntete(Node c) {
        this.setTop(c);
    }

    public void setMainContent(Node c) {
        this.mainContent.setContent(c);
    }

    public VuePrincipale() {
        this.sessionInfo = new SessionInfo();
        this.mainContent = new ScrollPane();
        JavaFXUtils.addSimpleBorder(this.mainContent);
        this.setCenter(this.mainContent);
        try {
             Connection con = GestionBdD.defautConnect();
             this.sessionInfo.setConBdD(con);
             this.setMainContent(new Label("Please login"));
        } catch (SQLException | ClassNotFoundException ex) {
            this.setMainContent(new ConnectionBDDForm(this));
        }
        this.setEntete(new EnteteInitialLogin(this));
    }

    /**
     * @return the sessionInfo
     */
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

}
