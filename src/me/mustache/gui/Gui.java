package me.mustache.gui;

import me.mustache.logic.Database;
import me.mustache.logic.LoginLogic;
import me.mustache.logic.Notenrechner;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class Gui extends JFrame {

    //getInstances
    Database db = Database.getInstance();
    Notenrechner nr = Notenrechner.getInstance();

    //Loginscreen
    JPanel loginScreen = new JPanel();
    JTextField benutzername = new HintTextField("Benutzername");
    JPasswordField passwort = new HintPasswordField("Passwort");
    JButton login = new JButton("Einloggen");

    //Userinfo
    JPanel userinfo = new JPanel();
    JLabel name;
    JLabel username;
    JLabel userClass;
    JLabel notenschnitt;

    //Faecherinfo
    JPanel faecherinfo = new JPanel();
    JLabel[] colmBezeichnung;
    JLabel[] fach;
    JLabel[] schriftlich;
    JLabel[] muendlich;
    JLabel[] zusatz;
    JLabel[] endnote;
    JButton[] wertung;
    JButton[] fachansicht;

    //Fachinfo
    JPanel fachinfo = new JPanel();
    JTable einzelNoten;
    DefaultTableModel einzelNote = new DefaultTableModel();
    JScrollPane notenScroll = new JScrollPane();



    public Gui() {
        createWindow();
    }

    public void createWindow() {
       this.setSize(1280,720);
       this.setResizable(false);
       this.setDefaultCloseOperation(EXIT_ON_CLOSE);
       this.setLayout(null);
       this.setVisible(true);

       loginScreen();
    }


    public void loginScreen(){
        loginScreen.setLayout(new GridLayout(3,1));
        loginScreen.setBorder(new LineBorder(Color.GREEN));
        loginScreen.setBounds(1280/2-200,720/2-100, 400, 200);
        loginScreen.setVisible(true);

        loginScreen.add(benutzername);
        loginScreen.add(passwort);
        loginScreen.add(login);

        this.add(loginScreen);
        loginScreen.updateUI();

        login.addActionListener(e -> {
            if(new LoginLogic().checkPassword(benutzername.getText(), String.valueOf(passwort.getPassword()))) setupUserscreen(db.getSchuelerIdByUsername(benutzername.getText()));wrongPassword();
        });
    }

    public void setupUserscreen(int schuelerId){
        this.remove(loginScreen);
        this.repaint();

        userinfo.setLayout(new GridLayout(1,4));
        userinfo.setBorder(new LineBorder(Color.GREEN));
        userinfo.setBounds(0,0,1264,50);
        userinfo.setVisible(true);

        name = new JLabel("Name: " + db.getSchuelerVornameBySchuelerId(schuelerId) + " " + db.getSchuelerNameBySchuelerId(schuelerId));
        username = new JLabel("Benutzername: " + db.getUsernameBySchuelerId(schuelerId));
        userClass = new JLabel(("Klasse: " + db.getKlasseBySchuelerId(schuelerId)));
        notenschnitt = new JLabel("Notenschnitt: " + (double) Math.round(nr.calculateAvgGrade(schuelerId)));

        userinfo.add(name);
        userinfo.add(username);
        userinfo.add(userClass);
        userinfo.add(notenschnitt);

        this.add(userinfo);
        userinfo.updateUI();

        setupFaecherinfo(schuelerId);
    }

    public void wrongPassword(){
        benutzername.setBackground(Color.RED);
        passwort.setBackground(Color.RED);
    }

    public void setupFaecherinfo(int schuelerId){
        faecherinfo.setLayout(new GridLayout(nr.getAnzFaecherBySchuelerId(schuelerId)+1,7));
        faecherinfo.setBorder(new LineBorder(Color.GREEN));
        faecherinfo.setBounds(0,50,1264,nr.getAnzFaecherBySchuelerId(schuelerId)+1*70);
        faecherinfo.setVisible(true);

        colmBezeichnung = new JLabel[7];


        colmBezeichnung[0] = new JLabel("Fach");
        colmBezeichnung[1] = new JLabel("Schriftlich");
        colmBezeichnung[2] = new JLabel("Muendlich");
        colmBezeichnung[3] = new JLabel("Zusatz");
        colmBezeichnung[4] = new JLabel("Endnote");
        colmBezeichnung[5] = new JLabel("Wertung");
        colmBezeichnung[6] = new JLabel("Einzelnoten");

        for (JLabel l : colmBezeichnung){
            faecherinfo.add(l);
        }

        initFaecherNote(schuelerId);

        this.add(faecherinfo);
        faecherinfo.updateUI();

    }

    private void initFaecherNote(int schuelerId){
        ArrayList<Integer> faecherIds = db.getFaecherIdBySchuelerId(schuelerId);

        fach = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        schriftlich = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        muendlich = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        zusatz = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        endnote = new JLabel[nr.getAnzFaecherBySchuelerId(schuelerId)];
        wertung = new JButton[nr.getAnzFaecherBySchuelerId(schuelerId)];
        fachansicht = new JButton[nr.getAnzFaecherBySchuelerId(schuelerId)];


        for (int i = 0; i< nr.getAnzFaecherBySchuelerId(schuelerId); i++){
            fach[i] = new JLabel(db.getFachById(faecherIds.get(i)));
            schriftlich[i] = new JLabel(String.valueOf(db.getAvgNoteSchriftlich(faecherIds.get(i), schuelerId)));
            muendlich[i] = new JLabel(String.valueOf(db.getAvgNoteMuendlich(faecherIds.get(i), schuelerId)));
            zusatz[i] = new JLabel(String.valueOf(db.getAvgNoteZusatz(faecherIds.get(i), schuelerId)));
            endnote[i] = new JLabel(String.valueOf(nr.calculateGrades(faecherIds.get(i), schuelerId)));
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

    private void setupNote(int fachId, int schuelerId, int arrayPlace){
        schriftlich[arrayPlace].setText(String.valueOf(db.getAvgNoteSchriftlich(fachId, schuelerId)));
        muendlich[arrayPlace].setText(String.valueOf(db.getAvgNoteMuendlich(fachId, schuelerId)));
        zusatz[arrayPlace].setText(String.valueOf(db.getAvgNoteZusatz(fachId, schuelerId)));

        for (ActionListener act :  wertung[arrayPlace].getActionListeners()){
            wertung[arrayPlace].removeActionListener(act);
        }

        wertung[arrayPlace].addActionListener(e -> {
            setupWertung(fachId,schuelerId,arrayPlace);
        });
    }

    private void setupWertung(int fachId, int schuelerId, int arrayPlace){
        schriftlich[arrayPlace].setText(Math.round(db.getWertungSchriftlich(fachId)*100) + "%");
        muendlich[arrayPlace].setText(Math.round(db.getWertungMuendlich(fachId)*100) + "%");
        zusatz[arrayPlace].setText(Math.round(db.getWertungZusatz(fachId) * 100) + "%");

        for (ActionListener act :  wertung[arrayPlace].getActionListeners()){
            wertung[arrayPlace].removeActionListener(act);
        }

        wertung[arrayPlace].addActionListener(e -> {
            setupNote(fachId, schuelerId, arrayPlace);
        });
    }

    private void setupFachinfo(int fachId, int schuelerId){
        this.remove(faecherinfo);

        einzelNoten.setBounds(0,50,1280,400);

        einzelNote.addColumn("Schriftlich");
        einzelNote.addColumn("Muendlich");
        einzelNote.addColumn("Zusatz");

        /*for (String str : db.getNotenSchriftlich(fachId, schuelerId)){
            einzelNote.
        }*/

        einzelNoten = new JTable(einzelNote);
        einzelNoten.add(notenScroll);
        einzelNoten.setVisible(true);
        this.add(einzelNoten);

    }

}


class HintTextField extends JTextField implements FocusListener {

    private final String hint;
    private boolean showingHint;

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}

class HintPasswordField extends JPasswordField implements FocusListener {

    private final String hint;
    private boolean showingHint;

    public HintPasswordField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}


