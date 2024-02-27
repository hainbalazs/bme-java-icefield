package IceField.controller;

import java.util.logging.*;

public class OptFormatter extends Formatter {
    static private String lastmessage = "";
    @Override
    public String format(LogRecord rec) {
        return "? " + lastmessage;
    }

    static public void opt(String[] str)
    {
        lastmessage = "";
        try
        {
            if (str.length == 0)
                throw new IllegalArgumentException("Formatting error: missing 'opt' value!");
            else
            {
                lastmessage = lastmessage.concat(str[0]);
                if(str.length == 1)
                    lastmessage = lastmessage.concat(" Y/N");
                lastmessage = lastmessage.concat("\n");
                for(Integer i = 1; i < str.length; i++)
                {
                    lastmessage = lastmessage.concat(i + ". " + str[i] + "\n");
                }
            }
        }
        catch(IllegalArgumentException iae) {iae.printStackTrace();}
        return;
    }

}