package uta.cse.cse3310.webchat;

public class JoinRequest {
    Integer requesterID;
    Integer chatRoomToJoin;
    
    public JoinRequest(Integer requesterID, Integer chatRoomToJoin) {
        this.requesterID = requesterID;
        this.chatRoomToJoin = chatRoomToJoin;
    }

    public Integer getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(Integer requesterID) {
        this.requesterID = requesterID;
    }

    public Integer getChatRoomToJoin() {
        return chatRoomToJoin;
    }

    public void setChatRoomToJoin(Integer chatRoomToJoin) {
        this.chatRoomToJoin = chatRoomToJoin;
    }
}