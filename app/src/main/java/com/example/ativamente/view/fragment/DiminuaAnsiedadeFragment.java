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

public class DiminuaAnsiedadeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diminua_ansiedade, container, false);

        view.findViewById(R.id.respiracao_consciente_meditacao).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Respiração consciente/ meditação");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.exercicio_leve_caminhada_alongamento).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Exercício leve (caminhada, alongamento)");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.encerramento_dia_revisar_calendario).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Encerramento do dia / revisar calendário");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.atividade_relaxante_ler_banho_quente).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Atividade relaxante (ler, banho quente)");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        view.findViewById(R.id.desconexao_total_sem_telas).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("rotina", "Desconexão total (sem telas)");
            Navigation.findNavController(v).navigate(R.id.homeFragment, bundle);
        });

        return view;
    }
}