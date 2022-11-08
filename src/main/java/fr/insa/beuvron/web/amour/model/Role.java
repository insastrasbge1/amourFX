/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.beuvron.web.amour.model;

/**
 *
 * @author francois
 */
public class Role {
    
    public static final Role ADMIN_ROLE = new Role(1,"admin");
    public static final Role USER_ROLE = new Role(2,"user");
    
    private int id;
    private String nrole;

    public Role(int id, String nrole) {
        this.id = id;
        this.nrole = nrole;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role) {
            Role r = (Role) obj;
            return this.id == r.id;
        } else {
            return false;
        }
    }
    
    
    
    @Override
    public String toString() {
        return this.nrole;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nrole
     */
    public String getNrole() {
        return nrole;
    }

    /**
     * @param nrole the nrole to set
     */
    public void setNrole(String nrole) {
        this.nrole = nrole;
    }
    
    
    
}
