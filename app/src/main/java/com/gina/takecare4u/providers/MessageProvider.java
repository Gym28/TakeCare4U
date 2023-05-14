package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Messages;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

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
        return mCollection.whereEqualTo("idChat", idChat).orderBy("timeestamp", Query.Direction.ASCENDING);
    }

    public Query getMessagesByChatAndSender(String idChat, String idSender){
        return mCollection.whereEqualTo("idChat", idChat).whereEqualTo("idSender", idSender).whereEqualTo("viewed", false);
    }
    public Query getLastThreeMessageByChatAndSender(String idChat, String idSender){
        return mCollection.whereEqualTo("idChat", idChat)
                .whereEqualTo("idSender", idSender)
                .whereEqualTo("viewed", false)
                .orderBy("timeestamp", Query.Direction.DESCENDING)
                .limit(3);
    }
    public Query getLastMessages(String idChat){
        return mCollection.whereEqualTo("idChat", idChat).orderBy("timeestamp", Query.Direction.DESCENDING).limit(1);
    }
    public Task <Void> updateViewed (String idDocument, boolean state){
        Map<String, Object> map= new HashMap<>();
        map.put("viewed", state);
        return mCollection.document(idDocument).update(map);
    }
}
