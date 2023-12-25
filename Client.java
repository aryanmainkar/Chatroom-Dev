package uta.cse.cse3310.webchat;

import java.util.Date;

public class Client {
    Integer client_id;
    String webSocketID;
    Date lastActive;

    public Client(int client_id, String webSocketID) {
        this.client_id = client_id;
        this.webSocketID = webSocketID;
        this.lastActive = new Date();
    }

    public Integer getClient_id() {
        return client_id;
    }

    public String getWebSocketID() {
        return webSocketID;
    }

    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }

    public void setWebSocket(String webSocketID) {
        this.webSocketID = webSocketID;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void updateLastActive() {
        this.lastActive = new Date();
    }

}