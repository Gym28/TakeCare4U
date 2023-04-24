package com.gina.takecare4u.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.MainActivity;
import com.gina.takecare4u.activities.SecondActivity;
import com.gina.takecare4u.adapter.PubliAdapter;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {

    View mview;
    FloatingActionButton mFab;

    MaterialSearchBar mSearchBar;

    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    PublicacionProvider mPublicacionProvider;
    PubliAdapter mPubliAdapter;
    PubliAdapter mAdapterSearch;

    private static final String TAG = "HomeFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        //implementaciones del searchBar
        setHasOptionsMenu(true);

        mview= inflater.inflate(R.layout.fragment_home, container, false);
        mFab=mview.findViewById(R.id.fab);
        mSearchBar = mview.findViewById(R.id.searchBar);
        mRecyclerView = mview.findViewById(R.id.recyclerViewHome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setHasOptionsMenu(true);
        mAuthProvider = new AuthProvider();
        mPublicacionProvider = new PublicacionProvider();

         //implementación del searchBar, para cerrar cesión
        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.itemLogout){
                    singOut();
                }
                return true;
            }
        });



        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecond();
            }
        }); return mview;
      }

      private void getAllpost(){
          //consulta a base de datos
          Query query = mPublicacionProvider.getAll();
          Log.e(TAG,"query:"+query);
          FirestoreRecyclerOptions <Publicaciones> options = new FirestoreRecyclerOptions.Builder<Publicaciones>()
                  .setQuery(query, Publicaciones.class)
                  .build();
          mPubliAdapter = new PubliAdapter(options, getContext());
          mPubliAdapter.notifyDataSetChanged();
          mRecyclerView.setAdapter(mPubliAdapter);
          mPubliAdapter.startListening();

    }

    private void searchByService(String servicio){
        //consulta a base de datos
        Query query = mPublicacionProvider.getPostByServicio(servicio);
        FirestoreRecyclerOptions <Publicaciones> options = new FirestoreRecyclerOptions.Builder<Publicaciones>()
                .setQuery(query, Publicaciones.class)
                .build();
        mAdapterSearch = new PubliAdapter(options, getContext());
        mAdapterSearch.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapterSearch);
        mAdapterSearch.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllpost();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPubliAdapter.stopListening();

        if(mAdapterSearch!=null){
            mAdapterSearch.stopListening();
        }
    }

    private void goToSecond() {

        Intent intent = new Intent(getContext(), SecondActivity.class);
         startActivity(intent);
    }


    private void singOut() {
        mAuthProvider.cerrarSesion();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

        if(!enabled ){
            getAllpost();
        }

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

        searchByService(text.toString().toLowerCase());

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}