package uta.cse.cse3310.webchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

//HTTP 
import net.freeutils.httpserver.*;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

//Gson
import com.google.gson.Gson;

public class App extends WebSocketServer {

    private static Integer numHits = -1;
    private ArrayList<Client> clients = null;
    private ArrayList<ChatRoom> chatRooms = null;

    // map a UUID of the websocket to the WebSocket
    /*
     * Although it would be preferable to store the webSocket in the client object
     * gson does not support stringifying websocket objects. Complains that they are
     * not serializable. Although we could override the
     * serialization/deserialization method
     * this was a much simpler solution
     */
    private Map<String, WebSocket> webSockets = null;

    private static Log my_log = new Log("log.txt");

    public App(InetSocketAddress address) {
        super(address);
        clients = new ArrayList<Client>();
        chatRooms = new ArrayList<ChatRoom>();
        webSockets = new ConcurrentHashMap<String, WebSocket>();
    }

    public static Log getLog() {
        return my_log;
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static void main(String[] args) throws IOException {

        // HTTP Server
        int httpPort = 8084;
        HTTPServer server = new HTTPServer(httpPort);
        VirtualHost host = server.getVirtualHost(null);
        host.addContexts(new Handlers());
        server.start();
        System.out.println("HTTP server started on port: " + httpPort);
        Gson gson = new Gson();
        String json = gson.toJson("HTTP server started on port: " + httpPort);

        // WebSocket Server
        int webSocketPort = 8085;
        InetSocketAddress webSocketAddress = new InetSocketAddress(webSocketPort);

        // App
        App app = new App(webSocketAddress);
        app.start();

        App.getLog().logger.info(json);
    }

    // Overrides for WebSocketServer
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        System.out.println("New WebSocket connection opened");

        // store client information in the appropriate data structures
        String webSocketID = UUID.randomUUID().toString();
        Client client = new Client(++numHits, webSocketID);

        // add client to the list of clients
        clients.add(client);
        webSockets.put(webSocketID, webSocket);

        // send the requesting client their client object
        ServeData servClient = new ServeData("clientObject", App.toJson(client));
        webSocket.send(App.toJson(servClient));

        // broadcast updated list of clients to each client
        this.broadcast(App.toJson(new ServeData("clientConnections", App.toJson(clients))));

        // broadcast the list of groupChats to the clients
        this.broadcast(App.toJson(new ServeData("chatRooms", App.toJson(chatRooms))));
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        System.out.println("Received message: " + message);

        // parse message for the identifier and the data
        Gson gson = new Gson();
        ServeData ServeData = gson.fromJson(message, ServeData.class);

        String identifier = ServeData.getIdentifier();
        String data = ServeData.getData();

        if (identifier.equals("directMessage")) {

            // cast message to Message
            DirectMessage directMessage = gson.fromJson(data, DirectMessage.class);

            // send message to intended client as indicated by the 'to' field
            Client recipient = null;
            for (Client client : clients) {
                if (client.getClient_id() == directMessage.getTo()) {
                    recipient = client;
                    break; // Stop iterating once the client is discovered
                }
            }

            // send the message on to the correct recipient
            WebSocket recipientSocket = webSockets.get(recipient.getWebSocketID());
            recipientSocket.send(message);

            // send the message to the sending client also, so they can display it in their
            // display
            webSocket.send(message);

            App.getLog().logger.info(message);

        } else if (identifier.equals("login")) {

            Login login = gson.fromJson(data, Login.class);
            if (login.VerifyLogin()) {
                // notify client that login was successful
                webSocket.send(App.toJson(new ServeData("loginResult", App.toJson("validation successful"))));
                System.out.println("validation passed!");
            } else {
                // notify client that login was unsuccessful
                webSocket.send(App.toJson(new ServeData("loginResult", App.toJson("validation unsuccessful"))));
                System.out.println("validation failed");
            }

        } else if (identifier.equals("creating an account")) {
            Login newUser = gson.fromJson(data, Login.class);
            String newUsername = newUser.getUsername();
            String newPassword = newUser.getPassword();
            newUser.SignUp(newUsername, newPassword);
            // Handle the result of the sign-up operation if needed
        
        } else if (identifier.equals("createChatRoom")) {

            // create a new chatroom
            ChatRoom chatRoom = new ChatRoom(chatRooms.size(), data);

            // append chatroom to the list of chatrooms
            this.chatRooms.add(chatRoom);

            // broadcast the updated list of chatrooms to all the clients
            this.broadcast(App.toJson(new ServeData("chatRooms", App.toJson(chatRooms))));
        } else if (identifier.equals("groupChatMessage")) {

            // append message to list of messages
            ChatRoomMessage chatRoomMessage = gson.fromJson(data, ChatRoomMessage.class);
            Integer chatRoomID = chatRoomMessage.getChatRoomID();
            chatRooms.get(chatRoomID).appendMessage(chatRoomMessage);

            System.out.println("sending message back! : " + message);

            // send updated chatRooms to all clients
            this.broadcastToOthers(App.toJson(new ServeData("chatRooms", App.toJson(chatRooms))), webSocket);
        } else if (identifier.equals("joinRequest")) {
            JoinRequest joinRequest = gson.fromJson(data, JoinRequest.class);

            // grant the request without checking
            Integer chatRoomID = joinRequest.getChatRoomToJoin();
            Integer requesterID = joinRequest.getRequesterID();

            Client requester = clients.get(requesterID);
            chatRooms.get(chatRoomID).addMember(requester);

            // broadcast the updated list of groupChats to the clients
            this.broadcast(App.toJson(new ServeData("chatRooms", App.toJson(chatRooms))));
        } else if (identifier.equals("disJoinRequest")) {
            JoinRequest joinRequest = gson.fromJson(data, JoinRequest.class);

            // grant the request without checking
            Integer chatRoomID = joinRequest.getChatRoomToJoin();
            Integer requesterID = joinRequest.getRequesterID();

            Client requester = clients.get(requesterID);
            chatRooms.get(chatRoomID).removeMember(requester);

            // broadcast the updated list of groupChats to the clients
            this.broadcast(App.toJson(new ServeData("chatRooms", App.toJson(chatRooms))));
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed with code: " + code);

        // elect which entry to remove based on the webSocket
        for (Client client : clients) {
            if (webSockets.get(client.getWebSocketID()) == webSocket) {
                clients.remove(client);
                break; // Stop iterating once the client is removed
            }
        }

        // broadcast updated list of clients to each client
        this.broadcast(App.toJson(new ServeData("chatRooms", App.toJson(chatRooms))));

        // TO DO: disconnect client from chatroom

        App.getLog().logger.info(toJson("WebSocket connection closed with code: " + code));
    }

    @Override
    public void onError(WebSocket webSocket, Exception ex) {
        System.out.println("WebSocket error occurred:");
        ex.printStackTrace();
    }

    public void broadcastToOthers(String message, WebSocket senderSocket) {
        for (WebSocket socket : webSockets.values()) {
            if (socket != senderSocket) {
                socket.send(message);
            }
        }
    }

    @Override
    public void onStart() {
        InetSocketAddress address = getAddress();
        System.out.println("WebSocket server started on address: " + address.getAddress().getHostAddress() + ", port: "
                + address.getPort());
        App.getLog().logger.info(toJson("WebSocket server started on address: " + address.getAddress().getHostAddress()
                + ", port: " + address.getPort()));
    }
}