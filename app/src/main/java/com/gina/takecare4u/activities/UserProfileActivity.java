package com.gina.takecare4u.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.adapter.ThePostAdapter;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView mtextViewPhone, mtextViewPostNumber, mtextViewNombre, mtextViewEmail, mtextViewZipCode, mtexViewSinPost;
    ImageView mImageViewCover;
    CircleImageView mCircleImageProfile;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PublicacionProvider mPublicacionesProvider;
    ThePostAdapter mThePostAdapter;
    RecyclerView mRecicleViewThePost;
    Toolbar mtoolbar;
    FloatingActionButton mFabChat;

    private static final String TAG = "UserProfileActivity";

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
        mtexViewSinPost =  findViewById(R.id.textViewSinPostProfile);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPublicacionesProvider = new PublicacionProvider();
        mtoolbar = findViewById(R.id.toolbarUserProfile);
        mFabChat = findViewById(R.id.fabButtonChat);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecicleViewThePost = findViewById(R.id.recyclerThePost);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecicleViewThePost.setLayoutManager(linearLayoutManager);

        mExtraIdUser = getIntent().getStringExtra("idUser");
        if(mAuthProvider.getUid().equals(mExtraIdUser)){
            mFabChat.setVisibility(View.GONE);
            }

        mFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });

        getUser();
        getPostNumber();
        existPost();


    }

    /**
     * mExtraIdUser: es el id del usuario activo
     */
    private void goToChatActivity() {
        Intent intent = new Intent(UserProfileActivity.this, ChatActivity.class);
            intent.putExtra("idUser1", mAuthProvider.getUid());
            intent.putExtra("idUser2", mExtraIdUser);
            startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        //consulta a base de datos
        Query query = mPublicacionesProvider.getPostByUser(mExtraIdUser);
        FirestoreRecyclerOptions<Publicaciones> options = new FirestoreRecyclerOptions.Builder<Publicaciones>()
                .setQuery(query, Publicaciones.class)
                .build();
        mThePostAdapter = new ThePostAdapter(options, UserProfileActivity.this);
        mRecicleViewThePost.setAdapter(mThePostAdapter);
        mThePostAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mThePostAdapter.stopListening();
    }

    private void existPost() {
        mPublicacionesProvider.getPostByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {

                    if (error != null) {
                        Log.d(TAG, "Error:" + error.getMessage());
                    } else {
                        int numberPost = value.size();
                        if (numberPost > 0) {
                            mtexViewSinPost.setText("PUBLICACIONES");
                            mtexViewSinPost.setTextColor(getResources().getColor(R.color.pink_mio));
                        } else {
                            mtexViewSinPost.setText("SIN PUBLICACIONES");
                            mtexViewSinPost.setTextColor(getResources().getColor(R.color.other_purple));
                        }
                    }

            }
        });


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}