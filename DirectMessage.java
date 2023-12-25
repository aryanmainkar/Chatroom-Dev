package uta.cse.cse3310.webchat;

import com.google.gson.Gson;

public class DirectMessage {
    int from;
    int to;
    String content;

    public DirectMessage(int from, int to, String content, String identifier) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}