package uta.cse.cse3310.webchat;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    public Logger logger;
    FileHandler fp;
    
    public Log(String fileName) 
    {
        File f = new File(fileName);
        try {
            if(f.exists())
        {
            fp = new FileHandler(fileName, false);
        }
        else
        {
            f.createNewFile();
            fp = new FileHandler(fileName,true);
        }
        } catch (Exception e) {System.out.println(e);}
        

        logger = Logger.getLogger("test");
        logger.addHandler(fp);
        SimpleFormatter formatter = new SimpleFormatter();
        fp.setFormatter(formatter);

    }
}