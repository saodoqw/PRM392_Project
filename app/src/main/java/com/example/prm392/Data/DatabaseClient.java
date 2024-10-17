package com.example.prm392.Data;

import android.content.Context;

import androidx.room.Room;

import lombok.Getter;

@Getter
public class DatabaseClient {
    private static DatabaseClient instance;

    private final AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "PRM392ShoesStore")
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public void close() {
        appDatabase.close();
    }
}
