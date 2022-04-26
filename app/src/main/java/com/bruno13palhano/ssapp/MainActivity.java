package com.bruno13palhano.ssapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bruno13palhano.ssapp.fragments.EditPlayerFragment;
import com.bruno13palhano.ssapp.fragments.HomeFragment;
import com.bruno13palhano.ssapp.fragments.PlayerFragment;
import com.bruno13palhano.ssapp.fragments.SettingsFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment;
    private PlayerFragment profileFragment;
    private SettingsFragment settingsFragment;
    private Bundle bundle;

    private FirebaseAuth auth;

    public void createSignInIntent(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();

        signInLauncher.launch(signInIntent);

    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result){
        IdpResponse response = result.getIdpResponse();
        if(result.getResultCode() == RESULT_OK){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            System.out.println("dados do user:"+user.getEmail());
        }else{
            System.out.println("deu errado novamente");
        }
    }

    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
//                .setLogo(R.drawable.my_great_logo)      // Set logo drawable
                .setTheme(R.style.Theme_SSApp)      // Set theme
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_theme_logo]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle = savedInstanceState;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_home_fragment);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        homeFragment = new HomeFragment();
        profileFragment = new PlayerFragment();
        settingsFragment = new SettingsFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.item_menu_home);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(bundle == null) {
            if (itemId == R.id.item_menu_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, homeFragment).commit();
                return true;

            } else if (itemId == R.id.item_menu_profile) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, profileFragment).commit();
                return true;

            } else if (itemId == R.id.item_menu_settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, settingsFragment).commit();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            System.out.println(user.getDisplayName()+" está online");
            // TODO: 17/04/2022 começar a implementar o acesso ao firebase...
        }else{
            themeAndLogo();
        }
    }

    //Método auxiliar para ativar e desativar o upButton nos fragmentos.
    public void enableUpButton(){
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException ignored){

        }
    }
    public void disableUpButton(){
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }catch (NullPointerException ignored){

        }
    }
}