package com.example.ativamente.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ativamente.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY time ASC")
    LiveData<List<Task>> getTasksByDate(String date);
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    Task getTaskById(int taskId);
}
