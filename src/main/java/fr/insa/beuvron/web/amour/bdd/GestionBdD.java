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
package fr.insa.beuvron.web.amour.bdd;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.web.amour.model.Role;
import fr.insa.beuvron.web.amour.model.Utilisateur;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 
 * @author francois
 */
public class GestionBdD {

    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
    }

    public static void creeSchema(Connection con)
            throws SQLException {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation des tables
            st.executeUpdate(
                    """
                    create table fdbrole (
                        id integer not null primary key,
                        nrole varchar(30) not null unique
                    )
                    """);
            st.executeUpdate(
                    """
                    create table fdbutilisateur (
                        id integer not null primary key
                        generated always as identity,
                    -- ceci est un exemple de commentaire SQL :
                    -- un commentaire commence par deux tirets,
                    -- et fini à la fin de la ligne
                    -- cela me permet de signaler que le petit mot clé
                    -- unique ci-dessous interdit deux valeurs semblables
                    -- dans la colonne des noms.
                    -- generated always as identity indique que c'est le
                    -- sgbd qui affecte automatiquement les id
                        nom varchar(30) not null unique,
                        pass varchar(30) not null,
                        role integer not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table fdbaime (
                        u1 integer not null,
                        u2 integer not null
                    )
                    """);
            // je defini les liens entre les clés externes et les clés primaires
            // correspondantes
            st.executeUpdate(
                    """
                    alter table fdbaime
                        add constraint fk_fdbaime_u1
                        foreign key (u1) references fdbutilisateur(id)
                    """);
            st.executeUpdate(
                    """
                    alter table fdbutilisateur
                        add constraint fk_fdbutilisateur_role
                        foreign key (role) references fdbrole(id)
                    """);
            st.executeUpdate(
                    """
                    alter table fdbaime
                        add constraint fk_fdbaime_u2
                        foreign key (u2) references fdbutilisateur(id)
                    """);
            // si j'arrive jusqu'ici, c'est que tout s'est bien passé
            // je confirme (commit) la transaction
            con.commit();
            // je retourne dans le mode par défaut de gestion des transaction :
            // chaque ordre au SGBD sera considéré comme une transaction indépendante
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;
        } finally {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
    }

    // vous serez bien contents, en phase de développement de pouvoir
    // "repartir de zero" : il est parfois plus facile de tout supprimer
    // et de tout recréer que d'essayer de modifier le schema et les données
    public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate(
                        """
                    alter table fdbaime
                        drop constraint fk_fdbaime_u1
                             """);
                System.out.println("constraint fk_fdbaime_u1 dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                    alter table fdbaime
                        drop constraint fk_fdbaime_u2
                    """);
                System.out.println("constraint fk_fdbaime_u2 dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                    alter table fdbutilisateur
                        drop constraint fk_fdbutilisateur_role
                    """);
                System.out.println("constraint fk_fdbutilisateur_role dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate(
                        """
                    drop table fdbaime
                    """);
                System.out.println("dable fdbaime dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table fdbutilisateur
                    """);
                System.out.println("table fdbutilisateur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table fdbrole
                    """);
                System.out.println("table fdbrole dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }

    // pas de probleme particulier pour créer une nouvelle relation aime
    // en supposant que l'on connait les identificateurs des utilisateurs
    // on ne verra que plus tard la création d'un utilisateur, car on
    // veut pouvoir "récupérer" l'identificateur créé automatiquement
    // pour cela, il nous faut voir le principe de la recherche dans la
    // base de donnée
    public static void createAime(Connection con, int idU1, int idU2)
            throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                """
                insert into fdbaime (u1,u2) values (?,?)
                """)) {
            pst.setInt(1, idU1);
            pst.setInt(2, idU2);
            pst.executeUpdate();
        }
    }

    // exemple de requete à la base de donnée
    public static void afficheTousLesUtilisateur(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour effectuer une recherche, il faut utiliser un "executeQuery"
            // et non un "executeUpdate".
            // un executeQuery retourne un ResultSet qui contient le résultat
            // de la recherche (donc une table avec quelques information supplémentaire)
            try ( ResultSet tlu = st.executeQuery("select id,nom,pass,role from fdbutilisateur")) {
                // un ResultSet se manipule un peu comme un fichier :
                // - il faut le fermer quand on ne l'utilise plus
                //   d'où l'utilisation du try(...) ci-dessus
                // - il faut utiliser la méthode next du ResultSet pour passer
                //   d'une ligne à la suivante.
                //   . s'il y avait effectivement une ligne suivante, next renvoie true
                //   . si l'on était sur la dernière ligne, next renvoie false
                //   . au début, on est "avant la première ligne", il faut donc
                //     faire un premier next pour accéder à la première ligne
                //     Note : ce premier next peut renvoyer false si le résultat
                //            du select était vide
                // on va donc très souvent avoir un next
                //   . dans un if si l'on veut tester qu'il y a bien un résultat
                //   . dans un while si l'on veut traiter l'ensemble des lignes
                //     de la table résultat

                System.out.println("liste des utilisateurs :");
                System.out.println("------------------------");
                // ici, on veut lister toutes les lignes, d'où le while
                while (tlu.next()) {
                    // Ensuite, pour accéder à chaque colonne de la ligne courante,
                    // on a les méthode getInt, getString... en fonction du type
                    // de la colonne.

                    // on peut accéder à une colonne par son nom :
                    int id = tlu.getInt("id");
                    // ou par son numéro (la première colonne a le numéro 1)
                    String nom = tlu.getString(2);
                    String pass = tlu.getString("pass");
                    String mess = id + " : " + nom + " (" + pass + ")";
                    int idRole = tlu.getInt("role");
                    if (idRole == 1) {
                        mess = mess + " --> admin";
                    }
                    System.out.println(mess);
                }
            }
        }

    }

    // exemple de requete à la base de donnée
    public static void afficheAmours(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tla = st.executeQuery(
                    """
                     select U1.nom,U2.nom
                        from fdbaime 
                            join fdbutilisateur as U1 on fdbaime.u1 = U1.id
                            join fdbutilisateur as U2 on fdbaime.u2 = U2.id
                     """)) {
                System.out.println("liste des amours :");
                System.out.println("------------------");
                while (tla.next()) {
                    String nom1 = tla.getString(1);
                    String nom2 = tla.getString(2);
                    System.out.println(nom1 + " aime " + nom2);
                }
            }
        }

    }

    /**
     * creation des roles : il sont à priori tous connus.
     * @param con
     * @throws SQLException 
     */
    public static void createRoles(Connection con) throws SQLException {
        try ( PreparedStatement cr = con.prepareStatement(
                "insert into fdbrole (id,nrole) values (?,?)")) {
            // role admin
            cr.setInt(1, 1);
            cr.setString(2, "admin");
            cr.executeUpdate();
            // role user
            cr.setInt(1, 2);
            cr.setString(2, "user");
            cr.executeUpdate();
        }
    }

    // Je veux explicitement gérer le fait que deux utilisateurs ne peuvent pas
    // avoir le même nom. 
    // Comme j'ai indiqué la colonne comme 'unique' au niveau de la définition
    // de la table, le SGBD interdira de toute façon deux utilisateurs de même
    // nom : un SQLException sera générée dans ce cas.
    // Mais si ce cas correspond à quelque chose que l'on veut gérer spécifiquement,
    // (par exemple : permeetra à un nouvel utilisateur de se créer avec le nom
    // de son choix, mais doir recevoir un message d'erreur s'il choisi un nom
    // qui existe déjà.
    // Utiliser le SQLException de base risque d'être difficile : il faudrait
    // s'assurer que l'exception correspond bien à ce cas particulier.
    // on choisi donc de créer une exception spécifique
    public static class NomExisteDejaException extends Exception {
    }

    // 
    // lors de la création d'un utilisateur, l'identificateur est automatiquement
    // créé par le SGBD.
    // on va souvent avoir besoin de cet identificateur dans le programme,
    // par exemple pour gérer des liens "aime" entre utilisateur
    // vous trouverez ci-dessous la façon de récupérer les identificateurs
    // créés : ils se présentent comme un ResultSet particulier.
    public static int createUtilisateur(Connection con, String nom, String pass, int roleID)
            throws SQLException, NomExisteDejaException {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
        try ( PreparedStatement chercheNom = con.prepareStatement(
                "select id from fdbutilisateur where nom = ?")) {
            chercheNom.setString(1, nom);
            ResultSet testNom = chercheNom.executeQuery();
            if (testNom.next()) {
                throw new NomExisteDejaException();
            }
            // lors de la creation du PreparedStatement, il faut que je précise
            // que je veux qu'il conserve les clés générées
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                insert into fdbutilisateur (nom,pass,role) values (?,?,?)
                """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nom);
                pst.setString(2, pass);
                pst.setInt(3, roleID);
                pst.executeUpdate();
                con.commit();

                // je peux alors récupérer les clés créées comme un result set :
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    // et comme ici je suis sur qu'il y a une et une seule clé, je
                    // fait un simple next 
                    rid.next();
                    // puis je récupère la valeur de la clé créé qui est dans la
                    // première colonne du ResultSet
                    int id = rid.getInt(1);
                    return id;
                }
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static boolean idUtilisateurExiste(Connection con, int id) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                "select id from fdbutilisateur where id = ?")) {
            pst.setInt(1, id);
            ResultSet res = pst.executeQuery();

            return res.next();
        }
    }

    public static boolean nomUtilisateurExiste(Connection con, String nom) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                "select id from fdbutilisateur where nom = ?")) {
            pst.setString(1, nom);
            ResultSet res = pst.executeQuery();
            return res.next();
        }
    }

    public static Optional<Utilisateur> login(Connection con, String nom, String pass) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                "select fdbutilisateur.id as uid,nrole"
                + " from fdbutilisateur "
                + "   join fdbrole on fdbutilisateur.role = fdbrole.id"
                + " where fdbutilisateur.nom = ? and pass = ?")) {

            pst.setString(1, nom);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                return Optional.of(new Utilisateur(res.getInt("uid"), nom, pass, res.getString("nrole")));
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<Utilisateur> tousLesUtilisateurs(Connection con) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select fdbutilisateur.id as uid,nom,pass,nrole"
                + " from fdbutilisateur "
                + "   join fdbrole on fdbutilisateur.role = fdbrole.id"
                + " order by nom asc")) {

            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Utilisateur(rs.getInt("uid"),
                            rs.getString("nom"), rs.getString("pass"),
                            rs.getString("nrole")));
                }
                return res;
            }
        }
    }

    public static List<Role> tousLesRoles(Connection con) throws SQLException {
        List<Role> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select id,nrole from fdbrole"
                + " order by nrole asc")) {

            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Role(rs.getInt("id"),
                            rs.getString("nrole")));
                }
                return res;
            }
        }
    }

    public static List<Utilisateur> quiAiment(Connection con, Utilisateur user) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select fdbutilisateur.id as uid,nom,pass,nrole"
                + " from fdbutilisateur "
                + "   join fdbrole on fdbutilisateur.role = fdbrole.id"
                + "   join fdbaime on u1 = fdbutilisateur.id"
                + " where u2 = ?"
                + " order by nom asc")) {
            pst.setInt(1, user.getId());
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Utilisateur(rs.getInt("uid"),
                            rs.getString("nom"), rs.getString("pass"),
                            rs.getString("nrole")));
                }
                return res;
            }
        }
    }

    public static List<Utilisateur> quiSontAimesPar(Connection con, Utilisateur user) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select fdbutilisateur.id as uid,nom,pass,nrole"
                + " from fdbutilisateur "
                + "   join fdbrole on fdbutilisateur.role = fdbrole.id"
                + "   join fdbaime on u2 = fdbutilisateur.id"
                + " where u1 = ?"
                + " order by nom asc")) {
            pst.setInt(1, user.getId());
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Utilisateur(rs.getInt("uid"),
                            rs.getString("nom"), rs.getString("pass"),
                            rs.getString("nrole")));

                }
                return res;
            }
        }
    }

    public static List<Utilisateur> quiNeSontPasAimesPar(Connection con, Utilisateur user) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select fdbutilisateur.id as uid,nom,pass,nrole"
                + " from fdbutilisateur "
                + "   join fdbrole on fdbutilisateur.role = fdbrole.id"
                + " where not exists (select * from fdbaime where u1 = ? and u2 = fdbutilisateur.id)"
                + " order by nom asc")) {
            pst.setInt(1, user.getId());
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Utilisateur(rs.getInt("uid"),
                            rs.getString("nom"), rs.getString("pass"),
                            rs.getString("nrole")));
                }
                return res;
            }
        }
    }

    public static List<Utilisateur> vraiAmisDe(Connection con, Utilisateur user) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select fdbutilisateur.id as uid,nom,pass,nrole"
                + " from fdbaime as A1 join fdbaime as A2 on A1.u2 = A2.u1 "
                + "   join fdbutilisateur on A1.u2 = fdbutilisateur.id"
                + "   join fdbrole on fdbutilisateur.role = fdbrole.id"
                + " where A1.u1 = ? and A1.u1 = A2.u2"
                + " order by nom asc")) {
            pst.setInt(1, user.getId());
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Utilisateur(rs.getInt("uid"),
                            rs.getString("nom"), rs.getString("pass"),
                            rs.getString("nrole")));
                }
                return res;
            }
        }
    }

    public static void changeAllAimes(Connection con, Utilisateur u1, Collection<Utilisateur> aimesParU1)
            throws SQLException {
        try {
            con.setAutoCommit(false);
            // suppression de tous les anciens amours
            try ( PreparedStatement pst = con.prepareStatement(
                    "delete from fdbaime where u1 = ?")) {
                pst.setInt(1, u1.getId());
                pst.executeUpdate();
            }
            // ajout de tous les nouveaux
            try ( PreparedStatement pst = con.prepareStatement(
                    "insert into fdbaime (u1,u2) values (?,?)")) {
                pst.setInt(1, u1.getId());
                for (Utilisateur u2 : aimesParU1) {
                    pst.setInt(2, u2.getId());
                    pst.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static int choisiUtilisateur(Connection con) throws SQLException {
        boolean ok = false;
        int id = -1;
        while (!ok) {
            System.out.println("------- choix d'un utilisateur");
            afficheTousLesUtilisateur(con);
            id = ConsoleFdB.entreeEntier("donnez l'identificateur de l'utilisateur :");
            ok = idUtilisateurExiste(con, id);
            if (!ok) {
                System.out.println("id invalide");
            }
        }
        return id;
    }
    
    /**
     * demande un role par menu.
     * @param con
     * @return 
     */
    public static int demandeRole(Connection con) {
        System.out.println("choix du role");
        try(PreparedStatement st = con.prepareStatement(
        "select id,nrole from fdbrole")) {
            int nbr = 0;
            List<Integer> ids = new ArrayList<>();
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                nbr ++;
                ids.add(rs.getInt("id"));
                System.out.println(nbr + " : " + rs.getString("nrole"));
            }
            int rep = -1;
            while (rep < 1 || rep > nbr) {
                rep = ConsoleFdB.entreeInt("indiquez le numéro du role :");
            }
            return ids.get(rep-1);
        } catch (SQLException ex) {
            throw new Error("Probleme BDD : " + ex.getLocalizedMessage());
        }
    }

    public static void demandeNouvelUtilisateur(Connection con) throws SQLException {
        boolean existe = true;
        while (existe) {
            System.out.println("--- creation nouvel utilisateur");
            String nom = ConsoleFdB.entreeString("nom :");
            String pass = ConsoleFdB.entreeString("pass :");
            int idRole = demandeRole(con);
            try {
                createUtilisateur(con, nom, pass,idRole);
                existe = false;
            } catch (NomExisteDejaException ex) {
                System.out.println("ce nom existe deja, choisissez en un autre");
            }
        }
    }

    public static void demandeNouvelAime(Connection con) throws SQLException {
        System.out.println("--- creation d'un nouveau lien U1 'aime' U2");
        System.out.println("choisissez U1");
        int idU1 = choisiUtilisateur(con);
        System.out.println("choisissez U2");
        int idU2 = choisiUtilisateur(con);
        createAime(con, idU1, idU2);
    }

    public static void recreeTout(Connection con) throws SQLException {
        // j'essaye d'abord de tout supprimer
        try {
            deleteSchema(con);
//            System.out.println("ancien schéma supprimé");
        } catch (SQLException ex) {
//            System.out.println("pas de suppression d'un ancien schéma");
        }
        creeSchema(con);
        System.out.println("Schema (re)-created");
        createRoles(con);
        List<Integer> ids = new ArrayList<>();
        try {
            ids.add(createUtilisateur(con, "toto", "p1",1));
            ids.add(createUtilisateur(con, "bob", "p2",2));
            ids.add(createUtilisateur(con, "bill", "p3",2));
        } catch (NomExisteDejaException ex) {
            throw new Error(ex);
        }
        // toto aime bob et bill
        createAime(con, ids.get(0), ids.get(1));
        createAime(con, ids.get(0), ids.get(2));
        // bob aime bill
        createAime(con, ids.get(1), ids.get(2));
        // bill aime toto
        createAime(con, ids.get(2), ids.get(1));
        System.out.println("initial data added");
    }

    public static void menu(Connection con) {
        int rep = -1;
        while (rep != 0) {
            System.out.println("Menu BdD Aime");
            System.out.println("=============");
            System.out.println("1) créer/recréer la BdD initiale");
            System.out.println("2) liste des utilisateurs");
            System.out.println("3) liste des liens 'Aime'");
            System.out.println("4) ajouter un utilisateur");
            System.out.println("5) ajouter un lien 'Aime'");
            System.out.println("6) ajouter n utilisateurs aléatoires");
            System.out.println("0) quitter");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                if (rep == 1) {
                    recreeTout(con);
                } else if (rep == 2) {
                    afficheTousLesUtilisateur(con);
                } else if (rep == 3) {
                    afficheAmours(con);
                } else if (rep == 4) {
                    demandeNouvelUtilisateur(con);
                } else if (rep == 5) {
                    demandeNouvelAime(con);
                } else if (rep == 6) {
                    System.out.println("création d'utilisateurs 'aléatoires'");
                    int combien = ConsoleFdB.entreeEntier("combien d'utilisateur : ");
                    for (int i = 0; i < combien; i++) {
                        boolean exist = true;
                        while (exist) {
                            String nom = "U" + ((int) (Math.random() * 10000));
                            try {
                                createUtilisateur(con, nom, "P" + ((int) (Math.random() * 10000)),2);
                                exist = false;
                            } catch (NomExisteDejaException ex) {
                            }
                        }

                    }
                }
            } catch (SQLException ex) {
                throw new Error(ex);
            }
        }
    }

    public static void main(String[] args) {
        try ( Connection con = defautConnect()) {
            System.out.println("connecté !!!");
            menu(con);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

}
