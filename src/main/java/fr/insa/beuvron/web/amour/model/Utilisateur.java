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
package fr.insa.beuvron.web.amour.model;

/**
 *
 * @author francois
 */
public class Utilisateur {
    
    private final int id;
    private String nom;
    private String pass;
    private String nomRole;

    public Utilisateur(int id, String nom, String pass, String nomRole) {
        this.id = id;
        this.nom = nom;
        this.pass = pass;
        this.nomRole = nomRole;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNomRole() {
        return nomRole;
    }
    
    
    
}
