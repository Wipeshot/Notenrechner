package me.mustache.main;
import me.mustache.gui.Gui;
import me.mustache.logic.Database;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    
    public static void main(String[] args) {
        Database.getInstance().setUrl("Notenrechner");
        new Gui();
    }
}
