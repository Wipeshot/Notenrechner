package me.mustache.logic;

import java.util.ArrayList;

public class Notenrechner {

    private static Notenrechner instance;
    public static Notenrechner getInstance() {
        if(instance == null) instance = new Notenrechner();
        return instance;
    }

    private Database db = Database.getInstance();

    private float grade;
    private float avgGrade;

    public Notenrechner(){

    }

    public float calculateGrades(int fachId, int schuelerId){
        float[] note = new float[3];
        float[] wertung = new float[3];

        note[0] = db.getAvgNoteMuendlich(fachId, schuelerId);
        note[1] = db.getAvgNoteSchriftlich(fachId, schuelerId);
        note[2] = db.getAvgNoteZusatz(fachId, schuelerId);

        wertung[0] = db.getWertungMuendlich(fachId);
        wertung[1] = db.getWertungSchriftlich(fachId);
        wertung[2] = db.getWertungZusatz(fachId);

        grade = note[0]*wertung[0]+note[1]*wertung[1]+note[2]*wertung[2];

        return grade;
    }

    public float calculateAvgGrade(int schuelerId){
        ArrayList<Integer> faecherId = db.getFaecherIdBySchuelerId(schuelerId);
        for (int i = 0; getAnzFaecherBySchuelerId(schuelerId) > i; i++){
            avgGrade = calculateGrades(faecherId.get(i), schuelerId) + avgGrade;
        }
        avgGrade = avgGrade/getAnzFaecherBySchuelerId(schuelerId);
        return avgGrade;
    }

    public int getAnzFaecherBySchuelerId(int schuelerId){
        return db.getFaecherIdBySchuelerId(schuelerId).size();
    }



}
