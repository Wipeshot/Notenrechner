package me.mustache.gui;

import me.mustache.logic.Database;
import me.mustache.logic.Notenrechner;

import javax.swing.border.LineBorder;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;


public class GradeScreen extends JFrame {

    Database db = Database.getInstance();
    Notenrechner nr = Notenrechner.getInstance();

    private JPanel firstPanel;

    private JLabel nameLabel;
    private JLabel subjectLabel;
    private JLabel pointsLabel;
    private JLabel gradeLabel;
    private JLabel part1;
    private JLabel part2;
    private JLabel part3;
    private JLabel part4;
    private JLabel[] subject;
    private JLabel[] points;
    private JLabel[] grade;
    private JLabel specialLabel;
    private JLabel spTopic;
    private JLabel spValuation;
    private JLabel spGrade;
    private JLabel examsubLabel;
    private JLabel exampointsLabel;
    private JLabel examgradeLabel;
    private JLabel[] examSubject ;
    private JLabel[] pointsS ;
    private JLabel[] pointsM ;
    private JLabel[] examGrade ;
    private JLabel coursePoints;
    private JLabel examPoints;
    private JLabel maxPoints;
    private JLabel maxGrade;
    private JLabel speakingLanguages;
    private JLabel firstLanguage;
    private JLabel secondLanguage;
    private JLabel locAndDate;
    private JLabel jury;
    private JLabel headmaster;
    private JLabel seal;
    private JLabel signature;


    public GradeScreen(){

        this.setSize(1280,1000);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);

        createPanels();
    }

    private void createPanels(){

        firstPanel = new JPanel(new GridLayout());
        firstPanel.setVisible(true);
        firstPanel.setBounds(100,300,400,600);
        firstPanel.setBorder(new LineBorder(Color.GREEN));
        this.add(firstPanel);


        createLabels();

    }



        private void createLabels(){
             nameLabel = new JLabel();
             subjectLabel = new JLabel();
             pointsLabel = new JLabel();
             gradeLabel = new JLabel();
             part1 = new JLabel();
             part2 = new JLabel();
             part3 = new JLabel();
             part4 = new JLabel();
             subject = new JLabel[23];
             points = new JLabel[92];
             grade = new JLabel[23];
             specialLabel= new JLabel();
             spTopic = new JLabel();
             spValuation = new JLabel();
             spGrade = new JLabel();
             examsubLabel = new JLabel();
             exampointsLabel = new JLabel();
             examgradeLabel = new JLabel();
             examSubject = new JLabel[5];
             pointsS = new JLabel[5];
             pointsM = new JLabel[5];
             examGrade = new JLabel[5];
             coursePoints = new JLabel();
             examPoints = new JLabel();
             maxPoints = new JLabel();
             maxGrade = new JLabel();
             speakingLanguages = new JLabel();
             firstLanguage = new JLabel();
             secondLanguage = new JLabel();
             locAndDate = new JLabel();
             jury = new JLabel();
             headmaster = new JLabel();
             seal = new JLabel();
             signature = new JLabel();

        nameLabel.setBorder(new LineBorder(Color.GREEN));
        nameLabel.setBounds(10,10,1240,100);
        nameLabel.setVisible(true);
        this.add(nameLabel);

        subjectLabel.setBorder(new LineBorder(Color.GREEN));
        subjectLabel.setBounds(10,15,30,20);
        subjectLabel.setVisible(true);
        this.add(subjectLabel);




    }



}

