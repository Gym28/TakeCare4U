package com.gina.takecare4u.adapter;
import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
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

        String relativeTime = RelativeTime.timeFormatAMPM(message.getTimeestamp(), context);
        holder.textViewDateCard.setText(relativeTime);

        if(message.getIdSender().equals(mAuthProvider.getUid())) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(150, 0, 0, 0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(30, 20, 25, 20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.rounded_linear_layout));
            holder.imageViewCheckCardMessage.setVisibility(View.VISIBLE);
            holder.textViewMessageCard.setTextColor(WHITE);
            holder.textViewDateCard.setTextColor(DKGRAY);
        }
         else {RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0, 0, 150, 0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(30, 20, 30, 20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.rounded_linear_layout_grey));
            holder.imageViewCheckCardMessage.setVisibility(View.GONE);
            holder.textViewMessageCard.setTextColor(DKGRAY);
            holder.textViewDateCard.setTextColor(DKGRAY);
        }



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
            LinearLayout linearLayoutMessage;
            View viewHolder;


            public ViewHolder (View view){
                super(view);

                textViewMessageCard = view.findViewById(R.id.textViewMessageCard);
                textViewDateCard = view.findViewById(R.id.textViewDateCardMessage);
                imageViewCheckCardMessage= view.findViewById(R.id.imageViewCheckCardMessage);
                linearLayoutMessage = view.findViewById(R.id.linearLayoutCardMessage);
                viewHolder = view;
            }
    }
}
