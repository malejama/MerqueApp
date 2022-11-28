package com.example.merqueapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.merqueapp.R;
import com.example.merqueapp.fragment.ChatsFragment;
import com.example.merqueapp.fragment.FiltersFragment;
import com.example.merqueapp.fragment.HomeFragment;
import com.example.merqueapp.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation=findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
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
                    if (item.getItemId()==R.id.itemHome){
                        openFragment(new HomeFragment());
                        //aqui se abre un fragment home
                    }else if (item.getItemId()==R.id.itemFilter){
                        openFragment(new FiltersFragment());
                        //aqui se abre un fragment filtros
                    }else if (item.getItemId()==R.id.itemChats){
                        openFragment(new ChatsFragment());
                        //aqui se abre un fragment chats
                    }else if (item.getItemId()==R.id.itemPerfil){
                        openFragment(new ProfileFragment());
                        //aqui se abre un fragment perfil
                }
                    return false;
                }
            };
}
