package com.gina.takecare4u.adapter;
import android.content.Context;
import android.content.Intent;
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
import com.gina.takecare4u.modelos.Publicaciones;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class PubliAdapter extends FirestoreRecyclerAdapter<Publicaciones, PubliAdapter.ViewHolder>{

    Context context;

    public PubliAdapter(FirestoreRecyclerOptions<Publicaciones> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NotNull ViewHolder holder, int position, Publicaciones post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String publicacionId= document.getId();
        holder.textViewTitle.setText(post.getNombre());
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
            ImageView imageViewPubli;
            View viewHolder;

            public ViewHolder (View view){
                super(view);
                textViewTitle = view.findViewById(R.id.textViewPubliCard);
                textViewDescription = view.findViewById(R.id.textViewPubliDesCard);
                textViewPrice = view.findViewById(R.id.textViewPubliPrecioCard);
                textViewPrice = view.findViewById(R.id.textViewPubliPrecioCard);
                imageViewPubli = view.findViewById(R.id.imageViewPubliCard);
                viewHolder = view;
            }
    }
}
