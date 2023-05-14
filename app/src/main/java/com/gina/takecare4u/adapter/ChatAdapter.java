package com.gina.takecare4u.adapter;
import static android.service.controls.ControlsProviderService.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.ChatActivity;
import com.gina.takecare4u.modelos.Chats;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.ChatProvider;
import com.gina.takecare4u.providers.CommentProvider;
import com.gina.takecare4u.providers.MessageProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends FirestoreRecyclerAdapter<Chats, ChatAdapter.ViewHolder>{

    Context context;
    UsersProvider mUserProvider;
    CommentProvider mCommentProvider;
    AuthProvider mAuthProvider;
    ChatProvider mChatProvider;
    MessageProvider mMessageProvider;
    String idPost = "";
    private static final String TAG = "CHATADAPTER";
    ListenerRegistration mListenerChatAdapter;

    public ChatAdapter(FirestoreRecyclerOptions<Chats> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UsersProvider();
        mAuthProvider= new AuthProvider();
        mCommentProvider= new CommentProvider();
        mChatProvider = new ChatProvider();
        mMessageProvider= new MessageProvider();


    }

    @Override
    protected void onBindViewHolder(@NotNull ViewHolder holder, int position, Chats chat) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String idChat= document.getId();

        long date = chat.getTimestamp();

        if(mAuthProvider.getUid().equals(chat.getIdUser1())){
            getUserinfo(chat.getIdUser2(), holder);
        } else {
                getUserinfo(chat.getIdUser1(), holder);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity(idChat, chat.getIdUser1(), chat.getIdUser2());
            }
        });
        getLastMessage(idChat, holder.textViewLastMessageChat);
        String idSender="";
        if(mAuthProvider.getUid()!=null) {
            if(mAuthProvider.getUid().equals(chat.getIdUser1())){
                idSender= chat.getIdUser2();
            }
            else {
                idSender=chat.getIdUser1();
            }

            getMessageNotRead(idChat, idSender, holder.mtextViewMessageNOleido, holder.FrameLayoutCardviewChat);
        }

    }

    private void getMessageNotRead(String idChat, String idSender, TextView mtextViewMessageNOleido, FrameLayout frameLayoutCardviewChat) {
    mListenerChatAdapter= mMessageProvider.getMessagesByChatAndSender(idChat, idSender).addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            int size = value.size();
            if(size>0){
                frameLayoutCardviewChat.setVisibility(View.VISIBLE);
                mtextViewMessageNOleido.setText(String.valueOf(size));
            } else {
                frameLayoutCardviewChat.setVisibility(View.GONE);
            }
        }

    });


    }

    public ListenerRegistration getListenerChatAdapter(){
        return mListenerChatAdapter;
    }

    private void getLastMessage(String idChat, TextView textViewLastMessageChat) {
        mListenerChatAdapter= mMessageProvider.getLastMessages(idChat).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    int size = value.size();
                    if (size > 0) {
                        String lastMessage = value.getDocuments().get(0).getString("message");
                        textViewLastMessageChat.setText(lastMessage);
                    } else {
                        Log.w(TAG, "fallo en mensajes no leidos :failure" );
                    }
                }
            }

        });
    }



    // enviamos la información necesaria para los mensajes
    private void goToChatActivity(String idChat, String idUser1, String idUser2) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idChat", idChat);
        intent.putExtra("idUser1", idUser1);
        intent.putExtra("idUser2",idUser2);
        context.startActivity(intent);

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
                        holder.textViewUsernameChat.setText(nombre.toUpperCase());

                    }  if(documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if(imageProfile != null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(context).load(imageProfile).into(holder.imageViewChat);
                            }
                        }
                    }


                }

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewUsernameChat;
        
            TextView textViewLastMessageChat;
            TextView textViewFecha;
            TextView mtextViewMessageNOleido;
            FrameLayout FrameLayoutCardviewChat;

            CircleImageView imageViewChat;

            View viewHolder;

            public ViewHolder (View view){
                super(view);

                textViewLastMessageChat = view.findViewById(R.id.textViewLastMessageChat);
                textViewUsernameChat = view.findViewById(R.id.textViewUserNameChat);
                imageViewChat= view.findViewById(R.id.circleImageViewChat);
                mtextViewMessageNOleido = view.findViewById(R.id.texVIewCountMessage);
                FrameLayoutCardviewChat = view.findViewById(R.id.frameLayoutMessageNoRead);
                viewHolder = view;
            }
    }
}
