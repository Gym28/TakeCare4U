package com.gina.takecare4u.activities.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.adapter.ChatAdapter;
import com.gina.takecare4u.adapter.PubliAdapter;
import com.gina.takecare4u.modelos.Chats;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.ChatProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    ChatAdapter mchatAdapter;
    RecyclerView mRecyclerView;
    View mView;

    ChatProvider mChatProvider;
    AuthProvider mAuthProvider;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    UsersProvider mUserProvider;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerViewChat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mChatProvider = new ChatProvider();
        mAuthProvider= new AuthProvider();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

            //consulta a base de datos
            Query query = mChatProvider.getAll(mAuthProvider.getUid());
            Log.e(TAG,"query:"+query);
            FirestoreRecyclerOptions<Chats> options = new FirestoreRecyclerOptions.Builder<Chats>()
                    .setQuery(query, Chats.class)
                    .build();
            mchatAdapter = new ChatAdapter(options, getContext());
            mRecyclerView.setAdapter(mchatAdapter);
             mchatAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mchatAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mchatAdapter.getListenerChatAdapter()!=null){
            mchatAdapter.getListenerChatAdapter().remove();

        }

    }
}