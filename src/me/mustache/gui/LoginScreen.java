package me.mustache.gui;

import me.mustache.logic.Database;
import me.mustache.logic.LoginLogic;
import me.mustache.logic.Notenrechner;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginScreen extends JFrame {

    //getInstances
    Database db = Database.getInstance();

    //Loginscreen
    JPanel loginScreen = new JPanel();
    JTextField benutzername = new HintTextField("Benutzername");
    JPasswordField passwort = new HintPasswordField("Passwort");
    JButton login = new JButton("Einloggen");

    public LoginScreen(){
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
            //if(new LoginLogic().checkPassword(benutzername.getText(), String.valueOf(passwort.getPassword()))) setupUserscreen(db.getSchuelerIdByUsername(benutzername.getText()));wrongPassword();
        });
    }




}
