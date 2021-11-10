package me.mustache.logic;

public class LoginLogic {

    Database db = Database.getInstance();

    public LoginLogic(){

    }

    public boolean checkPassword(String username, String password){
        String rightPassword = db.getPasswordByUsername(username);
        return (password.equals(rightPassword));
    }

}
