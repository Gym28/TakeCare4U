package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Chats;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class ChatProvider {

    CollectionReference mCollection;

    public ChatProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("CHATS");
    }

    //METODO para crear el chat
    public void  create (Chats chat){
        mCollection.document(chat.getIdUser1()).collection("USERS").document(chat.getIdUser2()).set(chat);
        mCollection.document(chat.getIdUser2()).collection("USERS").document(chat.getIdUser1()).set(chat);
    }
}
