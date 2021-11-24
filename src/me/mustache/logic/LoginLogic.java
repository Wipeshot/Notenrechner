package me.mustache.logic;

public class LoginLogic {

    Database db = Database.getInstance();

    public LoginLogic(){

    }

    /**
     * @param username
     * @param password
     * @return - true if the param password equals the password in Database; false if not
     */
    public boolean checkPassword(String username, String password){
        String rightPassword = db.getPasswordByUsername(username.toLowerCase());
        return (password.equals(rightPassword));
    }

}
