/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX.vues;

import fr.insa.beuvron.web.amour.bdd.GestionBdD;
import fr.insa.beuvron.web.amour.guiFX.VuePrincipale;
import fr.insa.beuvron.web.amour.model.Role;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author francois
 */
public class RoleComboBox extends ComboBox<Role> {

    private VuePrincipale main;

    public RoleComboBox(VuePrincipale main) {
        this.main = main;
        ObservableList<Role> alls;
        if (this.main.getSessionInfo().getConBdD() == null) {
            alls = FXCollections.observableArrayList(
                    new Role(-1, "No BDD"));
        } else {
            try {
                List<Role> allR = GestionBdD.tousLesRoles(this.main.getSessionInfo().getConBdD());
                alls = FXCollections.observableArrayList(allR);
            } catch (SQLException ex) {
                alls = FXCollections.observableArrayList(
                        new Role(-1, "Erreur BdD"));
            }
        }
        this.setItems(alls);
        this.getSelectionModel().select(1);
    }

    public Role selectedIdRole() {
        return this.getSelectionModel().getSelectedItem();
    }

}
