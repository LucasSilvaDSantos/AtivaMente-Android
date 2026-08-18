package com.example.ativamente.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ativamente.AtivamenteApp;
import com.example.ativamente.R;
import com.example.ativamente.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private Button editNameButton;
    private Button editPhoneButton;
    private Button editEmailButton;
    private Button editPasswordButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = requireActivity().getSharedPreferences(AtivamenteApp.PREFS_NAME, Context.MODE_PRIVATE);

        setupViews();
        loadData();
        setupListeners();
    }

    private void setupViews() {
        // Name
        nameEditText = binding.editTextName;
        editNameButton = binding.editNameButton;

        // Phone
        binding.itemPhone.itemIcon.setImageResource(android.R.drawable.ic_menu_call);
        phoneEditText = binding.itemPhone.itemEditText;
        phoneEditText.setHint("Número de telefone");
        editPhoneButton = binding.itemPhone.editButton;

        // Email
        binding.itemEmail.itemIcon.setImageResource(android.R.drawable.ic_menu_send);
        emailEditText = binding.itemEmail.itemEditText;
        emailEditText.setHint("Endereço de Email");
        editEmailButton = binding.itemEmail.editButton;

        // Password
        binding.itemPassword.itemIcon.setImageResource(android.R.drawable.ic_menu_set_as);
        passwordEditText = binding.itemPassword.itemEditText;
        passwordEditText.setHint("Senha");
        editPasswordButton = binding.itemPassword.editButton;

        // Theme Switch
        int currentNightMode = sharedPreferences.getInt(AtivamenteApp.KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        binding.switchTheme.setChecked(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);

        // Logout Button
        binding.buttonLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Sair")
                    .setMessage("Deseja mesmo sair da sua conta?")
                    .setPositiveButton("Sair", (dialog, which) -> {
                        mAuth.signOut();
                        Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_loginFragment);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void loadData() {
        FirebaseUser user = mAuth.getCurrentUser();
        
        if (user != null) {
            nameEditText.setText(user.getDisplayName() != null ? user.getDisplayName() : sharedPreferences.getString("name", "Nome"));
            emailEditText.setText(user.getEmail());
        } else {
            nameEditText.setText(sharedPreferences.getString("name", "Nome"));
            emailEditText.setText(sharedPreferences.getString("email", ""));
        }
        
        phoneEditText.setText(sharedPreferences.getString("phone", ""));
        passwordEditText.setText(sharedPreferences.getString("password", "********"));
    }

    private void setupListeners() {
        setupEditableField(nameEditText, editNameButton, "name");
        setupEditableField(phoneEditText, editPhoneButton, "phone");
        // E-mail e Senha normalmente não são editáveis assim no Firebase sem reautenticação,
        // mas manteremos a lógica de UI por enquanto ou desativaremos a edição direta.
        editEmailButton.setVisibility(View.GONE);
        editPasswordButton.setVisibility(View.GONE);

        binding.switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferences.edit().putInt(AtivamenteApp.KEY_THEME, AppCompatDelegate.MODE_NIGHT_YES).apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferences.edit().putInt(AtivamenteApp.KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO).apply();
            }
        });
    }

    private void setupEditableField(final EditText editText, final Button button, final String key) {
        button.setOnClickListener(v -> {
            if (button.getText().toString().equalsIgnoreCase("Editar")) {
                editText.setEnabled(true);
                editText.requestFocus();
                button.setText("Salvar");
            } else {
                editText.setEnabled(false);
                sharedPreferences.edit().putString(key, editText.getText().toString()).apply();
                button.setText("Editar");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
