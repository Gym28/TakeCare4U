package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Comments;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class CommentProvider {
    CollectionReference mCollection;

    public  CommentProvider (){
        mCollection = FirebaseFirestore.getInstance().collection("COMMENTS");

    }

    public Task <Void> create(Comments comment) {

        return mCollection.document().set(comment);


    }
    //consultamos todas las publicaciones ordenados por titulo en forma descendente
    public Query getAll(){
        return mCollection.orderBy("nombre", Query.Direction.DESCENDING);
    }

    public Query getidPost(){
        return mCollection.orderBy("idPost", Query.Direction.DESCENDING);
    }


    public Task<DocumentSnapshot> getComment(String idPost){
        return

                mCollection.document(idPost).get();
    }

    public Query getCommentsByPost(String idPost){
        return mCollection.whereEqualTo("idPost", idPost);
    }



}
