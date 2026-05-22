package com.example.ativamente.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ativamente.R;

public class SonoDeQualidadeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sono_de_qualidade, container, false);

        view.findViewById(R.id.acordar_sempre_horario).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Acordar sempre no mesmo horário");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.alongamento_respiracao).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Alongamento e respiração");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.tomar_cha_calmante).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Tomar chá calmante");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.desconectar_telas).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Desconectar de telas");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.dormir_sempre_horario).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Dormir sempre no mesmo horário");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        return view;
    }
}