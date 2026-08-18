package com.example.ativamente.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ativamente.databinding.FragmentExploreBinding;
import com.example.ativamente.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        setupRealData();
    }

    private void setupRealData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());

        // Observar total de tarefas de hoje
        taskViewModel.getTasksCountForDate(today).observe(getViewLifecycleOwner(), total -> {
            // Observar tarefas concluídas de hoje
            taskViewModel.getCompletedTasksCountForDate(today).observe(getViewLifecycleOwner(), completed -> {
                updateDailyProgress(total, completed);
            });
        });

        // Observar total acumulado de tarefas concluídas
        taskViewModel.getTotalCompletedCount().observe(getViewLifecycleOwner(), totalCompleted -> {
            binding.textTotalCompleted.setText(String.valueOf(totalCompleted));
            updateLevelAndXp(totalCompleted);
        });

        // Mock de sequência (Streak) - Lógica complexa para depois
        binding.textStreakCount.setText("3 dias");
    }

    private void updateDailyProgress(Integer total, Integer completed) {
        int t = (total != null) ? total : 0;
        int c = (completed != null) ? completed : 0;

        int percentage = (t > 0) ? (int) (((float) c / t) * 100) : 0;

        binding.progressDaily.setProgress(percentage);
        binding.textProgressPercentage.setText(percentage + "%");
        binding.textTasksSummary.setText("Você concluiu " + c + " de " + t + " tarefas hoje!");
    }

    private void updateLevelAndXp(Integer totalCompleted) {
        int total = (totalCompleted != null) ? totalCompleted : 0;

        // Regra simples: cada 20 tarefas = 1 nível
        int level = (total / 20) + 1;
        int currentXp = (total % 20) * 50; // Cada tarefa vale 50 XP
        int maxXp = 1000; // 20 tarefas * 50 XP

        int xpPercentage = (int) (((float) currentXp / maxXp) * 100);

        binding.textLevelTitle.setText("Seu Nível: " + level);
        binding.textXpStatus.setText(currentXp + " / " + maxXp + " XP");
        binding.progressXp.setProgress(xpPercentage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
