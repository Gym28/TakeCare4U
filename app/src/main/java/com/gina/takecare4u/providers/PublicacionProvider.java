package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Publicaciones;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PublicacionProvider {
    CollectionReference mCollection;

    public PublicacionProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Publicaciones");

    }

    public Task<Void> save(Publicaciones publicaciones){

        return mCollection.document().set(publicaciones);
    }

    //consultamos todas las publicaciones ordenados por titulo en forma descendente
    public Query getAll(){
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }

    //Obtenermos los post que ha hecho el usuario

    public Query getPostByUser(String id){
        return mCollection.whereEqualTo("idUser", id);
    }

    public Task<DocumentSnapshot> getPostById(String id){
        return mCollection.document(id).get();
    }
}
