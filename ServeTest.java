package uta.cse.cse3310;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uta.cse.cse3310.webchat.ServeData;

public class ServeTest
{
    int pass;

    @Test
    public void ServeDataTest()
    {
        ServeData test = new ServeData("test","test");
        
        if(test.getData().equals("test") && test.getIdentifier().equals("test"))
        {
            pass = 1;
        }
        else
        {
            pass = 0;
        }

        assertEquals(pass, 1);
    }
}
