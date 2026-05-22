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

public class ExploreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        view.findViewById(R.id.button_sono_de_qualidade).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_exploreFragment_to_sonoDeQualidadeFragment);
        });

        view.findViewById(R.id.button_organizacao_pessoal).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_exploreFragment_to_organizacaoPessoalFragment);
        });

        view.findViewById(R.id.button_diminua_ansiedade).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_exploreFragment_to_diminuaAnsiedadeFragment);
        });

        view.findViewById(R.id.button_diminua_fobia_social).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_exploreFragment_to_diminuaFobiaSocialFragment);
        });

        view.findViewById(R.id.button_estudo_de_qualidade).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_exploreFragment_to_estudoDeQualidadeFragment);
        });

        return view;
    }
}
