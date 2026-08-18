package com.example.ativamente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay de 2 segundos para a Splash Screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkUserSession();
        }, 2000);
    }

    private void checkUserSession() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        
        // Se o usuário já estiver logado, vai para MainActivity (que gerencia Home via NavGraph)
        // A MainActivity já tem lógica no LoginFragment para pular para a Home se logado,
        // mas aqui garantimos que a navegação aconteça.
        
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
