package me.mustache.gui;

import me.mustache.logic.Database;
import me.mustache.logic.LoginLogic;
import me.mustache.logic.Notenrechner;
import me.mustache.logic.Semester;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Gui extends JFrame {

    private int actualHalbjahr;
    private boolean halbjahrFrameBool = false;
    private boolean hinzufuegen = false;
    private boolean loeschen = false;
    private int choosedHalbjahr;
    private String halbjahr;
    private int notenTypeToAdd;
    private int prognoseJaNein = 0;

    JButton[] halbjahrButtonGrade = new JButton[4];
    JPanel halbjahrPanel = new JPanel();

    private int prognose = 0;

    //getInstances
    private final Database db = Database.getInstance();
    private final Notenrechner nr = Notenrechner.getInstance();

    //Loginscreen
    private final JPanel loginScreen = new JPanel();
    private final JTextField benutzername = new HintTextField("Benutzername");
    private final JPasswordField passwort = new HintPasswordField("Passwort");
    private final JButton login = new JButton("Einloggen");
    private JFrame halbjahrFrame;

    //Userinfo
    private final JPanel userinfo = new JPanel();
    private JLabel name;
    private JLabel username;
    private JLabel userClass;
    private JLabel notenschnitt;
    private JButton halbjahrButton;
    private JCheckBox prognoseCheckBox;
    private JLabel prognoseLabel;

    //HalbjahrFenster
    private JButton[] halbjahrButtons;

    //Faecherinfo
    private final JPanel faecherinfo = new JPanel();
    private JLabel[] colmBezeichnung;
    private JLabel[] fach;
    private JLabel[] schriftlich;
    private JLabel[] muendlich;
    private JLabel[] zusatz;
    private JLabel[] endnote;
    private JButton[] wertung;
    private JButton[] fachansicht;

    //Fachinfo
    private final JPanel fachinfo = new JPanel();
    private final JButton goBackToFaecherInfo = new JButton("Zurück zur Übersicht");
    private final JPanel panelNoteSchriftlich = new JPanel();
    private JPanel panelNoteMuendlich = new JPanel();
    private JPanel panelNoteZusatz = new JPanel();
    private final JLabel schriftlichSchriftzug = new JLabel("Schriftlich");
    private JLabel[] schriftlichEinzelNoten;
    private JLabel[] schriftlichNotenId;
    private JLabel[] schriftlichNoteSemester;
    private JLabel[] schriftlichPrognose;
    private JLabel[] muendlichEinzelNoten;
    private JLabel[] muendlichNotenId;
    private JLabel[] muendlichNoteSemester;
    private JLabel[] muendlichPrognose;
    private JLabel[] zusatzEinzelNoten;
    private JLabel[] zusatzNotenId;
    private JLabel[] zusatzNoteSemester;
    private JLabel[] zusatzPrognose;
    private JButton noteHinzufuegen = new JButton("Note hinzufügen");
    private JButton noteLoeschen = new JButton("Note löschen");
    private JButton schriftlichButton = new JButton("Schriftlich");
    private JButton muendlichButton = new JButton("Mündlich");
    private JButton zusatzButton = new JButton("Zusätzliches Projekt");
    private JFrame notenFrame;
    private JLabel idTopper;
    private JLabel noteTopper;
    private JLabel semesterTopper;
    private JLabel prognoseTopper;
    private JOptionPane optionpane = new JOptionPane();

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
        userinfo.setBounds(0, 0, 1154, 50);
        userinfo.setVisible(true);
        name = new JLabel("Name: " + db.getSchuelerVornameBySchuelerId(schuelerId) + " " + db.getSchuelerNameBySchuelerId(schuelerId));
        username = new JLabel("Benutzername: " + db.getUsernameBySchuelerId(schuelerId));
        userClass = new JLabel(("Klasse: " + db.getKlasseBySchuelerId(schuelerId)));
        notenschnitt = new JLabel("Notenschnitt: " + (double) Math.round(nr.calculateAvgGrade(schuelerId, actualHalbjahr, prognose)));
        halbjahrButton = new JButton("Halbjahr ändern");
        prognoseCheckBox = new JCheckBox();
        prognoseLabel = new JLabel("Prognose: ");

        prognoseLabel.setBounds(1164, 0, 70, 50);
        prognoseCheckBox.setBounds(1234, 0, 50, 50);

        prognoseCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    prognose = 1;
                    reloadNoten(schuelerId);
                } else {
                    prognose = 0;
                    reloadNoten(schuelerId);
                }
            }
        });

        userinfo.add(name);
        userinfo.add(username);
        userinfo.add(userClass);
        userinfo.add(notenschnitt);
        userinfo.add(halbjahrButton);

        halbjahrButton.addActionListener(e -> {
            if (!halbjahrFrameBool) halbjahrAendern(schuelerId);
        });

        this.add(prognoseCheckBox);
        this.add(prognoseLabel);
        this.add(userinfo);
        userinfo.updateUI();

        setupFaecherinfo(schuelerId);
    }

    private void halbjahrAendern(int schuelerid) {
        halbjahrFrameBool = true;
        halbjahrFrame = new JFrame("Halbjahr ändern");
        halbjahrFrame.setUndecorated(true);
        halbjahrFrame.setSize(250, 238);
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
        for (JButton btn : halbjahrButtons) {
            btn.setBounds(0, 0 + k, 250, 50);
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
        faecherinfo.setBounds(0, 50, 1264, nr.getAnzFaecherBySchuelerId(schuelerId) * 40);
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
            schriftlich[i] = new JLabel(String.valueOf(db.getAvgNoteSchriftlich(faecherIds.get(i), schuelerId, actualHalbjahr, prognose)));
            muendlich[i] = new JLabel(String.valueOf(db.getAvgNoteMuendlich(faecherIds.get(i), schuelerId, actualHalbjahr, prognose)));
            zusatz[i] = new JLabel(String.valueOf(db.getAvgNoteZusatz(faecherIds.get(i), schuelerId, actualHalbjahr, prognose)));
            endnote[i] = new JLabel(String.valueOf(nr.calculateGrades(faecherIds.get(i), schuelerId, actualHalbjahr, prognose)));
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
            schriftlich[i].setText(String.valueOf(db.getAvgNoteSchriftlich(faecherIds.get(i), schuelerid, actualHalbjahr, prognose)));
            muendlich[i].setText(String.valueOf(db.getAvgNoteMuendlich(faecherIds.get(i), schuelerid, actualHalbjahr, prognose)));
            zusatz[i].setText(String.valueOf(db.getAvgNoteZusatz(faecherIds.get(i), schuelerid, actualHalbjahr, prognose)));
            endnote[i].setText(String.valueOf(nr.calculateGrades(faecherIds.get(i), schuelerid, actualHalbjahr, prognose)));
        }
    }

    private void setupNote(int fachId, int schuelerId, int arrayPlace) {
        schriftlich[arrayPlace].setText(String.valueOf(db.getAvgNoteSchriftlich(fachId, schuelerId, getActualHalbjahr(), prognose)));
        muendlich[arrayPlace].setText(String.valueOf(db.getAvgNoteMuendlich(fachId, schuelerId, getActualHalbjahr(), prognose)));
        zusatz[arrayPlace].setText(String.valueOf(db.getAvgNoteZusatz(fachId, schuelerId, getActualHalbjahr(), prognose)));

        for (ActionListener act : wertung[arrayPlace].getActionListeners()) {
            wertung[arrayPlace].removeActionListener(act);
        }

        wertung[arrayPlace].addActionListener(e -> {
            setupWertung(fachId, schuelerId, arrayPlace);
        });
    }

    private void setupWertung(int fachId, int schuelerId, int arrayPlace) {
        schriftlich[arrayPlace].setText(Math.round(db.getWertungSchriftlich(fachId, db.getKlasseIdBySchuelerId(schuelerId)) * 100) + "%");
        muendlich[arrayPlace].setText(Math.round(db.getWertungMuendlich(fachId, db.getKlasseIdBySchuelerId(schuelerId)) * 100) + "%");
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
            this.remove(panelNoteMuendlich);
            this.remove(panelNoteZusatz);
            this.remove(noteHinzufuegen);
            this.remove(noteLoeschen);
            for (ActionListener act : goBackToFaecherInfo.getActionListeners()) {
                goBackToFaecherInfo.removeActionListener(act);
            }
            panelNoteSchriftlich.removeAll();
            panelNoteMuendlich.removeAll();
            panelNoteZusatz.removeAll();
            this.repaint();
        });

        setupListenerAddRemoveButton(fachId, schuelerId);
        setupNoteSchriftlichForFach(fachId, schuelerId);
        setupNoteMuendlichForFach(fachId, schuelerId);
        setupNoteZusatzForFach(fachId, schuelerId);


        goBackToFaecherInfo.setVisible(true);
        this.add(goBackToFaecherInfo);
        noteHinzufuegen.setVisible(true);
        this.add(noteHinzufuegen);
        noteLoeschen.setVisible(true);
        this.add(noteLoeschen);
        panelNoteMuendlich.setVisible(true);
        this.add(panelNoteMuendlich);
        panelNoteZusatz.setVisible(true);
        this.add(panelNoteZusatz);
        this.repaint();
    }

    private void setupListenerAddRemoveButton(int fachId, int schuelerId) {
        noteHinzufuegen.setBounds(0, 50, 200, 50);
        noteLoeschen.setBounds(201, 50, 200, 50);

        ActionListener hinzufuegenOderLoeschenButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(ActionListener act : schriftlichButton.getActionListeners()) {
                    schriftlichButton.removeActionListener(act);
                }
                for (ActionListener act : muendlichButton.getActionListeners()) {
                    muendlichButton.removeActionListener(act);
                }
                for (ActionListener act : zusatzButton.getActionListeners()) {
                    zusatzButton.removeActionListener(act);
                }
                schriftlichButton.addActionListener(act -> {
                    notenTypeToAdd = 1;
                    notenFrame.remove(schriftlichButton);
                    notenFrame.remove(muendlichButton);
                    notenFrame.remove(zusatzButton);
                    noteHinzufuegen(fachId, schuelerId);
                });
                muendlichButton.addActionListener(act -> {
                    notenTypeToAdd = 2;
                    notenFrame.remove(schriftlichButton);
                    notenFrame.remove(muendlichButton);
                    notenFrame.remove(zusatzButton);
                    noteHinzufuegen(fachId, schuelerId);
                });
                zusatzButton.addActionListener(act -> {
                    notenTypeToAdd = 3;
                    notenFrame.remove(schriftlichButton);
                    notenFrame.remove(muendlichButton);
                    notenFrame.remove(zusatzButton);
                    noteHinzufuegen(fachId, schuelerId);
                });
                notenFrame = new JFrame();
                notenFrame.setSize(250, 220);
                notenFrame.setLayout(null);
                notenFrame.setAlwaysOnTop(true);
                notenFrame.dispose();
                notenFrame.setUndecorated(true);
                notenFrame.setLocationRelativeTo(null);
                notenFrame.setResizable(false);
                notenFrame.setVisible(true);
                schriftlichButton.setBounds(0, 0, 250, 60);
                muendlichButton.setBounds(0, 61, 250, 60);
                zusatzButton.setBounds(0, 122, 250, 60);
                notenFrame.add(schriftlichButton);
                notenFrame.add(muendlichButton);
                notenFrame.add(zusatzButton);
            }
        };
        noteHinzufuegen.addActionListener(hinzufuegenOderLoeschenButton);
        noteLoeschen.addActionListener(e -> {
            noteLoeschen(fachId, schuelerId);
        });
    }

    private void noteLoeschen(int fachId, int schuelerId) {
        JFrame deleteGrade = new JFrame();
        JTextField enterField = new JTextField();
        JButton enter = new JButton("Eingabe");
        enter.addActionListener(e -> {
            if(enterField.getText() != null) {
                if(enterField.getText() != null) {
                    db.removeNote(Integer.parseInt(enterField.getText()), schuelerId);
                    deleteGrade.dispose();
                    panelNoteSchriftlich.removeAll();
                    panelNoteMuendlich.removeAll();
                    panelNoteZusatz.removeAll();
                    setupNoteSchriftlichForFach(fachId, schuelerId);
                    setupNoteMuendlichForFach(fachId, schuelerId);
                    setupNoteZusatzForFach(fachId, schuelerId);
                }
            }
        });
        JButton cancel = new JButton("Abbrechen");
        cancel.addActionListener(e -> {
            deleteGrade.dispose();
            panelNoteSchriftlich.removeAll();
            panelNoteMuendlich.removeAll();
            panelNoteZusatz.removeAll();
            setupNoteSchriftlichForFach(fachId, schuelerId);
            setupNoteMuendlichForFach(fachId, schuelerId);
            setupNoteZusatzForFach(fachId, schuelerId);
        });

        deleteGrade.setSize(250, 220);
        deleteGrade.setLayout(new GridLayout(4,1));
        deleteGrade.setAlwaysOnTop(true);
        deleteGrade.setLocationRelativeTo(null);
        deleteGrade.setResizable(false);
        deleteGrade.setVisible(true);

        deleteGrade.add(new JLabel("Noten ID Eingeben:"));
        deleteGrade.add(enterField);
        deleteGrade.add(enter);
        deleteGrade.add(cancel);
    }

    private void noteHinzufuegen(int fachId, int schuelerId) {
        notenFrame.dispose();
        notenFrame = new JFrame();
        notenFrame.setSize(250, 220);
        notenFrame.setLayout(null);
        notenFrame.setAlwaysOnTop(true);
        notenFrame.setLocationRelativeTo(null);
        notenFrame.setUndecorated(true);
        notenFrame.setResizable(false);
        notenFrame.setVisible(true);
        halbjahrPanel.setBounds(0,0,250,220);
        halbjahrPanel.setVisible(true);
        notenFrame.add(halbjahrPanel);
        halbjahrButtonGrade[0] = new JButton("1. Halbjahr");
        halbjahrButtonGrade[1] = new JButton("2. Halbjahr");
        halbjahrButtonGrade[2] = new JButton("3. Halbjahr");
        halbjahrButtonGrade[3] = new JButton("4. Halbjahr");
        halbjahrButtonGrade[0].addActionListener(e -> {
            choosedHalbjahr = 1;
            addGradeWindow(fachId, schuelerId);
            halbjahrPanel.removeAll();
            halbjahrPanel.revalidate();
            notenFrame.dispose();
        });
        halbjahrButtonGrade[1].addActionListener(e -> {
            choosedHalbjahr = 2;
            addGradeWindow(fachId, schuelerId);
            halbjahrPanel.removeAll();
            halbjahrPanel.revalidate();
            notenFrame.dispose();
        });
        halbjahrButtonGrade[2].addActionListener(e -> {
            choosedHalbjahr = 3;
            addGradeWindow(fachId, schuelerId);
            halbjahrPanel.removeAll();
            halbjahrPanel.revalidate();
            notenFrame.dispose();
        });
        halbjahrButtonGrade[3].addActionListener(e -> {
            choosedHalbjahr = 4;
            addGradeWindow(fachId, schuelerId);
            halbjahrPanel.removeAll();
            halbjahrPanel.revalidate();
            notenFrame.dispose();
        });
        halbjahrPanel.setLayout(new GridLayout(4,1));
        halbjahrPanel.add(halbjahrButtonGrade[0]);
        halbjahrPanel.add(halbjahrButtonGrade[1]);
        halbjahrPanel.add(halbjahrButtonGrade[2]);
        halbjahrPanel.add(halbjahrButtonGrade[3]);
        halbjahrPanel.updateUI();
    }

    private void addGradeWindow(int fachId, int schuelerId) {
        JFrame addGradeWindow = new JFrame();
        addGradeWindow.setSize(250, 220);
        addGradeWindow.setLayout(new GridLayout(6,1));
        addGradeWindow.setAlwaysOnTop(true);
        addGradeWindow.setLocationRelativeTo(null);
        addGradeWindow.setResizable(false);
        addGradeWindow.setVisible(true);

        JTextField enterField = new JTextField();
        JCheckBox prognoseJaNeinBox = new JCheckBox();
        prognoseJaNeinBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    prognoseJaNein = 1;
                } else {
                    prognoseJaNein = 0;
                }
            }
        });
        JButton enter = new JButton("Eingabe");
        enter.addActionListener(e -> {
            if(Integer.parseInt(enterField.getText()) <= 15 && Integer.parseInt(enterField.getText()) >= 0) {
                if(enterField.getText() != null) {
                    db.addNote(Integer.parseInt(enterField.getText()), notenTypeToAdd, fachId, schuelerId, choosedHalbjahr, prognoseJaNein);
                    addGradeWindow.dispose();
                    panelNoteSchriftlich.removeAll();
                    panelNoteMuendlich.removeAll();
                    panelNoteZusatz.removeAll();
                    for (ActionListener act : schriftlichButton.getActionListeners()) {
                        schriftlichButton.removeActionListener(act);
                    }
                    for (ActionListener act : muendlichButton.getActionListeners()) {
                        muendlichButton.removeActionListener(act);
                    }
                    for (ActionListener act : zusatzButton.getActionListeners()) {
                        zusatzButton.removeActionListener(act);
                    }
                    setupNoteSchriftlichForFach(fachId, schuelerId);
                    setupNoteMuendlichForFach(fachId, schuelerId);
                    setupNoteZusatzForFach(fachId, schuelerId);
                }
            }
        });
        JButton cancel = new JButton("Abbrechen");
        cancel.addActionListener(e -> {
            addGradeWindow.dispose();
            panelNoteSchriftlich.removeAll();
            panelNoteMuendlich.removeAll();
            panelNoteZusatz.removeAll();
            setupNoteSchriftlichForFach(fachId, schuelerId);
            setupNoteMuendlichForFach(fachId, schuelerId);
            setupNoteZusatzForFach(fachId, schuelerId);
        });

        addGradeWindow.add(new JLabel("Noten Eingeben:"));
        addGradeWindow.add(enterField);
        addGradeWindow.add(new JLabel("Prognose:"));
        addGradeWindow.add(prognoseJaNeinBox);
        addGradeWindow.add(enter);
        addGradeWindow.add(cancel);
    }

    private void setupNoteSchriftlichForFach(int fachId, int schuelerId) {
        ArrayList<Integer> notenIdSchriftlich = db.getNotenId(fachId, schuelerId, 1);
        ArrayList<Integer> notenSchriftlichList = db.getNotenSchriftlich(fachId, schuelerId);
        panelNoteSchriftlich.setBounds(0, 100, 426, 30 * notenIdSchriftlich.size() + 30);
        panelNoteSchriftlich.setBorder(new LineBorder(Color.GREEN));
        panelNoteSchriftlich.setLayout(new GridLayout(notenIdSchriftlich.size() + 1, 4));
        panelNoteSchriftlich.setVisible(true);
        schriftlichSchriftzug.setBounds(0, 100, 426, 30);
        idTopper = new JLabel("ID");
        noteTopper = new JLabel("Note");
        semesterTopper = new JLabel("Halbjahr");
        prognoseTopper = new JLabel("Prognose");
        panelNoteSchriftlich.add(idTopper);
        panelNoteSchriftlich.add(noteTopper);
        panelNoteSchriftlich.add(semesterTopper);
        panelNoteSchriftlich.add(prognoseTopper);

        schriftlichNotenId = new JLabel[notenSchriftlichList.size()];
        schriftlichEinzelNoten = new JLabel[notenSchriftlichList.size()];
        schriftlichNoteSemester = new JLabel[notenSchriftlichList.size()];
        schriftlichPrognose = new JLabel[notenSchriftlichList.size()];

        for (int i = 0; i < notenSchriftlichList.size(); i++) {
            schriftlichNotenId[i] = new JLabel();
            schriftlichEinzelNoten[i] = new JLabel();
            schriftlichNoteSemester[i] = new JLabel();
            schriftlichPrognose[i] = new JLabel();
            schriftlichNotenId[i].setText(String.valueOf(notenIdSchriftlich.get(i)));
            schriftlichEinzelNoten[i].setText(String.valueOf(notenSchriftlichList.get(i)));
            schriftlichNoteSemester[i].setText(String.valueOf(db.getHalbJahrByNotenId(notenIdSchriftlich.get(i))));
            if (db.getPrognoseByNotenId(notenIdSchriftlich.get(i)) == 1) {
                schriftlichPrognose[i].setText("Ja");
            } else {
                schriftlichPrognose[i].setText("Nein");
            }

            panelNoteSchriftlich.add(schriftlichNotenId[i]);
            panelNoteSchriftlich.add(schriftlichEinzelNoten[i]);
            panelNoteSchriftlich.add(schriftlichNoteSemester[i]);
            panelNoteSchriftlich.add(schriftlichPrognose[i]);
        }
        this.add(panelNoteSchriftlich);
        panelNoteSchriftlich.updateUI();
    }

    private void setupNoteMuendlichForFach(int fachId, int schuelerId) {
        ArrayList<Integer> notenIdMuendlich = db.getNotenId(fachId, schuelerId, 2);
        ArrayList<Integer> notenMuendlichList = db.getNotenMuendlich(fachId, schuelerId);
        panelNoteMuendlich.setBounds(427, 100, 426, 30 * notenIdMuendlich.size() + 30);
        panelNoteMuendlich.setBorder(new LineBorder(Color.GREEN));
        panelNoteMuendlich.setLayout(new GridLayout(notenIdMuendlich.size() + 1, 4));
        panelNoteMuendlich.setVisible(true);
        idTopper = new JLabel("ID");
        noteTopper = new JLabel("Note");
        semesterTopper = new JLabel("Halbjahr");
        prognoseTopper = new JLabel("Prognose");
        //muendlichSchriftzug.setBounds(0,100,426,30);
        panelNoteMuendlich.add(idTopper);
        panelNoteMuendlich.add(noteTopper);
        panelNoteMuendlich.add(semesterTopper);
        panelNoteMuendlich.add(prognoseTopper);

        muendlichNotenId = new JLabel[notenMuendlichList.size()];
        muendlichEinzelNoten = new JLabel[notenMuendlichList.size()];
        muendlichNoteSemester = new JLabel[notenMuendlichList.size()];
        muendlichPrognose = new JLabel[notenMuendlichList.size()];

        for (int i = 0; i < notenMuendlichList.size(); i++) {
            muendlichNotenId[i] = new JLabel();
            muendlichEinzelNoten[i] = new JLabel();
            muendlichNoteSemester[i] = new JLabel();
            muendlichPrognose[i] = new JLabel();
            muendlichNotenId[i].setText(String.valueOf(notenIdMuendlich.get(i)));
            muendlichEinzelNoten[i].setText(String.valueOf(notenMuendlichList.get(i)));
            muendlichNoteSemester[i].setText(String.valueOf(db.getHalbJahrByNotenId(notenIdMuendlich.get(i))));
            if (db.getPrognoseByNotenId(notenIdMuendlich.get(i)) == 1) {
                muendlichPrognose[i].setText("Ja");
            } else {
                muendlichPrognose[i].setText("Nein");
            }

            panelNoteMuendlich.add(muendlichNotenId[i]);
            panelNoteMuendlich.add(muendlichEinzelNoten[i]);
            panelNoteMuendlich.add(muendlichNoteSemester[i]);
            panelNoteMuendlich.add(muendlichPrognose[i]);
        }
        this.add(panelNoteMuendlich);
        panelNoteMuendlich.updateUI();
    }

    private void setupNoteZusatzForFach(int fachId, int schuelerId) {
        ArrayList<Integer> notenIdZusatz = db.getNotenId(fachId, schuelerId, 3);
        ArrayList<Integer> notenZusatzList = db.getNotenZusatz(fachId, schuelerId);
        panelNoteZusatz.setBounds(854, 100, 426, 30 * notenIdZusatz.size() + 30);
        panelNoteZusatz.setBorder(new LineBorder(Color.GREEN));
        panelNoteZusatz.setLayout(new GridLayout(notenIdZusatz.size() + 1, 4));
        panelNoteZusatz.setVisible(true);
        idTopper = new JLabel("ID");
        noteTopper = new JLabel("Note");
        semesterTopper = new JLabel("Halbjahr");
        prognoseTopper = new JLabel("Prognose");
        //muendlichSchriftzug.setBounds(0,100,426,30);
        panelNoteZusatz.add(idTopper);
        panelNoteZusatz.add(noteTopper);
        panelNoteZusatz.add(semesterTopper);
        panelNoteZusatz.add(prognoseTopper);

        zusatzNotenId = new JLabel[notenZusatzList.size()];
        zusatzEinzelNoten = new JLabel[notenZusatzList.size()];
        zusatzNoteSemester = new JLabel[notenZusatzList.size()];
        zusatzPrognose = new JLabel[notenZusatzList.size()];

        for (int i = 0; i < notenZusatzList.size(); i++) {
            zusatzNotenId[i] = new JLabel();
            zusatzEinzelNoten[i] = new JLabel();
            zusatzNoteSemester[i] = new JLabel();
            zusatzPrognose[i] = new JLabel();
            zusatzNotenId[i].setText(String.valueOf(notenIdZusatz.get(i)));
            zusatzEinzelNoten[i].setText(String.valueOf(notenZusatzList.get(i)));
            zusatzNoteSemester[i].setText(String.valueOf(db.getHalbJahrByNotenId(notenIdZusatz.get(i))));
            if (db.getPrognoseByNotenId(notenIdZusatz.get(i)) == 1) {
                zusatzPrognose[i].setText("Ja");
            } else {
                zusatzPrognose[i].setText("Nein");
            }

            panelNoteZusatz.add(zusatzNotenId[i]);
            panelNoteZusatz.add(zusatzEinzelNoten[i]);
            panelNoteZusatz.add(zusatzNoteSemester[i]);
            panelNoteZusatz.add(zusatzPrognose[i]);
        }
        this.add(panelNoteZusatz);
        panelNoteZusatz.updateUI();
    }

    private int getActualHalbjahr() {
        return actualHalbjahr;
    }

    private void setActualHalbjahr(int actualHalbjahr) {
        this.actualHalbjahr = actualHalbjahr;
    }

}


