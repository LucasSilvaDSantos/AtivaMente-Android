package com.example.ativamente.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.ativamente.model.Task;
import com.example.ativamente.repository.TaskRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository mRepository;
    private final MutableLiveData<String> mSelectedDate = new MutableLiveData<>();
    private final LiveData<List<Task>> mTasks;

    public interface OnTaskInsertedListener {
        void onTaskInserted(long id);
    }

    public TaskViewModel(Application application) {
        super(application);
        mRepository = new TaskRepository(application);

        mSelectedDate.setValue(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        mTasks = Transformations.switchMap(mSelectedDate, date -> 
            mRepository.getTasksByDate(date)
        );
    }

    public void setSelectedDate(String date) {
        mSelectedDate.setValue(date);
    }

    public LiveData<List<Task>> getTasks() {
        return mTasks;
    }

    public void insert(Task task, OnTaskInsertedListener listener) {
        mRepository.insert(task, id -> {
            if (listener != null) {
                listener.onTaskInserted(id);
            }
        });
    }

    public void update(Task task) {
        mRepository.update(task);
    }

    public void delete(Task task) {
        mRepository.delete(task);
    }
}
