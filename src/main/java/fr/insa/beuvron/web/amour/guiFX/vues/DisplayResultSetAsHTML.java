/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX.vues;

import fr.insa.beuvron.web.amour.bdd.SQLUtils;
import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

/**
 *
 * @author francois
 */
public class DisplayResultSetAsHTML extends ScrollPane {

    private VuePrincipale main;
    private WebView view;

    public DisplayResultSetAsHTML(VuePrincipale main) {
        this.main = main;
        this.view = new WebView();
//        this.view.getEngine().loadContent("<b> test html </b>");
        Connection con = this.main.getSessionInfo().getConBdD();
        try ( Statement st = con.createStatement();  ResultSet rs = st.executeQuery(
                "select * from fdbutilisateur")) {
            this.view.getEngine().loadContent(SQLUtils.formatResultSetAsHTMLTable(rs));
        } catch (SQLException ex) {
            this.view.getEngine().loadContent("<b> problem bdd : " + ex.getLocalizedMessage() + " </b>");

        }
        this.setContent(this.view);
    }

}
