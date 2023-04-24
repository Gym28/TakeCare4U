package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Tokens;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.Token;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

public class TokenProvider {

    CollectionReference mCollection;

    public TokenProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("TOKENS");
    }

    public void create(String idUser){
        if(idUser==null) {
            return;
        }
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Tokens token = new Tokens(s);
                mCollection.document(idUser).set(token);
            }
        });
    }

    public Task<DocumentSnapshot> getToken (String idUser){
        return mCollection.document(idUser).get();
    }

}
