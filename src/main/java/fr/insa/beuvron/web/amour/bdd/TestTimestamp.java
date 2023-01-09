/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import static java.time.temporal.TemporalQueries.zone;

/**
 * Un petit exemple d'utilisation de Timestamp
 *
 * @author francois
 */
public class TestTimestamp {

    public static void testTimestamp() {
        try ( Connection con = GestionBdD.defautConnect()) {
            PreparedStatement pstCreate = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS testtimestamp\n"
                    + "(\n"
                    + "    id integer NOT NULL,\n"
                    + "    debut timestamp without time zone NOT NULL,\n"
                    + "    fin timestamp without time zone NOT NULL,\n"
                    + "    CONSTRAINT testtimestamp_pkey PRIMARY KEY (id)\n"
                    + ")");
            pstCreate.executeUpdate();
            // pour manipuler des dates et temps, le plus pratique est d'utiliser
            // le package java.time : voir par exemple :
            // https://www.baeldung.com/java-8-date-time-intro
            // l'équivalent d'un timestamp dans ce package est la classe Instant
            // pour obtenir l'instant courant
            Instant curI = Instant.now();
            // on peut créer un Timestamp correspondant
            // compatible avec la base de donnée : classe java.sql.Timestamp
            // !!! il y a plusieurs classes Timestamp. Verifiez bien que
            // !!! vous importez java.sql.Timestamp
            Timestamp ts = Timestamp.from(curI);
            // un instant dans le temps peut être aussi représenté par un entier long :
            // c'est le nombre de milliseconde écoulées depuis le 01/01/1970 (epoch)
            // on peut récupérer cette information
            long nbrMilli = curI.toEpochMilli();
            // cela donne une autre façon de créer un TimeStamp
            Timestamp ts2 = new Timestamp(nbrMilli);
            
            // pour travailler sur les date et les temps (par exemple
            // pour ajouter des durées en jour, semaine, heure...
            // une classe bien utile est LocalDateTime
            LocalDateTime ldt = LocalDateTime.ofInstant(curI, ZoneId.systemDefault());
            
            // un instant dans le temps peut être représenté par un entier long :
            // c'est le nombre de milliseconde écoulées depuis le 01/01/1970 (epoch)
            long curTime = System.currentTimeMillis();
            // cela permet d'initialiser un Timestamp à l'instant courant
            // utiliser la classe java.sql.Timestamp
            Timestamp debut = new Timestamp(curTime);

        } catch (ClassNotFoundException | SQLException ex) {
            throw new Error(ex);
        }
    }

}
