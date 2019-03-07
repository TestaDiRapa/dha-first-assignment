package common;

/**
 * Class that contains the methods to validate the username
 */
public class UserValidator {

    /**
     * A function that says if the username is valid or not
     * @param username the username to validate
     * @return true if is valid, false otherwise
     */
    public static boolean isUsernameValid(String username){
        if(username.equals("")) return false;

        return !username.matches("^ *$");
    }

}
