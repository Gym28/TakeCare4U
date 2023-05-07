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
import com.gina.takecare4u.activities.ChatActivity;
import com.gina.takecare4u.activities.Utils.RelativeTime;
import com.gina.takecare4u.modelos.Chats;
import com.gina.takecare4u.modelos.Messages;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.CommentProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends FirestoreRecyclerAdapter<Messages, MessageAdapter.ViewHolder>{

    Context context;
    UsersProvider mUserProvider;
    CommentProvider mCommentProvider;
    AuthProvider mAuthProvider;
    String idPost = "";
    private static final String TAG = "COMMENTADAPTER";

    public MessageAdapter(FirestoreRecyclerOptions<Messages> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UsersProvider();
        mAuthProvider= new AuthProvider();
        mCommentProvider= new CommentProvider();


    }

    @Override
    protected void onBindViewHolder(@NotNull ViewHolder holder, int position, Messages message) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String MessageId= document.getId();
        holder.textViewMessageCard.setText(message.getMessage());

        String relativeTime = RelativeTime.getTimeAgo(message.getTimeestamp(), context);
        holder.textViewDateCard.setText(relativeTime);


    }


    private void getFecha(long date, ViewHolder holder){
        DateFormat fecha= new SimpleDateFormat("dd-MM-yyyy");
        String date2= fecha.format(date);
        holder.textViewDateCard.setText(date2);


    }


    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewMessageCard;
            TextView textViewDateCard;
            ImageView imageViewCheckCardMessage;
            View viewHolder;

            public ViewHolder (View view){
                super(view);

                textViewMessageCard = view.findViewById(R.id.textViewMessageCard);
                textViewDateCard = view.findViewById(R.id.textViewDateCardMessage);
                imageViewCheckCardMessage= view.findViewById(R.id.imageViewCheckCardMessage);
                viewHolder = view;
            }
    }
}
