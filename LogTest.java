package uta.cse.cse3310.webchat;
import org.junit.Test;
import static org.junit.Assert.*;

public class LogTest 
{
    static int count;
    private static Log my_log = new Log("logT.txt");

    public static int counts()
    {
        return count;
    }

    public static Log getLog()
    {
        return my_log;
    }

    @Test
    public void LoggerTest()
    {
        String test = "This is a test";
        LogTest.my_log.logger.info(test);
        assertEquals(test, 1, 1);
    }

    @Test
    public void LogCountTest()
    {
        for(int i = 0; i<10;i++)
        {
            LogTest.getLog().logger.info("count test");
            count++;
        }
        assertEquals(10, LogTest.counts());
    }

}
