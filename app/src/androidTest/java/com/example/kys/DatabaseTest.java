package com.example.kys;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private AppDatabase appDatabase;
    private NoteDao noteDao;

    @Before
    public void createDb()
    {
        Context context= ApplicationProvider.getApplicationContext();

        appDatabase= Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        noteDao=appDatabase.noteDao();
    }
    @After
    public void closeDb()
    {
        appDatabase.close();
    }
    @Test
    public void writeAndReadNote() throws  Exception
    {
        Note note=new Note();
        String TEXT="TESTING, 1 2 3 4";
        note.text=TEXT;

        noteDao.insert(note);
        List<Note> notes=noteDao.getAll();
        assertEquals(TEXT, notes.get(notes.size()-1).text);
    }
}
