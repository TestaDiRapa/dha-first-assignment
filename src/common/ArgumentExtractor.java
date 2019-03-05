package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentExtractor {

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
        String regex = "^<[A-Z]+>";
        for(int i=0; i<argument; i++){
            regex += " <([^<>]*)>";
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(command);
        if(m.find()) return m.group(argument);
        return "";
    }

}
