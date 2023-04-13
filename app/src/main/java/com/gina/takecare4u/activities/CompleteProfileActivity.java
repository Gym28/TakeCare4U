package com.gina.takecare4u.activities;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gina.takecare4u.R;
import com.gina.takecare4u.modelos.Users;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputName;
    TextInputEditText mTextInputZipCode;
    TextInputEditText mTextInputPhone;
    Button mBtnRegister;
    //objeto mAuth de firebase
    AuthProvider mAuthProvider;
    //firebase
    UsersProvider muserProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complete_profile);
        mTextInputName=findViewById(R.id.textImputName);
        mTextInputZipCode=findViewById(R.id.textImputZipCode);
        mTextInputPhone=findViewById(R.id.textInputPhone);
        mBtnRegister=findViewById(R.id.btnRegister);
        //instancia para controlar el registro de usuarios
        mAuthProvider = new AuthProvider();
       //instancia para trabajar con la base de datos
        muserProvider = new UsersProvider();
        mDialog= new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espera un momento")
                .setCancelable(false)
                .build();

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });

    }

    private void register(){
        String name = mTextInputName.getText().toString();
        String zipCode= mTextInputZipCode.getText().toString();
        String phone = mTextInputPhone.getText().toString();

        if(!name.isEmpty() && !zipCode.isEmpty() && !phone.isEmpty()){
                        if(zipCode.length()==5){
                            upDateUser(name,zipCode,phone);
                            Log.d(TAG, "createUserWithEmail:success");
                        }else{
                            Toast.makeText(this, "Verifica el Código Postal ", Toast.LENGTH_SHORT).show();
                        }

                        }else{

              Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    /*
     * método para validar email válido
     */
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /*
     * validar los campos que sean correctos , se ejcuta cuando se registre el usuario y validamos si la tarea es exitosa
     *
     */

    private void upDateUser(String name,String zipCode , String phone){
                    String id = mAuthProvider.getUid();
                    Users user = new Users();
                    user.setId(id);
                    user.setNombre(name);
                    user.setZipCode(zipCode);
                    user.setPhone(phone);
                    user.setTimestamp(new Date().getTime());
                    mDialog.show();
                    muserProvider.upDate(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NotNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class );
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(CompleteProfileActivity.this, "Ups!! no fuiste registrado", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });
                }


    }
