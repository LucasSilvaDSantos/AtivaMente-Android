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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.ativamente.R;
import com.example.ativamente.model.Task;
import com.example.ativamente.receiver.NotificationReceiver;
import com.example.ativamente.viewmodel.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddTaskFragment";
    private static final String ARG_TASK = "arg_task";

    private TaskViewModel taskViewModel;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSelectDate;
    private Button buttonSelectTime;

    private Task existingTask;
    private final Calendar calendar = Calendar.getInstance();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    saveTask(true);
                } else {
                    Toast.makeText(getContext(), "Permissão de notificação negada. A tarefa será salva sem lembrete.", Toast.LENGTH_LONG).show();
                    saveTask(false);
                }
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
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextDescription = view.findViewById(R.id.edit_text_description);
        buttonSelectDate = view.findViewById(R.id.button_select_date);
        buttonSelectTime = view.findViewById(R.id.button_select_time);
        Button buttonSave = view.findViewById(R.id.button_save);

        if (existingTask != null) {
            editTextTitle.setText(existingTask.getTitle());
            editTextDescription.setText(existingTask.getDescription());
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

        buttonSelectDate.setOnClickListener(v -> showDatePicker());
        buttonSelectTime.setOnClickListener(v -> showTimePicker());
        buttonSave.setOnClickListener(v -> handleSaveRequest());
    }

    private void handleSaveRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                return;
            }
        }
        saveTask(true);
    }

    private void saveTask(boolean scheduleNotification) {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getContext(), "O título não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String selectedTime = timeFormat.format(calendar.getTime());

        if (existingTask != null) {
            existingTask.setTitle(title);
            existingTask.setDescription(description);
            existingTask.setDate(selectedDate);
            existingTask.setTime(selectedTime);
            taskViewModel.update(existingTask);
            if (scheduleNotification) {
                scheduleNotification(existingTask);
            }
        } else {
            Task newTask = new Task(title, description, selectedDate, selectedTime, false);
            taskViewModel.insert(newTask, id -> {
                if (scheduleNotification) {
                    newTask.setId((int)id);
                    scheduleNotification(newTask);
                }
            });
        }

        dismiss();
    }

    private void scheduleNotification(Task task) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, task.getTitle());
        intent.putExtra(NotificationReceiver.EXTRA_DESCRIPTION, task.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateButtonText();
        };

        new DatePickerDialog(getContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeButtonText();
        };

        new TimePickerDialog(getContext(), timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true).show();
    }

    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        buttonSelectDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateTimeButtonText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        buttonSelectTime.setText(timeFormat.format(calendar.getTime()));
    }
}
