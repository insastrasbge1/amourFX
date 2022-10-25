/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.guiFX;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author francois
 */
public class TestFx extends BorderPane {

    public TestFx() {
        Button b = new Button("coucou");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println("vous avez cliqué sur coucou");
            }
        });
        b.setOnAction((t) -> {
            System.out.println("vous avez cliqué sur coucou");
        });
        Pane p = new Pane();
        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                System.out.println("clic en " + t.getX() + "," + t.getY());
            }
        });
        p.setOnMouseClicked((t) -> {
            System.out.println("clic en " + t.getX() + "," + t.getY());
        });
        this.setTop(b);
        this.setCenter(p);

    }

}
