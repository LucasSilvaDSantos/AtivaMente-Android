package com.example.ativamente.repository;

import com.example.ativamente.model.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TaskFirestoreRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void addTask(Task task) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getUid() != null) {
            String userId = user.getUid();
            db.collection("users").document(userId)
                    .collection("tasks").document(String.valueOf(task.getId()))
                    .set(task);
        } else {
            android.util.Log.e("Firestore", "Não foi possível salvar tarefa: usuário não autenticado");
        }
    }

    public void updateTask(Task task) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getUid() != null) {
            String userId = user.getUid();
            db.collection("users").document(userId)
                    .collection("tasks").document(String.valueOf(task.getId()))
                    .set(task);
        } else {
            android.util.Log.e("Firestore", "Não foi possível atualizar tarefa: usuário não autenticado");
        }
    }

    public void deleteTask(Task task) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getUid() != null) {
            String userId = user.getUid();
            db.collection("users").document(userId)
                    .collection("tasks").document(String.valueOf(task.getId()))
                    .delete();
        } else {
            android.util.Log.e("Firestore", "Não foi possível deletar tarefa: usuário não autenticado");
        }
    }

    public void getAllTasks(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getUid() != null) {
            String userId = user.getUid();
            db.collection("users").document(userId)
                    .collection("tasks")
                    .get()
                    .addOnCompleteListener(listener);
        } else {
            android.util.Log.e("Firestore", "Não foi possível buscar tarefas: usuário não autenticado");
        }
    }
}
