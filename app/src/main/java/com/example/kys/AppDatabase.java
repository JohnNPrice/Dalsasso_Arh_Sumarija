package com.example.kys;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract NoteDao noteDao();
}
