package com.example.ativamente.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ativamente.dao.TaskDao;
import com.example.ativamente.database.AppDatabase;
import com.example.ativamente.model.Task;

import java.util.List;

public class TaskRepository {

    private TaskDao mTaskDao;

    public interface OnTaskInsertedListener {
        void onTaskInserted(long id);
    }

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
    }

    public LiveData<List<Task>> getTasksByDate(String date) {
        return mTaskDao.getTasksByDate(date);
    }

    public void insert(Task task, OnTaskInsertedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = mTaskDao.insert(task);
            if (listener != null) {

                new Handler(Looper.getMainLooper()).post(() -> listener.onTaskInserted(id));
            }
        });
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }
}
