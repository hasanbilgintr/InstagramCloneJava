package com.hasanbilgin.instagramclonejava.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hasanbilgin.instagramclonejava.R;
import com.hasanbilgin.instagramclonejava.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    String password;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        //bu objeyi kullanarak tüm giriş çıkış işlemleri yapabiliriz


        //eğer kullanıcı giriş yaptıysa bilgisini tutar nullsa direk geçiş yapmaz tabi Sharepreferance ilede yapılabilirdi
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signInOnClickButton(View view) {
        email = binding.emailEditText.getText().toString();
        password = binding.passwordEditText.getText().toString();

        if (!email.equals("") && !password.equals("")) {
            //email ve password alarak giriş bilgileri varsa
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //e.getLocalizedMessage() kullanıcının anlayabildiği bir mesajdır
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("logtest65", e.getLocalizedMessage());
                }
            });
        } else {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
        }
    }


    public void signUpOnClickButton(View view) {
        email = binding.emailEditText.getText().toString();
        password = binding.passwordEditText.getText().toString();

        if (!email.equals("") && !password.equals("")) {
            //email ve password alarak giriş oluşturucak
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //e.getLocalizedMessage() kullanıcının anlayabildiği bir mesajdır
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("logtest65", e.getLocalizedMessage());
                }
            });
        } else {

            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
        }
    }
}