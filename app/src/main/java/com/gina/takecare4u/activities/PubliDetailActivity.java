package com.gina.takecare4u.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.adapter.CommentAdapter;
import com.gina.takecare4u.adapter.PubliAdapter;
import com.gina.takecare4u.adapter.SliderAdapter;
import com.gina.takecare4u.modelos.Comments;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.modelos.SliderItem;
import com.gina.takecare4u.modelos.Users;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.CommentProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PubliDetailActivity extends AppCompatActivity {
    CircleImageView mCircleImageViewBack;
    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    String mExtrapublicacionId;
    String midUser="";


    TextView mTexViewPubliTitulo, mTextViewPubliDescription, mTextViewPubliNombre, mTextViewPubliPhone,  mTextViewPublinameTipo, mTextViewPubliPrecio;
    ImageView mImageViewPubliTipo;
    CircleImageView mCircleViewPubliProfile;
    Button mbtnShowProfile;
    FloatingActionButton mFabComment;
    RecyclerView mRecyclerViewComment;
    CommentAdapter mcommentAdapter;

    UsersProvider mUsersProvider;
    PublicacionProvider mPublicacionProvider;
    CommentProvider mCommentProvider;
    AuthProvider mAuthProvider;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publi_detail);

         mSliderView = findViewById(R.id.imageSlider);
         mTexViewPubliTitulo =findViewById(R.id.textViewPubliTitulo);
         mTextViewPubliNombre =findViewById(R.id.textViewPubliNombre);
         mTextViewPubliPhone = findViewById(R.id.texViewPubliPhone);
         mTextViewPubliDescription = findViewById(R.id.textViewPubliDescripcion);
         mTextViewPublinameTipo = findViewById(R.id.textViewPublinameTipo);
         mTextViewPubliPrecio = findViewById(R.id.textViewPubliPrecio);
         mImageViewPubliTipo = findViewById(R.id.imageViewPubliTipo);
         mCircleViewPubliProfile = findViewById(R.id.circleImagePubliProfile);
         mbtnShowProfile =findViewById(R.id.btnShowProfile);
         mCircleImageViewBack= findViewById(R.id.circleImagePubliBack);
         mFabComment = findViewById(R.id.fabComentPubli);
         mRecyclerViewComment = findViewById(R.id.recyclerViewComment);
         //listamos comentarios
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PubliDetailActivity.this);
         mRecyclerViewComment.setLayoutManager(linearLayoutManager);

         mUsersProvider = new UsersProvider();
         mPublicacionProvider = new PublicacionProvider();
         mCommentProvider = new CommentProvider();
         mAuthProvider = new AuthProvider();

        mExtrapublicacionId = getIntent().getStringExtra("id");

        getPubli();

        mFabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mbtnShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            goToShowProfile();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = mCommentProvider.getCommentsByPost(mExtrapublicacionId);
        FirestoreRecyclerOptions<Comments> options = new FirestoreRecyclerOptions.Builder<Comments>()
                .setQuery(query, Comments.class)
                .build();
        mcommentAdapter = new CommentAdapter(options, PubliDetailActivity.this);
        mRecyclerViewComment.setAdapter(mcommentAdapter);
        mcommentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mcommentAdapter.stopListening();
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PubliDetailActivity.this);
        alert.setTitle("COMENTARIOS");
        alert.setMessage("Comenta el servicio Recibido");
        EditText editText = new EditText(PubliDetailActivity.this);
        editText.setHint("Comenta el servicio o la publicación");

        //establecemos margenes para el comentario
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36,0,36,36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PubliDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);
        alert.setView(container);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comentario = editText.getText().toString();
                if(!comentario.isEmpty()) {
                    createComment(comentario);
                }
                else {
                    Toast.makeText(PubliDetailActivity.this, "Debes escribir un comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private void createComment(String comentario) {
        Comments comment = new Comments();
        comment.setComment(comentario);
        comment.setIdPost(mExtrapublicacionId);
        //usuario de la sesión
        comment.setIdUser(mAuthProvider.getUid());
        comment.setTimestamp(new Date().getTime());
        mCommentProvider.create(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PubliDetailActivity.this, "Tu comentario ha sido guardado", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PubliDetailActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToShowProfile() {
        if(!midUser.equals("")) {
            Intent intent = new Intent(PubliDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("idUser", midUser);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "el Usuario no se encuentra", Toast.LENGTH_LONG).show();
        }

    }

    private void instanceSlider(){
        mSliderAdapter = new SliderAdapter(PubliDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        //animación e imágenes
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.CYAN);
        mSliderView.setIndicatorSelectedColor(Color.GRAY);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();

    }

    private void getPubli(){
        mPublicacionProvider.getPostById(mExtrapublicacionId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("imagen1")) {
                        String imagen1 = documentSnapshot.getString("imagen1");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(imagen1);
                        mSliderItems.add(item);

                    }
                    if(documentSnapshot.contains("imagen2")) {
                        String imagen2 = documentSnapshot.getString("imagen2");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(imagen2);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        mTexViewPubliTitulo.setText(nombre.toUpperCase(Locale.ROOT));
                    }   if(documentSnapshot.contains("descripcion")){
                        String descripcion = documentSnapshot.getString("descripcion");
                        mTextViewPubliDescription.setText(descripcion);
                    } if(documentSnapshot.contains("servicio")){
                        String servicio = documentSnapshot.getString("servicio");
                        mTextViewPublinameTipo.setText(servicio);

                        if(servicio.equals("NIÑOS")){
                            mImageViewPubliTipo.setImageResource(R.drawable.boys);
                        }  if(servicio.equals("ADULTO MAYOR")){
                            mImageViewPubliTipo.setImageResource(R.drawable.abuelo);
                        }  if(servicio.equals("MASCOTAS")){
                            mImageViewPubliTipo.setImageResource(R.drawable.pets);
                        }
                    }
                    if(documentSnapshot.contains("precio")){
                        String precio = documentSnapshot.getString("precio");
                        mTextViewPubliPrecio.setText(precio + "€/h");
                    }
                    if(documentSnapshot.contains("idUser")){
                         midUser = documentSnapshot.getString("idUser");
                        getUserInfo(midUser);
                    }

                    instanceSlider();
                }


            }

        });
    }

    private void getUserInfo(String idUser) {
        mUsersProvider.getUsers(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                         mTextViewPubliNombre.setText(nombre);
                    }  if(documentSnapshot.contains("phone")){
                        String telefono = documentSnapshot.getString("phone");
                         mTextViewPubliPhone.setText(telefono);
                    }  if(documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if(imageProfile!=null){
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(PubliDetailActivity.this).load(imageProfile).into(mCircleViewPubliProfile);
                            }

                        }

                    }

                }
            }
        });

    }


}