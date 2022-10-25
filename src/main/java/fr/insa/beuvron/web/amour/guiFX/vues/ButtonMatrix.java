/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX.vues;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Une petite classe utilitaire pour tester par exemple le scroll.
 *
 * @author francois
 */
public class ButtonMatrix extends GridPane {

    public ButtonMatrix(int nbrLig, int nbrCol) {
        for (int i = 0; i < nbrLig; i++) {
            for (int j = 0; j < nbrCol; j++) {
                this.add(new Button("bouton "+ i + "-" + j), i, j);
            }
        }
    }

}
