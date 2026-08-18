package com.example.ativamente.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ativamente.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 0 AND (date = :date OR (isRoutine = 1 AND daysOfWeek LIKE :dayOfWeekPattern)) AND (isRoutine = 0 OR excludedDates NOT LIKE '%' || :date || '%') ORDER BY time ASC")
    LiveData<List<Task>> getTasksByDateAndUser(String date, String dayOfWeekPattern, String userId);
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    Task getTaskById(int taskId);

    @Query("SELECT COUNT(*) FROM tasks WHERE userId = :userId AND isCompleted = 1")
    LiveData<Integer> getTotalCompletedTasksCount(String userId);

    @Query("SELECT COUNT(*) FROM tasks WHERE userId = :userId AND date = :date")
    LiveData<Integer> getTasksCountByDate(String date, String userId);

    @Query("SELECT COUNT(*) FROM tasks WHERE userId = :userId AND date = :date AND isCompleted = 1")
    LiveData<Integer> getCompletedTasksCountByDate(String date, String userId);
}
