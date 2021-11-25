package me.mustache.gui;

import me.mustache.logic.Database;
import me.mustache.logic.LoginLogic;
import me.mustache.logic.Notenrechner;
import me.mustache.logic.Semester;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Gui extends JFrame {

    private int actualHalbjahr;
    private boolean halbjahrFrameBool = false;

    //getInstances
    private Database db = Database.getInstance();
    private Notenrechner nr = Notenrechner.getInstance();

    //Loginscreen
    private JPanel loginScreen = new JPanel();
    private JTextField benutzername = new HintTextField("Benutzername");
    private JPasswordField passwort = new HintPasswordField("Passwort");
    private JButton login = new JButton("Einloggen");
    private JFrame halbjahrFrame;

    //Userinfo
    private JPanel userinfo = new JPanel();
    private JLabel name;
    private JLabel username;
    private JLabel userClass;
    private JLabel notenschnitt;
    private JButton halbjahrButton;

    //HalbjahrFenster
    private JButton[] halbjahrButtons;

    //Faecherinfo
    private JPanel faecherinfo = new JPanel();
    private JLabel[] colmBezeichnung;
    private JLabel[] fach;
    private JLabel[] schriftlich;
    private JLabel[] muendlich;
    private JLabel[] zusatz;
    private JLabel[] endnote;
    private JButton[] wertung;
    private JButton[] fachansicht;

    //Fachinfo
    private JPanel fachinfo = new JPanel();
    private JButton goBackToFaecherInfo = new JButton("Zurück zur Übersicht");
    private JPanel panelNoteSchriftlich = new JPanel();
    private JLabel schriftlichSchriftzugTable = new JLabel("Schriftlich");
    private JLabel[] schriftlichEinzelNoten;
    private JTable tabelleSchriftlich;
    private JTable tabelleMuendlich;
    private JTable tabelleZusatz;


    public Gui() {
        this.setSize(1280, 720);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);

        loginScreen();
    }



    public void loginScreen() {
        loginScreen.setLayout(new GridLayout(3, 1));

        loginScreen.setBorder(new LineBorder(Color.GREEN));
        loginScreen.setBounds(1280 / 2 - 200, 720 / 2 - 100, 400, 200);
        loginScreen.setVisible(true);

        loginScreen.add(benutzername);
        loginScreen.add(passwort);
        loginScreen.add(login);

        this.add(loginScreen);
        loginScreen.updateUI();

        login.addActionListener(e -> {
            if (new LoginLogic().checkPassword(benutzername.getText(), String.valueOf(passwort.getPassword())))
                setActualHalbjahr(new Semester().calculateNewestSemester(db.getSchuelerIdByUsername(benutzername.getText().toLowerCase())));
                setupUserscreen(db.getSchuelerIdByUsername(benutzername.getText().toLowerCase()));
                wrongPassword();
        });
    }

    private void setupUserscreen(int schuelerId) {
        this.remove(loginScreen);
        this.repaint();

        userinfo.setLayout(new GridLayout(1, 5));
        userinfo.setBorder(new LineBorder(Color.GREEN));
        userinfo.setBounds(0, 0, 1264, 50);
        userinfo.setVisible(true);

        name = new JLabel("Name: " + db.getSchuelerVornameBySchuelerId(schuelerId) + " " + db.getSchuelerNameBySchuelerId(schuelerId));
        username = new JLabel("Benutzername: " + db.getUsernameBySchuelerId(schuelerId));
        userClass = new JLabel(("Klasse: " + db.getKlasseBySchuelerId(schuelerId)));
        notenschnitt = new JLabel("Notenschnitt: " + (double) Math.round(nr.calculateAvgGrade(schuelerId, actualHalbjahr)));
        halbjahrButton = new JButton("Halbjahr ändern");


        userinfo.add(name);
        userinfo.add(username);
        userinfo.add(userClass);
        userinfo.add(notenschnitt);
        userinfo.add(halbjahrButton);

        halbjahrButton.addActionListener(e -> {
            if(!halbjahrFrameBool) halbjahrAendern(schuelerId);
        });

        this.add(userinfo);
        userinfo.updateUI();

        setupFaecherinfo(schuelerId);
    }

    private void halbjahrAendern(int schuelerid) {
        halbjahrFrameBool = true;
        halbjahrFrame = new JFrame("Halbjahr ändern");
        halbjahrFrame.setUndecorated(true);
        halbjahrFrame.setSize(250,200);
        halbjahrFrame.setLayout(null);
        halbjahrFrame.setAlwaysOnTop(true);
        halbjahrFrame.setLocationRelativeTo(null);
        halbjahrFrame.setResizable(false);
        halbjahrFrame.setVisible(true);

        halbjahrButtons = new JButton[4];
        halbjahrButtons[0] = new JButton("J1, 1. Halbjahr");
        halbjahrButtons[1] = new JButton("J1, 2. Halbjahr");
        halbjahrButtons[2] = new JButton("J2, 1. Halbjahr");
        halbjahrButtons[3] = new JButton("J2, 2. Halbjahr");
        int k = 0;
        for(JButton btn : halbjahrButtons){
            btn.setBounds(0,0+k, 250, 50);
            k += 50;
            halbjahrFrame.add(btn);
        }

        halbjahrButtons[0].addActionListener(e -> {
            setActualHalbjahr(1);
            halbjahrFrame.dispose();
            halbjahrFrameBool = false;
            reloadNoten(schuelerid);
        });

        halbjahrButtons[1].addActionListener(e -> {
            setActualHalbjahr(2);
            halbjahrFrame.dispose();
            halbjahrFrameBool = false;
            reloadNoten(schuelerid);
        });

        halbjahrButtons[2].addActionListener(e -> {
            setActualHalbjahr(3);
            halbjahrFrame.dispose();
            halbjahrFrameBool = false;
            reloadNoten(schuelerid);
        });

        halbjahrButtons[3].addActionListener(e -> {
            setActualHalbjahr(4);
            halbjahrFrame.dispose();
            halbjahrFrameBool = false;
            reloadNoten(schuelerid);
        });
    }

    public void wrongPassword() {
        benutzername.setBackground(Color.RED);
        passwort.setBackground(Color.RED);
    }

    public void setupFaecherinfo(int schuelerId) {
        faecherinfo.setLayout(new GridLayout(nr.getAnzFaecherBySchuelerId(schuelerId) + 1, 7));
        faecherinfo.setBorder(new LineBorder(Color.GREEN));
        faecherinfo.setBounds(0, 50, 1264, nr.getAnzFaecherBySchuelerId(schuelerId) + 1 * 70);
        faecherinfo.setBounds(0,50,1264, nr.getAnzFaecherBySchuelerId(schuelerId)*40);
        faecherinfo.setVisible(true);

        colmBezeichnung = new JLabel[7];


        colmBezeichnung[0] = new JLabel("Fach");
        colmBezeichnung[1] = new JLabel("Schriftlich");
        colmBezeichnung[2] = new JLabel("Muendlich");
        colmBezeichnung[3] = new JLabel("Zusatz");
        colmBezeichnung[4] = new JLabel("Endnote");
        colmBezeichnung[5] = new JLabel("Wertung");
        colmBezeichnung[6] = new JLabel("Einzelnoten");

        for (JLabel l : colmBezeichnung) {
            faecherinfo.add(l);
        }

        initFaecherNote(schuelerId);

        this.add(faecherinfo);
        faecherinfo.updateUI();

    }

    private void initFaecherNote(int schuelerId) {
        ArrayList<Integer> faecherIds = db.getFaecherIdBySchuelerId(schuelerId);

        fach = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        schriftlich = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        muendlich = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        zusatz = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        endnote = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        wertung = new JButton[nr.getAnzFaecherBySchuelerId(schuelerId)];
        fachansicht = new JButton[nr.getAnzFaecherBySchuelerId(schuelerId)];


        for (int i = 0; i < nr.getAnzFaecherBySchuelerId(schuelerId); i++) {
            fach[i] = new JLabel(db.getFachById(faecherIds.get(i)));
            schriftlich[i] = new JLabel(String.valueOf(db.getAvgNoteSchriftlich(faecherIds.get(i), schuelerId, actualHalbjahr)));
            muendlich[i] = new JLabel(String.valueOf(db.getAvgNoteMuendlich(faecherIds.get(i), schuelerId, actualHalbjahr)));
            zusatz[i] = new JLabel(String.valueOf(db.getAvgNoteZusatz(faecherIds.get(i), schuelerId, actualHalbjahr)));
            endnote[i] = new JLabel(String.valueOf(nr.calculateGrades(faecherIds.get(i), schuelerId, actualHalbjahr)));
            wertung[i] = new JButton("Umschalten");
            fachansicht[i] = new JButton("Mehr Informationen");


            int finali = i;
            wertung[finali].addActionListener(e -> {
                setupWertung(faecherIds.get(finali), schuelerId, finali);
            });

            fachansicht[finali].addActionListener(e -> {
                setupFachinfo(faecherIds.get(finali), schuelerId);
            });

            faecherinfo.add(fach[i]);
            faecherinfo.add(schriftlich[i]);
            faecherinfo.add(muendlich[i]);
            faecherinfo.add(zusatz[i]);
            faecherinfo.add(endnote[i]);
            faecherinfo.add(wertung[i]);
            faecherinfo.add(fachansicht[i]);
        }
    }

    private void reloadNoten(int schuelerid) {
        ArrayList<Integer> faecherIds = db.getFaecherIdBySchuelerId(schuelerid);

        for (int i = 0; i < nr.getAnzFaecherBySchuelerId(schuelerid); i++) {
            fach[i].setText(db.getFachById(faecherIds.get(i)));
            schriftlich[i].setText(String.valueOf(db.getAvgNoteSchriftlich(faecherIds.get(i), schuelerid, actualHalbjahr)));
            muendlich[i].setText(String.valueOf(db.getAvgNoteMuendlich(faecherIds.get(i), schuelerid, actualHalbjahr)));
            zusatz[i].setText(String.valueOf(db.getAvgNoteZusatz(faecherIds.get(i), schuelerid, actualHalbjahr)));
            endnote[i].setText(String.valueOf(nr.calculateGrades(faecherIds.get(i), schuelerid, actualHalbjahr)));
        }
    }

    private void setupNote(int fachId, int schuelerId, int arrayPlace) {
        schriftlich[arrayPlace].setText(String.valueOf(db.getAvgNoteSchriftlich(fachId, schuelerId, 1)));
        muendlich[arrayPlace].setText(String.valueOf(db.getAvgNoteMuendlich(fachId, schuelerId, 1)));
        zusatz[arrayPlace].setText(String.valueOf(db.getAvgNoteZusatz(fachId, schuelerId, 1)));

        for (ActionListener act : wertung[arrayPlace].getActionListeners()) {
            wertung[arrayPlace].removeActionListener(act);
        }

        wertung[arrayPlace].addActionListener(e -> {
            setupWertung(fachId, schuelerId, arrayPlace);
        });
    }


    private void setupWertung(int fachId, int schuelerId, int arrayPlace){
        schriftlich[arrayPlace].setText(Math.round(db.getWertungSchriftlich(fachId, db.getKlasseIdBySchuelerId(schuelerId))*100) + "%");
        muendlich[arrayPlace].setText(Math.round(db.getWertungMuendlich(fachId, db.getKlasseIdBySchuelerId(schuelerId))*100) + "%");
        zusatz[arrayPlace].setText(Math.round(db.getWertungZusatz(fachId, db.getKlasseIdBySchuelerId(schuelerId)) * 100) + "%");


        for (ActionListener act : wertung[arrayPlace].getActionListeners()) {
            wertung[arrayPlace].removeActionListener(act);
        }

        wertung[arrayPlace].addActionListener(e -> {
            setupNote(fachId, schuelerId, arrayPlace);
        });
    }

   private void setupFachinfo(int fachId, int schuelerId) {
        this.remove(faecherinfo);

        goBackToFaecherInfo.setBounds(this.getBounds().width - 200, userinfo.getBounds().height, 200, 50);
        goBackToFaecherInfo.addActionListener(e -> {
            this.add(faecherinfo);
            this.remove(goBackToFaecherInfo);
            this.remove(panelNoteSchriftlich);
            for (ActionListener act : goBackToFaecherInfo.getActionListeners()) {
                goBackToFaecherInfo.removeActionListener(act);
            }
            this.repaint();
        });

        panelNoteSchriftlich.setBounds(this.getBounds().width / 100, (userinfo.getBounds().height * 2) +50, (int) (this.getBounds().width)/2, this.getBounds().height / 5);
        panelNoteSchriftlich.setBorder(new LineBorder(Color.GREEN));
        panelNoteSchriftlich.setLayout(new GridLayout());
        /*setupNoteSchriftlichForFach(fachId, schuelerId, 1);

       // panelNoteSchriftlich.add(schriftlichSchriftzugTable);
       schriftlichSchriftzugTable.setVisible(true);*/

        tabelleSchriftlich = new JTable(db.getAnzNotenBySchuelerId(schuelerId,fachId,1,0),3);
        tabelleSchriftlich.setVisible(true);

       tabelleMuendlich = new JTable(db.getAnzNotenBySchuelerId(schuelerId,fachId,2,0),3);
       tabelleSchriftlich.setVisible(true);

       tabelleZusatz = new JTable(db.getAnzNotenBySchuelerId(schuelerId,fachId,3,0),3);
       tabelleSchriftlich.setVisible(true);

       panelNoteSchriftlich.add(tabelleSchriftlich);


        goBackToFaecherInfo.setVisible(true);
        this.add(goBackToFaecherInfo);
        panelNoteSchriftlich.setVisible(true);
        this.add(panelNoteSchriftlich);


        this.repaint();
    }


    private void setupNoteSchriftlichForFach(int fachId, int schuelerId, int halbjahr) {
        ArrayList<Integer> notenSchriftlichList = db.getNotenSchriftlich(fachId, schuelerId, halbjahr);

        this.schriftlichEinzelNoten = new JLabel[notenSchriftlichList.size()];
        for (int i = 0; i < notenSchriftlichList.size(); i++) {

            this.schriftlichEinzelNoten[i] = new JLabel();
            this.schriftlichEinzelNoten[i].setText(String.valueOf(notenSchriftlichList.get(i)));
            this.panelNoteSchriftlich.add(this.schriftlichEinzelNoten[i]);
            this.schriftlichEinzelNoten[i].setVisible(true);
        }
    }

    private int getActualHalbjahr() {
        return actualHalbjahr;
    }

    private void setActualHalbjahr(int actualHalbjahr) {
        this.actualHalbjahr = actualHalbjahr;
    }

}


