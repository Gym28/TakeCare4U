package com.gina.takecare4u.modelos;

public class Messages {
    private String idMessage;
    private String idSender;
    private String idReciver;
    private String idChat;
    private String message;
    private long timeestamp;
    private boolean viewed;

    public Messages() {
    }

    public Messages(String idMessage, String idSender, String idReciver, String idChat, String message, long timeestamp, boolean viewed) {
        this.idMessage = idMessage;
        this.idSender = idSender;
        this.idReciver = idReciver;
        this.idChat = idChat;
        this.message = message;
        this.timeestamp = timeestamp;
        this.viewed = viewed;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReciver() {
        return idReciver;
    }

    public void setIdReciver(String idReciver) {
        this.idReciver = idReciver;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeestamp() {
        return timeestamp;
    }

    public void setTimeestamp(long timeestamp) {
        this.timeestamp = timeestamp;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
