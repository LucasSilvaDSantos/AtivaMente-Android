package com.example.ativamente.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.ativamente.dao.TaskDao;
import com.example.ativamente.database.AppDatabase;
import com.example.ativamente.model.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class TaskRepository {

    private TaskDao mTaskDao;
    private TaskFirestoreRepository mFirestoreRepository;

    public interface OnTaskInsertedListener {
        void onTaskInserted(long id);
    }

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mFirestoreRepository = new TaskFirestoreRepository();
    }

    public LiveData<List<Task>> getTasksByDate(String date, int dayOfWeek, String userId) {
        String dayOfWeekPattern = "%" + dayOfWeek + "%";
        return mTaskDao.getTasksByDateAndUser(date, dayOfWeekPattern, userId);
    }

    public void insert(Task task, OnTaskInsertedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Salva no Room primeiro
            long id = mTaskDao.insert(task);
            task.setId((int) id);
            
            // Envia para o Firestore em segundo plano, sem bloquear o fluxo
            AppDatabase.databaseWriteExecutor.execute(() -> mFirestoreRepository.addTask(task));

            if (listener != null) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onTaskInserted(id));
            }
        });
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
            mFirestoreRepository.updateTask(task);
        });
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
            mFirestoreRepository.deleteTask(task);
        });
    }

    public LiveData<Integer> getTotalCompletedTasksCount(String userId) {
        return mTaskDao.getTotalCompletedTasksCount(userId);
    }

    public LiveData<Integer> getTasksCountByDate(String date, String userId) {
        return mTaskDao.getTasksCountByDate(date, userId);
    }

    public LiveData<Integer> getCompletedTasksCountByDate(String date, String userId) {
        return mTaskDao.getCompletedTasksCountByDate(date, userId);
    }

    public void syncWithFirestore() {
        mFirestoreRepository.getAllTasks(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Task t = doc.toObject(Task.class);
                        if (t != null) {
                            mTaskDao.insert(t);
                        }
                    }
                });
            }
        });
    }
}
