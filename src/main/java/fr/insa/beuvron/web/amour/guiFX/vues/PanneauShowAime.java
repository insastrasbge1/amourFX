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
import fr.insa.beuvron.web.amour.guiFX.JavaFXUtils;
import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import fr.insa.beuvron.web.amour.model.Utilisateur;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author francois
 */
public class PanneauShowAime extends GridPane {

    private VuePrincipale main;

    public PanneauShowAime(VuePrincipale main) {
        this.main = main;
        this.getChildren().add(new Label("Note : normalement, on n'affiche pas les ids"));
        VBox vlAime = new VBox();
        vlAime.getChildren().add(new BigLabel("vous aimez",30));
        try {
            List<Utilisateur> datas = GestionBdD.quiSontAimesPar(
                    this.main.getSessionInfo().getConBdD(), this.main.getSessionInfo().getCurUser().orElseThrow());
            vlAime.getChildren().add(new UtilisateurTable(this.main,datas));
        } catch (SQLException ex) {
            vlAime.getChildren().add(new BigLabel("Probleme BdD : "+ex.getLocalizedMessage(),30));
        }
        JavaFXUtils.addSimpleBorder(vlAime, Color.GREEN, 2);
        this.add(vlAime,0,0);
        VBox vlAimePar = new VBox();
        vlAimePar.getChildren().add(new BigLabel("vous êtes aimé par",30));
        try {
            List<Utilisateur> datas = GestionBdD.quiAiment(
                    this.main.getSessionInfo().getConBdD(), this.main.getSessionInfo().getCurUser().orElseThrow());
            vlAimePar.getChildren().add(new UtilisateurTable(this.main,datas));
        } catch (SQLException ex) {
            vlAimePar.getChildren().add(new BigLabel("Probleme BdD : "+ex.getLocalizedMessage(),30));
        }
        JavaFXUtils.addSimpleBorder(vlAimePar, Color.GREEN, 2);
        this.add(vlAimePar,1,0);
        VBox vlPasAimes = new VBox();
        vlPasAimes.getChildren().add(new BigLabel("vous n'aimez pas",30));
        try {
            List<Utilisateur> datas = GestionBdD.quiNeSontPasAimesPar(
                    this.main.getSessionInfo().getConBdD(), this.main.getSessionInfo().getCurUser().orElseThrow());
            vlPasAimes.getChildren().add(new UtilisateurTable(this.main,datas));
        } catch (SQLException ex) {
            vlPasAimes.getChildren().add(new BigLabel("Probleme BdD : "+ex.getLocalizedMessage(),30));
        }
        JavaFXUtils.addSimpleBorder(vlPasAimes, Color.GREEN, 2);
        this.add(vlPasAimes,2,0);
        VBox vlAmis = new VBox();
        vlAmis.getChildren().add(new BigLabel("vos vrai amis",30));
        try {
            List<Utilisateur> datas = GestionBdD.vraiAmisDe(
                    this.main.getSessionInfo().getConBdD(), this.main.getSessionInfo().getCurUser().orElseThrow());
            vlAmis.getChildren().add(new UtilisateurTable(this.main,datas));
        } catch (SQLException ex) {
            vlAmis.getChildren().add(new BigLabel("Probleme BdD : "+ex.getLocalizedMessage(),30));
        }
        JavaFXUtils.addSimpleBorder(vlAmis, Color.GREEN, 2);
        this.add(vlAmis,3,0);
    }

}
