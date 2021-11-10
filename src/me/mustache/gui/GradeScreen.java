package me.mustache.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static java.awt.Color.GREEN;
import static java.awt.Color.green;

public class GradeScreen extends JFrame{

    private JPanel mainPanel;
    private JTextField textField1;
    private JList list1;
    private JLabel label1;
    private JTable table1;
    private JList list2;
    private JTextArea textArea1;


    public GradeScreen(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(1280,720);
        this.pack();
        this.setVisible(true);

        mainPanel.setBorder( new LineBorder(Color.GREEN));
        mainPanel.setBounds(10,10,500,300);
        mainPanel.setVisible(true);

        textField1.setEditable(false);
        textField1.setBackground(GREEN);
        textField1.setText("Hurensonh");
    }


}
