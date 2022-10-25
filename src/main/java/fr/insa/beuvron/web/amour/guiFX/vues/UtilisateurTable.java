/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX.vues;

import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import fr.insa.beuvron.web.amour.model.Utilisateur;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Table pour afficher des utilisateurs.
 * voir https://devstory.net/11079/javafx-tableview
 * @author francois
 */
public class UtilisateurTable extends TableView {
    
    private VuePrincipale main;
    
    public UtilisateurTable(VuePrincipale main,List<Utilisateur> utilisateurs) {
        this.main = main;
        
        TableColumn<Utilisateur,String> cNom = 
                new TableColumn<>("nom");
        TableColumn<Utilisateur,String> cRole = 
                new TableColumn<>("role");
        this.getColumns().addAll(cNom,cRole);
        
        // si l'on ne veut pas d'espace supplémentaire
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        cNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        cRole.setCellValueFactory(new PropertyValueFactory<>("nomRole"));
        ObservableList<Utilisateur> olu = FXCollections.observableArrayList(utilisateurs);
        this.setItems(olu);
    }
    
}
