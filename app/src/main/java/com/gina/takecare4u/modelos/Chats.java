package com.gina.takecare4u.modelos;

import java.util.ArrayList;

public class Chats {

    private String id;

    private String idUser1;
    private String idUser2;
    private long idNotification;
    private boolean isWriting;
    private long timestamp;
    private ArrayList<String> ids;

    public Chats() {
    }

    public Chats(String id, String idUser1, String idUser2, long idNotification, boolean isWriting, long timestamp, ArrayList<String> ids) {
        this.id = id;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.idNotification = idNotification;
        this.isWriting = isWriting;
        this.timestamp = timestamp;
        this.ids = ids;
    }

    public boolean isWriting() {
        return isWriting;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public String getId() {
        return id;
    }

    public void setId(String idChat) {
        this.id = idChat;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public void setWriting(boolean writing) {
        isWriting = writing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(long idNotification) {
        this.idNotification = idNotification;
    }
}
