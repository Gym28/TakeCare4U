package com.gina.takecare4u.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.Utils.RelativeTime;
import com.gina.takecare4u.activities.Utils.ViewedMessageHelper;
import com.gina.takecare4u.adapter.MessageAdapter;
import com.gina.takecare4u.adapter.ThePostAdapter;
import com.gina.takecare4u.modelos.Chats;
import com.gina.takecare4u.modelos.FCMBody;
import com.gina.takecare4u.modelos.FCMResponse;
import com.gina.takecare4u.modelos.Messages;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.ChatProvider;
import com.gina.takecare4u.providers.MessageProvider;
import com.gina.takecare4u.providers.NotificationProvider;
import com.gina.takecare4u.providers.TokenProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    View mActionBarview;

    EditText mEditTextMessage;
    ImageView mImageViewSendMessage;

    //para el custom_chat_toolbar
    CircleImageView mCircleViewToolbar;
    ImageView mImageViewBackToolbar;
    TextView mTextViewUserToolbar, mTextViewRelativeTimeChat;
    //para el cardview de los mensajes
    MessageAdapter mMessageAdapter;
    RecyclerView mRecyclerViewMessage;

    String mExtraId1;
    String mExtraId2;
    String mExtraIdChat;
    long midNotificationChat;

    ChatProvider mChatProvider;
    MessageProvider mMessageProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUserProvider;
    TokenProvider mTokenProviderNotificationChat;
    NotificationProvider mNotificationProviderchat;
    LinearLayoutManager mLinearLayoutManager;
    ListenerRegistration mListenergRegistration;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSendMessage = findViewById(R.id.imageViewSendMessage);

        mRecyclerViewMessage= findViewById(R.id.recyclerViewMessage);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewMessage.setLayoutManager(mLinearLayoutManager);

        mImageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mExtraId1 = getIntent().getStringExtra("idUser1");
        mExtraId2 = getIntent().getStringExtra("idUser2");
        mExtraIdChat = getIntent().getStringExtra("id");
        mChatProvider = new ChatProvider();
        mMessageProvider = new MessageProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UsersProvider();
        mTokenProviderNotificationChat = new TokenProvider();
        mNotificationProviderchat = new NotificationProvider();

        showCustomToolbar(R.layout.custom_chat_toolbar);
        checkIfChatExist();
    }

    private void sendMessage() {
        String textMessage = mEditTextMessage.getText().toString();
        if(!textMessage.isEmpty()){
            Messages message= new Messages();
            message.setIdChat(mExtraIdChat);
            if(mAuthProvider.getUid().equals(mExtraId1)){
                message.setIdSender(mExtraId1);
                message.setIdReciver(mExtraId2);
            }
            else {
                message.setIdSender(mExtraId2);
                message.setIdReciver(mExtraId1);
            }
                message.setTimeestamp(new Date().getTime());
                message.setViewed(false);
                message.setIdChat(mExtraIdChat);
                message.setMessage(textMessage);

                mMessageProvider.createMessage(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mEditTextMessage.setText("");
                            mMessageAdapter.notifyDataSetChanged();
                            sendNotification(message);

                        }
                        else{
                            Toast.makeText(ChatActivity.this, "El mensaje no se pudo enviar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
    private void sendNotification(Messages message) {
        //usuario del que obtenemos la información del post
        String idUser = "";
        if(mAuthProvider.getUid().equals(mExtraId1)){
            idUser = mExtraId2;
        }
        else {
                    idUser= mExtraId1;
                }

        mTokenProviderNotificationChat.getToken(idUser).addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                if(documentSnapshot.contains("token")){
                    String token = documentSnapshot.getString("token");
                    getLastThreeMessages(message, token);
                }
            }
            else {
                Toast.makeText(ChatActivity.this, "Sin token asignado", Toast.LENGTH_SHORT).show();

            }

        });


    }

    private void getLastThreeMessages(Messages message, String token) {
        mMessageProvider.getLastThreeMessageByChatAndSender(mExtraIdChat, mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList <Messages> messageArrayList = new ArrayList<>();
                for(DocumentSnapshot d: queryDocumentSnapshots.getDocuments()){
                    if(d.exists()) {
                        //transformamos el query en objeto Message
                         Messages message = d.toObject(Messages.class);
                         messageArrayList.add(message);
                    }
                }
                if(messageArrayList.size()==0){
                    messageArrayList.add(message);
                }
                Collections.reverse(messageArrayList);
                messageArrayList.add(message);
                Gson gson = new Gson();
                String messages= gson.toJson(messageArrayList);
                Map<String, String> data = new HashMap<>();
                data.put("title", "NUEVO MENSAJE");
                data.put("body", message.getMessage());
                data.put("idNotification",String.valueOf(midNotificationChat));
                data.put("MESSAGES",messages);
                FCMBody body = new FCMBody(token, "high", "4500s", data);
                mNotificationProviderchat.sendNotification(body).enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if(response.body()!=null){
                            if(response.body().getSuccess()==1){
                                Toast.makeText(ChatActivity.this, "La notificación ha sido enviada", Toast.LENGTH_SHORT).show();


                            }
                            else {
                                Toast.makeText(ChatActivity.this, "La notificación no fue enviada", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(ChatActivity.this, "La notificación no enviada", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });

            }
        });
            }


    private void showCustomToolbar(int resourse) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarview = inflater.inflate(resourse, null);
        actionBar.setCustomView(mActionBarview);
        mCircleViewToolbar = mActionBarview.findViewById(R.id.circleImageViewUserToolbar);
        mTextViewRelativeTimeChat=mActionBarview.findViewById(R.id.textViewRelativeTimeChat);
        mTextViewUserToolbar = mActionBarview.findViewById(R.id.textViewUserToolbar);
        mImageViewBackToolbar = mActionBarview.findViewById(R.id.imageViewBackChat);
        mImageViewBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        getUserInfo();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMessageAdapter != null){
            mMessageAdapter.startListening();
            ViewedMessageHelper.updateOnline(true, ChatActivity.this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        mMessageAdapter.stopListening();

    }
    //para actualizar que si el usuario está en linea

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, ChatActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListenergRegistration != null){
            mListenergRegistration.remove();
        }
    }

    private void getMessageChat (){
        //consulta a base de datos
        Query query = mMessageProvider.getMessageByChat(mExtraIdChat);
        FirestoreRecyclerOptions<Messages> options = new FirestoreRecyclerOptions.Builder<Messages>()
                .setQuery(query, Messages.class)
                .build();
        mMessageAdapter = new MessageAdapter(options, ChatActivity.this);
        mRecyclerViewMessage.setAdapter(mMessageAdapter);
        mMessageAdapter.startListening();
        mMessageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateViewed();
                int numberMessage = mMessageAdapter.getItemCount();
                int lastMessagePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(lastMessagePosition == -1 || (positionStart >= (numberMessage -1 ) && lastMessagePosition ==(positionStart -1))){
                    mRecyclerViewMessage.scrollToPosition(positionStart);

                }
            }
        });
    }

    private void getUserInfo() {
        String idUserinfo;
        if (mAuthProvider.getUid().equals(mExtraId1)) {
            idUserinfo = mExtraId2;
        } else {
            idUserinfo = mExtraId1;
        }
       mListenergRegistration= mUserProvider.getUsersRealTime(idUserinfo).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("nombre")) {
                        String username = documentSnapshot.getString("nombre");
                        mTextViewUserToolbar.setText(username);
                    }
                    if (documentSnapshot.contains("onLine")) {
                        boolean onLine = documentSnapshot.getBoolean("onLine");
                        if (onLine) {
                            mTextViewRelativeTimeChat.setText("En Linea");
                        } else if (documentSnapshot.contains("lastConnect")) {
                            long lastConnect = documentSnapshot.getLong("lastConnect");
                            String relativeTime = RelativeTime.getTimeAgo(lastConnect, ChatActivity.this);
                            mTextViewRelativeTimeChat.setText(relativeTime);
                        }
                    }
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("imageProfile")) {
                            String imageProfile = documentSnapshot.getString("imageProfile");
                            if (imageProfile != null) {
                                if (!imageProfile.equals("")) {
                                    Picasso.with(ChatActivity.this).load(imageProfile).into(mCircleViewToolbar);

                                }
                            }
                        }
                    }
                }
            }

        });
    }

    //información para crear el chat
    private void createChat() {

        Chats chat = new Chats();
        chat.setIdUser1(mExtraId1);
        chat.setIdUser2(mExtraId2);
        chat.setWriting(false);
        chat.setTimestamp(new Date().getTime());
        chat.setId(mExtraId1 + mExtraId2);
        //para el idAleatorio
        Random random = new Random();
        int n = random.nextInt();
        chat.setIdNotification(n);
        midNotificationChat = n;

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mExtraId1);
        ids.add(mExtraId2);
        chat.setIds(ids);
        mChatProvider.create(chat);
        mExtraIdChat= chat.getId();
        getMessageChat();


    }

    private void checkIfChatExist(){
        mChatProvider.getChatUser1andUser2(mExtraId1, mExtraId2).get().addOnSuccessListener(queryDocumentSnapshots -> {
            int size = queryDocumentSnapshots.size();
            System.out.println(size);
            if (size ==0) {
                createChat();
            }
            else {
                mExtraIdChat= queryDocumentSnapshots.getDocuments().get(0).getId();
                midNotificationChat = queryDocumentSnapshots.getDocuments().get(0).getLong("idNotification");
                getMessageChat();
                updateViewed();

            }


        });
    }

    private void updateViewed() {
        String idSender = "";
        if(mAuthProvider.getUid().equals(mExtraId1)){
            idSender= mExtraId2;
        }
        else {
            idSender=mExtraId1;
        }
        mMessageProvider.getMessagesByChatAndSender(mExtraIdChat, idSender).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                    mMessageProvider.updateViewed(document.getId(), true);

                }
            }
        });
    }


}