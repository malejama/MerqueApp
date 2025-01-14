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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView mCircleImageViewBack;
    TextInputEditText mTextInputEditTextUserName;
    TextInputEditText mTextInputEditTextEmailR;
    TextInputEditText mTextInputEditTextPasswordR;
    TextInputEditText mTextInputEditTextConfirmPassword;
    Button mButtonRegister;
    //FirebaseAuth mAuth;
    //FirebaseFirestore mFirestore;
    AuthProviders mAuthProvider;
    UsersProviders mUsersProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //instancias
        mCircleImageViewBack=findViewById(R.id.circleimageback);
        mTextInputEditTextUserName=findViewById(R.id.textInputEditTextUserName);
        mTextInputEditTextEmailR=findViewById(R.id.textInputEditTextEmailR);
        mTextInputEditTextPasswordR=findViewById(R.id.textInputEditTextPasswordR);
        mTextInputEditTextConfirmPassword=findViewById(R.id.textInputEditTextConfirmPassword);
        mButtonRegister=findViewById(R.id.btnregister);

        mAuthProvider=new AuthProviders();
        mUsersProvider=new UsersProviders();

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

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
            String username=mTextInputEditTextUserName.getText().toString();
            String email=mTextInputEditTextEmailR.getText().toString();
            String password= mTextInputEditTextPasswordR.getText().toString();
            String confirmpassword=mTextInputEditTextConfirmPassword.getText().toString();

            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
                if (isEmailValid(email)){

                    if(password.equals(confirmpassword)){
                        if(password.length() >=6){
                            createUser(username,email,password);
                        }else {
                            Toast.makeText(this, "la contraseñas deben tener 6 caracteres", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this, "Ha insertado todos los campos y el correo no es valido", Toast.LENGTH_SHORT).show();
                }


            }else {
                Toast.makeText(this, "para continuar inserte todos los campos", Toast.LENGTH_SHORT).show();
            }

        }

    private void createUser(final  String username, final String email, String password) {
        mDialog.show();
        mAuthProvider.register(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String id=mAuthProvider.getUid();
                            Users user=new Users();
                            user.setId (id);
                            user.setEmail (email);
                            user.setUsername(username);
                            user.setPassword(password);

                            mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "el usuario se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK/Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "no se pudo almacenar en l base de datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(RegisterActivity.this, "El usuario se registro correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //verificar si el email es valido

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
