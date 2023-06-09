package com.gina.takecare4u.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider {
    //constructor

    private FirebaseAuth mAuth;

    public AuthProvider (){
            mAuth= FirebaseAuth.getInstance();
    }

    public Task <AuthResult> loggin (String email, String password){
       return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> Register(String email, String password ){

       return  mAuth.createUserWithEmailAndPassword(email, password) ;
    }


    public Task<AuthResult> GoogleSign(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return mAuth.signInWithCredential(credential);
    }

    public String getUid(){
        if(mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        } else {
           return null;
        }
    }

    // metodo para obtener el email
    public String getEmail (){
        if(mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail();
        } else {
            return null;
        }
    }


}
