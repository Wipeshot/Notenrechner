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


    /**
     * @param fileName - name of the file (without .db)
     *
     * Creates an Database file if not exists
     *
     */
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

    /**
     * @param fachId - Id of the subject
     * @param schuelerId - Id of the student
     * @return - Average written grade
     */
    public float getAvgNoteSchriftlich(int fachId, int schuelerId, int halbjahr, int prognose) {
        String avgNote;
        if(prognose == 0) {
            avgNote = "SELECT AVG (note)\n"
                    + "FROM note\n"
                    + "WHERE fachid = ?\n"
                    + "AND schuelerid = ?\n"
                    + "AND notentype = ?\n"
                    + "AND halbjahr = ?\n"
                    + "AND prognose = ?"
                    + ";";
        } else {
            avgNote = "SELECT AVG (note)\n"
                    + "FROM note\n"
                    + "WHERE fachid = ?\n"
                    + "AND schuelerid = ?\n"
                    + "AND notentype = ?\n"
                    + "AND halbjahr = ?\n"
                    + ";";
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(avgNote);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 1);
            pstmt.setInt(4, halbjahr);
            if(prognose == 0) {
                pstmt.setInt(5, prognose);
            }
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @param fachId - Id of the subject
     * @param schuelerId - Id of the student
     * @param halbjahr - half year (1/2)
     * @return - ArrayList of written grades as Integer
     */
    public ArrayList<Integer> getNotenSchriftlich(int fachId, int schuelerId, int halbjahr){
        String note = "SELECT note\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + "AND prognose = ?\n"
                + "AND halbjahr = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Integer> noten = new ArrayList<>();
            pstmt = conn.prepareStatement(note);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 1);
            pstmt.setInt(4,0);
            pstmt.setInt(5, halbjahr);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) noten.add(rs.getInt(1));
            return noten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param fachId - Id of the subject
     * @param schuelerId - Id of the student
     * @param halbjahr - half year (1/2)
     * @return - ArrayList of oral grades as Integer
     */
    public float getAvgNoteMuendlich(int fachId, int schuelerId, int halbjahr, int prognose) {
        String avgNote;
        if(prognose == 0) {
            avgNote = "SELECT AVG (note)\n"
                    + "FROM note\n"
                    + "WHERE fachid = ?\n"
                    + "AND schuelerid = ?\n"
                    + "AND notentype = ?\n"
                    + "AND halbjahr = ?\n"
                    + "AND prognose = ?"
                    + ";";
        } else {
            avgNote = "SELECT AVG (note)\n"
                    + "FROM note\n"
                    + "WHERE fachid = ?\n"
                    + "AND schuelerid = ?\n"
                    + "AND notentype = ?\n"
                    + "AND halbjahr = ?"
                    + ";";
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(avgNote);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 2);
            pstmt.setInt(4, halbjahr);
            if(prognose == 0){
                pstmt.setInt(5, prognose);
            }
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @param fachId - Id of the subject
     * @param schuelerId - Id of the student
     * @param halbjahr - half year (1/2)
     * @return - ArrayList of written grades as Integer
     */
    public ArrayList<Integer> getNotenMuendlich(int fachId, int schuelerId, int halbjahr){
        String note = "SELECT note\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + "AND halbjahr = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Integer> noten = new ArrayList<>();
            pstmt = conn.prepareStatement(note);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 2);
            pstmt.setInt(4, halbjahr);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) noten.add(rs.getInt(1));
            return noten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param fachId - Id of the subject
     * @param schuelerId - Id of the student
     * @param halbjahr - half year (1/2)
     * @return - Average extra grades
     */
    public float getAvgNoteZusatz(int fachId, int schuelerId, int halbjahr, int prognose) {
        String avgNote;
        if(prognose == 0) {
            avgNote = "SELECT AVG (note)\n"
                    + "FROM note\n"
                    + "WHERE fachid = ?\n"
                    + "AND schuelerid = ?\n"
                    + "AND notentype = ?\n"
                    + "AND halbjahr = ?\n"
                    + "AND prognose = ?"
                    + ";";
        } else {
            avgNote = "SELECT AVG (note)\n"
                    + "FROM note\n"
                    + "WHERE fachid = ?\n"
                    + "AND schuelerid = ?\n"
                    + "AND notentype = ?\n"
                    + "AND halbjahr = ?\n"
                    + ";";
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(avgNote);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 3);
            pstmt.setInt(4, halbjahr);
            if(prognose == 0){
                pstmt.setInt(5, prognose);
            }
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @param fachId - Id of the subject
     * @param schuelerId - Id of the student
     * @param halbjahr - half year (1/2)
     * @return - ArrayList of extra grades as Integer
     */
    public ArrayList<Integer> getNotenZusatz(int fachId, int schuelerId, int halbjahr){
        String note = "SELECT note\n"
                + "FROM note\n"
                + "WHERE fachid = ?\n"
                + "AND schuelerid = ?\n"
                + "AND notentype = ?\n"
                + "AND halbjahr = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Integer> noten = new ArrayList<>();
            pstmt = conn.prepareStatement(note);
            pstmt.setInt(1, fachId);
            pstmt.setInt(2, schuelerId);
            pstmt.setInt(3, 3);
            pstmt.setInt(4, halbjahr);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) noten.add(rs.getInt(1));
            return noten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * @param schuelerId - Id of the student you want all Subject ids
     * @return - ArrayList with all Subject ids as Integer
     */
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

    /**
     * Creates new student in the Database
     *
     * @param name - Name of the new student
     * @param vorname - prename of the new student
     * @param benutzername - username of the new student
     * @param passwort - password for students account
     * @param klasseid - Class the student is in
     */
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

    /**
     * Add a new grade to a student
     *
     * @param note - Grade (0 - 15)
     * @param notentype - Type of the Grade (1 = written, 2 = oral, 3 = extra)
     * @param fachid - Id of the subject
     * @param schuelerid - Id of the student
     */
    public void addNote(int note, int notentype, int fachid, int schuelerid, int halbjahr, int prognose){
        String addNote = "INSERT INTO note (note, notentype, fachid, schuelerid, halbjahr, prognose)\n"
                + "VALUES(?,?,?,?,?,?)"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(addNote);
            pstmt.setInt(1, note);
            pstmt.setInt(2, notentype);
            pstmt.setInt(3, fachid);
            pstmt.setInt(4, schuelerid);
            pstmt.setInt(5, halbjahr);
            pstmt.setInt(6, prognose);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param noteid - Id from the grade that you need to delete
     */
    public void removeNote(int noteid){
        String removeNote = "DELETE\n"
                + "FROM note\n"
                + "WHERE notenid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(removeNote);
            pstmt.setInt(1, noteid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Integer> getSemesterByStudentId(int studentid){
        ArrayList<Integer> semester = new ArrayList<>();
        String getSemester = "SELECT halbjahr\n"
                + "FROM note\n"
                + "WHERE schuelerid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getSemester);
            pstmt.setInt(1, studentid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                semester.add(rs.getInt(1));
            }
            return semester;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param username - Username you need password from
     * @return - right password for username
     */
    public String getPasswordByUsername(String username){
        String passwordByUsername = "SELECT passwort\n"
                + "FROM schueler\n"
                + "WHERE LOWER(benutzername) = ?";
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

    /**
     * @param username - Username
     * @return - ID of the student
     */
    public int getSchuelerIdByUsername(String username){
        String getSchuelerId = "SELECT schuelerid\n"
                + "FROM schueler\n"
                + "WHERE lower(benutzername) = ?"
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

    /**
     * @param schuelerId - Student id to get the name
     * @return - Returns the name of the student with id "schuelerId"
     */
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

    /**
     * @param schuelerId - Student id you want prename
     * @return - Returns prename of the student with the id "schuelerId"
     */
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

    /**
     * @param schuelerId - Student id you want username
     * @return - The username for the student with id "schuelerId"
     */
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

    /**
     * @param schuelerId - student Id
     * @return - Class name for the student
     */
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

    /**
     * @param fachid - Subject id you want written grading
     * @return - Grading for the subject with Id "fachid" as float (0.0 - 1.0)
     */
    public float getWertungSchriftlich(int fachid, int klassenid){
        String getWertungSchriftlich = "SELECT schriftlich\n"
                + "FROM noten_wertung\n"
                + "WHERE fachid = ?\n"
                + "AND klasseid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getWertungSchriftlich);
            pstmt.setInt(1, fachid);
            pstmt.setInt(2, klassenid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1)/100;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @param fachid - Subject id you want the oral grading
     * @return - Returns the oral grading with Id "fachid" as float (0.0 - 1.0)
     */
    public float getWertungMuendlich(int fachid, int klassenid){
        String getWertungMuendlich = "SELECT muendlich\n"
                + "FROM noten_wertung\n"
                + "WHERE fachid = ?\n"
                + "AND klasseid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getWertungMuendlich);
            pstmt.setInt(1, fachid);
            pstmt.setInt(2, klassenid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1)/100;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @param fachid - Subject id you want the extra grading
     * @return - Returns the extra grading for the subject with id "fachid" as float (0.0 - 1.0)
     */
    public float getWertungZusatz(int fachid, int klassenid){
        String getWertungSchriftlich = "SELECT sonstige\n"
                + "FROM noten_wertung\n"
                + "WHERE fachid = ?\n"
                + "AND klasseid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getWertungSchriftlich);
            pstmt.setInt(1, fachid);
            pstmt.setInt(2, klassenid);
            ResultSet rs = pstmt.executeQuery();
            return rs.getFloat(1)/100;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @param fachid - Subject id you want the name of
     * @return - Returns name of the subject with id "fachid" as String
     */
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

    public int getKlasseIdBySchuelerId(int schuelerId){
        String getFachById = "SELECT klasseid\n"
                + "FROM schueler\n"
                + "WHERE schuelerid = ?"
                + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getFachById);
            pstmt.setInt(1, schuelerId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public  int getAnzNotenBySchuelerId(int schuelerId, int fachId, int notentype, int prognose){
        String getAnzNoten = "SELECT note\n"
                + "FROM note\n"
                + "WHERE schuelerid = ?\n"
                + "AND fachId = ?\n"
                + "AND notentype = ?\n"
                + "AND prognose = ?"
                + ";";
        ArrayList<Integer> forCal = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            pstmt = conn.prepareStatement(getAnzNoten);
            pstmt.setInt(1, schuelerId);
            pstmt.setInt(2, fachId);
            pstmt.setInt(3, notentype);
            pstmt.setInt(4, prognose);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                forCal.add(rs.getInt(1));
            }
            System.out.println(forCal.size());
            return forCal.size();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

}