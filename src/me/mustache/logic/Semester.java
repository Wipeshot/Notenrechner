package me.mustache.logic;

import java.util.ArrayList;

public class Semester {

    private final Database db = Database.getInstance();

    public Semester(){

    }

    public int calculateNewestSemester(int schuelerid){
        try {
            ArrayList<Integer> semester = db.getSemesterByStudentId(schuelerid);
            int aktuellesSemester = semester.get(0);

            for (int i = 1; i < semester.size(); i++) {
                if (semester.get(i) > aktuellesSemester) aktuellesSemester = semester.get(i);
            }
            return aktuellesSemester;
        } catch (Exception e) {
            return 1;
        }
    }


}
