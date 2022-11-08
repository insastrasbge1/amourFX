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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author francois
 */
public class LoginForm extends GridPane{
    
    private VuePrincipale main;
    
    private TextField vnom;
    private PasswordField vpass;
    private Button vbLogin;
    
    public LoginForm(VuePrincipale main) {
        this.main = main;
        this.vnom = new TextField();
        this.vpass = new PasswordField();
        this.vbLogin = new Button("login");
        int lig = 0;
        this.add(new Label("nom : "), 0, lig);
        this.add(this.vnom, 1, lig);
        lig ++;
        this.add(new Label("pass : "), 0, lig);
        this.add(this.vpass, 1, lig);
        lig ++;
        this.add(this.vbLogin, 0, lig,2,1);
        lig ++;
        this.vbLogin.setOnAction((event) -> {
            this.doLogin();
        });
    }
    
    public void doLogin() {
        String nom = this.vnom.getText();
        String pass = this.vpass.getText();
        try {
            Connection con = this.main.getSessionInfo().getConBdD();
            Optional<Utilisateur> user = GestionBdD.login(con, nom, pass);
            if(user.isEmpty()) {
                JavaFXUtils.showErrorInAlert("Utilisateur ou pass invalide");
            } else {
                this.main.getSessionInfo().setCurUser(user);
                this.main.setEntete(new EnteteAfterLogin(this.main));
                this.main.setMainContent(new MainAfterLogin(this.main));
            }
        } catch (SQLException ex) {
            JavaFXUtils.showErrorInAlert("Problème interne : " + ex.getLocalizedMessage());
        }        
    }
}
