package com.gina.takecare4u.activities;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.Utils.ViewedMessageHelper;
import com.gina.takecare4u.activities.fragments.ChatFragment;
import com.gina.takecare4u.activities.fragments.FilterFragment;
import com.gina.takecare4u.activities.fragments.HomeFragment;
import com.gina.takecare4u.activities.fragments.ProfileFragment;
import com.gina.takecare4u.modelos.Users;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.TokenProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestoreException;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;


public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    TokenProvider mTokenProviderHome;
    // para obtener el id del usuario que ingresó a nuestra aplicación
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    String midUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mTokenProviderHome= new TokenProvider();
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
        createToken();
        midUser= mAuthProvider.getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.updateOnline(true, HomeActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, HomeActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ViewedMessageHelper.updateOnline(false, HomeActivity.this);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.itemHome) {
                        // FRAGMENT HOME
                        openFragment(new HomeFragment());
                    }
                    else if (item.getItemId() == R.id.itemChats) {
                        // FRAGMENT CHATS
                        openFragment(new ChatFragment());

                    }
                    else if (item.getItemId() == R.id.itemfilters) {
                        // FRAGMENT FILTROS
                        openFragment(new FilterFragment());

                    }
                    else if (item.getItemId() == R.id.itemPerfil) {
                        // FRAGMENT PROFILE
                        openFragment(new ProfileFragment());
                    }
                    return true;
                }
            };

    private void createToken(){

        mTokenProviderHome.create(mAuthProvider.getUid());

    }
}
