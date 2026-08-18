package com.example.ativamente.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.ativamente.model.Task;
import com.example.ativamente.repository.TaskRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository mRepository;
    private final MutableLiveData<String> mSelectedDate = new MutableLiveData<>();
    private final LiveData<List<Task>> mTasks;
    private final MutableLiveData<String> mUserLiveData = new MutableLiveData<>();
    private final LiveData<Integer> mCompletedCount;
    private String mUserId;

    public interface OnTaskInsertedListener {
        void onTaskInserted(long id);
    }

    public TaskViewModel(Application application) {
        super(application);
        mRepository = new TaskRepository(application);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mUserId = user.getUid();
            mUserLiveData.setValue(mUserId);
        }

        mSelectedDate.setValue(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        mTasks = Transformations.switchMap(mSelectedDate, date -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                mUserId = currentUser.getUid();
                mUserLiveData.setValue(mUserId);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date d = sdf.parse(date);
                    Calendar cal = Calendar.getInstance();
                    if (d != null) cal.setTime(d);
                    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                    return mRepository.getTasksByDate(date, dayOfWeek, mUserId);
                } catch (Exception e) {
                    return mRepository.getTasksByDate(date, 1, mUserId);
                }
            } else {
                return new MutableLiveData<>(new ArrayList<>());
            }
        });

        mCompletedCount = Transformations.switchMap(mUserLiveData, userId ->
                mRepository.getTotalCompletedTasksCount(userId)
        );
    }

    public LiveData<Integer> getCompletedCount() {
        return mCompletedCount;
    }

    public LiveData<Integer> getTotalCompletedCount() {
        return mCompletedCount;
    }

    public void refreshTasks() {
        String currentDate = mSelectedDate.getValue();
        if (currentDate != null) {
            mSelectedDate.setValue(currentDate);
        }
    }

    public String getUserId() {
        return mUserId;
    }

    public void setSelectedDate(String date) {
        mSelectedDate.setValue(date);
    }

    public LiveData<String> getSelectedDate() {
        return mSelectedDate;
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

    public LiveData<Integer> getTasksCountForDate(String date) {
        if (mUserId != null) {
            return mRepository.getTasksCountByDate(date, mUserId);
        }
        return new MutableLiveData<>(0);
    }

    public LiveData<Integer> getCompletedTasksCountForDate(String date) {
        if (mUserId != null) {
            return mRepository.getCompletedTasksCountByDate(date, mUserId);
        }
        return new MutableLiveData<>(0);
    }

    public void syncData() {
        mRepository.syncWithFirestore();
    }
}