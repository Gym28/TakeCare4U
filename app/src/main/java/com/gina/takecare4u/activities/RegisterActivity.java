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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class RegisterActivity extends AppCompatActivity {
    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputZipCode;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirm;
    TextInputEditText mTextInputPhone;
    Button mBtnRegister;
    //objeto de firebase
    AuthProvider mAutProvider;
    //firebase
    UsersProvider mUserProvider;
    //dialog
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mcircleImageViewBack=findViewById(R.id.circleImageBack);
        mTextInputEmail=findViewById(R.id.texImputEmail);
        mTextInputName=findViewById(R.id.textImputName);
        mTextInputZipCode=findViewById(R.id.textImputZipCode);
        mTextInputPassword=findViewById(R.id.textImputPassword);
        mTextInputConfirm=findViewById(R.id.textImputConfirm);
        mBtnRegister=findViewById(R.id.btnRegister);
        mTextInputPhone = findViewById(R.id.textInputPhone);

        //instancia para controlar el registro de usuarios
            mAutProvider= new AuthProvider();
        //instancia para trabajar con la base de datos
            mUserProvider = new UsersProvider();
            mDialog = new SpotsDialog.Builder()
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

        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void register(){
        String name = mTextInputName.getText().toString();
        String zipCode= mTextInputZipCode.getText().toString();
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        String confirm =mTextInputConfirm.getText().toString();
        String phone = mTextInputPhone.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !zipCode.isEmpty() && !password.isEmpty() && !confirm.isEmpty() && !phone.isEmpty()){
            if(isEmailValid(email)) {
                if (password.equals(confirm)) {
                    if (password.length() >= 6) {
                        if(zipCode.length()==5){
                            createUser(name,zipCode,email,password, phone);
                            Log.d(TAG, "createUserWithEmail:success");
                        }else{
                            Toast.makeText(this, "Verifica el Código Postal ", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "La contraseña debe tener mínimo 6 carcteres ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Debes escribir un email válido", Toast.LENGTH_LONG).show();
                 }

        }else{

            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    /*
     * método para validar email válido
     */
    public boolean isEmailValid(String email) {
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /*
     * validar los campos que sean correctos , se ejcuta cuando se registre el usuario y validamos si la tarea es exitosa
     *
     */

    private void createUser(String name,String zipCode, String email, String password, String phone){
        mDialog.show();
        mAutProvider.Register(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //get del uid que se genera en FireBase cuando se registra el usuario
                    String id = mAutProvider.getUid();
                    Users user= new Users();
                    user.setId(id);
                    user.setNombre(name);
                    user.setEmail(email);
                    user.setZipCode(zipCode);
                    user.setPhone(phone);
                    user.setTimestamp(new Date().getTime());
                    mUserProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NotNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, " wee Te registraste correctamente ", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Ups!! no fuiste registrado", Toast.LENGTH_SHORT).show();
                                 Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });
                } else {
                       mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "ohh! ya eres miembro de Care4U ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}