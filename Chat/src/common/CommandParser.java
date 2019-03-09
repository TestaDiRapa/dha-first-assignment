package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class command parser offers some functions to create and parse commands for the message protocol
 */
public class CommandParser {

    /**
     * Creates a formatted command
     * @param method the method
     * @param args the arguments
     * @return the formatted command
     */
    public static String createCommand(String method, String... args){
        String command = "<" + method + ">";
        if(args != null) {
            for (String arg : args) {
                command = command + " <" + arg + ">";
            }
        }
        return command;
    }

    /**
     * Extracts the command from the command
     * @param command the command to extract the command from
     * @return the command or an empty string, if error
     */
    public static String extractCommand(String command){
        Pattern p = Pattern.compile("^<([A-Z]+)>");
        Matcher m = p.matcher(command);
        if(m.find()) return m.group(1);
        return "";
    }

    /**
     * Extracts the first argument from a command
     * @param command the command
     * @param argument extracts the argument
     * @return the argument, if any, or empty string
     */
    public static String extractNthArgument(String command, int argument){
        command = command.substring(0, command.length()-1);
        String[] args = command.split("> <");
        if(argument+1 > args.length) return "";
        return args[argument];
    }

}
