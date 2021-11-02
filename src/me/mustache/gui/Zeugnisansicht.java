package me.mustache.gui;

import me.mustache.logic.Database;
import me.mustache.logic.Notenrechner;

import javax.swing.border.LineBorder;
import javax.swing.*;
import java.awt.*;


public class Zeugnisansicht extends JFrame {

    Database db = Database.getInstance();
    Notenrechner nr = Notenrechner.getInstance();


    JPanel zeugnisScreen;
    JLabel[] fach;
    JLabel[] note;


    public Zeugnisansicht() {
        createWindow();
    }


    private void createWindow() {


        this.setSize(1280, 720);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        createScreen();

    }

    private void createScreen() {

        zeugnisScreen = new JPanel();
        zeugnisScreen.setBorder(new LineBorder(Color.GREEN));
        zeugnisScreen.setBounds(100, 100, 1080, 520);
        zeugnisScreen.setVisible(true);
        this.add(zeugnisScreen);

        createLabels();

    }

    private void createLabels(){
        for(int i=0; i<23;i++){
            fach = new JLabel[i];
        }

        for(int i=0;i<23*4;i++){
            note = new JLabel[i];
        }





    }

}


