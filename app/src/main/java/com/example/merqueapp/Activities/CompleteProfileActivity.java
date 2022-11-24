package com.example.merqueapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.merqueapp.models.Users;
import com.example.merqueapp.Providers.AuthProviders;
import com.example.merqueapp.Providers.UsersProviders;
import com.example.merqueapp.R;
import com.example.merqueapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {
    TextInputEditText mTextInputUsername;
    Button mButtonRegister;
    //FirebaseAuth mAuth;
    //FirebaseFirestore mFirestore;
    AuthProviders mAuthProviders;
    UsersProviders mUsersProviders;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextInputUsername=findViewById(R.id.textInputEditTextUserNameC);
        mButtonRegister=findViewById(R.id.btnregisterC);

        mAuthProviders= new AuthProviders();
        mUsersProviders= new UsersProviders();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false).build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String username=mTextInputUsername.getText().toString();
        if (!username.isEmpty()) {
            updateUser(username);
        }else{
            Toast.makeText(this, "Para continuar inserta el nombre del usiario", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(String username) {
        String id = mAuthProviders.getUid();
        Users user=new Users();
        user.setUsername(username);
        user.setEmail(id);
        mDialog.show();
        mUsersProviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CompleteProfileActivity.this, "No se almaceno el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}