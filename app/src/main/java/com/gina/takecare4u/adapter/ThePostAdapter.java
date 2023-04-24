package com.gina.takecare4u.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.PubliDetailActivity;
import com.gina.takecare4u.activities.Utils.RelativeTime;
import com.gina.takecare4u.modelos.Likes;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.LikeProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThePostAdapter extends FirestoreRecyclerAdapter<Publicaciones, ThePostAdapter.ViewHolder>{

    Context context;
    PublicacionProvider mPubliProvider;
    UsersProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;

    private static final String TAG = "ThePostAdapter";

    public ThePostAdapter(FirestoreRecyclerOptions<Publicaciones> options, Context context){
        super(options);
         this.context = context;
        mPubliProvider = new PublicacionProvider();
        mUserProvider = new UsersProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NotNull ViewHolder holder, int position, Publicaciones post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String publicacionId= document.getId();
        String relativeTime = RelativeTime.getTimeAgo(post.getTimestamp(),context);
        holder.textViewRTimeThePost.setText(relativeTime);
        String idUser = post.getIdUser();
        holder.textViewTitle.setText(post.getNombre().toUpperCase());

        if(post.getIdUser().equals(mAuthProvider.getUid())){
            holder.mImageViewDeletePost.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewDeletePost.setVisibility(View.GONE);
        }
        if(post.getImagen1() != null) {
            if (!post.getImagen1().isEmpty()) {
                Picasso.with(context).load(post.getImagen1()).into(holder.circleImageViewUserThePost);
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
        holder.mImageViewDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm(publicacionId, post.getNombre());

            }
        });

    }

    /**
     *
     * @param publicacionId
     * @param titlePost nombre de la publicación
     */

    private void deleteConfirm(String publicacionId, String titlePost) {
       new  AlertDialog.Builder(context)
               .setIcon(android.R.drawable.ic_dialog_alert)
               .setTitle("Eliminar Publicación")
               .setMessage("¿Deseas eliminar la publicación " + titlePost + " ?")
               .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       deletePost(publicacionId);
                   }
               })
               .setNegativeButton("NO", null)
               .show();

    }

    /**
     *
     * @param publicacionId id del post obtenido del documento
     */
    private void deletePost(String publicacionId) {
        mPubliProvider.delete(publicacionId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "La publicación ha sido eliminada", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Ocurrió un error al eliminar la publicación", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_the_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageViewUserThePost;
        TextView textViewTitle;
        TextView textViewRTimeThePost;
        ImageView mImageViewDeletePost;
        View viewHolder;

            public ViewHolder (View view){
                super(view);
                textViewTitle = view.findViewById(R.id.textViewTitleThePost);
                textViewRTimeThePost= view.findViewById(R.id.textViewRelativeTimeThePost);
                circleImageViewUserThePost = view.findViewById(R.id.circleImageViewUsuarioThePost);
                mImageViewDeletePost = view.findViewById(R.id.imageViewDeleteThePost);
                viewHolder = view;
            }
    }
}
