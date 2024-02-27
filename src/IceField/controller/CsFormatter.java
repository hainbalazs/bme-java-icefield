package IceField.controller;

import java.util.logging.*;

public class CsFormatter extends Formatter {
    static private Integer stack_depth = 0;
    @Override
    public String format(LogRecord rec) {
        if (rec.getMessage() == "<<")
            stack_depth--;
        String output = "";
        for (Integer i = 0; i < stack_depth; i++)
            output = output.concat("\t");
        output = output.concat(rec.getMessage() + rec.getSourceClassName() + ":" + rec.getSourceMethodName() + "\n");
        if (rec.getMessage() == ">>")
            stack_depth++;
        return output;
    }
}
