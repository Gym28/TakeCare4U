package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Chats;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class ChatProvider {

    CollectionReference mCollection;

    public ChatProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("CHATS");
    }

    //METODO para crear el chat
    public void  create (Chats chat){
        mCollection.document(chat.getIdUser1() + chat.getIdUser2()).set(chat);

    }

    public Query getChatUser1andUser2 (String idUser1, String idUser2){
        ArrayList<String> ids = new ArrayList<>();
        ids.add(idUser1 + idUser2);
        ids.add(idUser2 + idUser1);
        return mCollection.whereIn("idChat", ids);
    }

    public Query getAll(String idUser){
        return mCollection.whereArrayContains("ids", idUser);}
}
