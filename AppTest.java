package uta.cse.cse3310.webchat;

import org.java_websocket.server.DefaultWebSocketServerFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

// Test to make sure the Websocket Server is connecting properly
public class AppTest {

    @Test
    public void testWebSocketServerInitialization() {
        InetSocketAddress address = new InetSocketAddress("localhost", 8085);
        App app = new App(address);

        //From JUnit Assert, Asserts that 'app' object isn't null 
        assertNotNull(app);
        //Asserts that our desired address, "localhost":8085, is the same as the connected address
        assertEquals(address, app.getAddress());
    }
    @Test
    public void testWrapChannel() {
    DefaultWebSocketServerFactory webSocketServerFactory = new DefaultWebSocketServerFactory();
    try (SocketChannel channel = (new Socket()).getChannel()) {
        SocketChannel result = webSocketServerFactory.wrapChannel(channel, null);
        assertSame("channel == result", channel, result);
    } catch (IOException e) { e.printStackTrace(); }
  }
}