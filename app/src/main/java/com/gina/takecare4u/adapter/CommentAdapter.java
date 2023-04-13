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
import com.gina.takecare4u.modelos.Comments;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.CommentProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comments, CommentAdapter.ViewHolder>{

    Context context;
    UsersProvider mUserProvider;
    CommentProvider mCommentProvider;
    String idPost = "";
    private static final String TAG = "COMMENTADAPTER";

    public CommentAdapter(FirestoreRecyclerOptions<Comments> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UsersProvider();
        mCommentProvider= new CommentProvider();


    }

    @Override
    protected void onBindViewHolder(@NotNull ViewHolder holder, int position, Comments comment) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String idUser = document.getString("idUser");
        String idPost= document.getString("idPost");
        long date = comment.getTimestamp();
        holder.textViewComment.setText(comment.getComment());
        getUserinfo(idUser, holder);
        getFecha(date, holder);

    }

    private void getFecha(long date, ViewHolder holder){
        DateFormat fecha= new SimpleDateFormat("dd-MM-yyyy");
        String date2= fecha.format(date);
        holder.textViewFecha.setText(date2);

    }


    private void getUserinfo(String idUser, ViewHolder holder){
        mUserProvider.getUsers(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        holder.textViewNombre.setText(nombre);

                    }  if(documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if(imageProfile != null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(context).load(imageProfile).into(holder.imageViewComment);
                            }
                        }
                    }


                }

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comments, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewNombre;
            TextView textViewComment;
            TextView textViewFecha;

            CircleImageView imageViewComment;
            View viewHolder;

            public ViewHolder (View view){
                super(view);

                textViewComment = view.findViewById(R.id.textViewDescripcionComment);
                textViewNombre = view.findViewById(R.id.textViewNombreCard);
                textViewFecha = view.findViewById(R.id.textViewFechaCard);
                imageViewComment = view.findViewById(R.id.circleImageViewComment);
                viewHolder = view;
            }
    }
}
