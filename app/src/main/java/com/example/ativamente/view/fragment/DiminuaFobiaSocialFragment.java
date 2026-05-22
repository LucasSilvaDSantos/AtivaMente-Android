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

public class DiminuaFobiaSocialFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diminua_fobia_social, container, false);

        view.findViewById(R.id.revisar_conteudos_estudados_recentemente).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Revisar conteúdos estudados recentemente");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.fazer_anotacoes_resumos).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Fazer anotações e resumos");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.treinar_memorizacao).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Treinar memorização (repetição ativa, flashcards, etc.)");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.atividade_relaxante_ler_banho_quente).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Atividade relaxante (ler, banho quente)");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.resolver_questoes_revisao).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Resolver questões de revisão");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        return view;
    }
}