package com.example.merqueapp.Providers;

import com.example.merqueapp.models.Users;
import com.example.merqueapp.models.Users;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProviders {

    private CollectionReference mcollection;

    public UsersProviders() {
        mcollection=FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUsers(String id) {
        return mcollection.document(id).get();
    }

    public Task<Void> create(Users user){
        return  mcollection.document(user.getId()).set(user);
    }

    public Task<Void> update(Users user){
        Map<String, Object>map=new HashMap<>();
        map.put("username",user.getUsername());
        return  mcollection.document(user.getId()).update(map);
    }
}
