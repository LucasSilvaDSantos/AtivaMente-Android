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
import com.example.ativamente.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(v -> registerUser());

        binding.textGoToLogin.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment)
        );
    }

    private void registerUser() {
        String name = binding.editRegisterName.getText().toString().trim();
        String email = binding.editRegisterEmail.getText().toString().trim();
        String password = binding.editRegisterPassword.getText().toString().trim();
        String confirmPassword = binding.editRegisterConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.editRegisterName.setError("Informe seu nome");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            binding.editRegisterEmail.setError("Informe seu e-mail");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.editRegisterPassword.setError("Informe sua senha");
            return;
        }

        if (password.length() < 6) {
            binding.editRegisterPassword.setError("A senha deve ter pelo menos 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.editRegisterConfirmPassword.setError("As senhas não coincidem");
            return;
        }

        binding.buttonRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                                binding.buttonRegister.setEnabled(true);
                                Toast.makeText(getContext(), "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
                            });
                        }
                    } else {
                        binding.buttonRegister.setEnabled(true);
                        String error = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(getContext(), "Erro ao cadastrar: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
