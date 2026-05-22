package com.example.ativamente.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.ativamente.AtivamenteApp;
import com.example.ativamente.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileFragment extends Fragment {

    private SwitchMaterial themeSwitch;
    private SharedPreferences sharedPreferences;

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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(AtivamenteApp.PREFS_NAME, Context.MODE_PRIVATE);

        setupViews(view);
        loadData();
        setupListeners();
    }

    private void setupViews(View view) {
        // Name
        nameEditText = view.findViewById(R.id.edit_text_name);
        editNameButton = view.findViewById(R.id.edit_name_button);

        // Phone
        View phoneItem = view.findViewById(R.id.item_phone);
        ((ImageView) phoneItem.findViewById(R.id.item_icon)).setImageResource(android.R.drawable.ic_menu_call);
        phoneEditText = phoneItem.findViewById(R.id.item_edit_text);
        phoneEditText.setHint("Número de telefone");
        editPhoneButton = phoneItem.findViewById(R.id.edit_button);

        // Email
        View emailItem = view.findViewById(R.id.item_email);
        ((ImageView) emailItem.findViewById(R.id.item_icon)).setImageResource(android.R.drawable.ic_menu_send);
        emailEditText = emailItem.findViewById(R.id.item_edit_text);
        emailEditText.setHint("Endereço de Email");
        editEmailButton = emailItem.findViewById(R.id.edit_button);

        // Password
        View passwordItem = view.findViewById(R.id.item_password);
        ((ImageView) passwordItem.findViewById(R.id.item_icon)).setImageResource(android.R.drawable.ic_menu_set_as);
        passwordEditText = passwordItem.findViewById(R.id.item_edit_text);
        passwordEditText.setHint("Senha");
        editPasswordButton = passwordItem.findViewById(R.id.edit_button);

        // Theme Switch
        themeSwitch = view.findViewById(R.id.switch_theme);
        int currentNightMode = sharedPreferences.getInt(AtivamenteApp.KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        themeSwitch.setChecked(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);

        // Logout Button
        Button logoutButton = view.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void loadData() {
        nameEditText.setText(sharedPreferences.getString("name", "Nome"));
        phoneEditText.setText(sharedPreferences.getString("phone", ""));
        emailEditText.setText(sharedPreferences.getString("email", ""));
        passwordEditText.setText(sharedPreferences.getString("password", ""));
    }

    private void setupListeners() {
        setupEditableField(nameEditText, editNameButton, "name");
        setupEditableField(phoneEditText, editPhoneButton, "phone");
        setupEditableField(emailEditText, editEmailButton, "email");
        setupEditableField(passwordEditText, editPasswordButton, "password");

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
}
