package com.example.ativamente.view.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ativamente.R;
import com.example.ativamente.model.Task;
import com.example.ativamente.receiver.NotificationReceiver;
import com.example.ativamente.view.adapter.DateAdapter;
import com.example.ativamente.view.adapter.TaskAdapter;
import com.example.ativamente.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements TaskAdapter.OnTaskClickListener, DateAdapter.OnDateClickListener {

    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private DateAdapter dateAdapter;
    private TextView monthTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monthTextView = view.findViewById(R.id.text_view_month);

        RecyclerView taskRecyclerView = view.findViewById(R.id.recycler_view_tasks);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter();
        taskAdapter.setOnTaskClickListener(this);
        taskRecyclerView.setAdapter(taskAdapter);

        RecyclerView dateRecyclerView = view.findViewById(R.id.recycler_view_dates);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dateAdapter = new DateAdapter();
        dateAdapter.setOnDateClickListener(this);
        dateRecyclerView.setAdapter(dateAdapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        if (getArguments() != null) {
            String rotina = getArguments().getString("rotina");
            if (rotina != null && !rotina.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
                String currentDate = dateFormat.format(new Date());
                Task task = new Task(rotina, "", currentDate, "", false);
                taskViewModel.insert(task, null);
                getArguments().clear();
            }
        }

        taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);
        });

        setupDateSelector(dateRecyclerView);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_task);
        fab.setOnClickListener(v -> {
            AddTaskFragment.newInstance().show(getParentFragmentManager(), AddTaskFragment.TAG);
        });

        setupSwipeToDelete(taskRecyclerView);
    }

    private void setupDateSelector(RecyclerView dateRecyclerView) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        for (int i = 0; i < 60; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Date today = new Date();
        dateAdapter.setDates(dates, today);
        updateMonth(today);

        for (int i = 0; i < dates.size(); i++) {
            if (isSameDay(dates.get(i), today)) {
                dateRecyclerView.scrollToPosition(i);
                break;
            }
        }
    }

    private void setupSwipeToDelete(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task taskToDelete = taskAdapter.getTaskAt(position);
                taskViewModel.delete(taskToDelete);
                cancelNotification(taskToDelete);
                Toast.makeText(getContext(), "Tarefa deletada", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onTaskStatusChanged(Task task, boolean isCompleted) {
        task.setCompleted(isCompleted);
        taskViewModel.update(task);
        if (isCompleted) {
            cancelNotification(task);
        }
    }

    @Override
    public void onDateClick(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
        taskViewModel.setSelectedDate(dateFormat.format(date));
        updateMonth(date);
    }

    @Override
    public void onTaskClick(Task task) {
        AddTaskFragment.newInstance(task).show(getParentFragmentManager(), AddTaskFragment.TAG);
    }

    private void cancelNotification(Task task) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), task.getId(), intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private void updateMonth(Date date) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("pt", "BR"));
        monthTextView.setText(monthFormat.format(date));
    }
}
