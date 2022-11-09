/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX.vues;

import fr.insa.beuvron.web.amour.bdd.GestionBdD;
import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


/**
 *
 * @author francois
 */
public class DefConnectionBDD extends GridPane{
    
    private VuePrincipale main;
    
    private SelectSGBDCombo sgbd;
    private TextField tfHost;
    private TextField tfPort;
    private TextField tfDatabase;
    private TextField tfUser;
    private PasswordField pfPass;
    private Button bConnect;
    
    public DefConnectionBDD(VuePrincipale main) {
        this.main = main;
        this.add(new Label("connection à la base de donnée"), 0, 0,2,1);
        int lig = 1;
        this.sgbd = new SelectSGBDCombo();
        this.add(new Label("sgbd"), 0, lig);
        this.add(this.sgbd, 1, lig);
        lig++;
        this.tfHost = new TextField("localhost");
        this.add(new Label("host"), 0, lig);
        this.add(this.tfHost, 1, lig);
        lig++;
        this.tfPort = new TextField("5439");
        this.add(new Label("port"), 0, lig);
        this.add(this.tfPort, 1, lig);
        lig++;
        this.tfDatabase = new TextField("postgres");
        this.add(new Label("database"), 0, lig);
        this.add(this.tfDatabase, 1, lig);
        lig++;
        this.tfUser = new TextField("postgres");
        this.add(new Label("user"), 0, lig);
        this.add(this.tfUser, 1, lig);
        lig++;
        this.pfPass = new PasswordField();
        this.add(new Label("password"), 0, lig);
        this.add(this.pfPass, 1, lig);
        lig++;
        this.bConnect = new Button("connection");
        this.bConnect.setOnAction((t) -> {
            try {
                int port = Integer.parseInt(this.tfPort.getText());
                Connection con;
                if (this.sgbd.selectedSGBD() == GestionBdD.PostgresqlSGBD) {
                con = GestionBdD.connectGeneralPostGres(this.tfHost.getText(),
                        port,
                        this.tfDatabase.getText(),
                        this.tfUser.getText(),
                        this.pfPass.getText());
                } else {
                con = GestionBdD.connectGeneralMySQL(this.tfHost.getText(),
                        port,
                        this.tfDatabase.getText(),
                        this.tfUser.getText(),
                        this.pfPass.getText());   
                }
                this.main.getSessionInfo().setConBdD(con);
                // TODO after connect
                this.main.setEntete(new EnteteInitialLogin(this.main));
                this.main.setMainContent(new BienvenueMainVue(main));
                
           }
            catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur entrée");
                alert.setHeaderText("je ne peux pas convertir le port " + this.tfPort.getText() + " en entier");
                alert.showAndWait();
            } catch (ClassNotFoundException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur BdD");
                alert.setHeaderText("je ne trouve pas le driver pour postgresql");
                alert.setContentText("il doit manquer une dépandance dans le pom");
                alert.showAndWait();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur BdD");
                alert.setHeaderText("La tentative de connection a échouée");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        });
        this.add(this.bConnect, 0, lig,2,1);
       
    }
    
}
