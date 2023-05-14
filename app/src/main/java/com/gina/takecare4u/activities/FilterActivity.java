package com.gina.takecare4u.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.Utils.ViewedMessageHelper;
import com.gina.takecare4u.adapter.PubliAdapter;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.google.firebase.firestore.Query;

public class FilterActivity extends AppCompatActivity {
    String mExtraServicio;

    RecyclerView mRecyclerViewFilter;
    TextView mTextViewNumberPostFilter;

    AuthProvider mAuthProvider;
    PublicacionProvider mPublicacionProvider;
    PubliAdapter mPubliAdapter;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mTextViewNumberPostFilter= findViewById(R.id.textViewNumberPostFilter);

        mRecyclerViewFilter = findViewById(R.id.recyclerViewFilter);

        mRecyclerViewFilter.setLayoutManager(new GridLayoutManager(FilterActivity.this, 2));

        //providers
        mAuthProvider = new AuthProvider();
        mPublicacionProvider = new PublicacionProvider();

        mExtraServicio =  getIntent().getStringExtra("Servicio");
        Toast.makeText(this, "Consultar servicios para " + mExtraServicio, Toast.LENGTH_SHORT).show();

        //TOOLBAR
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("PUBLICACIONES DE SERVICIOS  ");
        getSupportActionBar().setSubtitle("  OFRECIDOS A " +mExtraServicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onStart() {
        super.onStart();
        //consulta a base de datos
        Query query = mPublicacionProvider.getPubliByServiceandTimestamp(mExtraServicio);
        FirestoreRecyclerOptions<Publicaciones> options = new FirestoreRecyclerOptions.Builder<Publicaciones>()
                .setQuery(query, Publicaciones.class)
                .build();
        mPubliAdapter = new PubliAdapter(options, FilterActivity.this, mTextViewNumberPostFilter);
        mRecyclerViewFilter.setAdapter(mPubliAdapter);
        mPubliAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, FilterActivity.this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPubliAdapter.stopListening();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, FilterActivity.this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;

    }
}