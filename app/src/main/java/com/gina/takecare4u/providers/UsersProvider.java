package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.Users;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UsersProvider {
    //actualiza y obtiene datos de Firestore
    private CollectionReference  mColection;

    public UsersProvider(){
        mColection=FirebaseFirestore.getInstance().collection("USUARIOS");
    }

    public Task<DocumentSnapshot> getUsers(String id){
        return
           mColection.document(id).get();
    }

    public Task<DocumentSnapshot> getnombre(){
        return
                mColection.document().get();
    }


    public Task<Void> create(Users user){
        return mColection.document(user.getId()).set(user);
       }

    public  Task<Void> upDate( Users user){
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", user.getNombre());
        map.put("zipCode", user.getZipCode());
        map.put("phone",user.getPhone());
        map.put("timestamp", new Date().getTime());
        map.put("imageProfile", user.getImageProfile());
        map.put("imageCover", user.getImageCover());
        return mColection.document(user.getId()).update(map);

       }

}
