package me.mustache.logic;

import java.util.ArrayList;

public class Notenrechner {

    private static Notenrechner instance;
    public static Notenrechner getInstance() {
        if(instance == null) instance = new Notenrechner();
        return instance;
    }

    private final Database db = Database.getInstance();

    private int grade;
    private int avgGrade;

    public Notenrechner(){

    }

    /**
     * @param fachId - Subject id for calculating grades
     * @param schuelerId - Student you want the calculated grade
     * @return - Grade in dependency of grading as float
     */
    public int calculateGrades(int fachId, int schuelerId, int halbjahr, int prognose){
        float[] note = new float[3];
        float[] wertung = new float[3];

        note[0] = db.getAvgNoteMuendlich(fachId, schuelerId, halbjahr, prognose);
        note[1] = db.getAvgNoteSchriftlich(fachId, schuelerId, halbjahr, prognose);
        note[2] = db.getAvgNoteZusatz(fachId, schuelerId, halbjahr, prognose);

        wertung[0] = db.getWertungMuendlich(fachId, db.getKlasseIdBySchuelerId(schuelerId));
        wertung[1] = db.getWertungSchriftlich(fachId, db.getKlasseIdBySchuelerId(schuelerId));
        wertung[2] = db.getWertungZusatz(fachId, db.getKlasseIdBySchuelerId(schuelerId));

        grade = (int)(note[0]*wertung[0]+note[1]*wertung[1]+note[2]*wertung[2]);

        return grade;
    }

    /**
     * @param schuelerId - Student id for average grade of all subjects
     * @return - Average grade of all subjects for student "schuelerId" as float
     */
    public int calculateAvgGrade(int schuelerId, int halbjahr, int prognose){
        ArrayList<Integer> faecherId = db.getFaecherIdBySchuelerId(schuelerId);
        for (int i = 0; getAnzFaecherBySchuelerId(schuelerId) > i; i++){
            avgGrade = calculateGrades(faecherId.get(i), schuelerId, halbjahr, prognose) + avgGrade;
        }
        avgGrade = avgGrade/getAnzFaecherBySchuelerId(schuelerId);
        return (int)(avgGrade);
    }

    public int getAnzFaecherBySchuelerId(int schuelerId){
        return db.getFaecherIdBySchuelerId(schuelerId).size();
    }





}
