package uta.cse.cse3310.webchat;

public class ChatRoomMessage {
    Integer chatRoomID;
    Integer senderID;
    String content;
    
    public ChatRoomMessage(Integer chatRoomID, Integer senderID, String content) {
        this.chatRoomID = chatRoomID;
        this.senderID = senderID;
        this.content = content;
    }

    public Integer getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(Integer chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}