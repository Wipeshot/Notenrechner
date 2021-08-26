package me.mustache.main;

import me.mustache.gui.Gui;
import me.mustache.logic.Database;

public class Main {


    public static void main(String[] args) {
        Database.getInstance().setUrl("Notenrechner");
        Gui gui = new Gui();


    }

}
