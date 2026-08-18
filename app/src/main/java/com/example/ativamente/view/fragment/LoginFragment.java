package com.example.ativamente.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ativamente.R;
import com.example.ativamente.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToHome();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(v -> loginUser());

        binding.btnGuestLogin.setOnClickListener(v -> loginAnonymously());

        binding.textGoToRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    private void loginUser() {
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.editEmail.setError("Informe seu e-mail");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.editPassword.setError("Informe sua senha");
            return;
        }

        binding.buttonLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    binding.buttonLogin.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Erro ao realizar login";
                        Toast.makeText(getContext(), "Erro: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // realizar Login Anônimo
    private void loginAnonymously() {
        binding.btnGuestLogin.setEnabled(false); // Desabilita para evitar múltiplos cliques seguidos

        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    binding.btnGuestLogin.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Entrando como convidado...", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(getContext(), "Erro ao acessar modo convidado: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToHome() {
        if (getView() != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}