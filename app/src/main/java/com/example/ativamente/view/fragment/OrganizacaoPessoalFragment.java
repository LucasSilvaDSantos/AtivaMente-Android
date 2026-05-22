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

public class OrganizacaoPessoalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizacao_pessoal, container, false);

        view.findViewById(R.id.acordar_higiene_arrumar_cama).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Acordar, higiene, arrumar a cama");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.exercicio_leve).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Exercício leve, ativa corpo e mente");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.almoco_pausa_real).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Almoço e pausa real");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.tomar_banho_jantar).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "tomar banho e jantar");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.tempo_pessoal_dormir_cedo).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Tempo pessoal e dormir cedo");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        return view;
    }
}