package com.gina.takecare4u.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.gina.takecare4u.R;

import android.widget.Toast;

import com.gina.takecare4u.modelos.Users;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    TextView mtextViewRegister;
    TextInputEditText mTextImputEmail;
    TextInputEditText mTextImputPassword;
    Button mButtonLoggin;
    SignInButton mBtnGgoogle;
    AuthProvider mAuthProvider;
    UsersProvider mUserProvider;
    // variables para autencación con google (Firebase)
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextViewRegister = findViewById(R.id.textViewRegister);
        mTextImputEmail = findViewById(R.id.textInputEmail);
        mTextImputPassword = findViewById(R.id.textInputPassword);
        mButtonLoggin = findViewById(R.id.btnLoggin);
        mBtnGgoogle = findViewById(R.id.btnLoginGoogle);
        mDialog= new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espera un momento")
                .setCancelable(false)
                .build();

        //inicializamos Firebase con nuestra clase authProvider
        mAuthProvider =  new AuthProvider();

        //inicializamos el google Sing In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mUserProvider= new UsersProvider();

        mButtonLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mButtonLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggin();
            }
        });


        mtextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        mDialog.show();
        mAuthProvider.GoogleSign(idToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String id = mAuthProvider.getUid();
                            checkUserExist(id);
                            // Si el registro es exitoso, actualizamos el UI con la info del usuario
                           Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // si falla se delpliega el mensae de fallo
                            mDialog.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void checkUserExist(String id) {
        mUserProvider.getUsers(id)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    String email =mAuthProvider.getEmail();
                    Users user = new Users();
                    user.setEmail(email);
                    user.setId(id);
                    mUserProvider.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this,CompleteProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "No se pudo crear el usuario", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

    }
    // [END auth_with_google]

    // [START signin]
    //Dispara el proceso para autenticarse con google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    //metodo de loggin para mostrar email y password en consola
    // [START loggin]
    private void loggin() {
        String email = mTextImputEmail.getText().toString();
        String password = mTextImputPassword.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            mDialog.show();
              mAuthProvider.loggin(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(Task<AuthResult> task) {
                    mDialog.dismiss();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, "El email o contraseña no son correctos", Toast.LENGTH_LONG).show();
                    }
                }
            });
            //imprime mensaje en consola
            Log.d("CAMPO", "email: " + email);
            Log.d("CAMPO", "password: " + password);
        } else {

            Toast.makeText(MainActivity.this, "Debes introducir todos los campos", Toast.LENGTH_LONG).show();
        }

        // [END loggin]

    }
    // [END loggin]
}