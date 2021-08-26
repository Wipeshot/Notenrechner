package me.mustache.logic;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    private Statement stmt;
    private PreparedStatement pstmt;
    private String url;

    public void setUrl(String url){
        this.url = "jdbc:sqlite:database/" + url + ".db";
        System.out.println("URL gesetzt.");
        try (Connection conn = DriverManager.getConnection(this.url)) {
            if (conn != null) {
                System.out.println("Datenbank verbunden.");
                stmt = conn.createStatement();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewDatabase(String fileName) {


        url = "jdbc:sqlite:database/" + fileName + ".db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Drivers Name: " + meta.getDriverName());
                System.out.println("Datenbank ist erstellt.");
                stmt = conn.createStatement();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public float getAvgNoteSchriftlich(int fachId, int schuelerId) {
        String avgNote = "SELECT AVG (note)\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(avgNote);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 1);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getNotenSchriftlich(int fachId, int schuelerId){
        String note = "SELECT note\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Integer> noten = new ArrayList<>();
            pstmt = conn.prepareStatement(note);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 1);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) noten.add(rs.getInt(1));
            return noten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public float getAvgNoteMuendlich(int fachId, int schuelerId) {
        String avgNote = "SELECT AVG (note)\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(avgNote);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 2);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getNotenMuendlich(int fachId, int schuelerId){
        String note = "SELECT note\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Integer> noten = new ArrayList<>();
            pstmt = conn.prepareStatement(note);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 2);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) noten.add(rs.getInt(1));
            return noten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public float getAvgNoteZusatz(int fachId, int schuelerId){
        String avgNote = "SELECT AVG (note)\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(avgNote);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 3);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getNotenZusatz(int fachId, int schuelerId){
        String note = "SELECT note\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Integer> noten = new ArrayList<>();
            pstmt = conn.prepareStatement(note);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 3);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) noten.add(rs.getInt(1));
            return noten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public ArrayList<Integer> getFaecherIdBySchuelerId(int schuelerId){
        ArrayList<Integer> faecherId = new ArrayList<>();
        String getFaecherId = "SELECT faecherids\n"
                + "FROM schueler\n"
                + "WHERE schuelerid = ?\n"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getFaecherId);
            pstmt.setInt(1, schuelerId);
            ResultSet rs = pstmt.executeQuery();
            String ids = rs.getString(1);
            for(String s: ids.split(",")){
                faecherId.add(Integer.parseInt(s));
            }
            return faecherId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void addSchueler(String name, String vorname, String benutzername, String passwort, int klasseid){
        String addSchueler = "INSERT INTO schueler (schuelername, schuelervorname, benutzername, passwort, klasseid)"
                + "VALUES (?,?,?,?,?)"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(addSchueler);
            pstmt.setString(1, name);
            pstmt.setString(2, vorname);
            pstmt.setString(3, benutzername);
            pstmt.setString(4, passwort);
            pstmt.setInt(5, klasseid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFach(int klasseid, String bezeichnung, int schriftlich, int muendlich, int zusatz){
        String addFach = "INSERT INTO fach (klasseid ,fachbezeichnung, wertungschriftlich, wertungmuendlich, wertungzusatz)"
                + "VALUES (?,?,?,?,?)"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(addFach);
            pstmt.setInt(1,klasseid);
            pstmt.setString(2, bezeichnung);
            pstmt.setInt(3, schriftlich);
            pstmt.setInt(4, muendlich);
            pstmt.setInt(5, zusatz);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNote(int note, int notentype, int fachid, int schuelerid){
        String addNote = "INSERT INTO note (note, notentype, fachid, schuelerid)\n"
                + "VALUES(?,?,?,?)"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(addNote);
            pstmt.setInt(1, note);
            pstmt.setInt(2, notentype);
            pstmt.setInt(3, fachid);
            pstmt.setInt(4, schuelerid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void addKlasse(String name){
        String addKlasse = "INSERT INTO klasse (klasse)\n"
                + "VALUES (?)"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(addKlasse);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getPasswordByUsername(String username){
        String passwordByUsername = "SELECT passwort\n"
                + "FROM schueler\n"
                + "WHERE benutzername = ?";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(passwordByUsername);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int getSchuelerIdByUsername(String username){
        String getSchuelerId = "SELECT schuelerid\n"
                + "FROM schueler\n"
                + "WHERE benutzername = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getSchuelerId);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    public String getSchuelerNameBySchuelerId(int schuelerId){
        String getSchuelerName = "SELECT schuelername\n"
                + "FROM schueler\n"
                + "WHERE schuelerid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getSchuelerName);
            pstmt.setInt(1, schuelerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getSchuelerVornameBySchuelerId(int schuelerId){
        String getSchuelerVorname = "SELECT schuelervorname\n"
                + "FROM schueler\n"
                + "WHERE schuelerid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getSchuelerVorname);
            pstmt.setInt(1, schuelerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getUsernameBySchuelerId(int schuelerId){
        String getSchuelerVorname = "SELECT benutzername\n"
                + "FROM schueler\n"
                + "WHERE schuelerid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getSchuelerVorname);
            pstmt.setInt(1, schuelerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getKlasseBySchuelerId(int schuelerId){
        String getKlasseBySchuelerId = "SELECT k.klasse\n"
                + "FROM schueler s, klasse k\n"
                + "WHERE s.schuelerid = ?"
                + "AND s.klasseid = k.klassenid"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getKlasseBySchuelerId);
            pstmt.setInt(1, schuelerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public float getWertungSchriftlich(int fachid){
        String getWertungSchriftlich = "SELECT wertungschriftlich\n"
                + "FROM fach\n"
                + "WHERE fachid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getWertungSchriftlich);
            pstmt.setInt(1, fachid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1)/100;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public float getWertungMuendlich(int fachid){
        String getWertungMuendlich = "SELECT wertungmuendlich\n"
                + "FROM fach\n"
                + "WHERE fachid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getWertungMuendlich);
            pstmt.setInt(1, fachid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1)/100;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public float getWertungZusatz(int fachid){
        String getWertungSchriftlich = "SELECT wertungzusatz\n"
                + "FROM fach\n"
                + "WHERE fachid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getWertungSchriftlich);
            pstmt.setInt(1, fachid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1)/100;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public String getFachById(int fachid){
        String getFachById = "SELECT fachbezeichnung\n"
                + "FROM fach\n"
                + "WHERE fachid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getFachById);
            pstmt.setInt(1, fachid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}