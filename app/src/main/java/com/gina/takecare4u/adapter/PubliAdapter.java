package com.gina.takecare4u.adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.PubliDetailActivity;
import com.gina.takecare4u.modelos.Likes;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.LikeProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.gina.takecare4u.providers.UsersProvider;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Locale;

public class PubliAdapter extends FirestoreRecyclerAdapter<Publicaciones, PubliAdapter.ViewHolder>{

    Context context;
    PublicacionProvider mPubliProvider;
    UsersProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;

    TextView mtextViewNumberFilter;
    private static final String TAG = "PubliAdapter";

    public PubliAdapter(FirestoreRecyclerOptions<Publicaciones> options, Context context){
        super(options);
         this.context = context;
        mPubliProvider = new PublicacionProvider();
        mUserProvider = new UsersProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
    }
    public PubliAdapter(FirestoreRecyclerOptions<Publicaciones> options, Context context, TextView textView){
        super(options);
         this.context = context;
        mPubliProvider = new PublicacionProvider();
        mUserProvider = new UsersProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
        mtextViewNumberFilter = textView;
    }

    @Override
    protected void onBindViewHolder(@NotNull ViewHolder holder, int position, Publicaciones post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String publicacionId= document.getId();

        if(mtextViewNumberFilter!=null){
            int numberFilter = getSnapshots().size();
            mtextViewNumberFilter.setText(String.valueOf(numberFilter));

        }

        String idUser = post.getIdUser();
        holder.textViewTitle.setText(post.getNombre().toUpperCase());
        holder.textViewDescription.setText(post.getDescripcion());
        holder.textViewPrice.setText(post.getPrecio() + "€/h");
        if(post.getImagen1() != null) {
            if (!post.getImagen1().isEmpty()) {
                Picasso.with(context).load(post.getImagen1()).into(holder.imageViewPubli);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PubliDetailActivity.class);
                intent.putExtra("id", publicacionId);
            context.startActivity(intent);

            }
        });

        holder.imageViewLikeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Likes like = new Likes();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(publicacionId);
                like.setTimestamp(new Date().getTime());
                like(like, holder);
            }
        });

        getUserInfo(idUser, holder);
        getNumberLikesByPost(publicacionId,holder);
        existLike(publicacionId, mAuthProvider.getUid(),holder);

    }
    private void existLike(String idPost, String idUser, ViewHolder holder ) {
        Task<QuerySnapshot> querySnapshotTask = mLikeProvider.getLikBypostAndUser(idPost, idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0) {
                    holder.imageViewLikeCard.setImageResource(R.drawable.ic_like_color_hdpi);


                } else {
                    holder.imageViewLikeCard.setImageResource(R.drawable.ic_like_gris);
                }
            }
        });

    }

    private void getNumberLikesByPost(String idPost, final ViewHolder holder) {
        mLikeProvider.getLikeByPost(idPost).addSnapshotListener((queryDocumentSnapshots, e) -> {
                   if (e != null) {
                        Log.d(TAG, "Error:" + e.getMessage());
                    } else {
                        int numberLikes = queryDocumentSnapshots.size();
                        holder.textViewlikesCard.setText(String.valueOf(numberLikes) + " Likes");
                    }

        });
    }

    private void like(Likes like, ViewHolder holder ) {
        mLikeProvider.getLikBypostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if(numberDocuments>0) {
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLikeCard.setImageResource(R.drawable.ic_like_gris);
                    mLikeProvider.deleteLike(idLike);

                }
                else {
                    mLikeProvider.create(like);
                    holder.imageViewLikeCard.setImageResource(R.drawable.ic_like_color_hdpi);
                }
            }
        });

    }



    private void getUserInfo(String idUser, ViewHolder holder) {
        mUserProvider.getUsers(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        holder.textViewname.setText("POR: " + nombre.toUpperCase());
                    }
                }

            }
        });

    }


    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_publicaciones, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewTitle;
            TextView textViewDescription;
            TextView textViewPrice;
            TextView textViewname;
            TextView textViewlikesCard;
            ImageView imageViewPubli;
            ImageView imageViewLikeCard;
            View viewHolder;

            public ViewHolder (View view){
                super(view);
                textViewTitle = view.findViewById(R.id.textViewPubliCard);
                textViewDescription = view.findViewById(R.id.textViewPubliDesCard);
                textViewPrice = view.findViewById(R.id.textViewPubliPrecioCard);
                textViewlikesCard= view.findViewById(R.id.textViewlikesCard);
                textViewname = view.findViewById(R.id.textViewUsuarioCard);
                imageViewPubli = view.findViewById(R.id.imageViewPubliCard);
                imageViewLikeCard = view.findViewById(R.id.imageViewLikeCard);
                viewHolder = view;
            }
    }
}
