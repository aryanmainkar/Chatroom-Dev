package uta.cse.cse3310.webchat;

import java.util.ArrayList;

public class ChatRoom {
    Integer ID;
    String name;
    ArrayList<ChatRoomMessage> messages;
    ArrayList<Client> members;
    
    public ChatRoom(Integer ID, String name) {
        this.ID = ID;
        this.name = name;
        messages = new ArrayList<>();
        members = new ArrayList<>();
    }

    public void appendMessage(ChatRoomMessage message) {
        messages.add(message);
    }

    public void addMember(Client client) {
        members.add(client);
    }

    public void removeMember(Client client) {
        members.remove(client);
    }

}