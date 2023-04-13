package com.gina.takecare4u.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gina.takecare4u.R;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView mtextViewPhone, mtextViewPostNumber, mtextViewNombre, mtextViewEmail, mtextViewZipCode;
    ImageView mImageViewCover;
    CircleImageView mCircleImageProfile;
    CircleImageView mCircleImageViewBack;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PublicacionProvider mPublicacionesProvider;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mtextViewNombre = findViewById(R.id.textViewNombrePerfil);
        mtextViewEmail = findViewById(R.id.textViewMailPerfil);
        mtextViewZipCode = findViewById(R.id.textViewZipCodePerfil);
        mtextViewPhone = findViewById(R.id.textViewPhonePerfil);
        mtextViewPostNumber = findViewById(R.id.textViewPostNumber);
        mCircleImageProfile = findViewById(R.id.circleImageProfileProfile);
        mImageViewCover = findViewById(R.id.imageViewCoverProfile);
        mCircleImageViewBack= findViewById(R.id.circleImagePubliBack);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPublicacionesProvider = new PublicacionProvider();

        mExtraIdUser = getIntent().getStringExtra("idUser");
        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
        getPostNumber();


    }

    private void getUser(){

        mUsersProvider.getUsers(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("email")){
                        String email = documentSnapshot.getString("email");
                        mtextViewEmail.setText(email);
                    }   if (documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        mtextViewNombre.setText(nombre);
                    }     if (documentSnapshot.contains("phone")){
                        String phone = documentSnapshot.getString("phone");
                        mtextViewPhone.setText(phone);
                    }    if (documentSnapshot.contains("zipCode")){
                        String zipCode = documentSnapshot.getString("zipCode");
                        mtextViewZipCode.setText(zipCode);
                        if (documentSnapshot.contains("imageProfile")) {
                            String imageProfile = documentSnapshot.getString("imageProfile");
                            if(imageProfile!=null){
                                if (!imageProfile.isEmpty()) {
                                    Picasso.with(UserProfileActivity.this).load(imageProfile).into(mCircleImageProfile);
                                }
                            }
                        } if (documentSnapshot.contains("imageCover")){
                            String imageCover = documentSnapshot.getString("imageCover");
                            if(imageCover!= null){
                                if(!imageCover.isEmpty()){
                                    Picasso.with(UserProfileActivity.this).load(imageCover).into(mImageViewCover);
                                }
                            }
                        }
                    }
                }
            }
        });

    }
    private void getPostNumber(){
        mPublicacionesProvider.getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //cantidad de elementos que tenemos de la colección de publicaciones
                int numberPost = queryDocumentSnapshots.size();
                mtextViewPostNumber.setText(String.valueOf(numberPost));

            }
        });
    }
}