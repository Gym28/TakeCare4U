package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Messages;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageProvider {
    CollectionReference mCollection;

    public MessageProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("MESSAGES");
    }

    public Task<Void> createMessage (Messages message){
        DocumentReference document = mCollection.document();
        message.setIdMessage(document.getId());
        return document.set(message);

    }

    public Query  getMessageByChat(String idChat){
        return mCollection.whereEqualTo("idChat", idChat);
    }
}
