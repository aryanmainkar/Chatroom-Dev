package uta.cse.cse3310.webchat;

public class ServeData {
    String identifier;
    
    //shall be a json string
    String data;

    public ServeData(String identifier, String data) {
        this.identifier = identifier;
        this.data = data;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}