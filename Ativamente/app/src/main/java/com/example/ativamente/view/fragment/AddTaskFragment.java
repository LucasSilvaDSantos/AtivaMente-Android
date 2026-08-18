package com.example.ativamente.view.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.ativamente.databinding.FragmentAddTaskBinding;
import com.example.ativamente.model.Task;
import com.example.ativamente.receiver.NotificationReceiver;
import com.example.ativamente.viewmodel.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddTaskFragment";
    private static final String ARG_TASK = "arg_task";

    private FragmentAddTaskBinding binding;
    private TaskViewModel taskViewModel;
    private Task existingTask;
    private final Calendar calendar = Calendar.getInstance();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                saveTask(isGranted);
            });

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    public static AddTaskFragment newInstance(Task task) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            existingTask = (Task) getArguments().getSerializable(ARG_TASK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        if (existingTask != null) {
            binding.textAddTaskTitle.setText("Editar Tarefa");
            binding.editTextTitle.setText(existingTask.getTitle());
            binding.editTextDescription.setText(existingTask.getDescription());
            binding.switchRoutine.setChecked(existingTask.isRoutine());
            if (existingTask.isRoutine()) {
                binding.chipGroupDays.setVisibility(View.VISIBLE);
                setupExistingRoutineDays(existingTask.getDaysOfWeek());
            }

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                calendar.setTime(dateFormat.parse(existingTask.getDate()));
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(timeFormat.parse(existingTask.getTime()));
                calendar.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
            } catch (ParseException e) {
            }
        }

        updateDateButtonText();
        updateTimeButtonText();

        binding.buttonSelectDate.setOnClickListener(v -> showDatePicker());
        binding.buttonSelectTime.setOnClickListener(v -> showTimePicker());
        binding.buttonSave.setOnClickListener(v -> handleSaveRequest());

        binding.switchRoutine.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.chipGroupDays.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            binding.buttonSelectDate.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });
    }

    private void setupExistingRoutineDays(String daysOfWeek) {
        if (daysOfWeek == null) return;
        if (daysOfWeek.contains("1")) binding.chipSun.setChecked(true);
        if (daysOfWeek.contains("2")) binding.chipMon.setChecked(true);
        if (daysOfWeek.contains("3")) binding.chipTue.setChecked(true);
        if (daysOfWeek.contains("4")) binding.chipWed.setChecked(true);
        if (daysOfWeek.contains("5")) binding.chipThu.setChecked(true);
        if (daysOfWeek.contains("6")) binding.chipFri.setChecked(true);
        if (daysOfWeek.contains("7")) binding.chipSat.setChecked(true);
    }

    private void handleSaveRequest() {
        Log.d("AddTaskDebug", "Botão de salvar clicado!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                return;
            }
        }
        saveTask(true);
    }

    private void saveTask(boolean scheduleNotification) {
        // 1. Contexto Seguro (Application Context)
        final Context appContext = requireActivity().getApplicationContext();
        
        String title = binding.editTextTitle.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(appContext, "O título não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isRoutine = binding.switchRoutine.isChecked();
        String daysOfWeek = "";
        if (isRoutine) {
            List<String> selectedDays = new ArrayList<>();
            if (binding.chipSun.isChecked()) selectedDays.add("1");
            if (binding.chipMon.isChecked()) selectedDays.add("2");
            if (binding.chipTue.isChecked()) selectedDays.add("3");
            if (binding.chipWed.isChecked()) selectedDays.add("4");
            if (binding.chipThu.isChecked()) selectedDays.add("5");
            if (binding.chipFri.isChecked()) selectedDays.add("6");
            if (binding.chipSat.isChecked()) selectedDays.add("7");
            
            if (selectedDays.isEmpty()) {
                Toast.makeText(appContext, "Selecione pelo menos um dia para a rotina", Toast.LENGTH_SHORT).show();
                return;
            }
            daysOfWeek = String.join(",", selectedDays);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String selectedTime = timeFormat.format(calendar.getTime());

        String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Log.e("AddTaskDebug", "Salvamento apenas offline: Usuário nulo.");
        }

        // 2. Fluxo de Salvamento e Saída Imediata
        if (existingTask != null) {
            existingTask.setTitle(title);
            existingTask.setDescription(description);
            existingTask.setDate(selectedDate);
            existingTask.setTime(selectedTime);
            existingTask.setRoutine(isRoutine);
            existingTask.setDaysOfWeek(daysOfWeek);
            existingTask.setUserId(userId);
            
            taskViewModel.update(existingTask);
            if (scheduleNotification) {
                performNotificationSchedule(appContext, existingTask);
            }
        } else {
            Task newTask = new Task(title, description, selectedDate, selectedTime, false, userId);
            newTask.setRoutine(isRoutine);
            newTask.setDaysOfWeek(daysOfWeek);
            
            // O callback agora usa o appContext seguro
            taskViewModel.insert(newTask, id -> {
                newTask.setId((int)id);
                if (scheduleNotification) {
                    performNotificationSchedule(appContext, newTask);
                }
            });
        }

        // 3. FECHA A TELA IMEDIATAMENTE
        dismiss();
        Toast.makeText(appContext, "Tarefa salva!", Toast.LENGTH_SHORT).show();
    }

    private void performNotificationSchedule(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, task.getTitle());
        intent.putExtra(NotificationReceiver.EXTRA_DESCRIPTION, task.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (task.isRoutine() && task.getDaysOfWeek() != null && !task.getDaysOfWeek().isEmpty()) {
            String[] selectedDays = task.getDaysOfWeek().split(",");
            for (String day : selectedDays) {
                int dayOfWeek = Integer.parseInt(day);
                Calendar alarmCal = (Calendar) calendar.clone();
                alarmCal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                if (alarmCal.getTimeInMillis() <= System.currentTimeMillis()) {
                    alarmCal.add(Calendar.WEEK_OF_YEAR, 1);
                }
                int uniqueId = task.getId() * 10 + dayOfWeek;
                PendingIntent routineIntent = PendingIntent.getBroadcast(context, uniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(), routineIntent);
            }
        } else {
            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateButtonText();
        };
        new DatePickerDialog(requireContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute1);
            updateTimeButtonText();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        binding.buttonSelectDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateTimeButtonText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        binding.buttonSelectTime.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
