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

    public Task<Void> delete (String idDocument){

        return mCollection.document(idDocument).delete();
    }

    public Task<DocumentSnapshot> getPostById(String id){
        return mCollection.document(id).get();
    }

    //consultamos todas las publicaciones ordenados por fecha en forma descendente
    public Query getAll(){
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }


    //Obtenermos los post que ha hecho el usuario

    public Query getPostByUser(String id){
        return mCollection.whereEqualTo("idUser", id);
    }

    public Query getPostByServicio(String precio){
        return mCollection.orderBy("precio").startAt(precio).endAt(precio+'\uf8ff');

     }

    public Query getPubliByServiceandTimestamp(String service){
        return mCollection.whereEqualTo("servicio",service)
                          .orderBy("timestamp", Query.Direction.DESCENDING);
    }




}
