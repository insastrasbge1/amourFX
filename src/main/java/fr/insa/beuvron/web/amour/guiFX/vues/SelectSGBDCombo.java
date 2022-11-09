/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX.vues;

import fr.insa.beuvron.web.amour.bdd.GestionBdD;
import fr.insa.beuvron.web.amour.bdd.GestionBdD.SGBD;
import fr.insa.beuvron.web.amour.model.Role;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author francois
 */
public class SelectSGBDCombo extends ComboBox<GestionBdD.SGBD>{
    
        public SelectSGBDCombo() {
                ObservableList<SGBD> alls = FXCollections.observableArrayList();
                alls.add(GestionBdD.PostgresqlSGBD);
                alls.add(GestionBdD.MySQLSGBD);
        this.setItems(alls);
        this.getSelectionModel().select(0);
    }

    public SGBD selectedSGBD() {
        return this.getSelectionModel().getSelectedItem();
    }

}
