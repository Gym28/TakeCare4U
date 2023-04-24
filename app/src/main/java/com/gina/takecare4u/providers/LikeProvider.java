package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Likes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LikeProvider {

    CollectionReference mCollection;


    public LikeProvider() {
        mCollection= FirebaseFirestore.getInstance().collection("LIKES"); }

    public Task<Void> create(Likes like){
        DocumentReference document =mCollection.document();
        String id = document.getId();
        like.setId(id);
        return document.set(like);
    }

    public Query getLikBypostAndUser (String idPost, String idUser){
        return mCollection.whereEqualTo("idPost",idPost).whereEqualTo("idUser", idUser);

    }
    public Query getLikByUser ( String idUser){
        return mCollection.whereEqualTo("idUser", idUser);

    }

    public Task<Void> deleteLike (String id){
        return mCollection.document(id).delete();

    }

    public Query getLikeByPost(String idPost){
        return mCollection.whereEqualTo("idPost", idPost);
    }
}
