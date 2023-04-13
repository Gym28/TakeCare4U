package com.gina.takecare4u.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.EditProfileActivity;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    LinearLayout mLinearLayoutEditProfile;
    View mView;
    TextView mtextViewPhone, mtextViewPostNumber, mtextViewNombre, mtextViewEmail, mtextViewZipCode;
    ImageView mImageViewCover;
    CircleImageView mCircleImageProfile;


    UsersProvider musersProvider;
    AuthProvider mAuthProvider;
    PublicacionProvider mPublicacionesProvider;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        mView=inflater.inflate(R.layout.fragment_profile,container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditPerfil);
        mtextViewNombre = mView.findViewById(R.id.textViewNombrePerfil);
        mtextViewEmail = mView.findViewById(R.id.textViewMailPerfil);
        mtextViewZipCode = mView.findViewById(R.id.textViewZipCodePerfil);
        mtextViewPhone = mView.findViewById(R.id.textViewPhonePerfil);
        mtextViewPostNumber = mView.findViewById(R.id.textViewPostNumber);
        mCircleImageProfile = mView.findViewById(R.id.circleImageProfileProfile);
        mImageViewCover = mView.findViewById(R.id.imageViewCoverProfile);

        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        musersProvider  = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPublicacionesProvider = new PublicacionProvider();

        getUser();
        getPostNumber();
        return mView;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getPostNumber(){
  mPublicacionesProvider.getPostByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
          //cantidad de elementos que tenemos de la colección de publicaciones
          int numberPost = queryDocumentSnapshots.size();
          mtextViewPostNumber.setText(String.valueOf(numberPost));

      }
  });
    }

    private void getUser(){
        String id = mAuthProvider.getUid();
        musersProvider.getUsers(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("email")){
                        String email = documentSnapshot.getString("email");
                        mtextViewEmail.setText(email);
                    }   if (documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        mtextViewNombre.setText(nombre);
                    }     if (documentSnapshot.contains("phone")){
                        String phone = documentSnapshot.getString("phone");
                        mtextViewPhone.setText(phone);
                    }    if (documentSnapshot.contains("zipCode")){
                        String zipCode = documentSnapshot.getString("zipCode");
                        mtextViewZipCode.setText(zipCode);
                        if (documentSnapshot.contains("imageProfile")) {
                            String imageProfile = documentSnapshot.getString("imageProfile");
                            if(imageProfile!=null){
                                if (!imageProfile.isEmpty()) {
                                    Picasso.with(getContext()).load(imageProfile).into(mCircleImageProfile);
                                }
                            }
                        } if (documentSnapshot.contains("imageCover")){
                            String imageCover = documentSnapshot.getString("imageCover");
                            if(imageCover!= null){
                                if(!imageCover.isEmpty()){
                                    Picasso.with(getContext()).load(imageCover).into(mImageViewCover);
                                }
                            }
                        }
                    }
                }
            }
        });

    }
}